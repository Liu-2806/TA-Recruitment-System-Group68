(function () {
  const deadlineInput = document.getElementById("deadline");
  const deadlineField = document.getElementById("deadlinePickerField");
  const postingTypeSelect = document.getElementById("postingType");
  const activityFields = document.getElementById("activityFields");
  const activityFieldControls = activityFields
    ? activityFields.querySelectorAll("input, select, textarea")
    : [];

  if (deadlineInput && deadlineField) {
    deadlineField.addEventListener("click", function () {
      if (typeof deadlineInput.showPicker === "function") {
        deadlineInput.showPicker();
      } else {
        deadlineInput.focus();
        deadlineInput.click();
      }
    });
  }

  function syncPostingType() {
    if (!postingTypeSelect || !activityFields) {
      return;
    }

    const isActivityPosting = postingTypeSelect.value === "ACTIVITY";

    activityFields.hidden = !isActivityPosting;
    activityFields.setAttribute("aria-hidden", String(!isActivityPosting));

    activityFieldControls.forEach(function (control) {
      control.disabled = !isActivityPosting;
    });
  }

  if (postingTypeSelect && activityFields) {
    postingTypeSelect.addEventListener("change", syncPostingType);
    syncPostingType();
  }
})();
