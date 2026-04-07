<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  Object applicationObj = request.getAttribute("application");
  java.util.Map application = applicationObj instanceof java.util.Map ? (java.util.Map) applicationObj : java.util.Collections.emptyMap();
  java.util.Map job = application.get("job") instanceof java.util.Map ? (java.util.Map) application.get("job") : java.util.Collections.emptyMap();
  java.util.Map taProfile = application.get("taProfile") instanceof java.util.Map ? (java.util.Map) application.get("taProfile") : java.util.Collections.emptyMap();
  java.util.List matchedSkills = application.get("matchedSkills") instanceof java.util.List ? (java.util.List) application.get("matchedSkills") : java.util.Collections.emptyList();
  java.util.List missingSkills = application.get("missingSkills") instanceof java.util.List ? (java.util.List) application.get("missingSkills") : java.util.Collections.emptyList();

  request.setAttribute("headerBrandHref", contextPath + "/mo/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/mo/jobs/applicants?jobId=" + String.valueOf(job.getOrDefault("postingId", "")));
  request.setAttribute("headerBackLabel", "Back to List");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", "MO");
  request.setAttribute("currentUserRoleLabel", "Module Organizer");
  request.setAttribute("currentUserInitial", "M");
  request.setAttribute("notificationCount", Integer.valueOf(0));

  String resumeFileName = String.valueOf(taProfile.getOrDefault("resumeFileName", ""));
  boolean hasResume = resumeFileName != null && !resumeFileName.isBlank() && !"null".equalsIgnoreCase(resumeFileName);
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Applicant Profile | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/mo-applicant-details.css">
</head>
<body>
  <div class="mo-applicant-details-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="mo-applicant-details-main">
      <section class="mo-applicant-panel">
        <div class="mo-applicant-panel__header"><h2>Basic Information</h2></div>
        <div class="mo-applicant-info-grid">
          <div class="mo-applicant-info-item"><p>Name</p><strong><%= String.valueOf(application.getOrDefault("taName", "")) %></strong></div>
          <div class="mo-applicant-info-item"><p>Student ID</p><strong><%= String.valueOf(taProfile.getOrDefault("studentId", "")) %></strong></div>
          <div class="mo-applicant-info-item"><p>Major</p><strong><%= String.valueOf(taProfile.getOrDefault("majorProgram", "")) %></strong></div>
          <div class="mo-applicant-info-item"><p>Year</p><strong><%= String.valueOf(taProfile.getOrDefault("academicYear", "")) %></strong></div>
          <div class="mo-applicant-info-item mo-applicant-info-item--full"><p>Email</p><strong><%= String.valueOf(taProfile.getOrDefault("email", "")) %></strong></div>
        </div>
      </section>

      <section class="mo-applicant-panel">
        <div class="mo-applicant-panel__header"><h2>Application Summary</h2></div>
        <div class="mo-applicant-info-grid">
          <div class="mo-applicant-info-item"><p>Posting</p><strong><%= String.valueOf(job.getOrDefault("courseName", "")) %></strong></div>
          <div class="mo-applicant-info-item"><p>Status</p><strong><%= String.valueOf(application.getOrDefault("statusLabel", application.getOrDefault("status", ""))) %></strong></div>
          <div class="mo-applicant-info-item mo-applicant-info-item--full"><p>Statement</p><strong><%= String.valueOf(application.getOrDefault("statement", "")) %></strong></div>
        </div>
      </section>

      <section class="mo-applicant-panel">
        <div class="mo-applicant-panel__header"><h2>Skill Tags</h2></div>
        <div class="mo-applicant-skill-list">
          <%
            java.util.List skills = taProfile.get("skills") instanceof java.util.List ? (java.util.List) taProfile.get("skills") : java.util.Collections.emptyList();
            for (Object skillObj : skills) {
          %>
          <span class="mo-applicant-skill-chip"><%= String.valueOf(skillObj) %></span>
          <%
            }
          %>
        </div>
      </section>

      <section class="mo-applicant-panel">
        <div class="mo-applicant-panel__header"><h2>Resume</h2></div>
        <div class="mo-applicant-resume-card">
          <div class="mo-applicant-resume-card__left">
            <div class="mo-applicant-resume-card__meta">
              <strong><%= hasResume ? resumeFileName : "No resume uploaded" %></strong>
              <p><%= hasResume ? "Uploaded on " + String.valueOf(taProfile.getOrDefault("resumeUploadedAt", "")) : "Resume metadata not available." %></p>
            </div>
          </div>

          <div class="mo-applicant-resume-card__actions">
            <a href="<%= hasResume ? contextPath + "/mo/applicants/resume?applicationId=" + String.valueOf(application.getOrDefault("applicationId", "")) : "#" %>" <%= hasResume ? "" : "aria-disabled=\"true\" onclick=\"return false;\"" %>>Download</a>
          </div>
        </div>
      </section>

      <section class="mo-match-panel">
        <div class="mo-match-panel__header">
          <div class="mo-match-panel__title"><span>Job Match Analysis</span></div>
          <span class="mo-match-panel__badge"><%= String.valueOf(application.getOrDefault("matchMethod", application.getOrDefault("method", "N/A"))) %></span>
        </div>

        <div class="mo-match-panel__body">
          <div class="mo-match-score">
            <div class="mo-match-score__ring"><span><%= String.valueOf(application.getOrDefault("skillMatchScore", 0)) %>%</span></div>
            <div class="mo-match-score__meta">
              <strong>Advisory Match Score</strong>
              <p><%= String.valueOf(application.getOrDefault("skillMatchExplanation", "No explanation available.")) %></p>
            </div>
          </div>

          <div class="mo-match-breakdown">
            <div class="mo-match-breakdown__section">
              <p>Matched Skills</p>
              <div class="mo-match-breakdown__chips">
                <%
                  for (Object skillObj : matchedSkills) {
                %>
                <span class="mo-match-breakdown__chip mo-match-breakdown__chip--good"><%= String.valueOf(skillObj) %></span>
                <%
                  }
                  if (matchedSkills.isEmpty()) {
                %>
                <span class="mo-match-breakdown__chip">No direct overlaps identified</span>
                <%
                  }
                %>
              </div>
            </div>

            <div class="mo-match-breakdown__section">
              <p>Missing Skills</p>
              <div class="mo-match-breakdown__chips">
                <%
                  for (Object skillObj : missingSkills) {
                %>
                <span class="mo-match-breakdown__chip mo-match-breakdown__chip--warn"><%= String.valueOf(skillObj) %></span>
                <%
                  }
                  if (missingSkills.isEmpty()) {
                %>
                <span class="mo-match-breakdown__chip">No critical missing skills highlighted</span>
                <%
                  }
                %>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
