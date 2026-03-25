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
  <title>Available Positions | TA Recruitment Portal</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/ta-positions.css">
</head>
<body>
  <div class="ta-positions-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="ta-positions-main">
      <section class="ta-filter-panel">
        <div class="ta-filter-panel__header">
          <span class="ta-filter-panel__icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M4.75 6.25h14.5L14 12v5.25l-4 1.5V12Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <h2>Search &amp; Filter</h2>
        </div>

        <div class="ta-filter-grid">
          <div class="ta-filter-field ta-filter-field--wide">
            <label for="positionKeywords">Keywords</label>
            <div class="ta-filter-input">
              <span class="ta-filter-input__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M10.75 17a6.25 6.25 0 1 0 0-12.5 6.25 6.25 0 0 0 0 12.5Zm8.75 2.5-4.25-4.25" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <input id="positionKeywords" type="text" placeholder="Course title, MO...">
            </div>
          </div>

          <div class="ta-filter-field">
            <label for="positionMajor">Major</label>
            <div class="ta-filter-select">
              <select id="positionMajor">
                <option>Software Engineering</option>
                <option>Computer Science</option>
                <option>Artificial Intelligence</option>
              </select>
              <span class="ta-filter-select__caret" aria-hidden="true">v</span>
            </div>
          </div>

          <div class="ta-filter-field">
            <label for="positionSort">Sort By</label>
            <div class="ta-filter-select">
              <select id="positionSort">
                <option>Latest Posted</option>
                <option>Upcoming Deadline</option>
                <option>Most Vacancies</option>
              </select>
              <span class="ta-filter-select__caret" aria-hidden="true">v</span>
            </div>
          </div>

          <div class="ta-filter-actions">
            <button class="ta-filter-actions__apply" type="button">Apply Filter</button>
            <button class="ta-filter-actions__reset" type="button" aria-label="Reset filters">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M7 7.5V4.75m0 0H4.25M7 4.75 4.75 7M6.5 9.5a7 7 0 1 1-1.2 7" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
          </div>
        </div>
      </section>

      <section class="ta-listings">
        <article class="ta-position-card">
          <div class="ta-position-card__top">
            <div class="ta-position-card__identity">
              <span class="ta-position-card__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div>
                <h3>Software Engineering TA</h3>
                <p>Prof. Wang <span>Vacancies: 3</span></p>
              </div>
            </div>

            <div class="ta-position-card__meta-actions">
              <span class="ta-position-status ta-position-status--open">
                <span class="ta-position-status__dot"></span>
                Open
              </span>
              <a href="<%= contextPath %>/ta-position-details-preview.jsp" class="ta-position-card__details">
                <span>View Details</span>
                <span class="ta-position-card__details-icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M10 14 19 5m-5 0h5v5M19 13.5V18a1 1 0 0 1-1 1h-12a1 1 0 0 1-1-1V6a1 1 0 0 1 1-1h4.5" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
              </a>
            </div>
          </div>

          <div class="ta-position-card__bottom">
            <div class="ta-position-info">
              <p class="ta-position-info__label">Application Deadline</p>
              <p class="ta-position-info__value">
                <span class="ta-position-info__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                2026-03-30
              </p>
            </div>

            <div class="ta-position-skills">
              <p class="ta-position-skills__label">
                <span class="ta-position-skills__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M4.75 10.25 10.25 4.75H17l2.25 2.25v6.75L13.75 19.25 4.75 10.25Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    <circle cx="14.5" cy="9.5" r="1" fill="currentColor"/>
                  </svg>
                </span>
                Required Skills
              </p>
              <div class="ta-position-skills__list">
                <span>Java</span>
                <span>Object-Oriented Programming</span>
              </div>
            </div>
          </div>
        </article>

        <article class="ta-position-card">
          <div class="ta-position-card__top">
            <div class="ta-position-card__identity">
              <span class="ta-position-card__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div>
                <h3>Data Structures TA</h3>
                <p>Prof. Li <span>Vacancies: 2</span></p>
              </div>
            </div>

            <div class="ta-position-card__meta-actions">
              <span class="ta-position-status ta-position-status--open">
                <span class="ta-position-status__dot"></span>
                Open
              </span>
              <a href="<%= contextPath %>/ta-position-details-preview.jsp" class="ta-position-card__details">
                <span>View Details</span>
                <span class="ta-position-card__details-icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M10 14 19 5m-5 0h5v5M19 13.5V18a1 1 0 0 1-1 1h-12a1 1 0 0 1-1-1V6a1 1 0 0 1 1-1h4.5" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
              </a>
            </div>
          </div>

          <div class="ta-position-card__bottom">
            <div class="ta-position-info">
              <p class="ta-position-info__label">Application Deadline</p>
              <p class="ta-position-info__value">
                <span class="ta-position-info__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                2026-04-05
              </p>
            </div>

            <div class="ta-position-skills">
              <p class="ta-position-skills__label">
                <span class="ta-position-skills__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M4.75 10.25 10.25 4.75H17l2.25 2.25v6.75L13.75 19.25 4.75 10.25Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    <circle cx="14.5" cy="9.5" r="1" fill="currentColor"/>
                  </svg>
                </span>
                Required Skills
              </p>
              <div class="ta-position-skills__list">
                <span>C++</span>
                <span>Algorithms</span>
              </div>
            </div>
          </div>
        </article>

        <article class="ta-position-card">
          <div class="ta-position-card__top">
            <div class="ta-position-card__identity">
              <span class="ta-position-card__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <div>
                <h3>Machine Learning TA</h3>
                <p>Prof. Zhang <span>Vacancies: 1</span></p>
              </div>
            </div>

            <div class="ta-position-card__meta-actions">
              <span class="ta-position-status ta-position-status--open">
                <span class="ta-position-status__dot"></span>
                Open
              </span>
              <a href="<%= contextPath %>/ta-position-details-preview.jsp" class="ta-position-card__details">
                <span>View Details</span>
                <span class="ta-position-card__details-icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M10 14 19 5m-5 0h5v5M19 13.5V18a1 1 0 0 1-1 1h-12a1 1 0 0 1-1-1V6a1 1 0 0 1 1-1h4.5" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
              </a>
            </div>
          </div>

          <div class="ta-position-card__bottom">
            <div class="ta-position-info">
              <p class="ta-position-info__label">Application Deadline</p>
              <p class="ta-position-info__value">
                <span class="ta-position-info__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M7 3.75v3.5M17 3.75v3.5M4.75 8.25h14.5m-13 1.25h11a1.75 1.75 0 0 1 1.75 1.75v6.5A1.75 1.75 0 0 1 17.25 19.5H6.75A1.75 1.75 0 0 1 5 17.75v-6.5A1.75 1.75 0 0 1 6.75 9.5Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                2026-04-10
              </p>
            </div>

            <div class="ta-position-skills">
              <p class="ta-position-skills__label">
                <span class="ta-position-skills__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M4.75 10.25 10.25 4.75H17l2.25 2.25v6.75L13.75 19.25 4.75 10.25Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    <circle cx="14.5" cy="9.5" r="1" fill="currentColor"/>
                  </svg>
                </span>
                Required Skills
              </p>
              <div class="ta-position-skills__list">
                <span>Python</span>
                <span>PyTorch</span>
                <span>Calculus</span>
              </div>
            </div>
          </div>
        </article>
      </section>

      <footer class="ta-positions-footer">
        <p>Showing 8 available positions</p>
        <div class="ta-pagination">
          <button type="button" class="ta-pagination__nav" aria-label="Previous page">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M14.5 6.5 9 12l5.5 5.5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
          <button type="button" class="ta-pagination__page is-active">1</button>
          <button type="button" class="ta-pagination__page">2</button>
          <button type="button" class="ta-pagination__page">3</button>
          <span class="ta-pagination__dots">...</span>
          <button type="button" class="ta-pagination__nav" aria-label="Next page">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M9.5 6.5 15 12l-5.5 5.5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
        </div>
      </footer>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
