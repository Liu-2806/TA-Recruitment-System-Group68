(function () {
  const deadlineInput = document.getElementById("deadline");
  const deadlineField = document.getElementById("deadlinePickerField");

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
})();
