<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/admin/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/admin/dashboard");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", "Super Admin");
  request.setAttribute("currentUserRoleLabel", "Online");
  request.setAttribute("currentUserInitial", "A");
  request.setAttribute("notificationCount", Integer.valueOf(1));

  Object queryObj = request.getAttribute("query");
  java.util.Map query = queryObj instanceof java.util.Map ? (java.util.Map) queryObj : java.util.Collections.emptyMap();
  String keyword = String.valueOf(query.get("keyword") == null ? "" : query.get("keyword"));
  String department = String.valueOf(query.get("department") == null ? "" : query.get("department"));
  String sizeParam = String.valueOf(query.get("size") == null ? "5" : query.get("size"));

  Object pageObj = request.getAttribute("mosPage");
  com.bupt.ta.dto.PageResult mosPage = pageObj instanceof com.bupt.ta.dto.PageResult ? (com.bupt.ta.dto.PageResult) pageObj : null;
  java.util.List moRecords = mosPage == null || mosPage.getRecords() == null ? java.util.Collections.emptyList() : mosPage.getRecords();
  int currentPage = mosPage == null ? 1 : mosPage.getPage();
  int pageSize = mosPage == null ? 5 : mosPage.getSize();
  int total = mosPage == null ? moRecords.size() : (int) mosPage.getTotal();
  int totalPages = pageSize <= 0 ? 1 : Math.max(1, (int) Math.ceil(total / (double) pageSize));
  String baseParams = "keyword=" + java.net.URLEncoder.encode(keyword, "UTF-8")
      + "&department=" + java.net.URLEncoder.encode(department, "UTF-8")
      + "&size=" + java.net.URLEncoder.encode(sizeParam, "UTF-8");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>All MOs | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/admin-dashboard.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/admin-all-mos.css">
