document.addEventListener("DOMContentLoaded", function () {
  var generateButton = document.getElementById("generatePassword");
  var passwordInput = document.getElementById("moInitialPassword");
  var confirmInput = document.getElementById("moConfirmPassword");

  if (!generateButton || !passwordInput || !confirmInput) {
    return;
  }

  function generatePassword(length) {
    var chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%";
    var password = "";

    for (var i = 0; i < length; i += 1) {
      var index = Math.floor(Math.random() * chars.length);
      password += chars.charAt(index);
    }

    return password;
  }

  generateButton.addEventListener("click", function () {
    var nextPassword = generatePassword(12);
    passwordInput.value = nextPassword;
    confirmInput.value = nextPassword;
  });
});
