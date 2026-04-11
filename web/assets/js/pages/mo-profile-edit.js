(function () {
  const modal = document.getElementById("moPasswordModal");
  const openButton = document.getElementById("openPasswordDialog");
  const closeButton = document.getElementById("closePasswordDialog");
  const cancelButton = document.getElementById("cancelPasswordDialog");

  if (!modal || !openButton || !closeButton || !cancelButton) {
    return;
  }

  const closeTargets = modal.querySelectorAll("[data-close-password-modal='true']");

  function openModal() {
    modal.classList.add("is-open");
    modal.setAttribute("aria-hidden", "false");
    document.body.style.overflow = "hidden";
  }

  function closeModal() {
    modal.classList.remove("is-open");
    modal.setAttribute("aria-hidden", "true");
    document.body.style.overflow = "";
  }

  openButton.addEventListener("click", openModal);
  closeButton.addEventListener("click", closeModal);
  cancelButton.addEventListener("click", closeModal);

  closeTargets.forEach(function (target) {
    target.addEventListener("click", closeModal);
  });

  document.addEventListener("keydown", function (event) {
    if (event.key === "Escape" && modal.classList.contains("is-open")) {
      closeModal();
    }
  });
})();
