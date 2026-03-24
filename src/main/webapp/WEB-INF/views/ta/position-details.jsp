<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/ta-dashboard-preview.jsp");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/ta-positions-preview.jsp");
  request.setAttribute("headerBackLabel", "Back to Listings");
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
  <title>Position Details | TA Recruitment Portal</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/ta-position-details.css">
</head>
<body>
  <div class="ta-details-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="ta-details-main">
      <section class="ta-details-card">
        <div class="ta-details-card__header">
          <div>
            <span class="ta-details-card__code">SE3001</span>
            <h1>Software Engineering TA</h1>
          </div>
          <span class="ta-details-status">
            <span class="ta-details-status__dot"></span>
            Open
          </span>
        </div>

        <div class="ta-details-summary">
          <div class="ta-details-summary__item">
            <p class="ta-details-summary__label">
              <span class="ta-details-summary__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              Module Organizer
            </p>
            <p class="ta-details-summary__value">Prof. Wang</p>
          </div>

          <div class="ta-details-summary__item">
            <p class="ta-details-summary__label">
              <span class="ta-details-summary__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              Vacancies
            </p>
            <p class="ta-details-summary__value">3 Positions</p>
          </div>

          <div class="ta-details-summary__item">
            <p class="ta-details-summary__label">
              <span class="ta-details-summary__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              Deadline
            </p>
            <p class="ta-details-summary__value ta-details-summary__value--danger">March 30, 2026</p>
          </div>
        </div>

        <section class="ta-details-section">
          <h2 class="ta-details-section__title">
            <span class="ta-details-section__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M7.5 4.75h6l3 3v11.5H7.5Zm6 0v3h3M10 12.25h4m-4 3h4" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            Position Description
          </h2>

          <div class="ta-details-description">
            Assist in lab session teaching, grading student assignments and projects, and providing 2 hours of weekly Q&amp;A sessions for undergraduate students.
          </div>
        </section>

        <section class="ta-details-section">
          <h2 class="ta-details-section__title">
            <span class="ta-details-section__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M6 12.5 9.25 15.75 18 7" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            Required Skills &amp; Qualifications
          </h2>

          <ul class="ta-details-checklist">
            <li>
              <span class="ta-details-checklist__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.75 12.25 10.5 15l5.75-5.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>Proficient in Java programming</span>
            </li>
            <li>
              <span class="ta-details-checklist__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.75 12.25 10.5 15l5.75-5.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>Deep understanding of Object-Oriented Design (OOD)</span>
            </li>
            <li>
              <span class="ta-details-checklist__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7.75 12.25 10.5 15l5.75-5.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>Previous TA experience is preferred but not required</span>
            </li>
          </ul>
        </section>

        <section class="ta-details-section">
          <h2 class="ta-details-section__title">
            <span class="ta-details-section__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            Estimated Workload
          </h2>

          <div class="ta-details-workload">
            <span class="ta-details-workload__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <span>Approximately 6 hours per week</span>
          </div>
        </section>

        <section class="ta-match-card">
          <div class="ta-match-card__header">
            <span>AI Smart Match</span>
            <span class="ta-match-card__sparkle" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 3.5 13.6 8l4.9.4-3.75 2.95L15.95 16 12 13.55 8.05 16l1.2-4.65L5.5 8.4 10.4 8Zm6 9.5.7 2.05L20.75 16l-2.05.95L18 19l-.7-2.05L15.25 16l2.05-.95Z" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
          </div>

          <div class="ta-match-card__content">
            <div class="ta-match-score">
              <div class="ta-match-score__ring">
                <span>85%</span>
              </div>
              <div class="ta-match-score__meta">
                <strong>Excellent Match!</strong>
                <p>Based on your saved skills</p>
              </div>
            </div>

            <div class="ta-match-improvement">
              <p class="ta-match-improvement__label">Suggested Skill Improvement</p>
              <p class="ta-match-improvement__text">Python (Optional, can be learned on the job)</p>
            </div>
          </div>
        </section>

        <div class="ta-details-card__footer">
          <p>Check all details carefully before applying.</p>
          <button class="ta-details-apply" type="button" id="openApplyConfirm">Apply Now</button>
        </div>
      </section>
    </main>

    <div class="ta-apply-modal" id="applyConfirmModal" aria-hidden="true">
      <div class="ta-apply-modal__backdrop" data-close-modal="true"></div>
      <div class="ta-apply-modal__dialog" role="dialog" aria-modal="true" aria-labelledby="applyConfirmTitle">
        <button class="ta-apply-modal__close" type="button" id="closeApplyConfirm" aria-label="Close confirmation dialog">
          <svg viewBox="0 0 24 24" focusable="false">
            <path d="m7 7 10 10M17 7 7 17" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </button>

        <div class="ta-apply-modal__body">
          <section class="ta-confirm-card">
            <div class="ta-confirm-card__section">
              <p class="ta-confirm-card__eyebrow">You are currently applying for:</p>
              <div class="ta-confirm-role">
                <span class="ta-confirm-role__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <div class="ta-confirm-role__meta">
                  <h2 id="applyConfirmTitle">Software Engineering TA</h2>
                  <p>Module Organizer: <strong>Prof. Wang</strong></p>
                </div>
              </div>
            </div>

            <div class="ta-confirm-card__section">
              <p class="ta-confirm-card__eyebrow ta-confirm-card__eyebrow--success">Submission Checklist</p>

              <div class="ta-confirm-checklist">
                <div class="ta-confirm-item">
                  <span class="ta-confirm-item__status" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M7.75 12.25 10.5 15l5.75-5.75" fill="none" stroke="currentColor" stroke-width="1.9" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <div class="ta-confirm-item__content">
                    <strong>Personal profile completed</strong>
                  </div>
                  <span class="ta-confirm-item__tail" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M12 12a3.5 3.5 0 1 0-3.5-3.5A3.5 3.5 0 0 0 12 12Zm0 1.5c-2.88 0-5.25 1.64-5.25 3.67V18h10.5v-.83c0-2.03-2.37-3.67-5.25-3.67Z" fill="none" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                </div>

                <div class="ta-confirm-item">
                  <span class="ta-confirm-item__status" aria-hidden="true">
                    <svg viewBox="0 0 24 24" focusable="false">
                      <path d="M7.75 12.25 10.5 15l5.75-5.75" fill="none" stroke="currentColor" stroke-width="1.9" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                  </span>
                  <div class="ta-confirm-item__content">
                    <strong>Resume uploaded</strong>
                    <p>resume_john_doe_2026.pdf</p>
                  </div>
                  <a class="ta-confirm-item__action" href="<%= contextPath %>/ta-profile-preview.jsp">Change</a>
                </div>
              </div>
            </div>

            <div class="ta-confirm-warning">
              <span class="ta-confirm-warning__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M12 8.5v4.5m0 3h.01M10.1 4.96 4.56 14.2A2 2 0 0 0 6.28 17h11.44a2 2 0 0 0 1.72-2.8L13.9 4.96a2 2 0 0 0-3.8 0Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div class="ta-confirm-warning__content">
                <strong>Final Confirmation Required</strong>
                <p>Information <span>cannot be modified</span> after submission. Please ensure all details and attached documents are accurate.</p>
              </div>
            </div>
          </section>
        </div>

        <div class="ta-apply-modal__footer">
          <button class="ta-confirm-submit" type="button">Confirm &amp; Submit</button>
          <button class="ta-confirm-cancel" type="button" id="cancelApplyConfirm">
            <span class="ta-confirm-cancel__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="m7 7 10 10M17 7 7 17" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <span>Cancel</span>
          </button>
        </div>
      </div>
    </div>
  </div>

  <script src="<%= contextPath %>/assets/js/pages/ta-position-details.js"></script>
</body>
</html>
