<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/admin-dashboard-preview.jsp");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/admin-dashboard-preview.jsp");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", "Super Admin");
  request.setAttribute("currentUserRoleLabel", "Online");
  request.setAttribute("currentUserInitial", "A");
  request.setAttribute("notificationCount", Integer.valueOf(1));
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Create MO Account | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/admin-dashboard.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/admin-create-mo.css">
</head>
<body>
  <div class="admin-create-shell">
    <aside class="admin-sidebar">
      <div class="admin-sidebar__brand">
        <span class="app-brand__mark">T</span>
        <span class="admin-sidebar__brand-text">Admin Portal</span>
      </div>

      <nav class="admin-sidebar__nav" aria-label="Admin Navigation">
        <a class="admin-sidebar__link" href="<%= contextPath %>/admin-dashboard-preview.jsp">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M4.75 4.75h6.5v6.5h-6.5Zm8 0h6.5v6.5h-6.5Zm-8 8h6.5v6.5h-6.5Zm8 0h6.5v6.5h-6.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>Dashboard</span>
        </a>

        <a class="admin-sidebar__link is-active" href="<%= contextPath %>/admin-create-mo-preview.jsp">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22ZM18.5 5.5v6m-3-3h6" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>Create MO Account</span>
        </a>

        <a class="admin-sidebar__link" href="<%= contextPath %>/admin-all-mos-preview.jsp">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M8.5 10.5a3 3 0 1 0-3-3 3 3 0 0 0 3 3Zm7 0a3 3 0 1 0-3-3 3 3 0 0 0 3 3ZM8.5 12c-2.52 0-4.5 1.37-4.5 3.06V16h9v-.94C13 13.37 11.02 12 8.5 12Zm7 0c-.87 0-1.68.14-2.4.4 1.14.65 1.9 1.62 1.9 2.66V16H20v-.94c0-1.69-1.98-3.06-4.5-3.06Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>All MOs</span>
        </a>

        <a class="admin-sidebar__link" href="#">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>All Postings</span>
        </a>

        <a class="admin-sidebar__link" href="<%= contextPath %>/admin-ta-workload-preview.jsp">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>All TA Workload</span>
        </a>

      </nav>
    </aside>

    <div class="admin-create-main">
      <jsp:include page="/WEB-INF/views/common/header.jsp" />

      <main class="admin-create-content">
        <section class="admin-create-heading">
          <h1>Create New MO Account</h1>
          <p>Please provide credentials for the new Module Organizer.</p>
        </section>

        <section class="admin-create-card">
          <div class="admin-create-card__header">
            <div class="admin-create-card__title">
              <span class="admin-create-card__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22ZM18.5 5.5v6m-3-3h6" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <h2>Account Information</h2>
            </div>
            <span class="admin-create-card__badge">* Required Fields</span>
          </div>

          <form class="admin-create-form" action="#" method="post">
            <div class="admin-create-grid">
              <div class="admin-create-field">
                <label for="moFullName">* Full Name</label>
                <div class="admin-create-input">
                  <span class="admin-create-input__icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <input id="moFullName" type="text" placeholder="e.g. Prof. Zhang San">
                </div>
              </div>

              <div class="admin-create-field">
                <label for="moStaffId">* Staff ID</label>
                <div class="admin-create-input">
                  <span class="admin-create-input__icon admin-create-input__icon--text" aria-hidden="true">#</span>
                  <input id="moStaffId" type="text" placeholder="e.g. 2024MO01">
                </div>
              </div>

              <div class="admin-create-field admin-create-field--full">
                <label for="moEmail">* Email Address</label>
                <div class="admin-create-input">
                  <span class="admin-create-input__icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M4.75 7.25h14.5a1.25 1.25 0 0 1 1.25 1.25v7a1.25 1.25 0 0 1-1.25 1.25H4.75A1.25 1.25 0 0 1 3.5 15.5v-7a1.25 1.25 0 0 1 1.25-1.25Zm0 .75L12 12.75 19.25 8" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <input id="moEmail" type="email" placeholder="e.g. zhangsan@university.edu">
                </div>
              </div>

              <div class="admin-create-field">
                <label for="moInitialPassword">* Initial Password</label>
                <div class="admin-create-input admin-create-input--password">
                  <span class="admin-create-input__icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M7.75 10V8.5a4.25 4.25 0 0 1 8.5 0V10m-9 0h10a1.25 1.25 0 0 1 1.25 1.25v7.25a1.25 1.25 0 0 1-1.25 1.25h-10A1.25 1.25 0 0 1 6 18.5v-7.25A1.25 1.25 0 0 1 7.25 10Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <input id="moInitialPassword" type="text" placeholder="Enter password">
                  <button class="admin-create-input__random" id="generatePassword" type="button" aria-label="Generate random password">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M7 7.5V4.75m0 0H4.25M7 4.75 4.75 7M6.5 9.5a7 7 0 1 1-1.2 7" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </button>
                </div>
              </div>

              <div class="admin-create-field">
                <label for="moConfirmPassword">Confirm Password</label>
                <div class="admin-create-input">
                  <span class="admin-create-input__icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M7.75 10V8.5a4.25 4.25 0 0 1 8.5 0V10m-9 0h10a1.25 1.25 0 0 1 1.25 1.25v7.25a1.25 1.25 0 0 1-1.25 1.25h-10A1.25 1.25 0 0 1 6 18.5v-7.25A1.25 1.25 0 0 1 7.25 10Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <input id="moConfirmPassword" type="text" placeholder="Repeat password">
                </div>
              </div>

              <div class="admin-create-field">
                <label for="moDepartment">Department</label>
                <div class="admin-create-input admin-create-input--select">
                  <span class="admin-create-input__icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <select id="moDepartment">
                    <option>Software Engineering</option>
                    <option>Computer Science</option>
                    <option>Artificial Intelligence</option>
                  </select>
                  <span class="admin-create-input__caret" aria-hidden="true">v</span>
                </div>
              </div>

              <div class="admin-create-field">
                <label for="moPhone">Contact Phone</label>
                <div class="admin-create-input">
                  <span class="admin-create-input__icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M6.75 4.75h2.5l1.5 4-1.75 1.75a12.2 12.2 0 0 0 4.5 4.5l1.75-1.75 4 1.5v2.5A1.75 1.75 0 0 1 17.5 19 13.5 13.5 0 0 1 4 5.5 1.75 1.75 0 0 1 5.75 3.75Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <input id="moPhone" type="text" placeholder="e.g. +1 234 567 890">
                </div>
              </div>
            </div>

            <div class="admin-create-actions">
              <button class="admin-create-actions__primary" type="submit">
                <span class="admin-create-actions__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22ZM18.5 5.5v6m-3-3h6" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <span>Create Account</span>
              </button>

              <button class="admin-create-actions__secondary" type="reset">
                <span class="admin-create-actions__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M7 7.5V4.75m0 0H4.25M7 4.75 4.75 7M6.5 9.5a7 7 0 1 1-1.2 7" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <span>Reset</span>
              </button>
            </div>
          </form>
        </section>
      </main>

      <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </div>
  </div>

  <script src="<%= contextPath %>/assets/js/pages/admin-create-mo.js"></script>
</body>
</html>
