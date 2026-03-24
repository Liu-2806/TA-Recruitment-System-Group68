<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/ta-dashboard-preview.jsp");
  request.setAttribute("showHeaderBack", Boolean.FALSE);
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
  <title>TA Dashboard | TA Recruitment Portal</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/ta-dashboard.css">
</head>
<body>
  <div class="ta-dashboard-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="ta-dashboard-main">
      <div class="ta-dashboard-grid">
        <aside class="ta-dashboard-sidebar">
          <section class="ta-card ta-card--profile">
            <div class="ta-card__accent"></div>
            <div class="ta-profile-card">
              <div class="ta-profile-card__avatar">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M12 12a4 4 0 1 0-4-4 4 4 0 0 0 4 4Zm0 1.5c-3.3 0-6 1.97-6 4.4V19h12v-1.1c0-2.43-2.7-4.4-6-4.4Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <h2 class="ta-profile-card__name">Zhang San</h2>
              <p class="ta-profile-card__id">ID: 2021001234</p>
              <p class="ta-profile-card__major">Software Engineering</p>
            </div>
            <a class="ta-button ta-button--soft" href="<%= contextPath %>/ta-profile-preview.jsp">
              <span class="ta-button__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M5.5 18.5h3l8.25-8.25-3-3L5.5 15.5Zm0 0-.75 3.25L8 21m5.75-11.75 3 3" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>View / Edit Profile</span>
            </a>
          </section>

          <section class="ta-card">
            <h3 class="ta-card__eyebrow">Application Statistics</h3>
            <div class="ta-stat-grid">
              <div class="ta-stat-box ta-stat-box--pending">
                <p class="ta-stat-box__value">3</p>
                <p class="ta-stat-box__label">Pending</p>
              </div>
              <div class="ta-stat-box ta-stat-box--accepted">
                <p class="ta-stat-box__value">1</p>
                <p class="ta-stat-box__label">Accepted</p>
              </div>
              <div class="ta-stat-box ta-stat-box--rejected">
                <p class="ta-stat-box__value">0</p>
                <p class="ta-stat-box__label">Rejected</p>
              </div>
            </div>
          </section>

          <section class="ta-card">
            <h3 class="ta-card__eyebrow">Resume Status</h3>
            <div class="ta-file-box">
              <span class="ta-file-box__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.5 4.75h6l3 3v11.5H7.5Zm6 0v3h3" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div class="ta-file-box__meta">
                <p class="ta-file-box__name">resume_zhang_san.pdf</p>
                <p class="ta-file-box__note">Verified System PDF</p>
              </div>
            </div>
            <div class="ta-inline-actions">
              <a class="ta-mini-button ta-mini-button--link" href="<%= contextPath %>/ta-profile-preview.jsp">Update</a>
              <button class="ta-mini-button ta-mini-button--primary" type="button">
                <span class="ta-mini-button__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M12 5v9m0 0 3.5-3.5M12 14l-3.5-3.5M5.75 17.5v.75h12.5v-.75" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <span>Download</span>
              </button>
            </div>
          </section>

          <section class="ta-card">
            <div class="ta-card__header">
              <h3 class="ta-card__eyebrow">Expertise</h3>
              <button class="ta-card__icon-button" type="button" aria-label="Add skill">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M12 5v14M5 12h14" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </button>
            </div>
            <div class="ta-skill-list">
              <span class="ta-skill-chip">Java</span>
              <span class="ta-skill-chip">Python</span>
              <span class="ta-skill-chip">Project Management</span>
              <span class="ta-skill-chip">Communication</span>
            </div>
          </section>
        </aside>

        <section class="ta-dashboard-content">
          <section class="ta-hero-card">
            <div class="ta-hero-card__glow"></div>
            <div class="ta-hero-card__content">
              <div>
                <h2 class="ta-hero-card__title">Action Required</h2>
                <p class="ta-hero-card__text">
                  You have <span>2 applications</span> currently waiting for Module Organizer (MO) review.
                </p>
              </div>
              <a class="ta-hero-card__button" href="<%= contextPath %>/ta-applications-preview.jsp">
                <span>Track My Progress</span>
                <span class="ta-hero-card__button-icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M5.5 12h13m0 0-5-5m5 5-5 5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
              </a>
            </div>
          </section>

          <section class="ta-card ta-card--wide">
            <div class="ta-card__wide-header">
              <div class="ta-card__wide-title">
                <span class="ta-card__wide-icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <h3>Latest Job Postings</h3>
              </div>
              <a class="ui-link-button" href="<%= contextPath %>/ta-positions-preview.jsp">Browse All</a>
            </div>

            <div class="ta-job-grid">
              <article class="ta-job-card">
                <div class="ta-job-card__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>
                <h4>Software Engineering TA</h4>
                <p>Prof. Wang</p>
              </article>

              <article class="ta-job-card">
                <div class="ta-job-card__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>
                <h4>Data Structures TA</h4>
                <p>Prof. Li</p>
              </article>

              <article class="ta-job-card">
                <div class="ta-job-card__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>
                <h4>Computer Networks TA</h4>
                <p>Prof. Zhang</p>
              </article>
            </div>
          </section>

          <section class="ta-card ta-card--table">
            <div class="ta-table__head">
              <h3>Active Application Tracking</h3>
            </div>
            <div class="ta-table__wrap">
              <table class="ta-table">
                <thead>
                  <tr>
                    <th>Job Position</th>
                    <th class="ta-table__center">Status</th>
                    <th class="ta-table__right">Action</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>Software Engineering TA</td>
                    <td class="ta-table__center">
                      <span class="ta-status ta-status--pending">
                        <span class="ta-status__icon" aria-hidden="true">
                          <svg viewBox="0 0 24 24" focusable="false">
                            <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                          </svg>
                        </span>
                        Pending
                      </span>
                    </td>
                    <td class="ta-table__right"><a class="ui-link-button" href="<%= contextPath %>/ta-applications-preview.jsp">Details</a></td>
                  </tr>
                  <tr>
                    <td>Database Systems TA</td>
                    <td class="ta-table__center">
                      <span class="ta-status ta-status--accepted">
                        <span class="ta-status__icon" aria-hidden="true">
                          <svg viewBox="0 0 24 24" focusable="false">
                            <path d="M7.75 12.25 10.5 15l5.75-5.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                          </svg>
                        </span>
                        Accepted
                      </span>
                    </td>
                    <td class="ta-table__right"><a class="ui-link-button" href="<%= contextPath %>/ta-applications-preview.jsp">Details</a></td>
                  </tr>
                </tbody>
              </table>
            </div>
          </section>

          <div class="ta-dashboard-bottom">
            <section class="ta-card ta-card--bottom">
              <h3 class="ta-card__eyebrow ta-card__eyebrow--with-icon">
                <span class="ta-card__eyebrow-icon ta-card__eyebrow-icon--danger" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M12 8.75V13m0 3h.01M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                Urgent Deadlines
              </h3>
              <div class="ta-deadline-list">
                <div class="ta-deadline-item">
                  <div class="ta-deadline-item__date">
                    <strong>30</strong>
                    <span>Mar</span>
                  </div>
                  <p>Software Engineering Application Deadline</p>
                </div>
                <div class="ta-deadline-item">
                  <div class="ta-deadline-item__date">
                    <strong>02</strong>
                    <span>Apr</span>
                  </div>
                  <p>Data Structures Interview Session</p>
                </div>
              </div>
            </section>

            <section class="ta-card ta-card--bottom">
              <h3 class="ta-card__eyebrow ta-card__eyebrow--with-icon">
                <span class="ta-card__eyebrow-icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M5.5 7.75h13a1.25 1.25 0 0 1 1.25 1.25v6A1.25 1.25 0 0 1 18.5 16.25H9.75l-3.5 2v-2H5.5A1.25 1.25 0 0 1 4.25 15v-6A1.25 1.25 0 0 1 5.5 7.75Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                Portal Alerts
              </h3>
              <ul class="ta-alert-list">
                <li class="ta-alert-item">
                  <span class="ta-alert-item__dot ta-alert-item__dot--success"></span>
                  <p>Your resume has passed the initial screening.</p>
                </li>
                <li class="ta-alert-item">
                  <span class="ta-alert-item__dot"></span>
                  <p>3 new TA positions were posted this week.</p>
                </li>
              </ul>
            </section>
          </div>
        </section>
      </div>
    </main>

    <footer class="ta-dashboard-footer">
      <p>2025 University TA Recruitment Portal. Processing with academic integrity.</p>
    </footer>
  </div>
</body>
</html>
