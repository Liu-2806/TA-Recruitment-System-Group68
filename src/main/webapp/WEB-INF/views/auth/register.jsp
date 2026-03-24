<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/register-preview.jsp");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/login-preview.jsp");
  request.setAttribute("headerBackLabel", "Back to Login");
  request.setAttribute("showHeaderUser", Boolean.FALSE);
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Register | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/login.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/register.css">
</head>
<body>
  <div class="auth-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="auth-stage register-stage">
      <section class="auth-card register-card" aria-labelledby="register-heading">
        <div class="auth-card__hero register-card__hero">
          <h1 id="register-heading" class="auth-card__title">Sign Up as TA</h1>
          <p class="auth-card__subtitle">Please provide your academic credentials to get started</p>
        </div>

        <div class="auth-card__body register-card__body">
          <form class="auth-form register-form" action="#" method="post">
            <div class="register-form__section">
              <div class="register-form__section-header">
                <span class="register-form__section-icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M7.5 5.5h9v13h-9Zm-2 3h13m-7-3v13" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <span class="register-form__section-title">Registration Information</span>
              </div>

              <div class="register-grid">
                <div class="auth-form__group">
                  <label class="auth-label" for="fullName">Full Name</label>
                  <div class="auth-input">
                    <span class="auth-input__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <input id="fullName" name="fullName" type="text" placeholder="Enter your full name" required>
                  </div>
                </div>

                <div class="auth-form__group">
                  <label class="auth-label" for="studentId">Student ID</label>
                  <div class="auth-input">
                    <span class="auth-input__icon auth-input__icon--text" aria-hidden="true">#</span>
                    <input id="studentId" name="studentId" type="text" placeholder="e.g. 20240001" required>
                  </div>
                </div>

                <div class="auth-form__group">
                  <label class="auth-label" for="majorProgram">Major / Program</label>
                  <div class="auth-input">
                    <span class="auth-input__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M4.5 9.5 12 6l7.5 3.5L12 13Zm2.5 1.17V15.5c0 1.1 2.24 2 5 2s5-.9 5-2v-4.83" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <input id="majorProgram" name="majorProgram" type="text" placeholder="e.g. Computer Science" required>
                  </div>
                </div>

                <div class="auth-form__group register-grid__full">
                  <label class="auth-label" for="registerEmail">Email Address</label>
                  <div class="auth-input">
                    <span class="auth-input__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M4.5 7.25h15a1.25 1.25 0 0 1 1.25 1.25v7A1.25 1.25 0 0 1 19.5 16.75h-15A1.25 1.25 0 0 1 3.25 15.5v-7A1.25 1.25 0 0 1 4.5 7.25Zm0 .75L12 12.75 19.5 8" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <input id="registerEmail" name="email" type="email" placeholder="student@university.edu" autocomplete="email" required>
                  </div>
                </div>

                <div class="auth-form__group">
                  <label class="auth-label" for="registerPassword">Password</label>
                  <div class="auth-input">
                    <span class="auth-input__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M7.75 10V8.5a4.25 4.25 0 0 1 8.5 0V10m-9 0h10a1.25 1.25 0 0 1 1.25 1.25v7.25a1.25 1.25 0 0 1-1.25 1.25h-10A1.25 1.25 0 0 1 6 18.5v-7.25A1.25 1.25 0 0 1 7.25 10Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <input id="registerPassword" name="password" type="password" placeholder="Create a password" autocomplete="new-password" required>
                  </div>
                </div>

                <div class="auth-form__group">
                  <label class="auth-label" for="confirmPassword">Confirm Password</label>
                  <div class="auth-input">
                    <span class="auth-input__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M7.75 10V8.5a4.25 4.25 0 0 1 8.5 0V10m-9 0h10a1.25 1.25 0 0 1 1.25 1.25v7.25a1.25 1.25 0 0 1-1.25 1.25h-10A1.25 1.25 0 0 1 6 18.5v-7.25A1.25 1.25 0 0 1 7.25 10Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <input id="confirmPassword" name="confirmPassword" type="password" placeholder="Confirm your password" autocomplete="new-password" required>
                  </div>
                </div>
              </div>
            </div>

            <label class="agreement-box">
              <input id="agreeTerms" name="agreeTerms" type="checkbox" required>
              <span class="agreement-box__check" aria-hidden="true"></span>
              <span class="agreement-box__text">
                I have read and agree to the
                <a href="#">User Agreement</a>
                and
                <a href="#">Privacy Policy</a>.
              </span>
            </label>

            <div class="register-actions">
              <button class="auth-button auth-button--secondary register-button--soft" type="reset" id="resetRegistration">
                <span class="register-button__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M7 7.5V4.75m0 0H4.25M7 4.75 4.75 7M6.5 9.5a7 7 0 1 1-1.2 7" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <span>Reset Fields</span>
              </button>
              <button class="auth-button auth-button--primary register-button--primary" type="submit">
                <span class="register-button__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M12 3.75a8.25 8.25 0 1 0 8.25 8.25A8.25 8.25 0 0 0 12 3.75Zm-1.1 11.4-2.2-2.2 1.05-1.05 1.15 1.15 3.45-3.45 1.05 1.05Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <span>Create Account</span>
              </button>
            </div>
          </form>
        </div>
      </section>
    </main>
  </div>

  <script src="<%= contextPath %>/assets/js/pages/register.js"></script>
</body>
</html>
