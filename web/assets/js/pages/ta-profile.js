(function () {
  const fileInput = document.getElementById("resumeFileInput");
  const fileNameDisplay = document.getElementById("resumeFileNameDisplay");
  const uploadForm = document.getElementById("taResumeUploadForm");

  if (!fileInput || !fileNameDisplay || !uploadForm) {
    return;
  }

  function updateSelectedFile() {
    if (!fileInput.files || fileInput.files.length === 0) {
      fileNameDisplay.textContent = "No file chosen";
      return;
    }

    fileNameDisplay.textContent = fileInput.files[0].name;
  }

  fileInput.addEventListener("change", updateSelectedFile);

  uploadForm.addEventListener("submit", function () {
    const submitButton = uploadForm.querySelector(".ta-upload-box__upload");
    if (!submitButton) {
      return;
    }
    submitButton.disabled = true;
    submitButton.textContent = "Uploading...";
  });
})();
