<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/ta/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/ta/dashboard");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  Object profileObj = request.getAttribute("profile");
  java.util.Map profile = profileObj instanceof java.util.Map ? (java.util.Map) profileObj : null;
  Object skillTagsObj = request.getAttribute("allSkillTags");
  java.util.List allSkillTags = skillTagsObj instanceof java.util.List ? (java.util.List) skillTagsObj : java.util.Collections.emptyList();
  Object selectedSkillsObj = profile == null ? null : profile.get("skills");
  java.util.List selectedSkills = selectedSkillsObj instanceof java.util.List ? (java.util.List) selectedSkillsObj : java.util.Collections.emptyList();

  String currentUserName = profile == null ? "TA" : String.valueOf(profile.getOrDefault("fullName", "TA"));
  request.setAttribute("currentUserName", currentUserName);
  request.setAttribute("currentUserRoleLabel", "TA Applicant");
  request.setAttribute("currentUserInitial", currentUserName == null || currentUserName.isBlank() ? "T" : currentUserName.substring(0, 1).toUpperCase());
  request.setAttribute("notificationCount", Integer.valueOf(0));
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
      <%
        Object errorObj = request.getAttribute("errorMessage");
        String errorMessage = errorObj == null ? "" : String.valueOf(errorObj);
        if (errorMessage != null && !errorMessage.isBlank()) {
      %>
      <section class="ta-panel">
        <div class="ta-panel__header">
          <h2>Update failed</h2>
        </div>
        <p style="color:#b42318; margin: 0;"><%= errorMessage %></p>
      </section>
      <%
        }
      %>

      <form action="<%= contextPath %>/ta/profile" method="post">
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
              <input id="profileFullName" name="name" type="text" value="<%= profile == null ? "" : String.valueOf(profile.getOrDefault("fullName", "")) %>">
            </div>
          </div>

          <div class="ta-form-field">
            <label for="profileStudentId">Student ID (Read Only)</label>
            <div class="ta-form-input">
              <span class="ta-form-input__icon ta-form-input__icon--text" aria-hidden="true">#</span>
              <input id="profileStudentId" type="text" value="<%= profile == null ? "" : String.valueOf(profile.getOrDefault("studentId", "")) %>" readonly>
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
              <input id="profileMajor" name="major" type="text" value="<%= profile == null ? "" : String.valueOf(profile.getOrDefault("majorProgram", "")) %>">
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
              <input id="profileYear" name="grade" type="text" value="<%= profile == null ? "" : String.valueOf(profile.getOrDefault("academicYear", "")) %>">
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
              <input id="profileEmail" name="email" type="email" value="<%= profile == null ? "" : String.valueOf(profile.getOrDefault("email", "")) %>">
            </div>
          </div>

          <div class="ta-form-field ta-form-field--full">
            <label for="profilePhone">Phone</label>
            <div class="ta-form-input">
              <span class="ta-form-input__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7 4.5h2.25l1.25 4-1.5 1.25c1 2 2.5 3.5 4.5 4.5L15 12.75l4 1.25V16c0 1-1 2-2.25 2C10.75 18 6 13.25 6 6.75 6 5.5 6.95 4.5 7 4.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <input id="profilePhone" name="phone" type="text" value="<%= profile == null ? "" : String.valueOf(profile.getOrDefault("phone", "")) %>">
            </div>
          </div>

          <div class="ta-form-field ta-form-field--full">
            <label for="profileIntro">Introduction</label>
            <div class="ta-form-input">
              <span class="ta-form-input__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.5 7.5h9M7.5 12h9M7.5 16.5h6" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <input id="profileIntro" name="intro" type="text" value="<%= profile == null ? "" : String.valueOf(profile.getOrDefault("intro", "")) %>">
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
            <%
              if (allSkillTags.isEmpty()) {
            %>
            <p style="margin:0;">No skill tags configured.</p>
            <%
              } else {
                for (Object tagObj : allSkillTags) {
                  String tag = String.valueOf(tagObj);
                  boolean checked = selectedSkills != null && selectedSkills.contains(tag);
            %>
            <label class="ta-skill-pill" style="cursor:pointer;">
              <input type="checkbox" name="skillTags" value="<%= tag %>" <%= checked ? "checked" : "" %> style="margin-right:8px;">
              <%= tag %>
            </label>
            <%
                }
              }
            %>
          </div>

          <div class="ta-skill-manager__controls" style="opacity:0.65;">
            <input type="text" placeholder="Add new skill... (not supported in this iteration)" disabled>
            <button class="ta-skill-manager__add" type="button" disabled>
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
              <strong><%= profile == null ? "No resume uploaded" : String.valueOf(profile.getOrDefault("resumeFileName", "No resume uploaded")) %></strong>
            </div>
          </div>

          <div class="ta-resume-card__actions">
            <button type="button" disabled>Download</button>
            <button type="button" disabled>Replace</button>
          </div>
        </div>

        <div class="ta-upload-box" id="resume-upload">
          <h3>
            <span class="ta-upload-box__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 15V7m0 0 3 3m-3-3-3 3M5.75 15.75v1.5A1.75 1.75 0 0 0 7.5 19h9a1.75 1.75 0 0 0 1.75-1.75v-1.5" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            Upload New Resume
          </h3>

          <div class="ta-upload-box__controls">
            <div class="ta-upload-box__file-input" style="display:flex; gap:12px; align-items:center;">
              <input type="file" name="resumeFile" form="taResumeUploadForm" accept="application/pdf" required>
            </div>
            <button class="ta-upload-box__upload" type="submit" form="taResumeUploadForm">
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
        <button class="ta-profile-save" type="submit">
          <span class="ta-profile-save__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M7.5 4.75h8l3 3V19H5.5V4.75Zm2 0v4h5v-4M9.5 19v-5h5v5" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>Save All Changes</span>
        </button>
      </div>
      </form>

      <form id="taResumeUploadForm" action="<%= contextPath %>/ta/profile/resume" method="post" enctype="multipart/form-data"></form>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
