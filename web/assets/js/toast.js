(function () {
  function dismissToast(toast) {
    if (!toast || toast.classList.contains("is-leaving")) {
      return;
    }
    toast.classList.add("is-leaving");
    window.setTimeout(function () {
      const stack = toast.closest(".app-toast-stack");
      if (stack) {
        stack.remove();
      } else {
        toast.remove();
      }
    }, 220);
  }

  document.querySelectorAll("[data-toast]").forEach(function (toast) {
    const closeButton = toast.querySelector("[data-toast-close]");
    const timeout = Number(toast.getAttribute("data-toast-timeout") || "4200");

    if (closeButton) {
      closeButton.addEventListener("click", function () {
        dismissToast(toast);
      });
    }

    if (timeout > 0) {
      window.setTimeout(function () {
        dismissToast(toast);
      }, timeout);
    }
  });
})();
