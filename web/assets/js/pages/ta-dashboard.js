(function () {
  const calendarGrid = document.getElementById("taCalendarGrid");
  const weekLabel = document.getElementById("taWeekLabel");
  const prevWeekButton = document.getElementById("taPrevWeek");
  const nextWeekButton = document.getElementById("taNextWeek");
  const courseTitle = document.getElementById("taCourseTitle");
  const courseMeta = document.getElementById("taCourseMeta");
  const courseLink = document.getElementById("taCourseLink");
  const detailTitle = document.getElementById("taScheduleDetailTitle");
  const detailBadge = document.getElementById("taScheduleDetailBadge");
  const detailMeta = document.getElementById("taScheduleDetailMeta");
  const detailDescription = document.getElementById("taScheduleDetailDescription");
  const detailLink = document.getElementById("taScheduleDetailLink");

  if (!calendarGrid || !weekLabel || !prevWeekButton || !nextWeekButton) {
    return;
  }

  const positionDetailsUrl = calendarGrid.dataset.positionUrl || "#";
  const weekdayNames = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
  const today = startOfDay(new Date());
  const currentWeekStart = startOfWeek(today);

  const weeklySchedule = {
    "-1": {
      course: {
        title: "Software Engineering TA",
        meta: "Tue 14:00 - 16:00 · Queens Building QB-302 · Weekly support session",
        link: "#application-se3001"
      },
      activities: [
        {
          dayIndex: 0,
          type: "lab",
          calendarLabel: "Lab 10:00",
          title: "SE3001 Lab Preparation",
          time: "10:00 - 12:00",
          location: "QB-305",
          description: "Assist with debugging walkthroughs and answer student questions before the graded lab week.",
          detailUrl: positionDetailsUrl
        },
        {
          dayIndex: 2,
          type: "checkoff",
          calendarLabel: "Check-off 16:00",
          title: "Prototype Check-off",
          time: "16:00 - 17:30",
          location: "Innovation Studio 2",
          description: "Review student project milestones and confirm progress against the sprint acceptance checklist.",
          detailUrl: positionDetailsUrl
        }
      ]
    },
    "0": {
      course: {
        title: "Software Engineering TA",
        meta: "Tue 14:00 - 16:00 · Queens Building QB-302 · Weekly support session",
        link: "#application-se3001"
      },
      activities: [
        {
          dayIndex: 1,
          type: "lab",
          calendarLabel: "Lab 10:00",
          title: "SE3001 Lab Support",
          time: "10:00 - 12:00",
          location: "QB-302",
          description: "Guide students through the weekly lab, answer Java implementation questions, and record common issues for the MO.",
          detailUrl: positionDetailsUrl
        },
        {
          dayIndex: 2,
          type: "checkoff",
          calendarLabel: "Check-off 16:00",
          title: "Sprint Demo Check-off",
          time: "16:00 - 17:00",
          location: "Engineering Hub 1",
          description: "Observe each group demo, verify acceptance criteria, and note blockers to follow up after the session.",
          detailUrl: positionDetailsUrl
        },
        {
          dayIndex: 4,
          type: "exam",
          calendarLabel: "Exam 09:00",
          title: "Invigilation Duty",
          time: "09:00 - 11:00",
          location: "Exam Hall C",
          description: "Support module invigilation, seating checks, and post-exam material handover for the assessment team.",
          detailUrl: positionDetailsUrl
        }
      ]
    },
    "1": {
      course: {
        title: "Software Engineering TA",
        meta: "Tue 14:00 - 16:00 · Queens Building QB-302 · Weekly support session",
        link: "#application-se3001"
      },
      activities: [
        {
          dayIndex: 1,
          type: "lab",
          calendarLabel: "Lab 10:00",
          title: "Architecture Review Lab",
          time: "10:00 - 12:00",
          location: "QB-302",
          description: "Lead the architecture review activity and help students prepare for the upcoming design checkpoint.",
          detailUrl: positionDetailsUrl
        },
        {
          dayIndex: 3,
          type: "checkoff",
          calendarLabel: "Check-off 15:00",
          title: "Code Quality Check-off",
          time: "15:00 - 16:30",
          location: "Engineering Hub 1",
          description: "Assess repository structure, testing coverage, and merge-readiness before the next release rehearsal.",
          detailUrl: positionDetailsUrl
        }
      ]
    }
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
    return weeklySchedule[String(offset)] || {
      course: {
        title: "Software Engineering TA",
        meta: "No fixed course session arranged for this week",
        link: "#application-se3001"
      },
      activities: []
    };
  }

  function renderCourse(weekData) {
    if (!courseTitle || !courseMeta || !courseLink) {
      return;
    }
    courseTitle.textContent = weekData.course.title;
    courseMeta.textContent = weekData.course.meta;
    courseLink.href = weekData.course.link;
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

  function renderDetail(selectedDate, tasksForDay) {
    if (!detailTitle || !detailBadge || !detailMeta || !detailDescription || !detailLink) {
      return;
    }

    if (!selectedDate || !tasksForDay || tasksForDay.length === 0) {
      detailTitle.textContent = "Select a work day";
      detailBadge.textContent = "No selection";
      detailMeta.textContent = "Choose a highlighted day to inspect the arranged TA duty and jump to the related position detail page.";
      detailDescription.textContent = "Scheduled activity details for this week will appear here.";
      detailLink.href = positionDetailsUrl;
      detailLink.textContent = "Open Position Details";
      detailLink.classList.add("is-disabled");
      detailLink.setAttribute("aria-disabled", "true");
      return;
    }

    const firstTask = tasksForDay[0];
    const dateLabel = selectedDate.toLocaleDateString("en-GB", {
      weekday: "short",
      day: "2-digit",
      month: "short"
    });

    detailTitle.textContent = firstTask.title;
    detailBadge.textContent = tasksForDay.length === 1 ? firstTask.calendarLabel : tasksForDay.length + " tasks";
    detailMeta.textContent = dateLabel + " · " + firstTask.time + " · " + firstTask.location;
    detailDescription.textContent = firstTask.description;
    detailLink.href = firstTask.detailUrl;
    detailLink.textContent = tasksForDay.length === 1 ? "Open Position Details" : "Open Related Position Details";
    detailLink.classList.remove("is-disabled");
    detailLink.removeAttribute("aria-disabled");
  }

  function render() {
    const weekStart = addDays(currentWeekStart, state.weekOffset * 7);
    const weekData = getWeekData(state.weekOffset);
    const taskMap = new Map();

    weekData.activities.forEach(function (task) {
      const taskDate = addDays(weekStart, task.dayIndex);
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

    const selectedDate = state.selectedDateKey ? new Date(state.selectedDateKey + "T00:00:00") : null;
    const selectedTasks = state.selectedDateKey ? taskMap.get(state.selectedDateKey) : null;
    renderDetail(selectedDate, selectedTasks);
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
