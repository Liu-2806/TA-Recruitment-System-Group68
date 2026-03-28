<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  Object jobObj = request.getAttribute("job");
  java.util.Map job = jobObj instanceof java.util.Map ? (java.util.Map) jobObj : java.util.Collections.emptyMap();
  Object pageObj = request.getAttribute("applicationsPage");
  com.bupt.ta.dto.PageResult applicationsPage = pageObj instanceof com.bupt.ta.dto.PageResult ? (com.bupt.ta.dto.PageResult) pageObj : null;
  java.util.List applications = applicationsPage == null || applicationsPage.getRecords() == null ? java.util.Collections.emptyList() : applicationsPage.getRecords();
  Object queryObj = request.getAttribute("query");
  com.bupt.ta.dto.ApplicationQuery query = queryObj instanceof com.bupt.ta.dto.ApplicationQuery ? (com.bupt.ta.dto.ApplicationQuery) queryObj : new com.bupt.ta.dto.ApplicationQuery();

  request.setAttribute("headerBrandHref", contextPath + "/mo/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/mo/jobs/my");
  request.setAttribute("headerBackLabel", "Back to List");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", "MO");
  request.setAttribute("currentUserRoleLabel", "Module Organizer");
  request.setAttribute("currentUserInitial", "M");
  request.setAttribute("notificationCount", Integer.valueOf(0));
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Applicants | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/mo-applicants.css">
</head>
<body>
  <div class="mo-applicants-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="mo-applicants-main">
      <section class="mo-applicants-toolbar">
        <div class="mo-applicants-header__title-group">
          <h1>Applicants: <%= String.valueOf(job.getOrDefault("courseName", "")) %></h1>
          <p>Course Code: <%= String.valueOf(job.getOrDefault("courseCode", "")) %></p>
        </div>

        <form class="mo-applicants-toolbar__actions" action="<%= contextPath %>/mo/jobs/applicants" method="get">
          <input type="hidden" name="jobId" value="<%= String.valueOf(job.getOrDefault("postingId", "")) %>">
          <div class="mo-applicants-search">
            <input type="text" name="keyword" placeholder="Search name..." value="<%= query.getKeyword() == null ? "" : query.getKeyword() %>">
          </div>
          <div class="mo-applicants-search">
            <select name="sortBy">
              <option value="updated" <%= query.getSortBy() == null || query.getSortBy().isBlank() || "updated".equalsIgnoreCase(query.getSortBy()) ? "selected" : "" %>>Latest Updated</option>
              <option value="submitted" <%= "submitted".equalsIgnoreCase(query.getSortBy()) ? "selected" : "" %>>Recently Submitted</option>
              <option value="status" <%= "status".equalsIgnoreCase(query.getSortBy()) ? "selected" : "" %>>Status Priority</option>
            </select>
          </div>
          <button class="mo-applicants-broadcast" type="submit"><span>Apply</span></button>
        </form>
      </section>

      <section class="mo-applicants-summary">
        <div class="mo-applicants-summary__stats">
          <div class="mo-applicants-summary__item">
            <div>
              <p>Target Recruitment</p>
              <strong><%= String.valueOf(job.getOrDefault("vacancies", 0)) %> <span>Positions</span></strong>
            </div>
          </div>
          <div class="mo-applicants-summary__item">
            <div>
              <p>Received Applications</p>
              <strong><%= applicationsPage == null ? 0 : applicationsPage.getTotal() %> <span>Applicants</span></strong>
            </div>
          </div>
        </div>
      </section>

      <section class="mo-applicants-table-card">
        <div class="mo-applicants-table__wrap">
          <table class="mo-applicants-table">
            <thead>
              <tr>
                <th>Applicant</th>
                <th>Academic Info</th>
                <th>Skill Match</th>
                <th>Status</th>
                <th class="mo-applicants-table__right">Action</th>
              </tr>
            </thead>
            <tbody>
              <%
                if (applications.isEmpty()) {
              %>
              <tr>
                <td colspan="5">No applicants found for this posting.</td>
              </tr>
              <%
                } else {
                  for (Object appObj : applications) {
                    java.util.Map application = appObj instanceof java.util.Map ? (java.util.Map) appObj : java.util.Collections.emptyMap();
                    java.util.Map taProfile = application.get("taProfile") instanceof java.util.Map ? (java.util.Map) application.get("taProfile") : java.util.Collections.emptyMap();
                    String statusRaw = String.valueOf(application.getOrDefault("status", "SUBMITTED")).toLowerCase();
                    String statusCss = statusRaw.contains("accept") ? "accepted" : (statusRaw.contains("reject") ? "rejected" : "pending");
              %>
              <tr>
                <td>
                  <div class="mo-applicant-cell">
                    <div class="mo-applicant-cell__meta">
                      <strong><%= String.valueOf(application.getOrDefault("taName", "")) %></strong>
                      <p><%= String.valueOf(taProfile.getOrDefault("email", "")) %></p>
                    </div>
                  </div>
                </td>
                <td>
                  <div class="mo-academic-cell">
                    <strong><%= String.valueOf(taProfile.getOrDefault("majorProgram", "")) %></strong>
                    <p><%= String.valueOf(taProfile.getOrDefault("academicYear", "")) %></p>
                  </div>
                </td>
                <td>
                  <div class="mo-match-cell">
                    <div class="mo-match-chip"><%= String.valueOf(application.getOrDefault("skillMatchScore", 0)) %>% Match</div>
                    <div class="mo-match-bar"><span style="width:<%= String.valueOf(application.getOrDefault("skillMatchScore", 0)) %>%"></span></div>
                  </div>
                </td>
                <td>
                  <span class="mo-applicant-status mo-applicant-status--<%= statusCss %>"><%= String.valueOf(application.getOrDefault("statusLabel", application.getOrDefault("status", ""))) %></span>
                </td>
                <td class="mo-applicants-table__right">
                  <div class="mo-applicant-actions">
                    <a class="mo-applicant-actions__button" href="<%= contextPath %>/mo/applicants/detail?applicationId=<%= String.valueOf(application.getOrDefault("applicationId", "")) %>">View Profile</a>
                  </div>
                </td>
              </tr>
              <%
                  }
                }
              %>
            </tbody>
          </table>
        </div>
      </section>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
