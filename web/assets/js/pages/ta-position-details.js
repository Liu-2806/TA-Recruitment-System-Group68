document.addEventListener("DOMContentLoaded", function () {
  var modal = document.getElementById("applyConfirmModal");
  var openButton = document.getElementById("openApplyConfirm");
  var closeButton = document.getElementById("closeApplyConfirm");
  var cancelButton = document.getElementById("cancelApplyConfirm");
  var closeTargets = document.querySelectorAll("[data-close-modal='true']");

  if (!modal || !openButton) {
    return;
  }

  function openModal() {
    modal.classList.add("is-open");
    modal.setAttribute("aria-hidden", "false");
    document.body.classList.add("modal-open");
  }

  function closeModal() {
    modal.classList.remove("is-open");
    modal.setAttribute("aria-hidden", "true");
    document.body.classList.remove("modal-open");
  }

  openButton.addEventListener("click", openModal);

  if (closeButton) {
    closeButton.addEventListener("click", closeModal);
  }

  if (cancelButton) {
    cancelButton.addEventListener("click", closeModal);
  }

  closeTargets.forEach(function (target) {
    target.addEventListener("click", closeModal);
  });

  document.addEventListener("keydown", function (event) {
    if (event.key === "Escape" && modal.classList.contains("is-open")) {
      closeModal();
    }
  });
});
