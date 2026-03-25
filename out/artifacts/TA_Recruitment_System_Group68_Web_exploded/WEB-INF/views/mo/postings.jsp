<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/mo-dashboard-preview.jsp");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/mo-dashboard-preview.jsp");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", "Prof. Wang");
  request.setAttribute("currentUserRoleLabel", "Module Organizer");
  request.setAttribute("currentUserInitial", "W");
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

      <section class="mo-postings-filter">
        <div class="mo-postings-filter__left">
          <div class="mo-postings-filter__select">
            <span class="mo-postings-filter__label">Filter By:</span>
            <select>
              <option>All Statuses</option>
              <option>Open</option>
              <option>Closed</option>
            </select>
          </div>

          <div class="mo-postings-filter__search">
            <span class="mo-postings-filter__search-icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M10.75 17a6.25 6.25 0 1 0 0-12.5 6.25 6.25 0 0 0 0 12.5Zm8.75 2.5-4.25-4.25" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <input type="text" placeholder="Search by Course Name...">
          </div>
        </div>

        <div class="mo-postings-filter__actions">
          <button class="mo-postings-filter__apply" type="button">Apply Filter</button>
          <button class="mo-postings-filter__reset" type="button" aria-label="Reset filters">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M7 7.5V4.75m0 0H4.25M7 4.75 4.75 7M6.5 9.5a7 7 0 1 1-1.2 7" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
        </div>
      </section>

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
              <tr>
                <td>
                  <div class="mo-posting-course">
                    <span class="mo-posting-course__avatar">S</span>
                    <strong>Software Engineering TA</strong>
                  </div>
                </td>
                <td>
                  <div class="mo-posting-deadline">
                    <span class="mo-posting-deadline__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <span>2026-03-30</span>
                  </div>
                </td>
                <td>
                  <div class="mo-posting-metrics">
                    <strong>3 <span>/ 5</span></strong>
                    <p>Applications</p>
                  </div>
                </td>
                <td>
                  <span class="mo-posting-status mo-posting-status--open">
                    <span class="mo-posting-status__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M7.75 12.25 10.5 15l5.75-5.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    Open
                  </span>
                </td>
                <td class="mo-postings-table__right">
                  <div class="mo-posting-actions">
                    <a href="<%= contextPath %>/mo-applicants-preview.jsp" aria-label="Applicants">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </a>
                    <button type="button" aria-label="Edit">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M5.5 18.5h3l8.25-8.25-3-3L5.5 15.5Zm0 0-.75 3.25L8 21m5.75-11.75 3 3" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </button>
                    <button class="is-danger" type="button" aria-label="Delete">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="m8 8 8 8m0-8-8 8M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </button>
                    <button type="button" aria-label="More actions">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <circle cx="6.5" cy="12" r="1.5" fill="currentColor"/>
                        <circle cx="12" cy="12" r="1.5" fill="currentColor"/>
                        <circle cx="17.5" cy="12" r="1.5" fill="currentColor"/>
                      </svg>
                    </button>
                  </div>
                </td>
              </tr>

              <tr>
                <td>
                  <div class="mo-posting-course">
                    <span class="mo-posting-course__avatar">D</span>
                    <strong>Data Structures TA</strong>
                  </div>
                </td>
                <td>
                  <div class="mo-posting-deadline">
                    <span class="mo-posting-deadline__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <span>2026-04-05</span>
                  </div>
                </td>
                <td>
                  <div class="mo-posting-metrics">
                    <strong>2 <span>/ 2</span></strong>
                    <p>Applications</p>
                  </div>
                </td>
                <td>
                  <span class="mo-posting-status mo-posting-status--open">
                    <span class="mo-posting-status__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M7.75 12.25 10.5 15l5.75-5.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    Open
                  </span>
                </td>
                <td class="mo-postings-table__right">
                  <div class="mo-posting-actions">
                    <a href="<%= contextPath %>/mo-applicants-preview.jsp" aria-label="Applicants">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </a>
                    <button type="button" aria-label="Edit">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M5.5 18.5h3l8.25-8.25-3-3L5.5 15.5Zm0 0-.75 3.25L8 21m5.75-11.75 3 3" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </button>
                    <button class="is-danger" type="button" aria-label="Delete">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="m8 8 8 8m0-8-8 8M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </button>
                    <button type="button" aria-label="More actions">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <circle cx="6.5" cy="12" r="1.5" fill="currentColor"/>
                        <circle cx="12" cy="12" r="1.5" fill="currentColor"/>
                        <circle cx="17.5" cy="12" r="1.5" fill="currentColor"/>
                      </svg>
                    </button>
                  </div>
                </td>
              </tr>

              <tr>
                <td>
                  <div class="mo-posting-course">
                    <span class="mo-posting-course__avatar">D</span>
                    <strong>Database Systems TA</strong>
                  </div>
                </td>
                <td>
                  <div class="mo-posting-deadline">
                    <span class="mo-posting-deadline__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <span>2026-03-28</span>
                  </div>
                </td>
                <td>
                  <div class="mo-posting-metrics">
                    <strong>1 <span>/ 1</span></strong>
                    <p>Applications</p>
                  </div>
                </td>
                <td>
                  <span class="mo-posting-status mo-posting-status--closed">
                    <span class="mo-posting-status__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    Closed
                  </span>
                </td>
                <td class="mo-postings-table__right">
                  <div class="mo-posting-actions">
                    <a href="<%= contextPath %>/mo-applicants-preview.jsp" aria-label="Applicants">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </a>
                    <button type="button" aria-label="More actions">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <circle cx="6.5" cy="12" r="1.5" fill="currentColor"/>
                        <circle cx="12" cy="12" r="1.5" fill="currentColor"/>
                        <circle cx="17.5" cy="12" r="1.5" fill="currentColor"/>
                      </svg>
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="mo-postings-table__footer">
          <p>Total Job Postings: 3</p>
          <button class="mo-postings-sync" type="button">
            <span class="mo-postings-sync__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 6V3l4 4-4 4V8a4 4 0 1 0 3.46 6h2.1A6 6 0 1 1 12 6Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <span>Sync with Department HR</span>
          </button>
        </div>
      </section>

      <div class="mo-postings-bottom">
        <a class="mo-postings-bottom__primary" href="<%= contextPath %>/mo-post-position-preview.jsp">
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
