<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/admin-dashboard-preview.jsp");
  request.setAttribute("showHeaderBack", Boolean.FALSE);
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", "Super Admin");
  request.setAttribute("currentUserRoleLabel", "System Control");
  request.setAttribute("currentUserInitial", "A");
  request.setAttribute("notificationCount", Integer.valueOf(1));
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin Dashboard | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/admin-dashboard.css">
</head>
<body>
  <div class="admin-dashboard-shell">
    <aside class="admin-sidebar">
      <div class="admin-sidebar__brand">
        <span class="app-brand__mark">T</span>
        <span class="admin-sidebar__brand-text">Admin Portal</span>
      </div>

      <nav class="admin-sidebar__nav" aria-label="Admin Navigation">
        <a class="admin-sidebar__link is-active" href="<%= contextPath %>/admin-dashboard-preview.jsp">
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

        <a class="admin-sidebar__link" href="<%= contextPath %>/admin-all-jobs-preview.jsp">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>All Postings</span>
        </a>

        <a class="admin-sidebar__link" href="#">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>All TA Workload</span>
        </a>

        <a class="admin-sidebar__link" href="#">
          <span class="admin-sidebar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="m10.25 4.75 1-1.5h1.5l1 1.5 1.7.35.85 1.3-.55 1.65 1.15 1.3-.3 1.55-1.55.55-.55 1.55-1.55.3-1.3-1.15-1.65.55-1.3-.85-.35-1.7-1.5-1v-1.5l1.5-1-.35-1.7.85-1.3ZM12 15.5a3.5 3.5 0 1 0-3.5-3.5A3.5 3.5 0 0 0 12 15.5Z" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>Settings (Optional)</span>
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

    <div class="admin-main">
      <jsp:include page="/WEB-INF/views/common/header.jsp" />

      <main class="admin-content">
        <section class="admin-welcome">
          <h1>Welcome back, Admin!</h1>
          <p>Monitoring the recruitment system activity.</p>
        </section>

        <section class="admin-section">
          <p class="admin-section__eyebrow">System Overview</p>
          <div class="admin-stats">
            <article class="admin-stat-card">
              <span class="admin-stat-card__icon admin-stat-card__icon--blue" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M8.5 10.5a3 3 0 1 0-3-3 3 3 0 0 0 3 3Zm7 0a3 3 0 1 0-3-3 3 3 0 0 0 3 3ZM8.5 12c-2.52 0-4.5 1.37-4.5 3.06V16h9v-.94C13 13.37 11.02 12 8.5 12Zm7 0c-.87 0-1.68.14-2.4.4 1.14.65 1.9 1.62 1.9 2.66V16H20v-.94c0-1.69-1.98-3.06-4.5-3.06Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div class="admin-stat-card__meta">
                <p>Total TAs</p>
                <strong>25</strong>
              </div>
            </article>

            <article class="admin-stat-card">
              <span class="admin-stat-card__icon admin-stat-card__icon--purple" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22ZM18.5 6V9m-1.5-1.5H20" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div class="admin-stat-card__meta">
                <p>Total MOs</p>
                <strong>8</strong>
              </div>
            </article>

            <article class="admin-stat-card">
              <span class="admin-stat-card__icon admin-stat-card__icon--green" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div class="admin-stat-card__meta">
                <p>Total Postings</p>
                <strong>12</strong>
              </div>
            </article>
          </div>
        </section>

        <section class="admin-section">
          <p class="admin-section__eyebrow">Quick Actions</p>
          <div class="admin-actions">
            <a class="admin-action-card admin-action-card--primary" href="<%= contextPath %>/admin-create-mo-preview.jsp">
              <div>
                <h2>Create New MO</h2>
                <p>Onboard new module organizers</p>
              </div>
              <span class="admin-action-card__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M12 5v14M5 12h14" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
            </a>

            <a class="admin-action-card" href="<%= contextPath %>/admin-all-jobs-preview.jsp">
              <div>
                <h2>View All Postings</h2>
                <p>Audit active recruitment</p>
              </div>
              <span class="admin-action-card__icon admin-action-card__icon--muted" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.5 4.75h6l3 3v11.5H7.5Zm6 0v3h3M10 12.25h4m-4 3h4" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
            </a>

            <a class="admin-action-card" href="#">
              <div>
                <h2>Export Report</h2>
                <p>Download system summary</p>
              </div>
              <span class="admin-action-card__icon admin-action-card__icon--muted" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M12 5v9m0 0 3.5-3.5M12 14l-3.5-3.5M5.75 17.5v.75h12.5v-.75" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
            </a>
          </div>
        </section>

        <section class="admin-section">
          <div class="admin-section__header">
            <p class="admin-section__eyebrow">Recent Activity</p>
            <a class="admin-section__link" href="#">View All Logs</a>
          </div>

          <div class="admin-activity-card">
            <article class="admin-activity-item">
              <span class="admin-activity-item__icon admin-activity-item__icon--blue" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M12 5v14M5 12h14" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div class="admin-activity-item__content">
                <strong>Prof. Wang posted 'Software Engineering TA'</strong>
                <p>Just now</p>
              </div>
              <button class="admin-activity-item__more" type="button" aria-label="More">
                <svg viewBox="0 0 24 24" focusable="false">
                  <circle cx="12" cy="5.5" r="1.5" fill="currentColor"/>
                  <circle cx="12" cy="12" r="1.5" fill="currentColor"/>
                  <circle cx="12" cy="18.5" r="1.5" fill="currentColor"/>
                </svg>
              </button>
            </article>

            <article class="admin-activity-item">
              <span class="admin-activity-item__icon admin-activity-item__icon--green" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.75 12.25 10.5 15l5.75-5.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div class="admin-activity-item__content">
                <strong>Prof. Li hired Zhang San</strong>
                <p>2 hours ago</p>
              </div>
              <button class="admin-activity-item__more" type="button" aria-label="More">
                <svg viewBox="0 0 24 24" focusable="false">
                  <circle cx="12" cy="5.5" r="1.5" fill="currentColor"/>
                  <circle cx="12" cy="12" r="1.5" fill="currentColor"/>
                  <circle cx="12" cy="18.5" r="1.5" fill="currentColor"/>
                </svg>
              </button>
            </article>

            <article class="admin-activity-item">
              <span class="admin-activity-item__icon admin-activity-item__icon--gray" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="m8 8 8 8m0-8-8 8M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div class="admin-activity-item__content">
                <strong>Prof. Zhang closed 'Database Systems TA' position</strong>
                <p>5 hours ago</p>
              </div>
              <button class="admin-activity-item__more" type="button" aria-label="More">
                <svg viewBox="0 0 24 24" focusable="false">
                  <circle cx="12" cy="5.5" r="1.5" fill="currentColor"/>
                  <circle cx="12" cy="12" r="1.5" fill="currentColor"/>
                  <circle cx="12" cy="18.5" r="1.5" fill="currentColor"/>
                </svg>
              </button>
            </article>
          </div>
        </section>
      </main>
    </div>
  </div>
</body>
</html>
