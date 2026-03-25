<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/mo-dashboard-preview.jsp");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/mo-dashboard-preview.jsp");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", "Prof. James Wang");
  request.setAttribute("currentUserRoleLabel", "Module Organizer");
  request.setAttribute("currentUserInitial", "J");
  request.setAttribute("notificationCount", Integer.valueOf(1));
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Post New Position | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/mo-post-position.css">
</head>
<body>
  <div class="mo-post-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="mo-post-main">
      <section class="mo-post-card">
        <div class="mo-post-card__header">
          <div class="mo-post-card__title">
            <span class="mo-post-card__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 5v14M5 12h14" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <h2>Position Details</h2>
          </div>
          <span class="mo-post-card__badge">* Required Fields</span>
        </div>

        <form class="mo-post-form" action="#" method="post">
          <div class="mo-post-grid">
            <div class="mo-post-field">
              <label for="courseName">* Course Name</label>
              <div class="mo-post-input">
                <span class="mo-post-input__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <input id="courseName" type="text" placeholder="e.g. Software Engineering">
              </div>
            </div>

            <div class="mo-post-field">
              <label for="courseCode">* Course Code</label>
              <div class="mo-post-input">
                <span class="mo-post-input__icon mo-post-input__icon--text" aria-hidden="true">#</span>
                <input id="courseCode" type="text" placeholder="e.g. SE3001">
              </div>
            </div>

            <div class="mo-post-field">
              <label for="vacancies">* Number of Vacancies</label>
              <div class="mo-post-input">
                <input id="vacancies" type="number" value="3">
                <span class="mo-post-input__suffix">Persons</span>
              </div>
            </div>

            <div class="mo-post-field">
              <label for="deadline">* Application Deadline</label>
              <div class="mo-post-input">
                <input id="deadline" type="text" value="2026/03/30">
                <span class="mo-post-input__calendar" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
              </div>
            </div>

            <div class="mo-post-field mo-post-field--full">
              <label for="positionDescription">* Position Description</label>
              <textarea id="positionDescription" rows="5" placeholder="Briefly describe the responsibilities..."></textarea>
            </div>

            <div class="mo-post-field mo-post-field--full">
              <div class="mo-post-field__label-row">
                <label for="requiredSkills">* Required Skills</label>
                <span>Use commas to separate</span>
              </div>
              <input id="requiredSkills" type="text" placeholder="Java, Python, Communication Skills...">
            </div>

            <div class="mo-post-field mo-post-field--full">
              <label for="estimatedWorkload">Estimated Workload</label>
              <div class="mo-post-input">
                <input id="estimatedWorkload" type="number" value="6">
                <span class="mo-post-input__suffix">Hours / Week</span>
              </div>
            </div>
          </div>

          <div class="mo-post-actions">
            <button class="mo-post-actions__primary" type="submit">
              <span class="mo-post-actions__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="m4.75 12 14.5-6.5-4.75 13-3-5.25L4.75 12Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>Post Position</span>
            </button>

            <button class="mo-post-actions__secondary" type="button">
              <span class="mo-post-actions__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.5 4.75h8l3 3V19H5.5V4.75Zm2 0v4h5v-4M9.5 19v-5h5v5" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>Save Draft</span>
            </button>

            <button class="mo-post-actions__cancel" type="button">
              <span class="mo-post-actions__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="m7 7 10 10M17 7 7 17" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>Cancel</span>
            </button>
          </div>
        </form>
      </section>

      <div class="mo-post-tip">
        <span class="mo-post-tip__icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" focusable="false">
            <path d="M12 8.5v4.5m0 3h.01M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </span>
        <p><strong>Pro Tip:</strong> Once a position is posted, it will be visible to all eligible students immediately. You can track applicant progress from your dashboard.</p>
      </div>
    </main>
  </div>
</body>
</html>
