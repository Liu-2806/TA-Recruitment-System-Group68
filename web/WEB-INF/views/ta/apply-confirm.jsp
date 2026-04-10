<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  Object jobObj = request.getAttribute("job");
  java.util.Map job = jobObj instanceof java.util.Map ? (java.util.Map) jobObj : java.util.Collections.emptyMap();
  Object profileObj = request.getAttribute("profile");
  java.util.Map profile = profileObj instanceof java.util.Map ? (java.util.Map) profileObj : java.util.Collections.emptyMap();
  Object eligibilityObj = request.getAttribute("eligibilityResult");
  java.util.Map eligibility = eligibilityObj instanceof java.util.Map ? (java.util.Map) eligibilityObj : java.util.Collections.emptyMap();
  String returnHref = String.valueOf(request.getAttribute("returnHref") == null ? (contextPath + "/ta/jobs/detail?jobId=" + String.valueOf(job.getOrDefault("postingId", ""))) : request.getAttribute("returnHref"));
  String encodedReturnQuery = String.valueOf(request.getAttribute("encodedReturnQuery") == null ? "" : request.getAttribute("encodedReturnQuery"));

  String fullName = String.valueOf(profile.getOrDefault("fullName", "TA"));
  request.setAttribute("headerBrandHref", contextPath + "/ta/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", returnHref);
  request.setAttribute("headerBackLabel", "Back to Position");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", fullName);
  request.setAttribute("currentUserRoleLabel", "TA Applicant");
  request.setAttribute("currentUserInitial", fullName.isBlank() ? "T" : fullName.substring(0, 1).toUpperCase());
  request.setAttribute("notificationCount", Integer.valueOf(0));

  boolean eligible = Boolean.TRUE.equals(eligibility.get("eligible"));
  boolean profileCompleted = Boolean.TRUE.equals(eligibility.get("profileCompleted"));
  boolean resumeUploaded = Boolean.TRUE.equals(eligibility.get("resumeUploaded"));
  java.util.List reasons = eligibility.get("reasons") instanceof java.util.List ? (java.util.List) eligibility.get("reasons") : java.util.Collections.emptyList();
  String errorMessage = String.valueOf(request.getAttribute("errorMessage") == null ? "" : request.getAttribute("errorMessage"));
  String statementDraft = String.valueOf(request.getAttribute("statementDraft") == null ? "" : request.getAttribute("statementDraft"));
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Confirm Application | TA Recruitment Portal</title>
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
            <h1>Confirm Your Application</h1>
          </div>
          <span class="ta-details-status">
            <span class="ta-details-status__dot"></span>
            <%= eligible ? "Eligible" : "Action Required" %>
          </span>
        </div>

        <%
          if (!errorMessage.isBlank()) {
        %>
        <section class="ta-details-section">
          <h2 class="ta-details-section__title">Submission blocked</h2>
          <div class="ta-details-description" style="color:#b42318;"><%= errorMessage %></div>
        </section>
        <%
          }
        %>

        <section class="ta-details-section">
          <h2 class="ta-details-section__title">You are applying for</h2>
          <div class="ta-details-description">
            <strong><%= String.valueOf(job.getOrDefault("courseName", "")) %></strong><br>
            <%= String.valueOf(job.getOrDefault("moName", "")) %> · Deadline <%= String.valueOf(job.getOrDefault("deadline", "")) %>
          </div>
        </section>

        <section class="ta-details-section">
          <h2 class="ta-details-section__title">Submission Checklist</h2>
          <ul class="ta-details-checklist">
            <li><span><%= profileCompleted ? "Completed" : "Incomplete" %></span> Profile completed</li>
            <li><span><%= resumeUploaded ? "Uploaded" : "Missing" %></span> Resume uploaded</li>
            <li><span><%= String.valueOf(eligibility.getOrDefault("alreadyApplied", false)).equals("true") ? "Duplicate" : "OK" %></span> Duplicate application check</li>
            <li><span><%= String.valueOf(eligibility.getOrDefault("jobOpen", false)).equals("true") ? "Open" : "Closed" %></span> Posting status check</li>
            <li><span><%= String.valueOf(eligibility.getOrDefault("beforeDeadline", false)).equals("true") ? "Open" : "Closed" %></span> Deadline check</li>
            <li><span><%= String.valueOf(eligibility.getOrDefault("scheduleConflictFree", false)).equals("true") ? "Clear" : "Conflict" %></span> Timetable conflict check</li>
          </ul>
        </section>

        <%
          if (!reasons.isEmpty()) {
        %>
        <section class="ta-details-section">
          <h2 class="ta-details-section__title">Why you cannot submit yet</h2>
          <ul class="ta-details-checklist">
            <%
              for (Object reasonObj : reasons) {
            %>
            <li><span>Notice</span> <%= String.valueOf(reasonObj) %></li>
            <%
              }
            %>
          </ul>
        </section>
        <%
          }
        %>

        <section class="ta-details-section">
          <h2 class="ta-details-section__title">Supporting Statement</h2>
          <form action="<%= contextPath %>/ta/applications" method="post">
            <input type="hidden" name="jobId" value="<%= String.valueOf(job.getOrDefault("postingId", "")) %>">
            <input type="hidden" name="returnQuery" value="<%= encodedReturnQuery %>">
            <textarea name="statement" rows="6" style="width:100%; padding:12px; border-radius:16px; border:1px solid #d0d5dd; resize:vertical;" placeholder="Briefly explain why you are a good fit for this role."><%= statementDraft %></textarea>
            <div class="ta-details-card__footer" style="padding-left:0; padding-right:0;">
              <p><%= eligible ? "Check your statement carefully before submitting." : "Resolve the checklist items before submitting this application." %></p>
              <button class="ta-details-apply" type="submit" <%= eligible ? "" : "disabled" %>>Confirm &amp; Submit</button>
            </div>
          </form>
        </section>
      </section>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
