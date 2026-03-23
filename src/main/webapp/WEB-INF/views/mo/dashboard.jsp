<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>MO Dashboard | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/mo-dashboard.css">
</head>
<body>
  <div class="mo-dashboard-shell">
    <header class="mo-dashboard-header">
      <div class="mo-dashboard-header__inner">
        <a class="app-brand" href="<%= contextPath %>/mo-dashboard-preview.jsp">
          <span class="app-brand__mark">T</span>
          <span class="app-brand__text">MO Workspace: Prof. James Wang</span>
        </a>

        <div class="mo-dashboard-header__actions">
          <button class="ui-icon-button" type="button" aria-label="Notifications">
            <span class="mo-icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 4.25a4 4 0 0 0-4 4v2.06c0 .7-.2 1.39-.58 1.98L6 14.5h12l-1.42-2.21a3.75 3.75 0 0 1-.58-1.98V8.25a4 4 0 0 0-4-4Zm0 15.5a2.38 2.38 0 0 0 2.27-1.75H9.73A2.38 2.38 0 0 0 12 19.75Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <span class="ui-notification-dot"></span>
          </button>

          <div class="ui-user-block">
            <div class="ui-user-block__meta">
              <p class="ui-user-block__name">Prof. James Wang</p>
              <p class="ui-user-block__role">Prof.</p>
            </div>
            <span class="ui-avatar" aria-hidden="true">J</span>
            <span class="ui-chevron" aria-hidden="true">v</span>
          </div>
        </div>
      </div>
    </header>

    <main class="mo-dashboard-main">
      <div class="mo-dashboard-grid">
        <aside class="mo-dashboard-sidebar">
          <section class="mo-card mo-profile-card">
            <div class="mo-profile-card__avatar">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 12a4 4 0 1 0-4-4 4 4 0 0 0 4 4Zm0 1.5c-3.3 0-6 1.97-6 4.4V19h12v-1.1c0-2.43-2.7-4.4-6-4.4Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <h2>Prof. James Wang</h2>
            <p class="mo-profile-card__staff-id">Staff ID: M001</p>
            <p class="mo-profile-card__dept">School of Software Engineering</p>
            <a class="mo-profile-card__button" href="<%= contextPath %>/mo-profile-edit-preview.jsp">
              <span class="mo-profile-card__button-icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M5.5 18.5h3l8.25-8.25-3-3L5.5 15.5Zm0 0-.75 3.25L8 21m5.75-11.75 3 3" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>Edit Profile</span>
              <span class="mo-profile-card__button-arrow" aria-hidden="true">-></span>
            </a>
          </section>

          <section class="mo-card mo-metric-card">
            <div class="mo-metric-card__header">
              <h3>Awaiting Review</h3>
              <span class="mo-metric-card__header-icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.5 4.75h9v14.5h-9Zm3 3h3m-3 4h3m-3 4h3" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
            </div>
            <div class="mo-metric-card__value-row">
              <strong>8</strong>
              <span>applications found</span>
            </div>
            <button class="mo-metric-card__button" type="button">
              <span>Manage Review</span>
              <span aria-hidden="true">-></span>
            </button>
          </section>

          <section class="mo-card mo-alert-card">
            <div class="mo-alert-card__header">
              <h3>System Alerts</h3>
            </div>

            <div class="mo-alert-card__critical">
              <p>3 positions are closing soon. Finalize selections.</p>
            </div>

            <div class="mo-alert-card__minor">
              <span aria-hidden="true">+</span>
              <a href="#">Interview scheduler updated.</a>
            </div>
          </section>
        </aside>

        <section class="mo-dashboard-content">
          <div class="mo-dashboard-actions">
            <a class="mo-action-card mo-action-card--primary" href="<%= contextPath %>/mo-post-position-preview.jsp">
              <div>
                <h2>Post New Position</h2>
                <p>Start recruiting your next TA</p>
              </div>
              <span class="mo-action-card__plus" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M12 5v14M5 12h14" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
            </a>

            <a class="mo-action-card mo-action-card--secondary" href="<%= contextPath %>/mo-postings-preview.jsp">
              <div>
                <h2>My Job Postings</h2>
                <p>Monitor and edit existing listings</p>
              </div>
              <span class="mo-action-card__stack" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="m12 4 7 3.5-7 3.5-7-3.5L12 4Zm0 7 7 3.5-7 3.5-7-3.5L12 11Zm0 7 7-3.5" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
            </a>
          </div>

          <section class="mo-card mo-postings-card">
            <div class="mo-postings-card__header">
              <div class="mo-postings-card__title">
                <span class="mo-postings-card__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <h3>My Active Postings <span>(3)</span></h3>
              </div>
            </div>

            <div class="mo-postings-list">
              <article class="mo-posting-item">
                <div class="mo-posting-item__identity">
                  <span class="mo-posting-item__avatar">S</span>
                  <div>
                    <h4>Software Engineering TA</h4>
                    <p>Applicants: <span>5</span></p>
                  </div>
                </div>
                <a class="mo-posting-item__action" href="<%= contextPath %>/mo-applicants-preview.jsp">
                  <span>Review Candidates</span>
                  <span aria-hidden="true">-></span>
                </a>
              </article>

              <article class="mo-posting-item">
                <div class="mo-posting-item__identity">
                  <span class="mo-posting-item__avatar">D</span>
                  <div>
                    <h4>Data Structures TA</h4>
                    <p>Applicants: <span>2</span></p>
                  </div>
                </div>
                <a class="mo-posting-item__action" href="<%= contextPath %>/mo-applicants-preview.jsp">
                  <span>Review Candidates</span>
                  <span aria-hidden="true">-></span>
                </a>
              </article>

              <article class="mo-posting-item">
                <div class="mo-posting-item__identity">
                  <span class="mo-posting-item__avatar">D</span>
                  <div>
                    <h4>Database Systems TA</h4>
                    <p>Applicants: <span>1</span></p>
                  </div>
                </div>
                <a class="mo-posting-item__action" href="<%= contextPath %>/mo-applicants-preview.jsp">
                  <span>Review Candidates</span>
                  <span aria-hidden="true">-></span>
                </a>
              </article>
            </div>
          </section>

          <section class="mo-card mo-activity-card">
            <div class="mo-activity-card__header">
              <h3>Latest Recruitment Activity</h3>
            </div>

            <div class="mo-activity-card__item">
              <div class="mo-activity-card__item-left">
                <span class="mo-activity-card__item-icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <div>
                  <h4>Software Engineering TA</h4>
                  <p>Notification received: <strong>2 hours ago</strong></p>
                </div>
              </div>
              <span class="mo-activity-card__badge">+3 New Applications</span>
            </div>
          </section>
        </section>
      </div>
    </main>
  </div>
</body>
</html>
