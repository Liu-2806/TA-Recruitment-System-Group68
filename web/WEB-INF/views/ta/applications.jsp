<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/ta-dashboard-preview.jsp");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/ta-dashboard-preview.jsp");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", "Zhang San");
  request.setAttribute("currentUserRoleLabel", "TA Applicant");
  request.setAttribute("currentUserInitial", "Z");
  request.setAttribute("notificationCount", Integer.valueOf(1));
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
      <section class="ta-applications-filter">
        <div class="ta-applications-filter__left">
          <div class="ta-applications-filter__title">
            <span class="ta-applications-filter__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M4.75 6.25h14.5L14 12v5.25l-4 1.5V12Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <span>Filter by Status:</span>
          </div>

          <div class="ta-applications-tabs" role="tablist" aria-label="Application Status Filters">
            <button class="ta-applications-tabs__item is-active" type="button" aria-pressed="true">All</button>
            <button class="ta-applications-tabs__item" type="button" aria-pressed="false">Pending</button>
            <button class="ta-applications-tabs__item" type="button" aria-pressed="false">Accepted</button>
            <button class="ta-applications-tabs__item" type="button" aria-pressed="false">Rejected</button>
          </div>
        </div>

        <p class="ta-applications-filter__current">Current: <strong>All Records</strong></p>
      </section>

      <section class="ta-applications-table-card">
        <div class="ta-applications-table__wrap">
          <table class="ta-applications-table">
            <thead>
              <tr>
                <th>Position</th>
                <th>Applied Date</th>
                <th>Status</th>
                <th class="ta-applications-table__right">Action</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>
                  <div class="ta-application-position">
                    <span class="ta-application-position__icon ta-application-position__icon--pending" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 8.5v4.5m0 3h.01M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <div class="ta-application-position__meta">
                      <strong>Software Engineering TA</strong>
                      <p>MO: Prof. Wang</p>
                    </div>
                  </div>
                </td>
                <td class="ta-applications-table__date">2026-03-10</td>
                <td>
                  <span class="ta-application-status ta-application-status--pending">
                    <span class="ta-application-status__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    Pending
                  </span>
                </td>
                <td class="ta-applications-table__right">
                  <a class="ta-application-action" href="<%= contextPath %>/ta-position-details-preview.jsp">
                    <span class="ta-application-action__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M7.75 4.75h6l3 3v11.5H7.75Zm6 0v3h3M10 12.25h4m-4 3h4" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <span>View Details</span>
                  </a>
                </td>
              </tr>

              <tr>
                <td>
                  <div class="ta-application-position">
                    <span class="ta-application-position__icon ta-application-position__icon--accepted" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M7.75 12.25 10.5 15l5.75-5.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <div class="ta-application-position__meta">
                      <strong>Data Structures TA</strong>
                      <p>MO: Prof. Li</p>
                    </div>
                  </div>
                </td>
                <td class="ta-applications-table__date">2026-03-08</td>
                <td>
                  <span class="ta-application-status ta-application-status--accepted">
                    <span class="ta-application-status__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M7.75 12.25 10.5 15l5.75-5.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    Accepted
                  </span>
                </td>
                <td class="ta-applications-table__right">
                  <button class="ta-application-action" type="button">
                    <span class="ta-application-action__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M4.75 7.25h14.5a1.25 1.25 0 0 1 1.25 1.25v7a1.25 1.25 0 0 1-1.25 1.25H4.75A1.25 1.25 0 0 1 3.5 15.5v-7a1.25 1.25 0 0 1 1.25-1.25Zm0 .75L12 12.75 19.25 8" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <span>Contact MO</span>
                  </button>
                </td>
              </tr>

              <tr>
                <td>
                  <div class="ta-application-position">
                    <span class="ta-application-position__icon ta-application-position__icon--rejected" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="m8 8 8 8m0-8-8 8M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <div class="ta-application-position__meta">
                      <strong>Database Systems TA</strong>
                      <p>MO: Prof. Zhang</p>
                    </div>
                  </div>
                </td>
                <td class="ta-applications-table__date">2026-03-01</td>
                <td>
                  <span class="ta-application-status ta-application-status--rejected">
                    <span class="ta-application-status__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="m8 8 8 8m0-8-8 8M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    Rejected
                  </span>
                </td>
                <td class="ta-applications-table__right">
                  <button class="ta-application-action" type="button">
                    <span class="ta-application-action__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M5.5 7.75h13A1.25 1.25 0 0 1 19.75 9v6A1.25 1.25 0 0 1 18.5 16.25H9.75l-3.5 2v-2H5.5A1.25 1.25 0 0 1 4.25 15V9A1.25 1.25 0 0 1 5.5 7.75Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <span>View Feedback</span>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="ta-applications-table__footer">
          <p>Total Records: 3</p>
          <button class="ta-applications-export" type="button">
            <span>Export All Records</span>
            <span class="ta-applications-export__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M5.5 12h13m0 0-5-5m5 5-5 5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
          </button>
        </div>
      </section>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