</head>
<body>
  <div class="admin-allmos-shell">
    <aside class="admin-sidebar">
      <div class="admin-sidebar__brand">
        <span class="app-brand__mark">T</span>
        <span class="admin-sidebar__brand-text">Admin Portal</span>
      </div>

      <nav class="admin-sidebar__nav" aria-label="Admin Navigation">
        <a class="admin-sidebar__link" href="<%= contextPath %>/admin/dashboard">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M4.75 4.75h6.5v6.5h-6.5Zm8 0h6.5v6.5h-6.5Zm-8 8h6.5v6.5h-6.5Zm8 0h6.5v6.5h-6.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>Dashboard</span>
        </a>

        <a class="admin-sidebar__link" href="<%= contextPath %>/admin/mos/create">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22ZM18.5 5.5v6m-3-3h6" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>Create MO Account</span>
        </a>

        <a class="admin-sidebar__link is-active" href="<%= contextPath %>/admin/mos">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M8.5 10.5a3 3 0 1 0-3-3 3 3 0 0 0 3 3Zm7 0a3 3 0 1 0-3-3 3 3 0 0 0 3 3ZM8.5 12c-2.52 0-4.5 1.37-4.5 3.06V16h9v-.94C13 13.37 11.02 12 8.5 12Zm7 0c-.87 0-1.68.14-2.4.4 1.14.65 1.9 1.62 1.9 2.66V16H20v-.94c0-1.69-1.98-3.06-4.5-3.06Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>All MOs</span>
        </a>

        <a class="admin-sidebar__link" href="<%= contextPath %>/admin/jobs">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>All Postings</span>
        </a>

        <a class="admin-sidebar__link" href="<%= contextPath %>/admin/analytics/ta-workload">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>All TA Workload</span>
        </a>

      </nav>
    </aside>

    <div class="admin-allmos-main">
      <jsp:include page="/WEB-INF/views/common/header.jsp" />

      <main class="admin-allmos-content">
        <section class="admin-allmos-heading">
          <div>
            <h1>All MO Accounts</h1>
            <p>Manage and monitor all Module Organizer credentials.</p>
          </div>
          <a class="admin-allmos-add" href="<%= contextPath %>/admin/mos/create">
            <span class="admin-allmos-add__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22ZM18.5 5.5v6m-3-3h6" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <span>Add New MO</span>
          </a>
        </section>

        <form class="admin-allmos-filter" method="get" action="<%= contextPath %>/admin/mos">
          <div class="admin-allmos-filter__left">
            <div class="admin-allmos-filter__title">
              <span class="admin-allmos-filter__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M4.75 6.25h14.5L14 12v5.25l-4 1.5V12Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>Filter Options:</span>
            </div>

            <div class="admin-allmos-filter__search">
              <span class="admin-allmos-filter__search-icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M10.75 17a6.25 6.25 0 1 0 0-12.5 6.25 6.25 0 0 0 0 12.5Zm8.75 2.5-4.25-4.25" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <input type="text" name="keyword" value="<%= keyword %>" placeholder="Search by Name or Email...">
            </div>

            <div class="admin-allmos-filter__select">
              <select name="department">
                <option value="" <%= department.isBlank() ? "selected" : "" %>>All Departments</option>
                <option value="Software Engineering" <%= "Software Engineering".equalsIgnoreCase(department) ? "selected" : "" %>>Software Engineering</option>
                <option value="Computer Science" <%= "Computer Science".equalsIgnoreCase(department) ? "selected" : "" %>>Computer Science</option>
                <option value="Data Science" <%= "Data Science".equalsIgnoreCase(department) ? "selected" : "" %>>Data Science</option>
              </select>
              <span class="admin-allmos-filter__caret" aria-hidden="true">v</span>
            </div>
          </div>

          <div class="admin-allmos-filter__actions">
            <input type="hidden" name="size" value="<%= pageSize %>">
            <button class="admin-allmos-filter__search-button" type="submit">Search</button>
            <a class="admin-allmos-filter__reset" href="<%= contextPath %>/admin/mos" aria-label="Reset filters">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M7 7.5V4.75m0 0H4.25M7 4.75 4.75 7M6.5 9.5a7 7 0 1 1-1.2 7" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </a>
          </div>
        </form>

        <section class="admin-allmos-table-card">
          <div class="admin-allmos-table__wrap">
            <table class="admin-allmos-table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Staff ID</th>
                  <th>Email Address</th>
                  <th>Department</th>
                  <th class="admin-allmos-table__right">Actions</th>
                </tr>
              </thead>
              <tbody>
                <%
                  if (moRecords.isEmpty()) {
                %>
                <tr>
                  <td colspan="5">No MO records found.</td>
                </tr>
                <%
                  } else {
                    for (Object moObj : moRecords) {
                      com.bupt.ta.model.User mo = moObj instanceof com.bupt.ta.model.User ? (com.bupt.ta.model.User) moObj : null;
                      if (mo == null) {
                        continue;
                      }
                      String displayName = mo.getFullName() == null || mo.getFullName().isBlank() ? (mo.getDisplayName() == null ? "" : mo.getDisplayName()) : mo.getFullName();
                      String displayInitial = displayName == null || displayName.isBlank() ? "M" : displayName.substring(0, 1).toUpperCase();
                %>
                <tr>
                  <td>
                    <div class="admin-allmos-name">
                      <span class="admin-allmos-name__avatar"><%= displayInitial %></span>
                      <strong><%= displayName %></strong>
                    </div>
                  </td>
                  <td><span class="admin-allmos-staff"><%= mo.getStaffId() == null ? "" : mo.getStaffId() %></span></td>
                  <td class="admin-allmos-email"><%= mo.getEmail() == null ? "" : mo.getEmail() %></td>
                  <td class="admin-allmos-department"><%= mo.getDepartment() == null ? "" : mo.getDepartment() %></td>
                  <td class="admin-allmos-table__right">
                    <div class="admin-allmos-actions">
                      <a class="admin-allmos-actions__link" href="<%= contextPath %>/admin/mos/detail?moUserId=<%= mo.getId() %>">Details</a>
                      <form action="<%= contextPath %>/admin/mos/reset-password" method="post" style="display:inline;">
                        <input type="hidden" name="moUserId" value="<%= mo.getId() %>">
                        <input type="hidden" name="newPassword" value="Temp123!">
                        <button class="admin-allmos-actions__resetpwd" type="submit">Reset PWD</button>
                      </form>
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

          <div class="admin-allmos-table__footer">
            <p>Total Records: <%= total %></p>
            <div class="admin-allmos-pagination">
              <a class="admin-allmos-pagination__nav <%= currentPage <= 1 ? "is-disabled" : "" %>"
                 aria-label="Previous page"
                 href="<%= currentPage <= 1 ? "#" : (contextPath + "/admin/mos?" + baseParams + "&page=" + (currentPage - 1)) %>">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M14.5 6.5 9 12l5.5 5.5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </a>
              <span class="admin-allmos-pagination__page is-active"><%= currentPage %></span>
              <span class="admin-allmos-pagination__page"><%= totalPages %></span>
              <a class="admin-allmos-pagination__nav <%= currentPage >= totalPages ? "is-disabled" : "" %>"
                 aria-label="Next page"
                 href="<%= currentPage >= totalPages ? "#" : (contextPath + "/admin/mos?" + baseParams + "&page=" + (currentPage + 1)) %>">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M9.5 6.5 15 12l-5.5 5.5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </a>
            </div>
          </div>
        </section>
      </main>

      <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </div>
  </div>
</body>
</html>
