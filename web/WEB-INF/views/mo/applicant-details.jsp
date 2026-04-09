<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  Object appRecordObj = request.getAttribute("application");
  java.util.Map appRecord = appRecordObj instanceof java.util.Map ? (java.util.Map) appRecordObj : java.util.Collections.emptyMap();
  java.util.Map job = appRecord.get("job") instanceof java.util.Map ? (java.util.Map) appRecord.get("job") : java.util.Collections.emptyMap();
  java.util.Map taProfile = appRecord.get("taProfile") instanceof java.util.Map ? (java.util.Map) appRecord.get("taProfile") : java.util.Collections.emptyMap();
  java.util.List matchedSkills = appRecord.get("matchedSkills") instanceof java.util.List ? (java.util.List) appRecord.get("matchedSkills") : java.util.Collections.emptyList();
  java.util.List missingSkills = appRecord.get("missingSkills") instanceof java.util.List ? (java.util.List) appRecord.get("missingSkills") : java.util.Collections.emptyList();
  String scoreBand = String.valueOf(appRecord.getOrDefault("scoreBand", "Advisory Review"));
  String strengthSummary = String.valueOf(appRecord.getOrDefault("strengthSummary", "No main strengths highlighted."));
  String riskSummary = String.valueOf(appRecord.getOrDefault("riskSummary", "No main risks highlighted."));
  String confidenceHint = String.valueOf(appRecord.getOrDefault("confidenceHint", "This result is advisory only."));
  String methodLabel = String.valueOf(appRecord.getOrDefault("methodLabel", appRecord.getOrDefault("matchMethod", appRecord.getOrDefault("method", "N/A"))));
  String methodHint = String.valueOf(appRecord.getOrDefault("methodHint", "Generated from the available structured data."));

  Object currentUserObj = request.getSession(false) == null ? null : request.getSession(false).getAttribute("currentUser");
  com.bupt.ta.model.User currentUser = currentUserObj instanceof com.bupt.ta.model.User ? (com.bupt.ta.model.User) currentUserObj : null;
  String currentUserName = currentUser == null || currentUser.getDisplayName() == null || currentUser.getDisplayName().trim().isEmpty()
      ? "MO"
      : currentUser.getDisplayName().trim();
  String currentUserInitial = currentUserName.isEmpty() ? "M" : currentUserName.substring(0, 1).toUpperCase();

  String postingIdStr = String.valueOf(job.getOrDefault("postingId", ""));
  String applicationIdStr = String.valueOf(appRecord.getOrDefault("applicationId", ""));
  String statusNorm = String.valueOf(appRecord.getOrDefault("status", "")).trim().toUpperCase(java.util.Locale.ROOT).replace("-", "_");
  boolean canDecide = "SUBMITTED".equals(statusNorm) || "UNDER_REVIEW".equals(statusNorm);

  request.setAttribute("headerBrandHref", contextPath + "/mo/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/mo/jobs/applicants?jobId=" + postingIdStr);
  request.setAttribute("headerBackLabel", "Back to List");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", currentUserName);
  request.setAttribute("currentUserRoleLabel", "Module Organizer");
  request.setAttribute("currentUserInitial", currentUserInitial);
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
          <div class="mo-applicant-info-item"><p>Name</p><strong><%= String.valueOf(appRecord.getOrDefault("taName", "")) %></strong></div>
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
          <div class="mo-applicant-info-item"><p>Status</p><strong><%= String.valueOf(appRecord.getOrDefault("statusLabel", appRecord.getOrDefault("status", ""))) %></strong></div>
          <div class="mo-applicant-info-item mo-applicant-info-item--full"><p>Statement</p><strong><%= String.valueOf(appRecord.getOrDefault("statement", "")) %></strong></div>
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
            <a href="<%= hasResume ? contextPath + "/mo/applicants/resume?applicationId=" + String.valueOf(appRecord.getOrDefault("applicationId", "")) : "#" %>" <%= hasResume ? "" : "aria-disabled=\"true\" onclick=\"return false;\"" %>>Download</a>
          </div>
        </div>
      </section>

      <section class="mo-match-panel">
        <div class="mo-match-panel__header">
          <div class="mo-match-panel__title"><span>Job Match Analysis</span></div>
          <span class="mo-match-panel__badge"><%= scoreBand %></span>
        </div>

        <div class="mo-match-panel__body">
          <div class="mo-match-score">
            <div class="mo-match-score__ring"><span><%= String.valueOf(appRecord.getOrDefault("skillMatchScore", 0)) %>%</span></div>
            <div class="mo-match-score__meta">
              <strong><%= scoreBand %></strong>
              <p><%= String.valueOf(appRecord.getOrDefault("skillMatchExplanation", "No explanation available.")) %></p>
            </div>
          </div>

          <div class="mo-match-breakdown">
            <div class="mo-match-breakdown__section">
              <p>Main strengths</p>
              <div class="mo-match-breakdown__chips">
                <span class="mo-match-breakdown__chip mo-match-breakdown__chip--good"><%= strengthSummary %></span>
              </div>
            </div>

            <div class="mo-match-breakdown__section">
              <p>Main risks</p>
              <div class="mo-match-breakdown__chips">
                <span class="mo-match-breakdown__chip mo-match-breakdown__chip--warn"><%= riskSummary %></span>
              </div>
            </div>

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

            <div class="mo-match-breakdown__section">
              <p>Review guidance</p>
              <div class="mo-match-breakdown__chips">
                <span class="mo-match-breakdown__chip"><%= confidenceHint %></span>
                <span class="mo-match-breakdown__chip"><strong><%= methodLabel %></strong>: <%= methodHint %></span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <% if (canDecide) { %>
      <section id="decision-panel" class="mo-applicant-panel mo-decision-panel" aria-labelledby="decision-heading">
        <div class="mo-applicant-panel__header"><h2 id="decision-heading">Hiring decision</h2></div>
        <div class="mo-decision-panel__body">
          <p class="mo-decision-panel__hint">Accept to hire this TA for this posting, or reject with optional feedback. The applicant will see your feedback when provided.</p>
          <form class="mo-decision-panel__form" method="post" action="<%= contextPath %>/mo/applications/status">
            <input type="hidden" name="applicationId" value="<%= applicationIdStr %>">
            <input type="hidden" name="postingId" value="<%= postingIdStr %>">
            <label class="mo-decision-panel__label" for="decisionComment">Feedback to applicant</label>
            <textarea id="decisionComment" class="mo-decision-panel__textarea" name="comment" rows="3" placeholder="Optional for accept; recommended when rejecting."></textarea>
            <div class="mo-decision-panel__actions">
              <button type="submit" name="newStatus" value="ACCEPTED" class="mo-decision-panel__submit mo-decision-panel__submit--accept">Accept (hire TA)</button>
              <button type="submit" name="newStatus" value="REJECTED" class="mo-decision-panel__submit mo-decision-panel__submit--reject" onclick="return confirm('Reject this applicant?');">Reject</button>
            </div>
          </form>
        </div>
      </section>
      <% } else { %>
      <section class="mo-applicant-panel mo-decision-panel mo-decision-panel--readonly" aria-labelledby="decision-readonly-heading">
        <div class="mo-applicant-panel__header"><h2 id="decision-readonly-heading">Hiring decision</h2></div>
        <div class="mo-decision-panel__body">
          <p class="mo-decision-panel__hint">This application is no longer pending review. Status: <strong><%= String.valueOf(appRecord.getOrDefault("statusLabel", appRecord.getOrDefault("status", ""))) %></strong>.</p>
        </div>
      </section>
      <% } %>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
