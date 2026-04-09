<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  Object currentUserObj = request.getSession(false) == null ? null : request.getSession(false).getAttribute("currentUser");
  com.bupt.ta.model.User currentUser = currentUserObj instanceof com.bupt.ta.model.User ? (com.bupt.ta.model.User) currentUserObj : null;
  String currentUserName = currentUser == null || currentUser.getDisplayName() == null || currentUser.getDisplayName().trim().isEmpty()
      ? "MO"
      : currentUser.getDisplayName().trim();
  String currentUserInitial = currentUserName.isEmpty() ? "M" : currentUserName.substring(0, 1).toUpperCase();
  Object jobsPageObj = request.getAttribute("jobsPage");
  com.bupt.ta.dto.PageResult jobsPage = jobsPageObj instanceof com.bupt.ta.dto.PageResult ? (com.bupt.ta.dto.PageResult) jobsPageObj : null;
  java.util.List jobs = jobsPage == null || jobsPage.getRecords() == null ? java.util.Collections.emptyList() : jobsPage.getRecords();
  Object queryObj = request.getAttribute("query");
  com.bupt.ta.dto.JobQuery query = queryObj instanceof com.bupt.ta.dto.JobQuery ? (com.bupt.ta.dto.JobQuery) queryObj : new com.bupt.ta.dto.JobQuery();
  int currentPage = jobsPage == null ? 1 : jobsPage.getPage();
  int pageSize = jobsPage == null ? 6 : jobsPage.getSize();
  int total = jobsPage == null ? jobs.size() : (int) jobsPage.getTotal();
  int totalPages = pageSize <= 0 ? 1 : Math.max(1, (int) Math.ceil(total / (double) pageSize));

  request.setAttribute("headerBrandHref", contextPath + "/mo/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/mo/dashboard");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", currentUserName);
  request.setAttribute("currentUserRoleLabel", "Module Organizer");
  request.setAttribute("currentUserInitial", currentUserInitial);
  request.setAttribute("notificationCount", Integer.valueOf(1));
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>My Job Postings | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/mo-postings.css">
</head>
<body>
  <div class="mo-postings-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="mo-postings-main">
      <div class="mo-postings-heading">
        <h1>My Job Postings</h1>
      </div>

      <form class="mo-postings-filter" method="get" action="<%= contextPath %>/mo/jobs/my">
        <div class="mo-postings-filter__left">
          <div class="mo-postings-filter__select">
            <span class="mo-postings-filter__label">Filter By:</span>
            <select name="status">
              <option value="" <%= query.getStatus() == null || query.getStatus().isBlank() ? "selected" : "" %>>All Statuses</option>
              <option value="OPEN" <%= "OPEN".equalsIgnoreCase(query.getStatus()) ? "selected" : "" %>>Open</option>
              <option value="CLOSED" <%= "CLOSED".equalsIgnoreCase(query.getStatus()) ? "selected" : "" %>>Closed</option>
              <option value="DRAFT" <%= "DRAFT".equalsIgnoreCase(query.getStatus()) ? "selected" : "" %>>Draft</option>
            </select>
          </div>

          <div class="mo-postings-filter__search">
            <span class="mo-postings-filter__search-icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M10.75 17a6.25 6.25 0 1 0 0-12.5 6.25 6.25 0 0 0 0 12.5Zm8.75 2.5-4.25-4.25" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <input type="text" name="keyword" value="<%= query.getKeyword() == null ? "" : query.getKeyword() %>" placeholder="Search by Course Name...">
          </div>
        </div>

        <div class="mo-postings-filter__actions">
          <select name="sortBy">
            <option value="postingId" <%= query.getSortBy() == null || query.getSortBy().isBlank() || "postingId".equalsIgnoreCase(query.getSortBy()) ? "selected" : "" %>>Newest</option>
            <option value="deadlineAsc" <%= "deadlineAsc".equalsIgnoreCase(query.getSortBy()) ? "selected" : "" %>>Upcoming Deadline</option>
            <option value="applicationsDesc" <%= "applicationsDesc".equalsIgnoreCase(query.getSortBy()) ? "selected" : "" %>>Most Applications</option>
            <option value="vacancies" <%= "vacancies".equalsIgnoreCase(query.getSortBy()) ? "selected" : "" %>>Most Vacancies</option>
          </select>
          <button class="mo-postings-filter__apply" type="submit">Apply Filter</button>
          <a class="mo-postings-filter__reset" href="<%= contextPath %>/mo/jobs/my" aria-label="Reset filters">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M7 7.5V4.75m0 0H4.25M7 4.75 4.75 7M6.5 9.5a7 7 0 1 1-1.2 7" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </a>
        </div>
      </form>

      <section class="mo-postings-table-card">
        <div class="mo-postings-table__wrap">
          <table class="mo-postings-table">
            <thead>
              <tr>
                <th>Course Title</th>
                <th>Deadline</th>
                <th>Vacancies / Rec.</th>
                <th>Status</th>
                <th class="mo-postings-table__right">Actions</th>
              </tr>
            </thead>
            <tbody>
              <%
                if (jobs.isEmpty()) {
              %>
              <tr>
                <td colspan="5">No job postings found.</td>
              </tr>
              <%
                } else {
                  for (Object obj : jobs) {
                    java.util.Map job = obj instanceof java.util.Map ? (java.util.Map) obj : java.util.Collections.emptyMap();
                    String courseName = String.valueOf(job.getOrDefault("courseName", ""));
                    String courseCode = String.valueOf(job.getOrDefault("courseCode", ""));
                    String postingId = String.valueOf(job.getOrDefault("postingId", ""));
                    String deadline = String.valueOf(job.getOrDefault("deadline", "-"));
                    int vacancies = 0;
                    int applications = 0;
                    try { vacancies = Integer.parseInt(String.valueOf(job.getOrDefault("vacancies", "0"))); } catch (Exception ignored) {}
                    try { applications = Integer.parseInt(String.valueOf(job.getOrDefault("applicationCount", "0"))); } catch (Exception ignored) {}
                    String status = String.valueOf(job.getOrDefault("status", "OPEN")).toUpperCase();
                    String statusCss = "open";
                    if ("CLOSED".equals(status)) {
                      statusCss = "closed";
                    } else if ("DRAFT".equals(status)) {
                      statusCss = "pending";
                    }
                    String avatar = courseName.isBlank() ? "J" : courseName.substring(0, 1).toUpperCase();
              %>
              <tr>
                <td>
                  <div class="mo-posting-course">
                    <span class="mo-posting-course__avatar"><%= avatar %></span>
                    <strong><%= courseName %> <%= courseCode.isBlank() ? "" : ("(" + courseCode + ")") %></strong>
                  </div>
                </td>
                <td>
                  <div class="mo-posting-deadline">
                    <span class="mo-posting-deadline__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <span><%= deadline %></span>
                  </div>
                </td>
                <td>
                  <div class="mo-posting-metrics">
                    <strong><%= vacancies %> <span>/ <%= applications %></span></strong>
                    <p>Vacancies / Applications</p>
                  </div>
                </td>
                <td>
                  <span class="mo-posting-status mo-posting-status--<%= statusCss %>"><%= status %></span>
                </td>
                <td class="mo-postings-table__right">
                  <div class="mo-posting-actions">
                    <a href="<%= contextPath %>/mo/jobs/applicants?jobId=<%= postingId %>" aria-label="Applicants">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </a>
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

        <div class="mo-postings-table__footer">
          <%
            String baseParams = "keyword=" + java.net.URLEncoder.encode(query.getKeyword() == null ? "" : query.getKeyword(), "UTF-8")
                + "&status=" + java.net.URLEncoder.encode(query.getStatus() == null ? "" : query.getStatus(), "UTF-8")
                + "&sortBy=" + java.net.URLEncoder.encode(query.getSortBy() == null ? "" : query.getSortBy(), "UTF-8")
                + "&size=" + pageSize;
            int startIndex = total == 0 ? 0 : ((currentPage - 1) * pageSize) + 1;
            int endIndex = Math.min(total, currentPage * pageSize);
          %>
          <p>Showing <%= startIndex %>-<%= endIndex %> of <%= total %> job postings</p>
          <div class="mo-pagination">
            <a class="mo-pagination__nav <%= currentPage <= 1 ? "is-disabled" : "" %>"
               href="<%= currentPage <= 1 ? "#" : (contextPath + "/mo/jobs/my?" + baseParams + "&page=" + (currentPage - 1)) %>"
               aria-label="Previous page">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M14.5 6.5 9 12l5.5 5.5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </a>
            <%
              for (int pageNumber = 1; pageNumber <= totalPages; pageNumber++) {
            %>
            <a class="mo-pagination__page <%= pageNumber == currentPage ? "is-active" : "" %>"
               href="<%= contextPath + "/mo/jobs/my?" + baseParams + "&page=" + pageNumber %>"><%= pageNumber %></a>
            <%
              }
            %>
            <a class="mo-pagination__nav <%= currentPage >= totalPages ? "is-disabled" : "" %>"
               href="<%= currentPage >= totalPages ? "#" : (contextPath + "/mo/jobs/my?" + baseParams + "&page=" + (currentPage + 1)) %>"
               aria-label="Next page">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M9.5 6.5 15 12l-5.5 5.5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </a>
          </div>
        </div>
      </section>

      <div class="mo-postings-bottom">
        <a class="mo-postings-bottom__primary" href="<%= contextPath %>/mo/jobs/create">
          <span class="mo-postings-bottom__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M12 5v14M5 12h14" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>Post New Position</span>
        </a>
      </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
