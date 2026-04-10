<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  Object currentUserObj = request.getSession(false) == null ? null : request.getSession(false).getAttribute("currentUser");
  com.bupt.ta.model.User currentUser = currentUserObj instanceof com.bupt.ta.model.User ? (com.bupt.ta.model.User) currentUserObj : null;
  String currentUserName = currentUser == null || currentUser.getDisplayName() == null || currentUser.getDisplayName().trim().isEmpty()
      ? "MO"
      : currentUser.getDisplayName().trim();
  String currentUserInitial = currentUserName.isEmpty() ? "M" : currentUserName.substring(0, 1).toUpperCase();
  Object profileObj = request.getAttribute("profile");
  java.util.Map profile = profileObj instanceof java.util.Map ? (java.util.Map) profileObj : java.util.Collections.emptyMap();
  String moName = String.valueOf(profile.getOrDefault("fullName", currentUserName));
  String moStaffId = String.valueOf(profile.getOrDefault("staffId", "-"));
  String moEmail = String.valueOf(profile.getOrDefault("email", ""));
  String moDepartment = String.valueOf(profile.getOrDefault("department", "School of Software Engineering"));
  String moPhone = String.valueOf(profile.getOrDefault("phone", ""));
  String moDescription = String.valueOf(profile.getOrDefault("description", ""));

  request.setAttribute("headerBrandHref", contextPath + "/mo/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/mo/dashboard");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", currentUserName);
  request.setAttribute("currentUserRoleLabel", "Module Organizer");
  request.setAttribute("currentUserInitial", currentUserInitial);
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

          <form class="mo-profile-form" action="<%= contextPath %>/mo/profile" method="post">
            <div class="mo-profile-field">
              <label for="moName">* Full Name</label>
              <input id="moName" name="fullName" type="text" value="<%= moName %>" required>
            </div>

            <div class="mo-profile-field">
              <label for="moStaffId">* Staff ID</label>
              <div class="mo-profile-readonly">
                <span class="mo-profile-readonly__icon" aria-hidden="true">#</span>
                <span><%= moStaffId %></span>
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
                <input id="moEmail" name="email" type="email" value="<%= moEmail %>" required>
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
                <select id="moDepartment" name="department">
                  <option <%= "School of Software Engineering".equalsIgnoreCase(moDepartment) ? "selected" : "" %>>School of Software Engineering</option>
                  <option <%= "School of Computer Science".equalsIgnoreCase(moDepartment) ? "selected" : "" %>>School of Computer Science</option>
                  <option <%= "School of Artificial Intelligence".equalsIgnoreCase(moDepartment) ? "selected" : "" %>>School of Artificial Intelligence</option>
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
                <input id="moPhone" name="phone" type="text" value="<%= moPhone %>">
              </div>
            </div>
            <div class="mo-profile-actions">
              <button class="mo-profile-actions__save" type="submit">
                <span class="mo-profile-actions__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M7.5 4.75h8l3 3V19H5.5V4.75Zm2 0v4h5v-4M9.5 19v-5h5v5" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <span>Save Basic Info</span>
              </button>
              <a class="mo-profile-actions__cancel" href="<%= contextPath %>/mo/profile">
                <span class="mo-profile-actions__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="m7 7 10 10M17 7 7 17" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <span>Cancel</span>
              </a>
            </div>
          </form>
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

            <form class="mo-profile-description" action="<%= contextPath %>/mo/profile" method="post">
              <input type="hidden" name="fullName" value="<%= moName %>">
              <input type="hidden" name="email" value="<%= moEmail %>">
              <input type="hidden" name="department" value="<%= moDepartment %>">
              <input type="hidden" name="phone" value="<%= moPhone %>">
              <label for="moDescription">Research Interests &amp; Teaching Background</label>
              <textarea id="moDescription" name="description" rows="6"><%= moDescription %></textarea>
              <p>This description will be visible to potential TA applicants to help them understand the module's requirements.</p>
              <div class="mo-profile-actions" style="margin-top: 18px;">
                <button class="mo-profile-actions__save" type="submit">
                  <span class="mo-profile-actions__icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M7.5 4.75h8l3 3V19H5.5V4.75Zm2 0v4h5v-4M9.5 19v-5h5v5" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <span>Save Description</span>
                </button>
                <a class="mo-profile-actions__cancel" href="<%= contextPath %>/mo/profile">
                  <span class="mo-profile-actions__icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="m7 7 10 10M17 7 7 17" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <span>Cancel</span>
                </a>
              </div>
            </form>
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
                  <p style="margin:4px 0 0;color:#667085;">Managed separately in this prototype. Use admin password reset if needed.</p>
                </div>
              </div>
              <button class="mo-profile-security__button" type="button" id="openPasswordDialog">View Guidance</button>
            </div>
          </section>
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

        <form class="mo-password-form" action="<%= contextPath %>/admin/mos" method="get">
          <div class="mo-password-form__tips">
            <p>MO self-service password change is not enabled in this prototype yet.</p>
            <p>For demos, sign in with the seeded account or reset the password from the admin side.</p>
          </div>

          <div class="mo-password-form__actions">
            <button class="mo-password-form__cancel" type="button" id="cancelPasswordDialog">Cancel</button>
            <a class="mo-password-form__submit" href="<%= contextPath %>/admin/mos">Open Admin MO List</a>
          </div>
        </form>
      </div>
    </div>
  </div>

  <script src="<%= contextPath %>/assets/js/pages/mo-profile-edit.js"></script>
</body>
</html>
