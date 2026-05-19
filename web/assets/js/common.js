(function () {
  const userMenus = document.querySelectorAll("[data-user-menu]");

  function closeMenu(menu) {
    const trigger = menu.querySelector("[data-user-menu-trigger]");
    const panel = menu.querySelector("[data-user-menu-panel]");
    if (!trigger || !panel) {
      return;
    }
    menu.classList.remove("is-open");
    trigger.setAttribute("aria-expanded", "false");
    panel.hidden = true;
  }

  function openMenu(menu) {
    const trigger = menu.querySelector("[data-user-menu-trigger]");
    const panel = menu.querySelector("[data-user-menu-panel]");
    if (!trigger || !panel) {
      return;
    }
    menu.classList.add("is-open");
    trigger.setAttribute("aria-expanded", "true");
    panel.hidden = false;
  }

  function toggleMenu(menu) {
    if (menu.classList.contains("is-open")) {
      closeMenu(menu);
    } else {
      userMenus.forEach(closeMenu);
      openMenu(menu);
    }
  }

  userMenus.forEach(function (menu) {
    const trigger = menu.querySelector("[data-user-menu-trigger]");
    if (!trigger) {
      return;
    }

    trigger.addEventListener("click", function (event) {
      event.stopPropagation();
      toggleMenu(menu);
    });
  });

  document.addEventListener("click", function (event) {
    userMenus.forEach(function (menu) {
      if (!menu.contains(event.target)) {
        closeMenu(menu);
      }
    });
  });

  document.addEventListener("keydown", function (event) {
    if (event.key === "Escape") {
      userMenus.forEach(closeMenu);
    }
  });

  // ---------- Modal helper ----------
  function openModal(id) {
    const modal = document.getElementById(id);
    if (!modal) {
      return;
    }
    modal.classList.add("is-open");
    document.body.classList.add("app-modal-open");
    const focusTarget = modal.querySelector("[data-autofocus]");
    if (focusTarget) {
      setTimeout(function () { focusTarget.focus(); }, 30);
    }
  }

  function closeModal(modal) {
    if (!modal) {
      return;
    }
    modal.classList.remove("is-open");
    if (!document.querySelector(".app-modal.is-open")) {
      document.body.classList.remove("app-modal-open");
    }
  }

  document.querySelectorAll("[data-modal-open]").forEach(function (trigger) {
    trigger.addEventListener("click", function (event) {
      event.preventDefault();
      openModal(trigger.getAttribute("data-modal-open"));
    });
  });

  document.querySelectorAll(".app-modal").forEach(function (modal) {
    modal.addEventListener("click", function (event) {
      if (event.target === modal || event.target.hasAttribute("data-modal-close")) {
        closeModal(modal);
      }
    });
  });

  document.addEventListener("keydown", function (event) {
    if (event.key === "Escape") {
      document.querySelectorAll(".app-modal.is-open").forEach(closeModal);
    }
  });

  window.appModal = { open: openModal, close: function (id) { closeModal(document.getElementById(id)); } };

  // ---------- Notification dropdown ----------
  const notificationMenus = document.querySelectorAll("[data-notification-menu]");

  function closeNotificationMenu(menu) {
    const trigger = menu.querySelector("[data-notification-trigger]");
    const panel = menu.querySelector("[data-notification-panel]");
    if (!trigger || !panel) {
      return;
    }
    menu.classList.remove("is-open");
    trigger.setAttribute("aria-expanded", "false");
    panel.hidden = true;
  }

  function openNotificationMenu(menu) {
    const trigger = menu.querySelector("[data-notification-trigger]");
    const panel = menu.querySelector("[data-notification-panel]");
    if (!trigger || !panel) {
      return;
    }
    menu.classList.add("is-open");
    trigger.setAttribute("aria-expanded", "true");
    panel.hidden = false;
  }

  notificationMenus.forEach(function (menu) {
    const trigger = menu.querySelector("[data-notification-trigger]");
    if (!trigger) {
      return;
    }
    trigger.addEventListener("click", function (event) {
      event.stopPropagation();
      if (menu.classList.contains("is-open")) {
        closeNotificationMenu(menu);
      } else {
        notificationMenus.forEach(closeNotificationMenu);
        openNotificationMenu(menu);
      }
    });
  });

  document.addEventListener("click", function (event) {
    notificationMenus.forEach(function (menu) {
      if (!menu.contains(event.target)) {
        closeNotificationMenu(menu);
      }
    });
  });

  document.addEventListener("keydown", function (event) {
    if (event.key === "Escape") {
      notificationMenus.forEach(closeNotificationMenu);
    }
  });
})();
