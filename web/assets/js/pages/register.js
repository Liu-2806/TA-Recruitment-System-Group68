document.addEventListener("DOMContentLoaded", function () {
  var form = document.querySelector(".register-form");
  var passwordInput = document.getElementById("registerPassword");
  var confirmPasswordInput = document.getElementById("confirmPassword");
  var resetButton = document.getElementById("resetRegistration");

  function syncPasswordValidation() {
    if (!passwordInput || !confirmPasswordInput) {
      return;
    }

    if (confirmPasswordInput.value && passwordInput.value !== confirmPasswordInput.value) {
      confirmPasswordInput.setCustomValidity("Passwords do not match.");
    } else {
      confirmPasswordInput.setCustomValidity("");
    }
  }

  if (passwordInput && confirmPasswordInput) {
    passwordInput.addEventListener("input", syncPasswordValidation);
    confirmPasswordInput.addEventListener("input", syncPasswordValidation);
  }

  if (resetButton && form) {
    resetButton.addEventListener("click", function () {
      window.setTimeout(function () {
        if (confirmPasswordInput) {
          confirmPasswordInput.setCustomValidity("");
        }
      }, 0);
    });
  }
});
