<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/mo-dashboard-preview.jsp");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/mo-dashboard-preview.jsp");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", "Prof. Wang");
  request.setAttribute("currentUserRoleLabel", "Module Organizer");
  request.setAttribute("currentUserInitial", "W");
  request.setAttribute("notificationCount", Integer.valueOf(1));
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
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="mo-profile-main">
      <div class="mo-profile-heading">
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
                </div>
              </div>
              <button class="mo-profile-security__button" type="button" id="openPasswordDialog">Change Password</button>
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

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <div class="mo-password-modal" id="moPasswordModal" aria-hidden="true">
      <div class="mo-password-modal__backdrop" data-close-password-modal="true"></div>
      <div class="mo-password-modal__dialog" role="dialog" aria-modal="true" aria-labelledby="moPasswordDialogTitle">
        <button class="mo-password-modal__close" type="button" id="closePasswordDialog" aria-label="Close password dialog">
          <svg viewBox="0 0 24 24" focusable="false">
            <path d="m7 7 10 10M17 7 7 17" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>

        <div class="mo-password-modal__header">
          <span class="mo-password-modal__eyebrow">Account Security</span>
          <h2 id="moPasswordDialogTitle">Change Password</h2>
          <p>Update your account password here. The new password will be used from your next login.</p>
        </div>

        <form class="mo-password-form" action="#" method="post">
          <div class="mo-password-form__field">
            <label for="currentPassword">Current Password</label>
            <div class="mo-password-form__input">
              <span class="mo-password-form__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.75 10V8.5a4.25 4.25 0 0 1 8.5 0V10m-9 0h10a1.25 1.25 0 0 1 1.25 1.25v7.25a1.25 1.25 0 0 1-1.25 1.25h-10A1.25 1.25 0 0 1 6 18.5v-7.25A1.25 1.25 0 0 1 7.25 10Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <input id="currentPassword" name="currentPassword" type="password" placeholder="Enter current password">
            </div>
          </div>

          <div class="mo-password-form__field">
            <label for="newPassword">New Password</label>
            <div class="mo-password-form__input">
              <span class="mo-password-form__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.75 10V8.5a4.25 4.25 0 0 1 8.5 0V10m-9 0h10a1.25 1.25 0 0 1 1.25 1.25v7.25a1.25 1.25 0 0 1-1.25 1.25h-10A1.25 1.25 0 0 1 6 18.5v-7.25A1.25 1.25 0 0 1 7.25 10Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <input id="newPassword" name="newPassword" type="password" placeholder="Create a new password">
            </div>
          </div>

          <div class="mo-password-form__field">
            <label for="confirmNewPassword">Confirm New Password</label>
            <div class="mo-password-form__input">
              <span class="mo-password-form__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.75 10V8.5a4.25 4.25 0 0 1 8.5 0V10m-9 0h10a1.25 1.25 0 0 1 1.25 1.25v7.25a1.25 1.25 0 0 1-1.25 1.25h-10A1.25 1.25 0 0 1 6 18.5v-7.25A1.25 1.25 0 0 1 7.25 10Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <input id="confirmNewPassword" name="confirmNewPassword" type="password" placeholder="Re-enter the new password">
            </div>
          </div>

          <div class="mo-password-form__tips">
            <p>Recommended: use at least 8 characters and combine letters, numbers, and symbols.</p>
          </div>

          <div class="mo-password-form__actions">
            <button class="mo-password-form__cancel" type="button" id="cancelPasswordDialog">Cancel</button>
            <button class="mo-password-form__submit" type="submit">Update Password</button>
          </div>
        </form>
      </div>
    </div>
  </div>

  <script src="<%= contextPath %>/assets/js/pages/mo-profile-edit.js"></script>
</body>
</html>
