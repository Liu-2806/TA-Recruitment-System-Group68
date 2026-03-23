<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Applicant Profile | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/mo-applicant-details.css">
</head>
<body>
  <div class="mo-applicant-details-shell">
    <header class="mo-applicant-details-header">
      <div class="mo-applicant-details-header__inner">
        <a class="app-brand" href="<%= contextPath %>/mo-applicant-details-preview.jsp">
          <span class="app-brand__mark">T</span>
          <span class="app-brand__text">Applicant Profile: Zhang San</span>
        </a>

        <a class="mo-applicant-details-header__back" href="<%= contextPath %>/mo-applicants-preview.jsp">
          <span class="mo-applicant-details-header__back-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M15.5 6.5 10 12l5.5 5.5M11 12h8" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>Back to List</span>
        </a>
      </div>
    </header>

    <main class="mo-applicant-details-main">
      <section class="mo-applicant-panel">
        <div class="mo-applicant-panel__header">
          <h2>
            <span class="mo-applicant-panel__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            Basic Information
          </h2>
        </div>

        <div class="mo-applicant-info-grid">
          <div class="mo-applicant-info-item">
            <p>Name</p>
            <strong>Zhang San</strong>
          </div>
          <div class="mo-applicant-info-item">
            <p>Student ID</p>
            <strong>2021001234</strong>
          </div>
          <div class="mo-applicant-info-item">
            <p>Major</p>
            <strong>Software Engineering</strong>
          </div>
          <div class="mo-applicant-info-item">
            <p>Year</p>
            <strong>Year 3 (Junior)</strong>
          </div>
          <div class="mo-applicant-info-item mo-applicant-info-item--full">
            <p>Email</p>
            <strong>zhangsan@bupt.edu</strong>
          </div>
        </div>
      </section>

      <section class="mo-applicant-panel">
        <div class="mo-applicant-panel__header">
          <h2>Skill Tags</h2>
        </div>

        <div class="mo-applicant-skill-list">
          <span class="mo-applicant-skill-chip">Java</span>
          <span class="mo-applicant-skill-chip">Python</span>
          <span class="mo-applicant-skill-chip">Project Management</span>
        </div>
      </section>

      <section class="mo-applicant-panel">
        <div class="mo-applicant-panel__header">
          <h2>Resume</h2>
        </div>

        <div class="mo-applicant-resume-card">
          <div class="mo-applicant-resume-card__left">
            <span class="mo-applicant-resume-card__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M7.5 4.75h6l3 3v11.5H7.5Zm6 0v3h3M10 12.25h4m-4 3h4" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <div class="mo-applicant-resume-card__meta">
              <strong>resume_zhang_san.pdf</strong>
              <p>Uploaded on Mar 12, 2026</p>
            </div>
          </div>

          <div class="mo-applicant-resume-card__actions">
            <button type="button">Preview</button>
            <button type="button">Download</button>
          </div>
        </div>
      </section>

      <section class="mo-match-panel">
        <div class="mo-match-panel__header">
          <div class="mo-match-panel__title">
            <span class="mo-match-panel__spark" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 3.5 13.6 8l4.9.4-3.75 2.95L15.95 16 12 13.55 8.05 16l1.2-4.65L5.5 8.4 10.4 8Zm6 9.5.7 2.05L20.75 16l-2.05.95L18 19l-.7-2.05L15.25 16l2.05-.95Z" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <span>AI Job Match Analysis (PB-14)</span>
          </div>
          <span class="mo-match-panel__badge">Verified Algorithm</span>
        </div>

        <div class="mo-match-panel__body">
          <div class="mo-match-score">
            <div class="mo-match-score__ring">
              <span>85%</span>
            </div>
            <div class="mo-match-score__meta">
              <strong>Strong Candidate Match</strong>
              <p>Based on course requirements and applicant profile.</p>
            </div>
          </div>

          <div class="mo-match-breakdown">
            <div class="mo-match-breakdown__section">
              <p>Matched Skills</p>
              <div class="mo-match-breakdown__chips">
                <span class="mo-match-breakdown__chip mo-match-breakdown__chip--good">Java</span>
                <span class="mo-match-breakdown__chip mo-match-breakdown__chip--good">Project Management</span>
              </div>
            </div>

            <div class="mo-match-breakdown__section">
              <p>Missing Skills</p>
              <div class="mo-match-breakdown__chips">
                <span class="mo-match-breakdown__chip mo-match-breakdown__chip--warn">Python (Course Requirement)</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <footer class="mo-applicant-details-footer">
        <p>&copy; 2025 University TA Recruitment Portal. MO Decision Interface.</p>
      </footer>
    </main>

    <div class="mo-applicant-decision-bar" id="decision-panel">
      <button class="mo-applicant-decision-bar__accept" type="button">
        <span class="mo-applicant-decision-bar__icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" focusable="false">
            <path d="M7.75 12.25 10.5 15l5.75-5.75" fill="none" stroke="currentColor" stroke-width="1.9" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </span>
        <span>Hire This TA</span>
      </button>

      <button class="mo-applicant-decision-bar__reject" type="button">
        <span class="mo-applicant-decision-bar__icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" focusable="false">
            <path d="m7 7 10 10M17 7 7 17" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </span>
        <span>Reject &amp; Feedback</span>
      </button>
    </div>
  </div>
</body>
</html>
