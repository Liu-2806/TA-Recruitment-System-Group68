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
              <a class="ta-mini-button ta-mini-button--link" href="<%= contextPath %>/ta-profile-preview.jsp#resume-upload">Upload / Replace</a>
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
        </aside>

        <section class="ta-dashboard-content">
          <section class="ta-board-card ta-board-card--timetable">
            <div class="ta-board-card__header">
              <div class="ta-board-card__title-group">
                <span class="ta-board-card__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <div>
                  <p class="ta-board-card__eyebrow">Timetable</p>
                  <h2>Weekly Teaching Schedule</h2>
                </div>
              </div>
              <div class="ta-board-card__toolbar">
                <button class="ta-week-switch" type="button" id="taPrevWeek" aria-label="Previous week">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M14.5 6.5 9 12l5.5 5.5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </button>
                <span class="ta-board-card__meta" id="taWeekLabel">Week of 25 Mar - 31 Mar</span>
                <button class="ta-week-switch" type="button" id="taNextWeek" aria-label="Next week">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M9.5 6.5 15 12l-5.5 5.5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </button>
              </div>
            </div>

            <div class="ta-course-strip" id="taCourseStrip">
              <div class="ta-course-strip__content">
                <span class="ta-course-strip__label" id="taCourseLabel">Course TA</span>
                <strong id="taCourseTitle">Software Engineering TA</strong>
                <p id="taCourseMeta">Tue 14:00 - 16:00 · Queens Building QB-302 · Weekly support session</p>
              </div>
              <a class="ta-course-strip__link" id="taCourseLink" href="<%= contextPath %>/ta-applications-preview.jsp#application-se3001">Related Application</a>
            </div>

            <div class="ta-calendar-legend">
              <span class="ta-calendar-legend__item"><span class="ta-calendar-legend__dot ta-calendar-legend__dot--lab"></span>Lab Support</span>
              <span class="ta-calendar-legend__item"><span class="ta-calendar-legend__dot ta-calendar-legend__dot--exam"></span>Invigilation</span>
              <span class="ta-calendar-legend__item"><span class="ta-calendar-legend__dot ta-calendar-legend__dot--checkoff"></span>Project Check-off</span>
            </div>

            <div class="ta-calendar">
              <div class="ta-calendar__weekdays">
                <div class="ta-calendar__weekday">Mon</div>
                <div class="ta-calendar__weekday">Tue</div>
                <div class="ta-calendar__weekday">Wed</div>
                <div class="ta-calendar__weekday">Thu</div>
                <div class="ta-calendar__weekday">Fri</div>
                <div class="ta-calendar__weekday">Sat</div>
                <div class="ta-calendar__weekday">Sun</div>
              </div>
              <div class="ta-calendar__grid" id="taCalendarGrid" data-position-url="<%= contextPath %>/ta-position-details-preview.jsp"></div>
            </div>

            <section class="ta-schedule-detail" id="taScheduleDetail" aria-live="polite">
              <div class="ta-schedule-detail__header">
                <div>
                  <p class="ta-schedule-detail__eyebrow">Selected Task</p>
                  <h3 id="taScheduleDetailTitle">Select a work day</h3>
                </div>
                <span class="ta-schedule-detail__badge" id="taScheduleDetailBadge">No selection</span>
              </div>
              <p class="ta-schedule-detail__meta" id="taScheduleDetailMeta">
                Choose a highlighted day to inspect the arranged TA duty and jump to the related position detail page.
              </p>
              <p class="ta-schedule-detail__description" id="taScheduleDetailDescription">
                Scheduled activity details for this week will appear here.
              </p>
              <div class="ta-schedule-detail__actions">
                <a class="ta-schedule-detail__action" id="taScheduleDetailLink" href="<%= contextPath %>/ta-position-details-preview.jsp">Open Position Details</a>
              </div>
            </section>
          </section>

          <div class="ta-dashboard-panels">
            <section class="ta-board-card">
              <div class="ta-board-card__header">
                <div class="ta-board-card__title-group">
                  <span class="ta-board-card__icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <div>
                    <p class="ta-board-card__eyebrow">Positions</p>
                    <h2>Recommended Open Roles</h2>
                  </div>
                </div>
                <a class="ta-board-card__link" href="<%= contextPath %>/ta-positions-preview.jsp">Browse All</a>
              </div>

              <div class="ta-quick-list">
                <article class="ta-quick-item">
                  <div class="ta-quick-item__meta">
                    <h3>Software Engineering TA</h3>
                    <p>Prof. Wang · Deadline 30 Mar · Java, Testing</p>
                  </div>
                  <a class="ta-quick-item__action" href="<%= contextPath %>/ta-position-details-preview.jsp">View Details</a>
                </article>

                <article class="ta-quick-item">
                  <div class="ta-quick-item__meta">
                    <h3>Database Systems TA</h3>
                    <p>Prof. Li · Deadline 31 Mar · SQL, Data Analysis</p>
                  </div>
                  <a class="ta-quick-item__action" href="<%= contextPath %>/ta-position-details-preview.jsp">View Details</a>
                </article>

                <article class="ta-quick-item">
                  <div class="ta-quick-item__meta">
                    <h3>AI Foundations TA</h3>
                    <p>Prof. Zhang · Deadline 02 Apr · Python, ML</p>
                  </div>
                  <a class="ta-quick-item__action" href="<%= contextPath %>/ta-position-details-preview.jsp">View Details</a>
                </article>
              </div>
            </section>

            <section class="ta-board-card">
              <div class="ta-board-card__header">
                <div class="ta-board-card__title-group">
                  <span class="ta-board-card__icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M7.5 4.75h9v14.5h-9Zm3 3h3m-3 4h5m-5 4h4" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <div>
                    <p class="ta-board-card__eyebrow">Applications</p>
                    <h2>Recent Application List</h2>
                  </div>
                </div>
                <a class="ta-board-card__link" href="<%= contextPath %>/ta-applications-preview.jsp">Open All</a>
              </div>

              <div class="ta-history-list">
                <article class="ta-history-item">
                  <div class="ta-history-item__status ta-history-item__status--pending">Pending</div>
                  <div class="ta-history-item__content">
                    <h3>Software Engineering TA</h3>
                    <p>Submitted 24 Mar · Waiting for MO review</p>
                  </div>
                  <a class="ta-history-item__link" href="<%= contextPath %>/ta-applications-preview.jsp#application-se3001">View History</a>
                </article>

                <article class="ta-history-item">
                  <div class="ta-history-item__status ta-history-item__status--accepted">Accepted</div>
                  <div class="ta-history-item__content">
                    <h3>Data Structures TA</h3>
                    <p>Offer received 19 Mar · Start onboarding</p>
                  </div>
                  <a class="ta-history-item__link" href="<%= contextPath %>/ta-applications-preview.jsp#application-cs2202">View History</a>
                </article>

                <article class="ta-history-item">
                  <div class="ta-history-item__status ta-history-item__status--rejected">Rejected</div>
                  <div class="ta-history-item__content">
                    <h3>Database Systems TA</h3>
                    <p>Closed 12 Mar · Feedback available</p>
                  </div>
                  <a class="ta-history-item__link" href="<%= contextPath %>/ta-applications-preview.jsp#history-archive">View History</a>
                </article>
              </div>
            </section>
          </div>
        </section>
      </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
  <script src="<%= contextPath %>/assets/js/pages/ta-dashboard.js"></script>
</body>
</html>
