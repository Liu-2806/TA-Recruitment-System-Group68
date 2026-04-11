<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/ta/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/ta/dashboard");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  Object currentUserObj = request.getSession(false) == null ? null : request.getSession(false).getAttribute("currentUser");
  com.bupt.ta.model.User currentUser = currentUserObj instanceof com.bupt.ta.model.User ? (com.bupt.ta.model.User) currentUserObj : null;
  String currentUserName = currentUser == null || currentUser.getDisplayName() == null || currentUser.getDisplayName().trim().isEmpty()
      ? "TA"
      : currentUser.getDisplayName().trim();
  request.setAttribute("currentUserName", currentUserName);
  request.setAttribute("currentUserRoleLabel", "TA Applicant");
  request.setAttribute("currentUserInitial", currentUserName.isBlank() ? "T" : currentUserName.substring(0, 1).toUpperCase());
  request.setAttribute("notificationCount", Integer.valueOf(0));

  Object queryObj = request.getAttribute("query");
  com.bupt.ta.dto.ApplicationQuery query = queryObj instanceof com.bupt.ta.dto.ApplicationQuery ? (com.bupt.ta.dto.ApplicationQuery) queryObj : new com.bupt.ta.dto.ApplicationQuery();
  Object pageObj = request.getAttribute("applicationsPage");
  com.bupt.ta.dto.PageResult applicationsPage = pageObj instanceof com.bupt.ta.dto.PageResult ? (com.bupt.ta.dto.PageResult) pageObj : null;
  java.util.List applications = applicationsPage == null || applicationsPage.getRecords() == null ? java.util.Collections.emptyList() : applicationsPage.getRecords();
  int currentPage = applicationsPage == null ? 1 : applicationsPage.getPage();
  int pageSize = applicationsPage == null ? 6 : applicationsPage.getSize();
  int total = applicationsPage == null ? applications.size() : (int) applicationsPage.getTotal();
  int totalPages = pageSize <= 0 ? 1 : Math.max(1, (int) Math.ceil(total / (double) pageSize));
  String createdId = String.valueOf(request.getParameter("created") == null ? "" : request.getParameter("created"));
  String updatedId = String.valueOf(request.getParameter("updated") == null ? "" : request.getParameter("updated"));
  String errorMessage = String.valueOf(request.getAttribute("errorMessage") == null
      ? (request.getParameter("error") == null ? "" : request.getParameter("error"))
      : request.getAttribute("errorMessage"));
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
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/ta-positions.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/ta-applications.css">
</head>
<body>
  <div class="ta-applications-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="ta-applications-main">
      <%
        if (!createdId.isBlank()) {
      %>
      <section class="ta-applications-toolbar" style="margin-bottom: 16px;">
        <div class="ta-applications-toolbar__header">
          <h2 style="color:#027a48;">Application submitted successfully</h2>
        </div>
        <p style="margin:0; color:#027a48;">Your application <strong><%= createdId %></strong> is now visible in the history list below.</p>
      </section>
      <%
        }
        if (!updatedId.isBlank()) {
      %>
      <section class="ta-applications-toolbar" style="margin-bottom: 16px;">
        <div class="ta-applications-toolbar__header">
          <h2 style="color:#027a48;">Application updated</h2>
        </div>
        <p style="margin:0; color:#027a48;">Your latest withdrawal or revocation request for <strong><%= updatedId %></strong> has been recorded.</p>
      </section>
      <%
        }
        if (!errorMessage.isBlank()) {
      %>
      <section class="ta-applications-toolbar" style="margin-bottom: 16px;">
        <div class="ta-applications-toolbar__header">
          <h2 style="color:#b42318;">Unable to update application</h2>
        </div>
        <p style="margin:0; color:#b42318;"><%= errorMessage %></p>
      </section>
      <%
        }
      %>

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
                <option value="UNDER_REVIEW" <%= "UNDER_REVIEW".equalsIgnoreCase(query.getStatus()) ? "selected" : "" %>>Under Review</option>
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
        <article class="ta-position-card ta-position-card--application">
          <div class="ta-position-card__top">
            <div class="ta-position-card__identity">
              <div>
                <h3>No applications found</h3>
                <p>Try adjusting the filters or submit your first application.</p>
              </div>
            </div>
          </div>
          <div class="ta-application-card__actions ta-application-card__actions--right">
            <a class="ta-application-card__primary" href="<%= contextPath %>/ta/jobs">Browse Open Roles</a>
          </div>
        </article>
        <%
          } else {
            for (Object appObj : applications) {
              java.util.Map appRow = appObj instanceof java.util.Map ? (java.util.Map) appObj : java.util.Collections.emptyMap();
              String applicationId = String.valueOf(appRow.getOrDefault("applicationId", ""));
              String postingId = String.valueOf(appRow.getOrDefault("postingId", ""));
              String postingTitle = String.valueOf(appRow.getOrDefault("postingTitle", ""));
              String courseCode = String.valueOf(appRow.getOrDefault("courseCode", ""));
              String moName = String.valueOf(appRow.getOrDefault("moName", ""));
              String deadline = String.valueOf(appRow.getOrDefault("deadline", ""));
              String vacancies = String.valueOf(appRow.getOrDefault("vacancies", ""));
              String department = String.valueOf(appRow.getOrDefault("department", ""));
              String moduleType = String.valueOf(appRow.getOrDefault("moduleType", ""));
              String postingType = String.valueOf(appRow.getOrDefault("postingType", "TA"));
              String activityDate = String.valueOf(appRow.getOrDefault("activityDate", ""));
              String activityStartTime = String.valueOf(appRow.getOrDefault("activityStartTime", ""));
              String activityEndTime = String.valueOf(appRow.getOrDefault("activityEndTime", ""));
              String activityLocation = String.valueOf(appRow.getOrDefault("activityLocation", ""));
              String status = String.valueOf(appRow.getOrDefault("statusLabel", appRow.getOrDefault("status", "Pending Review")));
              String statusRaw = String.valueOf(appRow.getOrDefault("status", "SUBMITTED")).toLowerCase();
              String statusCss = statusRaw.contains("accept") ? "accepted" : (statusRaw.contains("reject") ? "rejected" : (statusRaw.contains("withdraw") ? "withdrawn" : (statusRaw.contains("revocation") ? "revocation" : "pending")));
              boolean canWithdraw = "submitted".equals(statusRaw) || "under_review".equals(statusRaw) || "underreview".equals(statusRaw) || "accepted".equals(statusRaw);
              Object requiredSkillsObj = appRow.get("requiredSkills");
              java.util.List requiredSkillsList = requiredSkillsObj instanceof java.util.List ? (java.util.List) requiredSkillsObj : java.util.Collections.emptyList();
        %>
        <article id="application-<%= applicationId %>" class="ta-position-card ta-position-card--application">
          <div class="ta-position-card__top">
            <div class="ta-position-card__identity">
              <span class="ta-position-card__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div>
                <h3><%= postingTitle %></h3>
                <p><%= moName %><%= vacancies == null || vacancies.isBlank() ? "" : " " %><span><%= vacancies == null || vacancies.isBlank() ? "" : "Vacancies: " + vacancies %></span></p>
                <p><%= department %><%= department.isBlank() || moduleType.isBlank() ? "" : " | " %><%= moduleType %></p>
              </div>
            </div>

            <div class="ta-position-card__meta-actions">
              <span class="ta-application-badge ta-application-badge--<%= statusCss %>"><%= status %></span>
              <a class="ta-position-card__details" href="<%= contextPath %>/ta/jobs/detail?jobId=<%= postingId %>">
                <span>Open Position</span>
                <span class="ta-position-card__details-icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M10 14 19 5m-5 0h5v5M19 13.5V18a1 1 0 0 1-1 1h-12a1 1 0 0 1-1-1V6a1 1 0 0 1 1-1h4.5" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
              </a>
            </div>
          </div>

          <div class="ta-position-card__bottom">
            <div class="ta-position-info">
              <p class="ta-position-info__label"><%= "ACTIVITY".equalsIgnoreCase(postingType) ? "Activity Schedule" : "Application Deadline" %></p>
              <p class="ta-position-info__value">
                <span class="ta-position-info__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <%
                  if ("ACTIVITY".equalsIgnoreCase(postingType)) {
                %>
                <%= activityDate %> <%= activityStartTime %><%= activityEndTime == null || activityEndTime.isBlank() ? "" : " - " + activityEndTime %><%= activityLocation == null || activityLocation.isBlank() ? "" : " | " + activityLocation %>
                <%
                  } else {
                %>
                <%= deadline %>
                <%
                  }
                %>
              </p>
            </div>

            <div class="ta-position-skills">
              <p class="ta-position-skills__label">
                <span class="ta-position-skills__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M4.75 10.25 10.25 4.75H17l2.25 2.25v6.75L13.75 19.25 4.75 10.25Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    <circle cx="14.5" cy="9.5" r="1" fill="currentColor"/>
                  </svg>
                </span>
                Required Skills
              </p>
              <div class="ta-position-skills__list">
                <%
                  if (requiredSkillsList.isEmpty()) {
                %>
                <span>No specific skills listed</span>
                <%
                  } else {
                    int shown = 0;
                    for (Object skillObj : requiredSkillsList) {
                      if (shown >= 3) { break; }
                      String skill = String.valueOf(skillObj);
                      shown++;
                %>
                <span><%= skill %></span>
                <%
                    }
                  }
                %>
              </div>
            </div>
          </div>

          <div class="ta-application-card__actions ta-application-card__actions--right">
            <div class="ta-application-card__action-group ta-application-card__action-group--right">
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
        <%
            }
          }
        %>
      </section>

      <footer class="ta-positions-footer ta-positions-footer--applications">
        <%
          int startIndex = total == 0 ? 0 : ((currentPage - 1) * pageSize) + 1;
          int endIndex = Math.min(total, currentPage * pageSize);
          String baseParams = "keyword=" + java.net.URLEncoder.encode(query.getKeyword() == null ? "" : query.getKeyword(), "UTF-8")
              + "&status=" + java.net.URLEncoder.encode(query.getStatus() == null ? "" : query.getStatus(), "UTF-8")
              + "&sortBy=" + java.net.URLEncoder.encode(query.getSortBy() == null ? "" : query.getSortBy(), "UTF-8")
              + "&size=" + pageSize;
        %>
        <p>Showing <%= startIndex %>-<%= endIndex %> of <%= total %> applications</p>
        <div class="ta-pagination">
          <a class="ta-pagination__nav <%= currentPage <= 1 ? "is-disabled" : "" %>"
             aria-label="Previous page"
             href="<%= currentPage <= 1 ? "#" : (contextPath + "/ta/applications/my?" + baseParams + "&page=" + (currentPage - 1)) %>">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M14.5 6.5 9 12l5.5 5.5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </a>
          <%
            for (int pageNumber = 1; pageNumber <= totalPages; pageNumber++) {
          %>
          <a class="ta-pagination__page <%= pageNumber == currentPage ? "is-active" : "" %>"
             href="<%= contextPath + "/ta/applications/my?" + baseParams + "&page=" + pageNumber %>"><%= pageNumber %></a>
          <%
            }
          %>
          <a class="ta-pagination__nav <%= currentPage >= totalPages ? "is-disabled" : "" %>"
             aria-label="Next page"
             href="<%= currentPage >= totalPages ? "#" : (contextPath + "/ta/applications/my?" + baseParams + "&page=" + (currentPage + 1)) %>">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M9.5 6.5 15 12l-5.5 5.5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </a>
        </div>
      </footer>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
