(function () {
  const calendarGrid = document.getElementById("taCalendarGrid");
  const weekLabel = document.getElementById("taWeekLabel");
  const prevWeekButton = document.getElementById("taPrevWeek");
  const nextWeekButton = document.getElementById("taNextWeek");
  const courseTitle = document.getElementById("taCourseTitle");
  const courseMeta = document.getElementById("taCourseMeta");
  const courseLink = document.getElementById("taCourseLink");

  if (!calendarGrid || !weekLabel || !prevWeekButton || !nextWeekButton) {
    return;
  }

  const weekdayNames = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
  const today = startOfDay(new Date());
  const currentWeekStart = startOfWeek(today);

  const serverSchedule = window.taDashboardSchedule || {};
  const fallbackSchedule = {
    course: {
      title: "No course assignment",
      meta: "No fixed course session arranged for this week",
      link: "#"
    },
    activities: []
  };

  const state = {
    weekOffset: 0
  };

  function startOfDay(date) {
    return new Date(date.getFullYear(), date.getMonth(), date.getDate());
  }

  function startOfWeek(date) {
    const normalized = startOfDay(date);
    const day = normalized.getDay();
    const diff = day === 0 ? -6 : 1 - day;
    normalized.setDate(normalized.getDate() + diff);
    return normalized;
  }

  function addDays(date, days) {
    const copy = new Date(date);
    copy.setDate(copy.getDate() + days);
    return copy;
  }

  function isoKey(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return year + "-" + month + "-" + day;
  }

  function formatRange(startDate) {
    const endDate = addDays(startDate, 6);
    const startLabel = startDate.toLocaleDateString("en-GB", {
      day: "2-digit",
      month: "short"
    });
    const endLabel = endDate.toLocaleDateString("en-GB", {
      day: "2-digit",
      month: "short"
    });
    return "Week of " + startLabel + " - " + endLabel;
  }

  function parseEventDate(value) {
    if (!value) {
      return null;
    }
    const parsed = new Date(value + "T00:00:00");
    return Number.isNaN(parsed.getTime()) ? null : startOfDay(parsed);
  }

  function normalizeTimeRange(startTime, endTime) {
    if (!startTime && !endTime) {
      return "";
    }
    if (!endTime) {
      return String(startTime || "").trim();
    }
    return String(startTime || "").trim() + " - " + String(endTime).trim();
  }

  function getWeekData() {
    return {
      course: {
        title: serverSchedule.course && serverSchedule.course.title ? serverSchedule.course.title : fallbackSchedule.course.title,
        meta: serverSchedule.course && serverSchedule.course.meta ? serverSchedule.course.meta : fallbackSchedule.course.meta,
        link: serverSchedule.course && serverSchedule.course.link ? serverSchedule.course.link : fallbackSchedule.course.link
      },
      activities: Array.isArray(serverSchedule.activities) ? serverSchedule.activities : fallbackSchedule.activities
    };
  }

  function renderCourse(weekData) {
    if (!courseTitle || !courseMeta || !courseLink) {
      return;
    }
    courseTitle.textContent = weekData.course.title;
    courseMeta.textContent = weekData.course.meta;
    courseLink.href = weekData.course.link || "#";
  }

  function buildTaskMap(weekStart, weekData) {
    const weekEnd = addDays(weekStart, 6);
    const taskMap = new Map();

    function addTask(taskDate, task) {
      if (!taskDate || taskDate < weekStart || taskDate > weekEnd) {
        return;
      }
      const key = isoKey(taskDate);
      if (!taskMap.has(key)) {
        taskMap.set(key, []);
      }
      taskMap.get(key).push(task);
    }

    weekData.activities.forEach(function (event) {
      const taskDate = parseEventDate(event.date);
      addTask(taskDate, {
        title: event.title || "Scheduled activity",
        time: normalizeTimeRange(event.startTime, event.endTime),
        type: event.type || "others",
        detailUrl: event.detailUrl || "#",
        startTime: event.startTime || "",
        description: event.description || "",
        location: event.location || ""
      });
    });

    taskMap.forEach(function (tasks) {
      tasks.sort(function (left, right) {
        const leftTime = left.startTime || "";
        const rightTime = right.startTime || "";
        if (leftTime !== rightTime) {
          return leftTime.localeCompare(rightTime);
        }
        return (left.title || "").localeCompare(right.title || "");
      });
    });

    return taskMap;
  }

  function buildTaskLink(task) {
    const link = document.createElement("a");
    link.className = "ta-calendar__event ta-calendar__event--" + (task.type || "others");
    link.href = task.detailUrl || "#";

    const title = document.createElement("span");
    title.className = "ta-calendar__event-title";
    title.textContent = task.title || "Scheduled activity";

    const time = document.createElement("span");
    time.className = "ta-calendar__event-time";
    time.textContent = task.time || "Time TBC";

    link.appendChild(title);
    link.appendChild(time);
    return link;
  }

  function buildDayCard(date, tasksForDay) {
    const card = document.createElement("article");
    card.className = "ta-calendar__day";

    if (date < today) {
      card.classList.add("is-past");
    }

    if (isoKey(date) === isoKey(today)) {
      card.classList.add("is-today");
    }

    const dateLabel = document.createElement("span");
    dateLabel.className = "ta-calendar__date";
    dateLabel.textContent = date.getDate();
    card.appendChild(dateLabel);

    if (tasksForDay.length > 0) {
      card.classList.add("has-task");
      const eventsList = document.createElement("div");
      eventsList.className = "ta-calendar__events";
      tasksForDay.forEach(function (task) {
        eventsList.appendChild(buildTaskLink(task));
      });
      card.appendChild(eventsList);
    }

    return card;
  }

  function render() {
    const weekStart = addDays(currentWeekStart, state.weekOffset * 7);
    const weekData = getWeekData();
    const taskMap = buildTaskMap(weekStart, weekData);

    weekLabel.textContent = formatRange(weekStart);
    renderCourse(weekData);
    calendarGrid.innerHTML = "";

    for (let index = 0; index < weekdayNames.length; index += 1) {
      const date = addDays(weekStart, index);
      const key = isoKey(date);
      const tasksForDay = taskMap.get(key) || [];
      calendarGrid.appendChild(buildDayCard(date, tasksForDay));
    }
  }

  prevWeekButton.addEventListener("click", function () {
    state.weekOffset -= 1;
    render();
  });

  nextWeekButton.addEventListener("click", function () {
    state.weekOffset += 1;
    render();
  });

  render();
})();
