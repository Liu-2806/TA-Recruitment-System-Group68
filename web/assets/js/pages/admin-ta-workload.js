(function () {
  const modal = document.getElementById("adminTaModal");
  const closeButton = document.getElementById("closeAdminTaModal");
  const detailButtons = document.querySelectorAll("[data-ta-detail]");
  const personalInfo = document.getElementById("adminTaPersonalInfo");
  const positionList = document.getElementById("adminTaPositionList");
  const analysisList = document.getElementById("adminTaAnalysisList");
  const suggestionList = document.getElementById("adminTaSuggestionList");
  const modalTitle = document.getElementById("adminTaModalTitle");
  const modalSubtitle = document.getElementById("adminTaModalSubtitle");

  if (!modal || !closeButton || detailButtons.length === 0) {
    return;
  }

  const dataset = {
    "zhang-san": {
      name: "Zhang San",
      subtitle: "Balanced workload across two active TA assignments.",
      personal: [
        ["Student ID", "2021001234"],
        ["Major", "Software Engineering"],
        ["Email", "zhang.san@univ.edu"],
        ["Current Load", "8h / week"]
      ],
      positions: [
        ["Software Engineering TA", "Weekly lab support · 6h"],
        ["Project Check-off", "Friday acceptance sessions · 2h"]
      ],
      analysis: [
        ["Current Status", "Normal workload distribution"],
        ["Peak Day", "Friday afternoon"],
        ["Risk Level", "Low"]
      ],
      suggestions: [
        "Safe to keep current assignments without redistribution.",
        "Suitable backup candidate for one short invigilation duty if needed.",
        "Continue monitoring only if new tasks are added this week."
      ]
    },
    "li-si": {
      name: "Li Si",
      subtitle: "Heavy workload concentration with multiple active positions.",
      personal: [
        ["Student ID", "2021002345"],
        ["Major", "Computer Science"],
        ["Email", "li.si@univ.edu"],
        ["Current Load", "12h / week"]
      ],
      positions: [
        ["Data Structures TA", "Tutorial + marking · 6h"],
        ["Database Systems TA", "Lab support · 4h"],
        ["Exam Invigilation", "Assessment duty · 2h"]
      ],
      analysis: [
        ["Current Status", "High alert"],
        ["Peak Day", "Tuesday and Friday"],
        ["Risk Level", "High"]
      ],
      suggestions: [
        "Avoid assigning new duties this week.",
        "Rebalance at least one short operational task to another TA.",
        "MO should confirm whether the exam duty can be shared."
      ]
    },
    "wang-wu": {
      name: "Wang Wu",
      subtitle: "Light workload with room for additional academic support.",
      personal: [
        ["Student ID", "2021003456"],
        ["Major", "Software Engineering"],
        ["Email", "wang.wu@univ.edu"],
        ["Current Load", "4h / week"]
      ],
      positions: [
        ["Software Engineering TA", "One lab block · 4h"]
      ],
      analysis: [
        ["Current Status", "Normal"],
        ["Peak Day", "Wednesday"],
        ["Risk Level", "Low"]
      ],
      suggestions: [
        "Candidate can absorb one extra short task if needed.",
        "Good option for replacing urgent temporary absence.",
        "Keep skill growth aligned with software lab duties."
      ]
    },
    "zhao-liu": {
      name: "Zhao Liu",
      subtitle: "No current TA assignment, available for future matching.",
      personal: [
        ["Student ID", "2021004567"],
        ["Major", "Communication Engineering"],
        ["Email", "zhao.liu@univ.edu"],
        ["Current Load", "0h / week"]
      ],
      positions: [
        ["No active positions", "Available for future assignment"]
      ],
      analysis: [
        ["Current Status", "Not applied"],
        ["Peak Day", "None"],
        ["Risk Level", "None"]
      ],
      suggestions: [
        "Can be recommended to modules with low applicant coverage.",
        "Priority outreach candidate for upcoming postings.",
        "Consider matching based on communication-heavy support roles."
      ]
    }
  };

  function renderPairs(container, items, itemClass) {
    container.innerHTML = "";
    items.forEach(function (item) {
      const wrapper = document.createElement("div");
      wrapper.className = itemClass;
      wrapper.innerHTML = "<strong>" + item[0] + "</strong><span>" + item[1] + "</span>";
      container.appendChild(wrapper);
    });
  }

  function openModal(key) {
    const payload = dataset[key];
    if (!payload) {
      return;
    }

    modalTitle.textContent = payload.name;
    modalSubtitle.textContent = payload.subtitle;
    renderPairs(personalInfo, payload.personal, "admin-ta-detail-item");
    renderPairs(positionList, payload.positions, "admin-ta-position-item");
    renderPairs(analysisList, payload.analysis, "admin-ta-analysis-item");

    suggestionList.innerHTML = "";
    payload.suggestions.forEach(function (item) {
      const li = document.createElement("li");
      li.textContent = item;
      suggestionList.appendChild(li);
    });

    modal.classList.add("is-open");
    modal.setAttribute("aria-hidden", "false");
    document.body.style.overflow = "hidden";
  }

  function closeModal() {
    modal.classList.remove("is-open");
    modal.setAttribute("aria-hidden", "true");
    document.body.style.overflow = "";
  }

  detailButtons.forEach(function (button) {
    button.addEventListener("click", function () {
      openModal(button.getAttribute("data-ta-detail"));
    });
  });

  closeButton.addEventListener("click", closeModal);
  modal.querySelectorAll("[data-close-ta-modal='true']").forEach(function (node) {
    node.addEventListener("click", closeModal);
  });

  document.addEventListener("keydown", function (event) {
    if (event.key === "Escape" && modal.classList.contains("is-open")) {
      closeModal();
    }
  });
})();
