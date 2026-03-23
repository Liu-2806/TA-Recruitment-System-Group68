<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>MO Profile | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/mo-profile-edit.css">
</head>
<body>
  <div class="mo-profile-shell">
    <header class="mo-profile-header">
      <div class="mo-profile-header__inner">
        <a class="app-brand" href="<%= contextPath %>/mo-dashboard-preview.jsp">
          <span class="app-brand__mark">T</span>
          <span class="app-brand__text">TA Recruitment System</span>
        </a>

        <div class="mo-profile-search">
          <span class="mo-profile-search__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M10.75 17a6.25 6.25 0 1 0 0-12.5 6.25 6.25 0 0 0 0 12.5Zm8.75 2.5-4.25-4.25" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <input type="text" placeholder="Quick search...">
        </div>

        <div class="mo-profile-header__actions">
          <button class="ui-icon-button" type="button" aria-label="Notifications">
            <span class="mo-icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 4.25a4 4 0 0 0-4 4v2.06c0 .7-.2 1.39-.58 1.98L6 14.5h12l-1.42-2.21a3.75 3.75 0 0 1-.58-1.98V8.25a4 4 0 0 0-4-4Zm0 15.5a2.38 2.38 0 0 0 2.27-1.75H9.73A2.38 2.38 0 0 0 12 19.75Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <span class="ui-notification-dot"></span>
          </button>

          <div class="ui-user-block">
            <div class="ui-user-block__meta">
              <p class="ui-user-block__name">Prof. Wang</p>
              <p class="ui-user-block__role">Module Organizer</p>
            </div>
            <span class="ui-avatar" aria-hidden="true">W</span>
            <span class="ui-chevron" aria-hidden="true">v</span>
          </div>
        </div>
      </div>
    </header>

    <main class="mo-profile-main">
      <div class="mo-profile-heading">
        <a class="mo-profile-heading__back" href="<%= contextPath %>/mo-dashboard-preview.jsp">
          <span class="mo-profile-heading__back-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M15.5 6.5 10 12l5.5 5.5M11 12h8" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>Back to Dashboard</span>
        </a>
        <h1>My Profile</h1>
      </div>

      <div class="mo-profile-grid">
        <section class="mo-profile-card mo-profile-card--basic">
          <div class="mo-profile-card__titlebar">
            <h2>
              <span class="mo-profile-card__title-accent"></span>
              Basic Information
            </h2>
            <span class="mo-profile-card__badge">Editable</span>
          </div>

          <div class="mo-profile-form">
            <div class="mo-profile-field">
              <label for="moName">* Full Name</label>
              <input id="moName" type="text" value="Prof. Wang">
            </div>

            <div class="mo-profile-field">
              <label for="moStaffId">* Staff ID</label>
              <div class="mo-profile-readonly">
                <span class="mo-profile-readonly__icon" aria-hidden="true">#</span>
                <span>M001</span>
                <span class="mo-profile-readonly__tag">READ ONLY</span>
              </div>
            </div>

            <div class="mo-profile-field">
              <label for="moEmail">* Email Address</label>
              <div class="mo-profile-input">
                <span class="mo-profile-input__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M4.75 7.25h14.5a1.25 1.25 0 0 1 1.25 1.25v7a1.25 1.25 0 0 1-1.25 1.25H4.75A1.25 1.25 0 0 1 3.5 15.5v-7a1.25 1.25 0 0 1 1.25-1.25Zm0 .75L12 12.75 19.25 8" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <input id="moEmail" type="email" value="wang@bupt.edu">
              </div>
            </div>

            <div class="mo-profile-field">
              <label for="moDepartment">Department</label>
              <div class="mo-profile-input mo-profile-input--select">
                <span class="mo-profile-input__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <select id="moDepartment">
                  <option>School of Software Engineering</option>
                  <option>School of Computer Science</option>
                  <option>School of Artificial Intelligence</option>
                </select>
                <span class="mo-profile-input__caret" aria-hidden="true">v</span>
              </div>
            </div>

            <div class="mo-profile-field">
              <label for="moPhone">Contact Phone</label>
              <div class="mo-profile-input">
                <span class="mo-profile-input__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M6.75 4.75h2.5l1.5 4-1.75 1.75a12.2 12.2 0 0 0 4.5 4.5l1.75-1.75 4 1.5v2.5A1.75 1.75 0 0 1 17.5 19 13.5 13.5 0 0 1 4 5.5 1.75 1.75 0 0 1 5.75 3.75Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <input id="moPhone" type="text" value="123-4567-8901">
              </div>
            </div>
          </div>
        </section>

        <section class="mo-profile-side">
          <section class="mo-profile-card">
            <div class="mo-profile-card__titlebar">
              <h2>
                <span class="mo-profile-card__title-accent"></span>
                Personal Description
                <span class="mo-profile-card__optional">(Optional)</span>
              </h2>
            </div>

            <div class="mo-profile-description">
              <label for="moDescription">Research Interests &amp; Teaching Background</label>
              <textarea id="moDescription" rows="6">Main research interests: Software Engineering, Agile Development, Human-Computer Interaction.</textarea>
              <p>This description will be visible to potential TA applicants to help them understand the module's requirements.</p>
            </div>
          </section>

          <section class="mo-profile-card">
            <div class="mo-profile-card__titlebar">
              <h2>
                <span class="mo-profile-card__title-accent"></span>
                Account Security
              </h2>
            </div>

            <div class="mo-profile-security">
              <div class="mo-profile-security__info">
                <span class="mo-profile-security__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M7.75 10V8.5a4.25 4.25 0 0 1 8.5 0V10m-9 0h10a1.25 1.25 0 0 1 1.25 1.25v7.25a1.25 1.25 0 0 1-1.25 1.25h-10A1.25 1.25 0 0 1 6 18.5v-7.25A1.25 1.25 0 0 1 7.25 10Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <div>
                  <strong>Login Password</strong>
                  <p>Last login: 2026-03-18 14:20:05</p>
                </div>
              </div>
              <button class="mo-profile-security__button" type="button">Change Password</button>
            </div>
          </section>

          <div class="mo-profile-actions">
            <button class="mo-profile-actions__save" type="button">
              <span class="mo-profile-actions__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.5 4.75h8l3 3V19H5.5V4.75Zm2 0v4h5v-4M9.5 19v-5h5v5" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>Save Changes</span>
            </button>
            <button class="mo-profile-actions__cancel" type="button">
              <span class="mo-profile-actions__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="m7 7 10 10M17 7 7 17" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>Cancel</span>
            </button>
          </div>
        </section>
      </div>
    </main>

    <footer class="mo-profile-footer">
      <p>&copy; 2026 TA Recruitment System - BUPT Department HR</p>
    </footer>
  </div>
</body>
</html>
