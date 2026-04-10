(function () {
  const deadlineInput = document.getElementById("deadline");
  const deadlineField = document.getElementById("deadlinePickerField");
  const postingTypeSelect = document.getElementById("postingType");
  const activityFields = document.getElementById("activityFields");

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
    activityFields.hidden = postingTypeSelect.value !== "ACTIVITY";
  }

  if (postingTypeSelect && activityFields) {
    postingTypeSelect.addEventListener("change", syncPostingType);
    syncPostingType();
  }
})();
