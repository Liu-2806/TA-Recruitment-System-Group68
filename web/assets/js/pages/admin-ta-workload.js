(function () {
  const modal = document.getElementById("adminTaModal");
  const closeButton = document.getElementById("closeAdminTaModal");
  const detailButtons = document.querySelectorAll("[data-ta-id]");
  const personalInfo = document.getElementById("adminTaPersonalInfo");
  const positionList = document.getElementById("adminTaPositionList");
  const analysisList = document.getElementById("adminTaAnalysisList");
  const suggestionList = document.getElementById("adminTaSuggestionList");
  const modalTitle = document.getElementById("adminTaModalTitle");
  const modalSubtitle = document.getElementById("adminTaModalSubtitle");

  if (!modal || !closeButton || detailButtons.length === 0) {
    return;
  }

  function renderPairs(container, items, itemClass) {
    container.innerHTML = "";
    items.forEach(function (item) {
      const wrapper = document.createElement("div");
      wrapper.className = itemClass;
      wrapper.innerHTML = "<strong>" + item[0] + "</strong><span>" + item[1] + "</span>";
      container.appendChild(wrapper);
    });
  }

  function openModal(payload) {
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

  function buildPayload(detail) {
    const taProfile = detail.taProfile || {};
    const workloadAnalysis = detail.workloadAnalysis || {};
    const workingPositions = Array.isArray(detail.workingPositions) ? detail.workingPositions : [];
    const suggestions = Array.isArray(detail.adminSuggestions) ? detail.adminSuggestions : [];
    const name = taProfile.fullName || taProfile.taId || "TA Detail";
    const totalHours = workloadAnalysis.totalWorkloadHours || 0;

    return {
      name: name,
      subtitle: "Current workload: " + totalHours + "h across " + (workloadAnalysis.activePositionCount || 0) + " accepted position(s).",
      personal: [
        ["TA ID", taProfile.taId || "-"],
        ["Student ID", taProfile.studentId || "-"],
        ["Major", taProfile.majorProgram || "-"],
        ["Academic Year", taProfile.academicYear || "-"],
        ["Email", taProfile.email || "-"],
        ["Phone", taProfile.phone || "-"]
      ],
      positions: workingPositions.length === 0 ? [["No accepted positions", "This TA currently has no accepted assignments."]] : workingPositions.map(function (position) {
        return [
          position.courseName || position.courseCode || "Position",
          (position.roleType || "TA Duty") + " | " + (position.workloadHours || 0) + "h | " + (position.status || "UNKNOWN")
        ];
      }),
      analysis: [
        ["Current Status", workloadAnalysis.statusLabel || "-"],
        ["Risk Level", workloadAnalysis.riskLevel || "-"],
        ["Peak Day", workloadAnalysis.peakDay || "-"],
        ["Active Positions", String(workloadAnalysis.activePositionCount || 0)],
        ["Total Hours", String(totalHours) + "h"]
      ],
      suggestions: suggestions.length === 0 ? ["No additional admin suggestions were generated."] : suggestions
    };
  }

  function fetchJson(url) {
    return fetch(url, { headers: { Accept: "application/json" } }).then(function (response) {
      return response.json().then(function (payload) {
        if (!response.ok || payload.errorMessage) {
          throw new Error(payload.errorMessage || "Failed to load TA detail.");
        }
        return payload;
      });
    });
  }

  detailButtons.forEach(function (button) {
    button.addEventListener("click", function () {
      const taId = button.getAttribute("data-ta-id");
      if (!taId) {
        return;
      }

      button.disabled = true;
      fetchJson(window.location.origin + window.location.pathname.replace(/\/admin\/analytics\/ta-workload.*$/, "") + "/admin/analytics/ta-workload/detail?taId=" + encodeURIComponent(taId))
        .then(function (payload) {
          openModal(buildPayload(payload.detail || {}));
        })
        .catch(function (error) {
          openModal({
            name: "Unable to load details",
            subtitle: error.message,
            personal: [],
            positions: [],
            analysis: [],
            suggestions: ["Please refresh the page and try again."]
          });
        })
        .finally(function () {
          button.disabled = false;
        });
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
