document.addEventListener("DOMContentLoaded", function () {
  var hiddenRoleInput = document.getElementById("selectedRole");
  var roleButtons = document.querySelectorAll(".role-card");

  roleButtons.forEach(function (button) {
    button.addEventListener("click", function () {
      var selectedRole = button.getAttribute("data-role");

      roleButtons.forEach(function (item) {
        item.classList.remove("is-active");
        item.setAttribute("aria-pressed", "false");
      });

      button.classList.add("is-active");
      button.setAttribute("aria-pressed", "true");

      if (hiddenRoleInput) {
        hiddenRoleInput.value = selectedRole;
      }
    });
  });
});
