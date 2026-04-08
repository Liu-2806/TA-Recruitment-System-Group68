<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/login-preview.jsp");
  request.setAttribute("showHeaderBack", Boolean.FALSE);
  request.setAttribute("showHeaderUser", Boolean.FALSE);
  String errorMessage = String.valueOf(request.getAttribute("errorMessage") == null ? "" : request.getAttribute("errorMessage"));
  String selectedRole = request.getParameter("role");
  if (selectedRole == null || selectedRole.isBlank()) {
    selectedRole = "applicant";
  }
  String emailValue = String.valueOf(request.getParameter("email") == null ? "" : request.getParameter("email"));
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/login.css">
</head>
<body>
  <div class="auth-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="auth-stage">
      <section class="auth-card" aria-labelledby="login-heading">
        <div class="auth-card__hero">
          <h1 id="login-heading" class="auth-card__title">Welcome</h1>
          <p class="auth-card__subtitle">Please sign in to your account</p>
        </div>

        <div class="auth-card__body">
          <%
            if (!errorMessage.isBlank()) {
          %>
          <div style="margin-bottom: 12px; padding: 10px 12px; border-radius: 8px; background: #fff3f3; color: #b42318; border: 1px solid #f5c2c7;">
            <%= errorMessage %>
          </div>
          <%
            }
          %>
          <form class="auth-form" action="<%= contextPath %>/auth/login" method="post">
            <input id="selectedRole" type="hidden" name="role" value="<%= selectedRole %>">

            <div class="auth-form__group">
              <p class="auth-form__section-title">Select Your Role</p>
              <div class="role-selector" role="tablist" aria-label="Role Selection">
                <button class="role-card <%= "applicant".equalsIgnoreCase(selectedRole) ? "is-active" : "" %>" type="button" data-role="applicant" aria-pressed="<%= "applicant".equalsIgnoreCase(selectedRole) ? "true" : "false" %>">
                  <span class="role-card__icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <span class="role-card__label">Applicant</span>
                </button>

                <button class="role-card <%= "mo".equalsIgnoreCase(selectedRole) ? "is-active" : "" %>" type="button" data-role="mo" aria-pressed="<%= "mo".equalsIgnoreCase(selectedRole) ? "true" : "false" %>">
                  <span class="role-card__icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M4.5 8.5h15v11h-15Zm5-4h5v4h-5Zm0 4v11m5-11v11" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <span class="role-card__label">MO</span>
                </button>

                <button class="role-card <%= "admin".equalsIgnoreCase(selectedRole) ? "is-active" : "" %>" type="button" data-role="admin" aria-pressed="<%= "admin".equalsIgnoreCase(selectedRole) ? "true" : "false" %>">
                  <span class="role-card__icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M12 3.8 6 6.3v4.63c0 4.03 2.58 7.73 6 9.27 3.42-1.54 6-5.24 6-9.27V6.3Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <span class="role-card__label">Admin</span>
                </button>
              </div>
            </div>

            <div class="auth-form__group">
              <label class="auth-label" for="email">Email Address</label>
              <div class="auth-input">
                <span class="auth-input__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M4.5 7.25h15a1.25 1.25 0 0 1 1.25 1.25v7A1.25 1.25 0 0 1 19.5 16.75h-15A1.25 1.25 0 0 1 3.25 15.5v-7A1.25 1.25 0 0 1 4.5 7.25Zm0 .75L12 12.75 19.5 8" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <input id="email" name="email" type="email" placeholder="name@university.edu" autocomplete="email" value="<%= emailValue %>" required>
              </div>
            </div>

            <div class="auth-form__group">
              <label class="auth-label" for="password">Password</label>
              <div class="auth-input">
                <span class="auth-input__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M7.75 10V8.5a4.25 4.25 0 0 1 8.5 0V10m-9 0h10a1.25 1.25 0 0 1 1.25 1.25v7.25a1.25 1.25 0 0 1-1.25 1.25h-10A1.25 1.25 0 0 1 6 18.5v-7.25A1.25 1.25 0 0 1 7.25 10Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <input id="password" name="password" type="password" placeholder="........" autocomplete="current-password" required>
              </div>
            </div>

            <button class="auth-button auth-button--primary" type="submit">Login</button>
            <a class="auth-button auth-button--secondary" href="<%= contextPath %>/ta/register">Sign up as TA</a>
          </form>

          <div class="auth-card__footer">
            <span class="auth-card__footer-text">
              By continuing, you agree to the
              <a class="auth-card__footer-link" href="<%= contextPath %>/legal?type=agreement#user-agreement">User Agreement</a>
              and
              <a class="auth-card__footer-link" href="<%= contextPath %>/legal?type=privacy#privacy-policy">Privacy Policy</a>.
            </span>
          </div>

        </div>
      </section>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>

  <script src="<%= contextPath %>/assets/js/pages/login.js"></script>
</body>
</html>
