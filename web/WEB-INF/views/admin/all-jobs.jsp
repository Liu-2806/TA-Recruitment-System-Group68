<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/admin-dashboard-preview.jsp");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/admin-dashboard-preview.jsp");
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
  <title>All Job Positions | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/admin-dashboard.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/admin-all-jobs.css">
</head>
<body>
  <div class="admin-alljobs-shell">
    <aside class="admin-sidebar">
      <div class="admin-sidebar__brand">
        <span class="app-brand__mark">T</span>
        <span class="admin-sidebar__brand-text">Admin Portal</span>
      </div>

      <nav class="admin-sidebar__nav" aria-label="Admin Navigation">
        <a class="admin-sidebar__link" href="<%= contextPath %>/admin-dashboard-preview.jsp">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M4.75 4.75h6.5v6.5h-6.5Zm8 0h6.5v6.5h-6.5Zm-8 8h6.5v6.5h-6.5Zm8 0h6.5v6.5h-6.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>Dashboard</span>
        </a>

        <a class="admin-sidebar__link" href="<%= contextPath %>/admin-create-mo-preview.jsp">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22ZM18.5 5.5v6m-3-3h6" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>Create MO Account</span>
        </a>

        <a class="admin-sidebar__link" href="<%= contextPath %>/admin-all-mos-preview.jsp">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M8.5 10.5a3 3 0 1 0-3-3 3 3 0 0 0 3 3Zm7 0a3 3 0 1 0-3-3 3 3 0 0 0 3 3ZM8.5 12c-2.52 0-4.5 1.37-4.5 3.06V16h9v-.94C13 13.37 11.02 12 8.5 12Zm7 0c-.87 0-1.68.14-2.4.4 1.14.65 1.9 1.62 1.9 2.66V16H20v-.94c0-1.69-1.98-3.06-4.5-3.06Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>All MOs</span>
        </a>

        <a class="admin-sidebar__link is-active" href="<%= contextPath %>/admin-all-jobs-preview.jsp">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>All Positions</span>
        </a>

        <a class="admin-sidebar__link" href="<%= contextPath %>/admin-ta-workload-preview.jsp">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>All TA Workload</span>
        </a>

      </nav>
    </aside>

    <div class="admin-alljobs-main">
      <jsp:include page="/WEB-INF/views/common/header.jsp" />

      <main class="admin-alljobs-content">
        <div class="admin-alljobs-heading">
          <h1>All Job Positions</h1>
        </div>

        <section class="admin-alljobs-filter">
          <div class="admin-alljobs-filter__title">
            <span class="admin-alljobs-filter__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M4.75 6.25h14.5L14 12v5.25l-4 1.5V12Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <span>Search Filter:</span>
          </div>

          <div class="admin-alljobs-filter__search">
            <span class="admin-alljobs-filter__search-icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M10.75 17a6.25 6.25 0 1 0 0-12.5 6.25 6.25 0 0 0 0 12.5Zm8.75 2.5-4.25-4.25" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <input type="text" placeholder="Course Title...">
          </div>

          <div class="admin-alljobs-filter__select">
            <select>
              <option>All Statuses</option>
              <option>Open</option>
              <option>Closed</option>
            </select>
            <span class="admin-alljobs-filter__caret" aria-hidden="true">v</span>
          </div>

          <div class="admin-alljobs-filter__select">
            <select>
              <option>All MOs</option>
              <option>Prof. Wang</option>
              <option>Prof. Li</option>
              <option>Prof. Zhang</option>
            </select>
            <span class="admin-alljobs-filter__caret" aria-hidden="true">v</span>
          </div>

          <div class="admin-alljobs-filter__actions">
            <button class="admin-alljobs-filter__button" type="button">Filter</button>
            <button class="admin-alljobs-filter__reset" type="button" aria-label="Reset filters">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M7 7.5V4.75m0 0H4.25M7 4.75 4.75 7M6.5 9.5a7 7 0 1 1-1.2 7" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
          </div>
        </section>

        <section class="admin-alljobs-table-card">
          <div class="admin-alljobs-table__wrap">
            <table class="admin-alljobs-table">
              <thead>
                <tr>
                  <th>Course Title</th>
                  <th>Posted MO</th>
                  <th>Deadline</th>
                  <th>Vacancies / Apps</th>
                  <th>Status</th>
                  <th class="admin-alljobs-table__right">Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>
                    <div class="admin-alljobs-course">
                      <span class="admin-alljobs-course__avatar">S</span>
                      <strong>Software Engineering</strong>
                    </div>
                  </td>
                  <td class="admin-alljobs-mo">Prof. Wang</td>
                  <td>
                    <div class="admin-alljobs-deadline">
                      <span class="admin-alljobs-deadline__icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24" focusable="false">
                          <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                      </span>
                      <span>2026-03-30</span>
                    </div>
                  </td>
                  <td>
                    <div class="admin-alljobs-metrics">
                      <strong>3 <span>/ 5</span></strong>
                      <p>Cap / Recv</p>
                    </div>
                  </td>
                  <td>
                    <span class="admin-alljobs-status admin-alljobs-status--open">
                      <span class="admin-alljobs-status__icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24" focusable="false">
                          <path d="M7.75 12.25 10.5 15l5.75-5.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                      </span>
                      Open
                    </span>
                  </td>
                  <td class="admin-alljobs-table__right">
                    <button class="admin-alljobs-action-more" type="button" aria-label="More">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <circle cx="12" cy="5.5" r="1.5" fill="currentColor"/>
                        <circle cx="12" cy="12" r="1.5" fill="currentColor"/>
                        <circle cx="12" cy="18.5" r="1.5" fill="currentColor"/>
                      </svg>
                    </button>
                  </td>
                </tr>

                <tr>
                  <td>
                    <div class="admin-alljobs-course">
                      <span class="admin-alljobs-course__avatar">D</span>
                      <strong>Data Structures</strong>
                    </div>
                  </td>
                  <td class="admin-alljobs-mo">Prof. Li</td>
                  <td>
                    <div class="admin-alljobs-deadline">
                      <span class="admin-alljobs-deadline__icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24" focusable="false">
                          <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                      </span>
                      <span>2026-04-05</span>
                    </div>
                  </td>
                  <td>
                    <div class="admin-alljobs-metrics">
                      <strong>2 <span>/ 2</span></strong>
                      <p>Cap / Recv</p>
                    </div>
                  </td>
                  <td>
                    <span class="admin-alljobs-status admin-alljobs-status--open">
                      <span class="admin-alljobs-status__icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24" focusable="false">
                          <path d="M7.75 12.25 10.5 15l5.75-5.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                      </span>
                      Open
                    </span>
                  </td>
                  <td class="admin-alljobs-table__right">
                    <button class="admin-alljobs-action-more" type="button" aria-label="More">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <circle cx="12" cy="5.5" r="1.5" fill="currentColor"/>
                        <circle cx="12" cy="12" r="1.5" fill="currentColor"/>
                        <circle cx="12" cy="18.5" r="1.5" fill="currentColor"/>
                      </svg>
                    </button>
                  </td>
                </tr>

                <tr>
                  <td>
                    <div class="admin-alljobs-course">
                      <span class="admin-alljobs-course__avatar">D</span>
                      <strong>Database Systems</strong>
                    </div>
                  </td>
                  <td class="admin-alljobs-mo">Prof. Zhang</td>
                  <td>
                    <div class="admin-alljobs-deadline">
                      <span class="admin-alljobs-deadline__icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24" focusable="false">
                          <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                      </span>
                      <span>2026-03-28</span>
                    </div>
                  </td>
                  <td>
                    <div class="admin-alljobs-metrics">
                      <strong>1 <span>/ 1</span></strong>
                      <p>Cap / Recv</p>
                    </div>
                  </td>
                  <td>
                    <span class="admin-alljobs-status admin-alljobs-status--closed">
                      <span class="admin-alljobs-status__icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24" focusable="false">
                          <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                      </span>
                      Closed
                    </span>
                  </td>
                  <td class="admin-alljobs-table__right">
                    <button class="admin-alljobs-action-more" type="button" aria-label="More">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <circle cx="12" cy="5.5" r="1.5" fill="currentColor"/>
                        <circle cx="12" cy="12" r="1.5" fill="currentColor"/>
                        <circle cx="12" cy="18.5" r="1.5" fill="currentColor"/>
                      </svg>
                    </button>
                  </td>
                </tr>

                <tr>
                  <td>
                    <div class="admin-alljobs-course">
                      <span class="admin-alljobs-course__avatar">C</span>
                      <strong>Computer Networks</strong>
                    </div>
                  </td>
                  <td class="admin-alljobs-mo">Prof. Wang</td>
                  <td>
                    <div class="admin-alljobs-deadline">
                      <span class="admin-alljobs-deadline__icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24" focusable="false">
                          <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                      </span>
                      <span>2026-04-10</span>
                    </div>
                  </td>
                  <td>
                    <div class="admin-alljobs-metrics">
                      <strong>2 <span>/ 0</span></strong>
                      <p>Cap / Recv</p>
                    </div>
                  </td>
                  <td>
                    <span class="admin-alljobs-status admin-alljobs-status--open">
                      <span class="admin-alljobs-status__icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24" focusable="false">
                          <path d="M7.75 12.25 10.5 15l5.75-5.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                      </span>
                      Open
                    </span>
                  </td>
                  <td class="admin-alljobs-table__right">
                    <button class="admin-alljobs-action-more" type="button" aria-label="More">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <circle cx="12" cy="5.5" r="1.5" fill="currentColor"/>
                        <circle cx="12" cy="12" r="1.5" fill="currentColor"/>
                        <circle cx="12" cy="18.5" r="1.5" fill="currentColor"/>
                      </svg>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <div class="admin-alljobs-table__footer">
            <p>Total Postings: 12</p>
            <div class="admin-alljobs-pagination">
              <button type="button" class="admin-alljobs-pagination__nav" aria-label="Previous page">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M14.5 6.5 9 12l5.5 5.5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </button>
              <button type="button" class="admin-alljobs-pagination__page is-active">1</button>
              <button type="button" class="admin-alljobs-pagination__page">2</button>
              <button type="button" class="admin-alljobs-pagination__page">3</button>
              <button type="button" class="admin-alljobs-pagination__nav" aria-label="Next page">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M9.5 6.5 15 12l-5.5 5.5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </button>
            </div>
          </div>
        </section>
      </main>

      <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </div>
  </div>
</body>
</html>
