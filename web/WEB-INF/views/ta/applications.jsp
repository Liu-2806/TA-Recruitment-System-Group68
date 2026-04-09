<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/ta/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/ta/dashboard");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", "TA");
  request.setAttribute("currentUserRoleLabel", "TA Applicant");
  request.setAttribute("currentUserInitial", "T");
  request.setAttribute("notificationCount", Integer.valueOf(0));

  Object queryObj = request.getAttribute("query");
  com.bupt.ta.dto.ApplicationQuery query = queryObj instanceof com.bupt.ta.dto.ApplicationQuery ? (com.bupt.ta.dto.ApplicationQuery) queryObj : new com.bupt.ta.dto.ApplicationQuery();
  Object pageObj = request.getAttribute("applicationsPage");
  com.bupt.ta.dto.PageResult applicationsPage = pageObj instanceof com.bupt.ta.dto.PageResult ? (com.bupt.ta.dto.PageResult) pageObj : null;
  java.util.List applications = applicationsPage == null || applicationsPage.getRecords() == null ? java.util.Collections.emptyList() : applicationsPage.getRecords();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>My Applications | TA Recruitment Portal</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/ta-applications.css">
</head>
<body>
  <div class="ta-applications-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="ta-applications-main">
      <section class="ta-applications-toolbar">
        <div class="ta-applications-toolbar__header">
          <span class="ta-applications-toolbar__icon" aria-hidden="true">Filter</span>
          <h2>Search &amp; Filter</h2>
        </div>

        <form class="ta-applications-toolbar__grid" action="<%= contextPath %>/ta/applications/my" method="get">
          <div class="ta-toolbar-field ta-toolbar-field--wide">
            <label for="applicationKeywords">Keywords</label>
            <div class="ta-toolbar-input">
              <input id="applicationKeywords" name="keyword" type="text" placeholder="Position, MO, course code..." value="<%= query.getKeyword() == null ? "" : query.getKeyword() %>">
            </div>
          </div>

          <div class="ta-toolbar-field">
            <label for="applicationStatus">Status</label>
            <div class="ta-toolbar-select">
              <select id="applicationStatus" name="status">
                <option value="" <%= query.getStatus() == null || query.getStatus().isBlank() ? "selected" : "" %>>All Status</option>
                <option value="SUBMITTED" <%= "SUBMITTED".equalsIgnoreCase(query.getStatus()) ? "selected" : "" %>>Pending</option>
                <option value="ACCEPTED" <%= "ACCEPTED".equalsIgnoreCase(query.getStatus()) ? "selected" : "" %>>Accepted</option>
                <option value="REJECTED" <%= "REJECTED".equalsIgnoreCase(query.getStatus()) ? "selected" : "" %>>Rejected</option>
                <option value="WITHDRAWN" <%= "WITHDRAWN".equalsIgnoreCase(query.getStatus()) ? "selected" : "" %>>Withdrawn</option>
                <option value="REVOCATION_REQUESTED" <%= "REVOCATION_REQUESTED".equalsIgnoreCase(query.getStatus()) ? "selected" : "" %>>Revocation Requested</option>
              </select>
              <span class="ta-toolbar-select__caret" aria-hidden="true">v</span>
            </div>
          </div>

          <div class="ta-toolbar-field">
            <label for="applicationSort">Sort By</label>
            <div class="ta-toolbar-select">
              <select id="applicationSort" name="sortBy">
                <option value="updated" <%= query.getSortBy() == null || query.getSortBy().isBlank() || "updated".equalsIgnoreCase(query.getSortBy()) ? "selected" : "" %>>Latest Updated</option>
                <option value="submitted" <%= "submitted".equalsIgnoreCase(query.getSortBy()) ? "selected" : "" %>>Recently Submitted</option>
                <option value="status" <%= "status".equalsIgnoreCase(query.getSortBy()) ? "selected" : "" %>>Status Priority</option>
              </select>
              <span class="ta-toolbar-select__caret" aria-hidden="true">v</span>
            </div>
          </div>

          <div class="ta-toolbar-actions">
            <button class="ta-toolbar-actions__apply" type="submit">Apply Filter</button>
            <a class="ta-toolbar-actions__reset" href="<%= contextPath %>/ta/applications/my" aria-label="Reset filters">Reset</a>
          </div>
        </form>
      </section>

      <section class="ta-application-board">
        <%
          if (applications.isEmpty()) {
        %>
        <article class="ta-application-card">
          <div class="ta-application-card__top">
            <div class="ta-application-card__identity">
              <div>
                <h3>No applications found</h3>
                <p>Try adjusting the filters or submit your first application.</p>
              </div>
            </div>
          </div>
          <div class="ta-application-card__actions">
            <a class="ta-application-card__primary" href="<%= contextPath %>/ta/jobs">Browse Open Roles</a>
          </div>
        </article>
        <%
          } else {
            for (Object appObj : applications) {
              java.util.Map appRow = appObj instanceof java.util.Map ? (java.util.Map) appObj : java.util.Collections.emptyMap();
              String applicationId = String.valueOf(appRow.getOrDefault("applicationId", ""));
              String postingId = String.valueOf(appRow.getOrDefault("postingId", ""));
              String status = String.valueOf(appRow.getOrDefault("statusLabel", appRow.getOrDefault("status", "Pending Review")));
              String statusRaw = String.valueOf(appRow.getOrDefault("status", "SUBMITTED")).toLowerCase();
              String statusCss = statusRaw.contains("accept") ? "accepted" : (statusRaw.contains("reject") ? "rejected" : (statusRaw.contains("withdraw") ? "withdrawn" : (statusRaw.contains("revocation") ? "revocation" : "pending")));
              boolean canWithdraw = "submitted".equals(statusRaw) || "under_review".equals(statusRaw) || "underreview".equals(statusRaw) || "accepted".equals(statusRaw);
              String matchExplanation = String.valueOf(appRow.getOrDefault("skillMatchExplanation", "No match explanation available yet."));
              java.util.List historyLogs = appRow.get("historyLogs") instanceof java.util.List ? (java.util.List) appRow.get("historyLogs") : java.util.Collections.emptyList();
        %>
        <article id="application-<%= applicationId %>" class="ta-application-card">
          <div class="ta-application-card__top">
            <div class="ta-application-card__identity">
              <span class="ta-application-card__icon ta-application-card__icon--<%= statusCss %>" aria-hidden="true">Status</span>
              <div>
                <h3><%= String.valueOf(appRow.getOrDefault("postingTitle", "")) %></h3>
                <p><%= String.valueOf(appRow.getOrDefault("courseCode", "")) %> · <%= String.valueOf(appRow.getOrDefault("moName", "")) %> · Submitted <%= String.valueOf(appRow.getOrDefault("appliedAt", "")) %></p>
              </div>
            </div>

            <div class="ta-application-card__meta-actions">
              <span class="ta-application-badge ta-application-badge--<%= statusCss %>"><%= status %></span>
              <a class="ta-application-card__details" href="<%= contextPath %>/ta/jobs/detail?jobId=<%= postingId %>">Open Position</a>
            </div>
          </div>

          <div class="ta-application-card__bottom">
            <div class="ta-application-info">
              <p class="ta-application-info__label">Current Stage</p>
              <p class="ta-application-info__value"><%= status %></p>
            </div>
            <div class="ta-application-info">
              <p class="ta-application-info__label">Match Insight</p>
              <p class="ta-application-info__value"><%= matchExplanation %></p>
            </div>
            <div class="ta-application-info">
              <p class="ta-application-info__label">Feedback</p>
              <p class="ta-application-info__value"><%= String.valueOf(appRow.getOrDefault("feedback", "")).isBlank() ? "No feedback yet." : String.valueOf(appRow.get("feedback")) %></p>
            </div>
          </div>

          <div class="ta-application-card__actions">
            <a class="ta-application-card__secondary" href="#history-<%= applicationId %>">View Full History</a>
            <div class="ta-application-card__action-group">
              <a class="ta-application-card__primary" href="<%= contextPath %>/ta/jobs/detail?jobId=<%= postingId %>">View Matching Details</a>
              <%
                if (canWithdraw) {
                  String withdrawLabel = "accepted".equals(statusRaw) ? "Request Revocation" : "Withdraw";
              %>
              <details class="ta-withdraw-panel">
                <summary class="ta-application-card__primary ta-application-card__primary--ghost"><%= withdrawLabel %></summary>
                <form action="<%= contextPath %>/ta/applications/withdraw" method="post" class="ta-withdraw-panel__form">
                  <input type="hidden" name="applicationId" value="<%= applicationId %>">
                  <label for="reason-<%= applicationId %>"><%= "accepted".equals(statusRaw) ? "Reason for revocation request" : "Reason for withdrawal" %></label>
                  <textarea id="reason-<%= applicationId %>" name="reason" rows="3" required placeholder="Briefly explain why you need to update this application."></textarea>
                  <button type="submit" class="ta-application-card__primary ta-application-card__primary--ghost"><%= withdrawLabel %></button>
                </form>
              </details>
              <%
                }
              %>
            </div>
          </div>
        </article>

        <article id="history-<%= applicationId %>" class="ta-history-card">
          <div class="ta-history-card__head">
            <h3><%= String.valueOf(appRow.getOrDefault("postingTitle", "")) %></h3>
            <span class="ta-history-card__badge ta-history-card__badge--<%= statusCss %>"><%= status %></span>
          </div>
          <ul class="ta-history-card__steps">
            <%
              for (Object logObj : historyLogs) {
                java.util.Map log = logObj instanceof java.util.Map ? (java.util.Map) logObj : java.util.Collections.emptyMap();
            %>
            <li><strong><%= String.valueOf(log.getOrDefault("time", "")) %></strong><span><%= String.valueOf(log.getOrDefault("description", "")) %></span></li>
            <%
              }
            %>
          </ul>
        </article>
        <%
            }
          }
        %>
      </section>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
