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
})();
