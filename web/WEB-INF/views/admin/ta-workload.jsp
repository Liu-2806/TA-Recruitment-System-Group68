<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  Object reportObj = request.getAttribute("reportPage");
  com.bupt.ta.dto.PageResult reportPage = reportObj instanceof com.bupt.ta.dto.PageResult ? (com.bupt.ta.dto.PageResult) reportObj : null;
  java.util.List rows = reportPage == null || reportPage.getRecords() == null ? java.util.Collections.emptyList() : reportPage.getRecords();
  Object queryObj = request.getAttribute("query");
  java.util.Map query = queryObj instanceof java.util.Map ? (java.util.Map) queryObj : java.util.Collections.emptyMap();
  request.setAttribute("headerBrandHref", contextPath + "/admin/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/admin/dashboard");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", "Super Admin");
  request.setAttribute("currentUserRoleLabel", "Online");
  request.setAttribute("currentUserInitial", "A");
  request.setAttribute("notificationCount", Integer.valueOf(1));
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>TA Workload Management | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/admin-dashboard.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/admin-ta-workload.css">
</head>
<body>
  <div class="admin-workload-shell">
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

        <a class="admin-sidebar__link" href="<%= contextPath %>/admin/mos">
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
          <span>All Positions</span>
        </a>

        <a class="admin-sidebar__link is-active" href="<%= contextPath %>/admin/analytics/ta-workload">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>All TA Workload</span>
        </a>

      </nav>

      <div class="admin-sidebar__footer">
        <p class="admin-sidebar__footer-title">System Health</p>
        <div class="admin-sidebar__health">
          <span class="admin-sidebar__health-dot"></span>
          <span>All services stable</span>
        </div>
      </div>
    </aside>

    <div class="admin-workload-main">
      <jsp:include page="/WEB-INF/views/common/header.jsp" />

      <main class="admin-workload-content">
        <section class="admin-workload-heading">
          <p class="admin-workload-heading__eyebrow">Performance &amp; Workload Analysis</p>
          <a class="admin-workload-heading__back" href="<%= contextPath %>/admin/dashboard">
            <span class="admin-workload-heading__back-icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M15.5 6.5 10 12l5.5 5.5M11 12h8" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <span>Back to Dashboard</span>
          </a>
          <h1>TA Workload Management</h1>
        </section>

        <form class="admin-ta-filter" method="get" action="<%= contextPath %>/admin/analytics/ta-workload">
          <div class="admin-ta-filter__field admin-ta-filter__field--wide">
            <label for="taKeyword">Name / Student ID</label>
            <div class="admin-ta-filter__input">
              <span class="admin-ta-filter__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M10.75 17a6.25 6.25 0 1 0 0-12.5 6.25 6.25 0 0 0 0 12.5Zm8.75 2.5-4.25-4.25" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <input id="taKeyword" name="keyword" type="text" value="<%= String.valueOf(query.getOrDefault("keyword", "")) %>" placeholder="Enter name or ID...">
            </div>
          </div>

          <div class="admin-ta-filter__field">
            <label for="taMajorFilter">Major Filter</label>
            <div class="admin-ta-filter__select">
              <select id="taMajorFilter" name="term">
                <option value="" <%= String.valueOf(query.getOrDefault("term", "")).isBlank() ? "selected" : "" %>>All Majors</option>
                <option value="Software" <%= "Software".equalsIgnoreCase(String.valueOf(query.getOrDefault("term", ""))) ? "selected" : "" %>>Software</option>
                <option value="Computer" <%= "Computer".equalsIgnoreCase(String.valueOf(query.getOrDefault("term", ""))) ? "selected" : "" %>>Computer</option>
                <option value="Communication" <%= "Communication".equalsIgnoreCase(String.valueOf(query.getOrDefault("term", ""))) ? "selected" : "" %>>Communication</option>
              </select>
              <span class="admin-ta-filter__caret" aria-hidden="true">v</span>
            </div>
          </div>

          <div class="admin-ta-filter__field">
            <label for="taStatusFilter">Workload Status</label>
            <div class="admin-ta-filter__select">
              <select id="taStatusFilter" name="minHours">
                <option value="" <%= String.valueOf(query.getOrDefault("minHours", "")).isBlank() ? "selected" : "" %>>All Status</option>
                <option value="1" <%= "1".equals(String.valueOf(query.getOrDefault("minHours", ""))) ? "selected" : "" %>>Normal</option>
                <option value="3" <%= "3".equals(String.valueOf(query.getOrDefault("minHours", ""))) ? "selected" : "" %>>High Alert</option>
                <option value="0" <%= "0".equals(String.valueOf(query.getOrDefault("minHours", ""))) ? "selected" : "" %>>Not Applied</option>
              </select>
              <span class="admin-ta-filter__caret" aria-hidden="true">v</span>
            </div>
          </div>

          <div class="admin-ta-filter__actions">
            <button class="admin-ta-filter__search" type="submit">Search</button>
            <a class="admin-ta-filter__reset" href="<%= contextPath %>/admin/analytics/ta-workload" aria-label="Reset filters">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M7 7.5V4.75m0 0H4.25M7 4.75 4.75 7M6.5 9.5a7 7 0 1 1-1.2 7" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </a>
          </div>
        </form>

        <section class="admin-ta-table-card">
          <div class="admin-ta-table__wrap">
            <table class="admin-ta-table">
              <thead>
                <tr>
                  <th>TA Name</th>
                  <th>Student ID</th>
                  <th>Major</th>
                  <th>Positions</th>
                  <th>Hours</th>
                  <th>Status</th>
                  <th class="admin-ta-table__right">Actions</th>
                </tr>
              </thead>
              <tbody>
                <%
                  if (rows.isEmpty()) {
                %>
                <tr><td colspan="7">No TA workload records found.</td></tr>
                <%
                  } else {
                    for (Object obj : rows) {
                      java.util.Map row = obj instanceof java.util.Map ? (java.util.Map) obj : java.util.Collections.emptyMap();
                      String fullName = String.valueOf(row.getOrDefault("fullName", ""));
                      String studentId = String.valueOf(row.getOrDefault("studentId", ""));
                      String major = String.valueOf(row.getOrDefault("majorProgram", row.getOrDefault("major", "")));
                      int appCount = 0;
                      int accepted = 0;
                      try { appCount = Integer.parseInt(String.valueOf(row.getOrDefault("applicationCount", "0"))); } catch (Exception ignored) {}
                      try { accepted = Integer.parseInt(String.valueOf(row.getOrDefault("acceptedCount", "0"))); } catch (Exception ignored) {}
                      int hours = appCount * 4;
                      String statusText = hours >= 12 ? "High Alert" : (hours == 0 ? "Not Applied" : "Normal");
                      String statusCss = hours >= 12 ? "alert" : (hours == 0 ? "idle" : "normal");
                %>
                <tr>
                  <td><strong class="admin-ta-name"><%= fullName %></strong></td>
                  <td><span class="admin-ta-student"><%= studentId %></span></td>
                  <td><span class="admin-ta-major"><%= major %></span></td>
                  <td><strong class="admin-ta-count"><%= appCount %></strong></td>
                  <td><strong class="admin-ta-hours <%= hours >= 12 ? "admin-ta-hours--alert" : "" %>"><%= hours %>h</strong></td>
                  <td><span class="admin-ta-status admin-ta-status--<%= statusCss %>"><%= statusText %></span></td>
                  <td class="admin-ta-table__right"><button class="admin-ta-details-button" type="button">Details</button></td>
                </tr>
                <%
                    }
                  }
                %>
              </tbody>
            </table>
          </div>
        </section>

        <section class="admin-ta-analysis-card">
          <div class="admin-ta-analysis-card__header">
            <div>
              <div class="admin-ta-analysis-card__title">
                <span class="admin-ta-analysis-card__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M6.5 17.5V10m5 7.5V6.5m5 11V12m-10 8h11" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <h2>Workload Distribution Analysis</h2>
              </div>
              <p class="admin-ta-analysis-card__subtitle">Real-time statistical report</p>
            </div>

            <div class="admin-ta-analysis-card__toggle">
              <button type="button">Major-Based</button>
              <button class="is-active" type="button">Hour-Based</button>
            </div>
          </div>

          <div class="admin-ta-analysis-card__body">
            <div class="admin-ta-chart">
              <div class="admin-ta-chart__baseline"></div>
              <div class="admin-ta-chart__bars">
                <div class="admin-ta-chart__group">
                  <div class="admin-ta-chart__bar admin-ta-chart__bar--light" style="height: 24px;"></div>
                  <span>0-4h</span>
                </div>
                <div class="admin-ta-chart__group">
                  <div class="admin-ta-chart__bar admin-ta-chart__bar--medium" style="height: 56px;"></div>
                  <span>4-8h</span>
                </div>
                <div class="admin-ta-chart__group">
                  <div class="admin-ta-chart__bar admin-ta-chart__bar--strong" style="height: 104px;"></div>
                  <span>8-12h</span>
                </div>
                <div class="admin-ta-chart__group">
                  <div class="admin-ta-chart__bar admin-ta-chart__bar--warn" style="height: 38px;"></div>
                  <span>12h+</span>
                </div>
              </div>
            </div>

            <div class="admin-ta-analysis-side">
              <article class="admin-ta-insight-card">
                <span class="admin-ta-insight-card__eyebrow">Peak Workload</span>
                <strong>Software Dept</strong>
                <p>Most active TA assignments are concentrated in software-related courses this week.</p>
              </article>

              <article class="admin-ta-insight-card admin-ta-insight-card--alert">
                <span class="admin-ta-insight-card__eyebrow">Critical Alerts</span>
                <strong>2 Students</strong>
                <p>Two TAs are near the workload ceiling and should be reviewed before assigning new tasks.</p>
              </article>
            </div>
          </div>
        </section>

        <div class="admin-ta-export">
          <button class="admin-ta-export__button" type="button">
            <span class="admin-ta-export__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 5v9m0 0 3.5-3.5M12 14l-3.5-3.5M5.75 17.5v.75h12.5v-.75" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <span>Export Full Workload Report</span>
          </button>
        </div>
      </main>

      <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </div>

    <div class="admin-ta-modal" id="adminTaModal" aria-hidden="true">
      <div class="admin-ta-modal__backdrop" data-close-ta-modal="true"></div>
      <div class="admin-ta-modal__dialog" role="dialog" aria-modal="true" aria-labelledby="adminTaModalTitle">
        <button class="admin-ta-modal__close" type="button" id="closeAdminTaModal" aria-label="Close details dialog">
          <svg viewBox="0 0 24 24" focusable="false">
            <path d="m7 7 10 10M17 7 7 17" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>

        <div class="admin-ta-modal__header">
          <span class="admin-ta-modal__eyebrow">TA Details</span>
          <h2 id="adminTaModalTitle">Zhang San</h2>
          <p id="adminTaModalSubtitle">Student profile, working positions, workload analysis, and admin suggestions.</p>
        </div>

        <div class="admin-ta-modal__grid">
          <section class="admin-ta-detail-card">
            <h3>Personal Information</h3>
            <div class="admin-ta-detail-card__list" id="adminTaPersonalInfo"></div>
          </section>

          <section class="admin-ta-detail-card">
            <h3>Currently Working Positions</h3>
            <div class="admin-ta-position-list" id="adminTaPositionList"></div>
          </section>

          <section class="admin-ta-detail-card">
            <h3>Workload Analysis</h3>
            <div class="admin-ta-analysis-list" id="adminTaAnalysisList"></div>
          </section>

          <section class="admin-ta-detail-card">
            <h3>Admin Suggestions</h3>
            <ul class="admin-ta-suggestion-list" id="adminTaSuggestionList"></ul>
          </section>
        </div>
      </div>
    </div>
  </div>

  <script src="<%= contextPath %>/assets/js/pages/admin-ta-workload.js"></script>
</body>
</html>
