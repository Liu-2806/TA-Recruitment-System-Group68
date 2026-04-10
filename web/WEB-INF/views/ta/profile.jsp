<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/ta/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/ta/dashboard");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  Object profileObj = request.getAttribute("profile");
  java.util.Map profile = profileObj instanceof java.util.Map ? (java.util.Map) profileObj : java.util.Collections.emptyMap();

  String currentUserName = String.valueOf(profile.getOrDefault("fullName", "TA"));
  request.setAttribute("currentUserName", currentUserName);
  request.setAttribute("currentUserRoleLabel", "TA Applicant");
  request.setAttribute("currentUserInitial", currentUserName.isBlank() ? "T" : currentUserName.substring(0, 1).toUpperCase());
  request.setAttribute("notificationCount", Integer.valueOf(0));

  String resumeFileName = String.valueOf(profile.getOrDefault("resumeFileName", ""));
  String resumeUploadedAt = String.valueOf(profile.getOrDefault("resumeUploadedAt", ""));
  boolean hasResume = resumeFileName != null && !resumeFileName.isBlank() && !"null".equalsIgnoreCase(resumeFileName);
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
      <form action="<%= contextPath %>/ta/profile" method="post">
        <section class="ta-panel">
          <div class="ta-panel__header"><h2>Basic Information</h2></div>
          <div class="ta-form-grid">
            <div class="ta-form-field">
              <label for="profileFullName">Full Name</label>
              <div class="ta-form-input"><input id="profileFullName" name="name" type="text" value="<%= String.valueOf(profile.getOrDefault("fullName", "")) %>"></div>
            </div>
            <div class="ta-form-field">
              <label for="profileStudentId">Student ID</label>
              <div class="ta-form-input"><input id="profileStudentId" type="text" value="<%= String.valueOf(profile.getOrDefault("studentId", "")) %>" readonly></div>
            </div>
            <div class="ta-form-field">
              <label for="profileMajor">Major / Program</label>
              <div class="ta-form-input"><input id="profileMajor" name="major" type="text" value="<%= String.valueOf(profile.getOrDefault("majorProgram", "")) %>"></div>
            </div>
            <div class="ta-form-field">
              <label for="profileYear">Academic Year</label>
              <div class="ta-form-input"><input id="profileYear" name="grade" type="text" value="<%= String.valueOf(profile.getOrDefault("academicYear", "")) %>"></div>
            </div>
            <div class="ta-form-field ta-form-field--full">
              <label for="profileEmail">Email Address</label>
              <div class="ta-form-input"><input id="profileEmail" name="email" type="email" value="<%= String.valueOf(profile.getOrDefault("email", "")) %>"></div>
            </div>
            <div class="ta-form-field ta-form-field--full">
              <label for="profilePhone">Phone</label>
              <div class="ta-form-input"><input id="profilePhone" name="phone" type="text" value="<%= String.valueOf(profile.getOrDefault("phone", "")) %>"></div>
            </div>
            <div class="ta-form-field ta-form-field--full">
              <label for="profileIntro">Introduction</label>
              <div class="ta-form-input"><input id="profileIntro" name="intro" type="text" value="<%= String.valueOf(profile.getOrDefault("intro", "")) %>"></div>
            </div>
          </div>
        </section>

        <div class="ta-profile-actions">
          <button class="ta-profile-save" type="submit"><span>Save All Changes</span></button>
        </div>
      </form>

      <section class="ta-panel ta-panel--resume">
        <div class="ta-panel__header ta-panel__header--resume">
          <div class="ta-panel__title-group">
            <p class="ta-panel__eyebrow">Resume</p>
            <h2>Resume Management</h2>
          </div>
          <span class="ta-resume-status <%= hasResume ? "ta-resume-status--ready" : "ta-resume-status--missing" %>">
            <%= hasResume ? "Ready for matching" : "Upload required" %>
          </span>
        </div>

        <div class="ta-resume-layout">
          <article class="ta-resume-summary-card">
            <div class="ta-resume-summary-card__top">
              <span class="ta-resume-summary-card__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.5 4.75h6l3 3v11.5H7.5Zm6 0v3h3" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div class="ta-resume-summary-card__meta">
                <p class="ta-resume-summary-card__label">Current file</p>
                <strong><%= hasResume ? resumeFileName : "No resume uploaded yet" %></strong>
                <p class="ta-resume-summary-card__description">
                  <%= hasResume && resumeUploadedAt != null && !resumeUploadedAt.isBlank()
                      ? "Last updated on " + resumeUploadedAt
                      : "Upload a PDF resume so the system can use it for screening and matching." %>
                </p>
              </div>
            </div>

            <div class="ta-resume-summary-card__details">
              <div class="ta-resume-summary-card__detail">
                <span>Format</span>
                <strong>PDF only</strong>
              </div>
              <div class="ta-resume-summary-card__detail">
                <span>Limit</span>
                <strong>Up to 5 MB</strong>
              </div>
            </div>

            <div class="ta-resume-summary-card__actions">
              <a class="ta-mini-button ta-mini-button--primary" href="<%= hasResume ? contextPath + "/ta/resume/download" : "#" %>" <%= hasResume ? "" : "aria-disabled=\"true\" onclick=\"return false;\" style=\"opacity:0.6; pointer-events:none;\"" %>>Download Resume</a>
              <a class="ta-mini-button ta-mini-button--link" href="#resume-upload">Replace file</a>
            </div>
          </article>

          <form id="taResumeUploadForm" class="ta-upload-box" action="<%= contextPath %>/ta/profile/resume" method="post" enctype="multipart/form-data">
            <div class="ta-upload-box__header" id="resume-upload">
              <h3>Upload a new PDF</h3>
              <p>Choose a single PDF file and submit it to replace the currently stored resume.</p>
            </div>

            <div class="ta-upload-box__controls">
              <input class="ta-upload-box__native-input" id="resumeFileInput" type="file" name="resumeFile" accept="application/pdf" required>
              <div class="ta-upload-box__file-picker" aria-live="polite">
                <label class="ta-upload-box__browse" for="resumeFileInput">Choose PDF</label>
                <div class="ta-upload-box__selected-file">
                  <span class="ta-upload-box__selected-label">Selected file</span>
                  <strong id="resumeFileNameDisplay">No file chosen</strong>
                </div>
              </div>
              <button class="ta-upload-box__upload" type="submit"><span>Upload Resume</span></button>
            </div>

            <p class="ta-upload-box__hint">English labels are custom here, so the browser's default localised file button will not appear.</p>
          </form>
        </div>
      </section>

    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
  <script src="<%= contextPath %>/assets/js/pages/ta-profile.js"></script>
</body>
</html>
