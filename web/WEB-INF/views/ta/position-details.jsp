<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  Object jobObj = request.getAttribute("job");
  java.util.Map job = jobObj instanceof java.util.Map ? (java.util.Map) jobObj : java.util.Collections.emptyMap();
  Object matchObj = request.getAttribute("matchAnalysis");
  java.util.Map matchAnalysis = matchObj instanceof java.util.Map ? (java.util.Map) matchObj : java.util.Collections.emptyMap();
  String returnHref = String.valueOf(request.getAttribute("returnHref") == null ? (contextPath + "/ta/jobs") : request.getAttribute("returnHref"));
  String encodedReturnQuery = String.valueOf(request.getAttribute("encodedReturnQuery") == null ? "" : request.getAttribute("encodedReturnQuery"));

  Object currentUserObj = request.getSession(false) == null ? null : request.getSession(false).getAttribute("currentUser");
  com.bupt.ta.model.User currentUser = currentUserObj instanceof com.bupt.ta.model.User ? (com.bupt.ta.model.User) currentUserObj : null;
  String currentUserName = currentUser == null || currentUser.getDisplayName() == null || currentUser.getDisplayName().trim().isEmpty()
      ? "TA"
      : currentUser.getDisplayName().trim();
  request.setAttribute("headerBrandHref", contextPath + "/ta/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", returnHref);
  request.setAttribute("headerBackLabel", "Back to Listings");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", currentUserName);
  request.setAttribute("currentUserRoleLabel", "TA Applicant");
  request.setAttribute("currentUserInitial", currentUserName.isBlank() ? "T" : currentUserName.substring(0, 1).toUpperCase());
  request.setAttribute("notificationCount", Integer.valueOf(0));

  java.util.List requiredSkills = job.get("requiredSkills") instanceof java.util.List ? (java.util.List) job.get("requiredSkills") : java.util.Collections.emptyList();
  java.util.List matchedSkills = matchAnalysis.get("matchedSkills") instanceof java.util.List ? (java.util.List) matchAnalysis.get("matchedSkills") : java.util.Collections.emptyList();
  java.util.List missingSkills = matchAnalysis.get("missingSkills") instanceof java.util.List ? (java.util.List) matchAnalysis.get("missingSkills") : java.util.Collections.emptyList();
  String score = String.valueOf(matchAnalysis.getOrDefault("score", 0));
  String explanation = String.valueOf(matchAnalysis.getOrDefault("explanation", "Match analysis is unavailable."));
  String method = String.valueOf(matchAnalysis.getOrDefault("method", "UNAVAILABLE"));
  String scoreBand = String.valueOf(matchAnalysis.getOrDefault("scoreBand", "Advisory Review"));
  String nextStepSuggestion = String.valueOf(matchAnalysis.getOrDefault("nextStepSuggestion", "Review the role requirements carefully before deciding whether to apply."));
  String strengthSummary = String.valueOf(matchAnalysis.getOrDefault("strengthSummary", "No main strengths highlighted."));
  String riskSummary = String.valueOf(matchAnalysis.getOrDefault("riskSummary", "No main risks highlighted."));
  String confidenceHint = String.valueOf(matchAnalysis.getOrDefault("confidenceHint", "This result is advisory only."));
  String methodLabel = String.valueOf(matchAnalysis.getOrDefault("methodLabel", method));
  String methodHint = String.valueOf(matchAnalysis.getOrDefault("methodHint", "Generated from the available structured data."));
  Object eligibilityObj = request.getAttribute("eligibilityResult");
  java.util.Map eligibility = eligibilityObj instanceof java.util.Map ? (java.util.Map) eligibilityObj : java.util.Collections.emptyMap();
  boolean canApply = Boolean.TRUE.equals(eligibility.get("eligible"));
  boolean alreadyApplied = Boolean.TRUE.equals(eligibility.get("alreadyApplied"));
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Position Details | TA Recruitment Portal</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/ta-position-details.css">
</head>
<body>
  <div class="ta-details-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="ta-details-main">
      <section class="ta-details-card">
        <div class="ta-details-card__header">
          <div>
            <span class="ta-details-card__code"><%= String.valueOf(job.getOrDefault("courseCode", "")) %></span>
            <h1><%= String.valueOf(job.getOrDefault("courseName", "Unknown Position")) %></h1>
          </div>
          <span class="ta-details-status">
            <span class="ta-details-status__dot"></span>
            <%= String.valueOf(job.getOrDefault("status", "OPEN")) %>
          </span>
        </div>

        <div class="ta-details-summary">
          <div class="ta-details-summary__item">
            <p class="ta-details-summary__label">Module Organizer</p>
            <p class="ta-details-summary__value"><%= String.valueOf(job.getOrDefault("moName", "")) %></p>
          </div>
          <div class="ta-details-summary__item">
            <p class="ta-details-summary__label">Vacancies</p>
            <p class="ta-details-summary__value"><%= String.valueOf(job.getOrDefault("vacancies", "")) %> Positions</p>
          </div>
          <div class="ta-details-summary__item">
            <p class="ta-details-summary__label">Deadline</p>
            <p class="ta-details-summary__value ta-details-summary__value--danger"><%= String.valueOf(job.getOrDefault("deadline", "")) %></p>
          </div>
        </div>

        <section class="ta-details-section">
          <h2 class="ta-details-section__title">Position Description</h2>
          <div class="ta-details-description"><%= String.valueOf(job.getOrDefault("description", "")) %></div>
        </section>

        <section class="ta-details-section">
          <h2 class="ta-details-section__title">Required Skills &amp; Qualifications</h2>
          <ul class="ta-details-checklist">
            <%
              for (Object skillObj : requiredSkills) {
            %>
            <li><span>Required</span> <%= String.valueOf(skillObj) %></li>
            <%
              }
            %>
          </ul>
        </section>

        <section class="ta-details-section">
          <h2 class="ta-details-section__title">Estimated Workload</h2>
          <div class="ta-details-workload">
            <span>Approximately <%= String.valueOf(job.getOrDefault("estimatedWorkloadHours", "")) %> hours per week</span>
          </div>
        </section>

        <section class="ta-match-card">
          <div class="ta-match-card__header">
            <span>Should You Apply?</span>
            <span class="ta-match-card__sparkle"><%= scoreBand %></span>
          </div>

          <div class="ta-match-card__content">
            <div class="ta-match-score">
              <div class="ta-match-score__ring">
                <span><%= score %>%</span>
              </div>
              <div class="ta-match-score__meta">
                <strong><%= scoreBand %></strong>
                <p><%= nextStepSuggestion %></p>
              </div>
            </div>

            <div class="ta-match-improvement">
              <p class="ta-match-improvement__label">Why this result was generated</p>
              <p class="ta-match-improvement__text"><%= explanation %></p>
            </div>
          </div>

          <div class="ta-details-summary">
            <div class="ta-details-summary__item">
              <p class="ta-details-summary__label">Why you look suitable</p>
              <p class="ta-details-summary__value"><%= strengthSummary %></p>
            </div>
            <div class="ta-details-summary__item">
              <p class="ta-details-summary__label">What may hold you back</p>
              <p class="ta-details-summary__value"><%= riskSummary %></p>
            </div>
          </div>

          <div class="ta-details-summary">
            <div class="ta-details-summary__item">
              <p class="ta-details-summary__label">Matched Evidence</p>
              <p class="ta-details-summary__value"><%= matchedSkills.isEmpty() ? "No direct overlaps identified from the current structured resume." : String.join(", ", (java.util.List<String>) matchedSkills) %></p>
            </div>
            <div class="ta-details-summary__item">
              <p class="ta-details-summary__label">Missing or Unclear Evidence</p>
              <p class="ta-details-summary__value"><%= missingSkills.isEmpty() ? "No critical gaps highlighted from the listed required skills." : String.join(", ", (java.util.List<String>) missingSkills) %></p>
            </div>
          </div>

          <div class="ta-details-summary">
            <div class="ta-details-summary__item">
              <p class="ta-details-summary__label">Result Source</p>
              <p class="ta-details-summary__value"><strong><%= methodLabel %></strong><br><%= methodHint %></p>
            </div>
            <div class="ta-details-summary__item">
              <p class="ta-details-summary__label">Confidence Hint</p>
              <p class="ta-details-summary__value"><%= confidenceHint %></p>
            </div>
          </div>
        </section>

        <div class="ta-details-card__footer">
          <p><%= alreadyApplied ? "You have already submitted an application for this role." : "Review the role requirements and your current profile before submitting." %></p>
          <%
            if (canApply) {
          %>
          <a class="ta-details-apply" href="<%= contextPath %>/ta/applications/confirm?jobId=<%= String.valueOf(job.getOrDefault("postingId", "")) %><%= encodedReturnQuery.isBlank() ? "" : "&returnQuery=" + encodedReturnQuery %>">Apply Now</a>
          <%
            } else {
          %>
          <span class="ta-details-apply" style="opacity:0.6; pointer-events:none;"><%= alreadyApplied ? "Applied" : "Unavailable" %></span>
          <%
            }
          %>
        </div>
      </section>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
