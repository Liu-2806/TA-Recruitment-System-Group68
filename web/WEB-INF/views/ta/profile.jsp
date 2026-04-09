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
  Object skillTagsObj = request.getAttribute("allSkillTags");
  java.util.List allSkillTags = skillTagsObj instanceof java.util.List ? (java.util.List) skillTagsObj : java.util.Collections.emptyList();
  java.util.List selectedSkills = profile.get("skills") instanceof java.util.List ? (java.util.List) profile.get("skills") : java.util.Collections.emptyList();
  java.util.Map extractedResume = profile.get("extractedResume") instanceof java.util.Map ? (java.util.Map) profile.get("extractedResume") : java.util.Collections.emptyMap();

  String currentUserName = String.valueOf(profile.getOrDefault("fullName", "TA"));
  request.setAttribute("currentUserName", currentUserName);
  request.setAttribute("currentUserRoleLabel", "TA Applicant");
  request.setAttribute("currentUserInitial", currentUserName.isBlank() ? "T" : currentUserName.substring(0, 1).toUpperCase());
  request.setAttribute("notificationCount", Integer.valueOf(0));

  String resumeFileName = String.valueOf(profile.getOrDefault("resumeFileName", ""));
  String resumeUploadedAt = String.valueOf(profile.getOrDefault("resumeUploadedAt", ""));
  boolean hasResume = resumeFileName != null && !resumeFileName.isBlank() && !"null".equalsIgnoreCase(resumeFileName);
  boolean profileSaved = "1".equals(request.getParameter("saved"));
  boolean resumeUpdated = "1".equals(request.getParameter("resumeUpdated"));
  boolean resumeUploadFailed = Boolean.TRUE.equals(request.getAttribute("resumeUploadFailed"));
  boolean hasExtractedResume = !extractedResume.isEmpty();
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
        if (!errorMessage.isBlank()) {
      %>
      <section class="ta-panel">
        <div class="ta-panel__header"><h2><%= resumeUploadFailed ? "Resume upload failed" : "Update failed" %></h2></div>
        <p style="color:#b42318; margin: 0;"><%= errorMessage %></p>
      </section>
      <%
        }
        if (profileSaved) {
      %>
      <section class="ta-panel">
        <div class="ta-panel__header"><h2>Profile updated</h2></div>
        <p style="color:#027a48; margin: 0;">Your TA profile details were saved successfully.</p>
      </section>
      <%
        }
        if (resumeUpdated) {
      %>
      <section class="ta-panel">
        <div class="ta-panel__header"><h2>Resume updated</h2></div>
        <p style="color:#027a48; margin: 0;">Your PDF resume was uploaded, extracted, and linked to your profile.</p>
      </section>
      <%
        }
      %>

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

        <section class="ta-panel">
          <div class="ta-panel__header"><h2>Skill Tags</h2></div>
          <div class="ta-skill-manager">
            <div class="ta-skill-manager__list">
              <%
                for (Object tagObj : allSkillTags) {
                  String tag = String.valueOf(tagObj);
                  boolean checked = selectedSkills.contains(tag);
              %>
              <label class="ta-skill-pill" style="cursor:pointer;">
                <input type="checkbox" name="skillTags" value="<%= tag %>" <%= checked ? "checked" : "" %> style="margin-right:8px;">
                <%= tag %>
              </label>
              <%
                }
              %>
            </div>
          </div>
        </section>

        <div class="ta-profile-actions">
          <button class="ta-profile-save" type="submit"><span>Save All Changes</span></button>
        </div>
      </form>

      <section class="ta-panel">
        <div class="ta-panel__header"><h2>Resume Management</h2></div>

        <div class="ta-resume-card">
          <div class="ta-resume-card__file">
            <div class="ta-resume-card__file-meta">
              <p>Current Resume</p>
              <strong><%= hasResume ? resumeFileName : "No resume uploaded" %></strong>
              <p><%
                if (hasResume && resumeUploadedAt != null && !resumeUploadedAt.isBlank()) {
                  out.print("Uploaded at " + resumeUploadedAt);
                } else if (hasResume) {
                  out.print("Resume linked to your profile. Upload again to refresh the extracted summary.");
                } else {
                  out.print("Upload a PDF resume to enable extraction and matching.");
                }
              %></p>
            </div>
          </div>
          <div class="ta-resume-card__actions">
            <a class="ta-mini-button ta-mini-button--primary" href="<%= hasResume ? contextPath + "/ta/resume/download" : "#" %>" <%= hasResume ? "" : "aria-disabled=\"true\" onclick=\"return false;\" style=\"opacity:0.6; pointer-events:none;\"" %>>Download</a>
            <a class="ta-mini-button ta-mini-button--link" href="#resume-upload">Replace</a>
          </div>
        </div>

        <form id="taResumeUploadForm" action="<%= contextPath %>/ta/profile/resume" method="post" enctype="multipart/form-data">
          <div class="ta-upload-box" id="resume-upload">
            <h3>Upload New Resume</h3>
            <div class="ta-upload-box__controls">
              <input type="file" name="resumeFile" accept="application/pdf" required>
              <button class="ta-upload-box__upload" type="submit"><span>Upload</span></button>
            </div>
            <p class="ta-upload-box__hint">Only PDF files supported, maximum size 5MB.</p>
          </div>
        </form>
      </section>

      <section class="ta-panel">
        <div class="ta-panel__header"><h2>Extracted Resume Summary</h2></div>
        <%
          if (!hasResume) {
        %>
        <p style="margin: 0 0 20px; color: #475467;">No structured resume data is available yet. Upload a PDF resume to generate the extracted summary used for matching.</p>
        <%
          } else if (!hasExtractedResume) {
        %>
        <p style="margin: 0 0 20px; color: #475467;">A resume file is linked to your profile, but no structured fields are available yet. Re-upload the PDF if extraction did not finish correctly.</p>
        <%
          }
        %>
        <div class="ta-form-grid">
          <div class="ta-form-field">
            <label>Name</label>
            <div class="ta-form-input"><input type="text" value="<%= String.valueOf(extractedResume.getOrDefault("name", "")) %>" readonly></div>
          </div>
          <div class="ta-form-field">
            <label>Email</label>
            <div class="ta-form-input"><input type="text" value="<%= String.valueOf(extractedResume.getOrDefault("email", "")) %>" readonly></div>
          </div>
          <div class="ta-form-field">
            <label>Phone</label>
            <div class="ta-form-input"><input type="text" value="<%= String.valueOf(extractedResume.getOrDefault("phone", "")) %>" readonly></div>
          </div>
          <div class="ta-form-field ta-form-field--full">
            <label>Education</label>
            <div class="ta-form-input"><input type="text" value="<%= String.valueOf(extractedResume.getOrDefault("education", "")) %>" readonly></div>
          </div>
          <div class="ta-form-field ta-form-field--full">
            <label>Extracted Skills</label>
            <div class="ta-form-input"><input type="text" value="<%= extractedResume.get("skills") instanceof java.util.List && !((java.util.List) extractedResume.get("skills")).isEmpty() ? String.join(", ", (java.util.List<String>) extractedResume.get("skills")) : "No structured skills extracted yet" %>" readonly></div>
          </div>
          <div class="ta-form-field ta-form-field--full">
            <label>Experience Highlights</label>
            <div class="ta-form-input"><input type="text" value="<%= extractedResume.get("experienceHighlights") instanceof java.util.List && !((java.util.List) extractedResume.get("experienceHighlights")).isEmpty() ? String.join(" | ", (java.util.List<String>) extractedResume.get("experienceHighlights")) : "No experience highlights extracted yet" %>" readonly></div>
          </div>
        </div>
      </section>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
