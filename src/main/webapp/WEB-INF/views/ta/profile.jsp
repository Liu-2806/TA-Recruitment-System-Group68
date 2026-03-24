<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/ta-dashboard-preview.jsp");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/ta-dashboard-preview.jsp");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", "Zhang San");
  request.setAttribute("currentUserRoleLabel", "TA Applicant");
  request.setAttribute("currentUserInitial", "Z");
  request.setAttribute("notificationCount", Integer.valueOf(1));
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>My Profile | TA Recruitment Portal</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/ta-profile.css">
</head>
<body>
  <div class="ta-profile-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="ta-profile-main">
      <section class="ta-panel">
        <div class="ta-panel__header">
          <span class="ta-panel__header-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <h2>Basic Information (Editable)</h2>
        </div>

        <div class="ta-form-grid">
          <div class="ta-form-field">
            <label for="profileFullName">Full Name</label>
            <div class="ta-form-input">
              <span class="ta-form-input__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <input id="profileFullName" type="text" value="John Doe">
            </div>
          </div>

          <div class="ta-form-field">
            <label for="profileStudentId">Student ID (Read Only)</label>
            <div class="ta-form-input">
              <span class="ta-form-input__icon ta-form-input__icon--text" aria-hidden="true">#</span>
              <input id="profileStudentId" type="text" value="2021001234" readonly>
            </div>
          </div>

          <div class="ta-form-field">
            <label for="profileMajor">Major / Program</label>
            <div class="ta-form-input">
              <span class="ta-form-input__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M4.5 9.5 12 6l7.5 3.5L12 13Zm2.5 1.17V15.5c0 1.1 2.24 2 5 2s5-.9 5-2v-4.83" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <input id="profileMajor" type="text" value="Software Engineering">
            </div>
          </div>

          <div class="ta-form-field">
            <label for="profileYear">Academic Year</label>
            <div class="ta-form-input">
              <span class="ta-form-input__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <input id="profileYear" type="text" value="Year 3">
            </div>
          </div>

          <div class="ta-form-field ta-form-field--full">
            <label for="profileEmail">Email Address</label>
            <div class="ta-form-input">
              <span class="ta-form-input__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M4.5 7.25h15a1.25 1.25 0 0 1 1.25 1.25v7A1.25 1.25 0 0 1 19.5 16.75h-15A1.25 1.25 0 0 1 3.25 15.5v-7A1.25 1.25 0 0 1 4.5 7.25Zm0 .75L12 12.75 19.5 8" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <input id="profileEmail" type="email" value="john.doe@university.edu">
            </div>
          </div>
        </div>
      </section>

      <section class="ta-panel">
        <div class="ta-panel__header">
          <span class="ta-panel__header-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M12 5v14M5 12h14" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <h2>Skill Tags</h2>
        </div>

        <div class="ta-skill-manager">
          <div class="ta-skill-manager__list">
            <span class="ta-skill-pill">Java <button type="button">x</button></span>
            <span class="ta-skill-pill">Python <button type="button">x</button></span>
            <span class="ta-skill-pill">Project Management <button type="button">x</button></span>
            <span class="ta-skill-pill">Communication <button type="button">x</button></span>
          </div>

          <div class="ta-skill-manager__controls">
            <input type="text" placeholder="Add new skill...">
            <button class="ta-skill-manager__add" type="button">
              <span aria-hidden="true">+</span>
              <span>Add</span>
            </button>
          </div>
        </div>
      </section>

      <section class="ta-panel">
        <div class="ta-panel__header">
          <span class="ta-panel__header-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M7.5 4.75h6l3 3v11.5H7.5Zm6 0v3h3" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <h2>Resume Management</h2>
        </div>

        <div class="ta-resume-card">
          <div class="ta-resume-card__file">
            <span class="ta-resume-card__file-icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M7.5 4.75h6l3 3v11.5H7.5Zm6 0v3h3M10 12.25h4m-4 3h4" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <div class="ta-resume-card__file-meta">
              <p>Current Resume</p>
              <strong>resume_john_doe_20260301.pdf</strong>
            </div>
          </div>

          <div class="ta-resume-card__actions">
            <button type="button">Download</button>
            <button type="button">Replace</button>
          </div>
        </div>

        <div class="ta-upload-box">
          <h3>
            <span class="ta-upload-box__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 15V7m0 0 3 3m-3-3-3 3M5.75 15.75v1.5A1.75 1.75 0 0 0 7.5 19h9a1.75 1.75 0 0 0 1.75-1.75v-1.5" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            Upload New Resume
          </h3>

          <div class="ta-upload-box__controls">
            <div class="ta-upload-box__file-input">
              <span>No file selected</span>
              <button type="button">Select File</button>
            </div>
            <button class="ta-upload-box__upload" type="button">
              <span class="ta-upload-box__upload-icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M12 15V7m0 0 3 3m-3-3-3 3M5.75 15.75v1.5A1.75 1.75 0 0 0 7.5 19h9a1.75 1.75 0 0 0 1.75-1.75v-1.5" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>Upload</span>
            </button>
          </div>

          <p class="ta-upload-box__hint">Only PDF files supported, maximum size 5MB.</p>
        </div>
      </section>

      <div class="ta-profile-actions">
        <button class="ta-profile-save" type="button">
          <span class="ta-profile-save__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M7.5 4.75h8l3 3V19H5.5V4.75Zm2 0v4h5v-4M9.5 19v-5h5v5" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>Save All Changes</span>
        </button>
      </div>
    </main>
  </div>
</body>
</html>
