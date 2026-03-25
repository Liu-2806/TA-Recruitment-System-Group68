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
      <section class="ta-applications-toolbar">
        <div class="ta-applications-toolbar__header">
          <span class="ta-applications-toolbar__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M4.75 6.25h14.5L14 12v5.25l-4 1.5V12Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <h2>Search &amp; Filter</h2>
        </div>

        <div class="ta-applications-toolbar__grid">
          <div class="ta-toolbar-field ta-toolbar-field--wide">
            <label for="applicationKeywords">Keywords</label>
            <div class="ta-toolbar-input">
              <span class="ta-toolbar-input__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M10.75 17a6.25 6.25 0 1 0 0-12.5 6.25 6.25 0 0 0 0 12.5Zm8.75 2.5-4.25-4.25" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <input id="applicationKeywords" type="text" placeholder="Position, MO, course code...">
            </div>
          </div>

          <div class="ta-toolbar-field">
            <label for="applicationStatus">Status</label>
            <div class="ta-toolbar-select">
              <select id="applicationStatus">
                <option>All Status</option>
                <option>Pending</option>
                <option>Accepted</option>
                <option>Rejected</option>
              </select>
              <span class="ta-toolbar-select__caret" aria-hidden="true">v</span>
            </div>
          </div>

          <div class="ta-toolbar-field">
            <label for="applicationSort">Sort By</label>
            <div class="ta-toolbar-select">
              <select id="applicationSort">
                <option>Latest Updated</option>
                <option>Recently Submitted</option>
                <option>Status Priority</option>
              </select>
              <span class="ta-toolbar-select__caret" aria-hidden="true">v</span>
            </div>
          </div>

          <div class="ta-toolbar-actions">
            <button class="ta-toolbar-actions__apply" type="button">Apply Filter</button>
            <button class="ta-toolbar-actions__reset" type="button" aria-label="Reset filters">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M7 7.5V4.75m0 0H4.25M7 4.75 4.75 7M6.5 9.5a7 7 0 1 1-1.2 7" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
          </div>
        </div>
      </section>

      <section class="ta-application-board">
        <article id="application-se3001" class="ta-application-card">
          <div class="ta-application-card__top">
            <div class="ta-application-card__identity">
              <span class="ta-application-card__icon ta-application-card__icon--pending" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div>
                <h3>Software Engineering TA</h3>
                <p>SE3001 · Prof. Wang · Submitted 24 Mar 2026</p>
              </div>
            </div>

            <div class="ta-application-card__meta-actions">
              <span class="ta-application-badge ta-application-badge--pending">Pending Review</span>
              <a class="ta-application-card__details" href="<%= contextPath %>/ta-position-details-preview.jsp">Open Position</a>
            </div>
          </div>

          <div class="ta-application-card__bottom">
            <div class="ta-application-info">
              <p class="ta-application-info__label">Current Stage</p>
              <p class="ta-application-info__value">Waiting for MO shortlisting decision</p>
            </div>
            <div class="ta-application-info">
              <p class="ta-application-info__label">Match Insight</p>
              <p class="ta-application-info__value">Strong fit in Java, testing, and lab communication</p>
            </div>
            <div class="ta-application-info">
              <p class="ta-application-info__label">History</p>
              <p class="ta-application-info__value">Submitted -> Under Review -> Awaiting Result</p>
            </div>
          </div>

          <div class="ta-application-card__actions">
            <a class="ta-application-card__secondary" href="#history-se3001">View Full History</a>
            <a class="ta-application-card__primary" href="<%= contextPath %>/ta-position-details-preview.jsp">View Matching Details</a>
          </div>
        </article>

        <article id="application-cs2202" class="ta-application-card">
          <div class="ta-application-card__top">
            <div class="ta-application-card__identity">
              <span class="ta-application-card__icon ta-application-card__icon--accepted" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.75 12.25 10.5 15l5.75-5.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div>
                <h3>Data Structures TA</h3>
                <p>CS2202 · Prof. Li · Accepted on 19 Mar 2026</p>
              </div>
            </div>

            <div class="ta-application-card__meta-actions">
              <span class="ta-application-badge ta-application-badge--accepted">Accepted</span>
              <a class="ta-application-card__details" href="<%= contextPath %>/ta-position-details-preview.jsp">Open Position</a>
            </div>
          </div>

          <div class="ta-application-card__bottom">
            <div class="ta-application-info">
              <p class="ta-application-info__label">Current Stage</p>
              <p class="ta-application-info__value">Offer confirmed, onboarding message received</p>
            </div>
            <div class="ta-application-info">
              <p class="ta-application-info__label">Work Pattern</p>
              <p class="ta-application-info__value">Weekly tutorial support with one marking slot</p>
            </div>
            <div class="ta-application-info">
              <p class="ta-application-info__label">History</p>
              <p class="ta-application-info__value">Submitted -> Interviewed -> Accepted</p>
            </div>
          </div>

          <div class="ta-application-card__actions">
            <a class="ta-application-card__secondary" href="#history-cs2202">View Full History</a>
            <button class="ta-application-card__primary" type="button">Contact MO</button>
          </div>
        </article>

        <article id="application-db1101" class="ta-application-card">
          <div class="ta-application-card__top">
            <div class="ta-application-card__identity">
              <span class="ta-application-card__icon ta-application-card__icon--rejected" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="m8 8 8 8m0-8-8 8M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div>
                <h3>Database Systems TA</h3>
                <p>DB1101 · Prof. Zhang · Closed on 12 Mar 2026</p>
              </div>
            </div>

            <div class="ta-application-card__meta-actions">
              <span class="ta-application-badge ta-application-badge--rejected">Rejected</span>
              <a class="ta-application-card__details" href="<%= contextPath %>/ta-position-details-preview.jsp">Open Position</a>
            </div>
          </div>

          <div class="ta-application-card__bottom">
            <div class="ta-application-info">
              <p class="ta-application-info__label">Current Stage</p>
              <p class="ta-application-info__value">Application closed with feedback available</p>
            </div>
            <div class="ta-application-info">
              <p class="ta-application-info__label">Feedback Summary</p>
              <p class="ta-application-info__value">Strong motivation, but SQL teaching exposure was limited</p>
            </div>
            <div class="ta-application-info">
              <p class="ta-application-info__label">History</p>
              <p class="ta-application-info__value">Submitted -> Reviewed -> Rejected</p>
            </div>
          </div>

          <div class="ta-application-card__actions">
            <a class="ta-application-card__secondary" href="#history-archive">View Full History</a>
            <button class="ta-application-card__primary ta-application-card__primary--ghost" type="button">Read Feedback</button>
          </div>
        </article>
      </section>

      <section id="history-archive" class="ta-history-board">
        <div class="ta-history-board__header">
          <div>
            <p class="ta-history-board__eyebrow">History Archive</p>
            <h2>Detailed application timeline</h2>
          </div>
          <span class="ta-history-board__meta">3 records kept for this semester</span>
        </div>

        <div class="ta-history-timeline">
          <article id="history-se3001" class="ta-history-card">
            <div class="ta-history-card__head">
              <h3>Software Engineering TA</h3>
              <span class="ta-history-card__badge ta-history-card__badge--pending">Pending</span>
            </div>
            <ul class="ta-history-card__steps">
              <li><strong>24 Mar</strong><span>Application submitted with supporting statement and resume.</span></li>
              <li><strong>25 Mar</strong><span>System match analysis generated and attached to application.</span></li>
              <li><strong>Now</strong><span>Waiting for MO shortlist review and potential interview invite.</span></li>
            </ul>
          </article>

          <article id="history-cs2202" class="ta-history-card">
            <div class="ta-history-card__head">
              <h3>Data Structures TA</h3>
              <span class="ta-history-card__badge ta-history-card__badge--accepted">Accepted</span>
            </div>
            <ul class="ta-history-card__steps">
              <li><strong>08 Mar</strong><span>Application submitted and shortlisted after initial review.</span></li>
              <li><strong>15 Mar</strong><span>Interview completed with positive feedback on tutorial delivery.</span></li>
              <li><strong>19 Mar</strong><span>Offer accepted and onboarding message delivered.</span></li>
            </ul>
          </article>

          <article class="ta-history-card">
            <div class="ta-history-card__head">
              <h3>Database Systems TA</h3>
              <span class="ta-history-card__badge ta-history-card__badge--rejected">Rejected</span>
            </div>
            <ul class="ta-history-card__steps">
              <li><strong>01 Mar</strong><span>Application submitted with database coursework portfolio.</span></li>
              <li><strong>06 Mar</strong><span>MO reviewed profile and compared candidate teaching fit.</span></li>
              <li><strong>12 Mar</strong><span>Final decision recorded with note to strengthen SQL lab experience.</span></li>
            </ul>
          </article>
        </div>
      </section>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
