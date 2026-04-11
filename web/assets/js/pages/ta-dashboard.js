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

  const positionDetailsUrl = calendarGrid.dataset.positionUrl || "#";
  const weekdayNames = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
  const today = startOfDay(new Date());
  const currentWeekStart = startOfWeek(today);

  const serverSchedule = window.taDashboardSchedule;
  const fallbackSchedule = {
    course: {
      title: "No course assignment",
      meta: "No fixed course session arranged for this week",
      link: "#"
    },
    activities: []
  };

  const state = {
    weekOffset: 0,
    selectedDateKey: null
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

  function isoKey(date) {
    return date.toISOString().slice(0, 10);
  }

  function getWeekData(offset) {
    if (serverSchedule && Array.isArray(serverSchedule.activities)) {
      return {
        course: {
          title: serverSchedule.course && serverSchedule.course.title ? serverSchedule.course.title : fallbackSchedule.course.title,
          meta: serverSchedule.course && serverSchedule.course.meta ? serverSchedule.course.meta : fallbackSchedule.course.meta,
          link: serverSchedule.course && serverSchedule.course.link ? serverSchedule.course.link : fallbackSchedule.course.link
        },
        activities: serverSchedule.activities
      };
    }
    return fallbackSchedule;
  }

  function renderCourse(weekData) {
    if (!courseTitle || !courseMeta || !courseLink) {
      return;
    }
    courseTitle.textContent = weekData.course.title;
    courseMeta.textContent = weekData.course.meta;
    courseLink.href = weekData.course.link || "#";
  }

  function buildDayButton(date, tasksForDay) {
    const button = document.createElement("button");
    button.type = "button";
    button.className = "ta-calendar__day";
    button.dataset.dateKey = isoKey(date);
    button.innerHTML = '<span class="ta-calendar__date">' + date.getDate() + "</span>";

    if (date < today) {
      button.classList.add("is-past");
    }

    if (isoKey(date) === isoKey(today)) {
      button.classList.add("is-today");
    }

    if (tasksForDay.length > 0) {
      button.classList.add("has-task");
      tasksForDay.forEach(function (task) {
        const taskBadge = document.createElement("span");
        taskBadge.className = "ta-calendar__event ta-calendar__event--" + task.type;
        taskBadge.textContent = task.calendarLabel;
        button.appendChild(taskBadge);
      });
    } else {
      button.disabled = true;
      button.setAttribute("aria-disabled", "true");
    }

    button.addEventListener("click", function () {
      if (tasksForDay.length === 0) {
        return;
      }
      state.selectedDateKey = button.dataset.dateKey;
      render();
    });

    return button;
  }

  function render() {
    const weekStart = addDays(currentWeekStart, state.weekOffset * 7);
    const weekEnd = addDays(weekStart, 6);
    const weekData = getWeekData(state.weekOffset);
    const taskMap = new Map();

    weekData.activities.forEach(function (task) {
      if (!task.date) {
        return;
      }
      const taskDate = startOfDay(new Date(task.date + "T00:00:00"));
      if (Number.isNaN(taskDate.getTime()) || taskDate < weekStart || taskDate > weekEnd) {
        return;
      }
      const key = isoKey(taskDate);
      if (!taskMap.has(key)) {
        taskMap.set(key, []);
      }
      taskMap.get(key).push(task);
    });

    weekLabel.textContent = formatRange(weekStart);
    renderCourse(weekData);

    const preferredTodayKey = state.weekOffset === 0 ? isoKey(today) : null;
    if (preferredTodayKey && taskMap.has(preferredTodayKey)) {
      state.selectedDateKey = state.selectedDateKey || preferredTodayKey;
    }
    if (!state.selectedDateKey || !taskMap.has(state.selectedDateKey)) {
      state.selectedDateKey = taskMap.size > 0 ? Array.from(taskMap.keys())[0] : null;
    }

    calendarGrid.innerHTML = "";

    for (let index = 0; index < weekdayNames.length; index += 1) {
      const date = addDays(weekStart, index);
      const key = isoKey(date);
      const tasksForDay = taskMap.get(key) || [];
      const dayButton = buildDayButton(date, tasksForDay);

      if (state.selectedDateKey === key && tasksForDay.length > 0) {
        dayButton.classList.add("is-selected");
      }

      calendarGrid.appendChild(dayButton);
    }
  }

  prevWeekButton.addEventListener("click", function () {
    state.weekOffset -= 1;
    state.selectedDateKey = null;
    render();
  });

  nextWeekButton.addEventListener("click", function () {
    state.weekOffset += 1;
    state.selectedDateKey = null;
    render();
  });

  render();
})();
