<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  request.setAttribute("headerBrandHref", contextPath + "/mo-dashboard-preview.jsp");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/mo-postings-preview.jsp");
  request.setAttribute("headerBackLabel", "Back to List");
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
  <title>Applicants | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/mo-applicants.css">
</head>
<body>
  <div class="mo-applicants-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="mo-applicants-main">
      <section class="mo-applicants-toolbar">
        <div class="mo-applicants-header__title-group">
          <h1>Applicants: Software Engineering TA</h1>
          <p>Course Code: SE3001</p>
        </div>

        <div class="mo-applicants-toolbar__actions">
          <div class="mo-applicants-search">
            <span class="mo-applicants-search__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M10.75 17a6.25 6.25 0 1 0 0-12.5 6.25 6.25 0 0 0 0 12.5Zm8.75 2.5-4.25-4.25" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <input type="text" placeholder="Search name...">
          </div>

          <button class="mo-applicants-broadcast" type="button">
            <span class="mo-applicants-broadcast__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M4.75 7.25h14.5a1.25 1.25 0 0 1 1.25 1.25v7a1.25 1.25 0 0 1-1.25 1.25H4.75A1.25 1.25 0 0 1 3.5 15.5v-7a1.25 1.25 0 0 1 1.25-1.25Zm0 .75L12 12.75 19.25 8" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <span>Broadcast Email</span>
          </button>
        </div>
      </section>

      <section class="mo-applicants-summary">
        <div class="mo-applicants-summary__stats">
          <div class="mo-applicants-summary__item">
            <span class="mo-applicants-summary__icon mo-applicants-summary__icon--blue" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <div>
              <p>Target Recruitment</p>
              <strong>3 <span>Positions</span></strong>
            </div>
          </div>

          <div class="mo-applicants-summary__item">
            <span class="mo-applicants-summary__icon mo-applicants-summary__icon--purple" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 3.5 13.6 8l4.9.4-3.75 2.95L15.95 16 12 13.55 8.05 16l1.2-4.65L5.5 8.4 10.4 8Zm6 9.5.7 2.05L20.75 16l-2.05.95L18 19l-.7-2.05L15.25 16l2.05-.95Z" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <div>
              <p>Received Applications</p>
              <strong>5 <span>Applicants</span></strong>
            </div>
          </div>
        </div>

        <div class="mo-applicants-summary__progress">
          Selection Progress: <strong>0 of 3 positions filled.</strong>
        </div>
      </section>

      <section class="mo-applicants-table-card">
        <div class="mo-applicants-table__wrap">
          <table class="mo-applicants-table">
            <thead>
              <tr>
                <th class="mo-applicants-table__checkbox">
                  <input type="checkbox" aria-label="Select all applicants">
                </th>
                <th>Applicant</th>
                <th>Academic Info</th>
                <th>Skill Match</th>
                <th>Status</th>
                <th class="mo-applicants-table__right">Action</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td class="mo-applicants-table__checkbox"><input type="checkbox" aria-label="Select Zhang San"></td>
                <td>
                  <div class="mo-applicant-cell">
                    <span class="mo-applicant-cell__avatar" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <div class="mo-applicant-cell__meta">
                      <strong>Zhang San</strong>
                      <p>zhang.san@univ.edu</p>
                    </div>
                  </div>
                </td>
                <td>
                  <div class="mo-academic-cell">
                    <strong>Software Engineering</strong>
                    <p>Junior (Year 3)</p>
                  </div>
                </td>
                <td>
                  <div class="mo-match-cell">
                    <div class="mo-match-chip">85% Match</div>
                    <div class="mo-match-bar"><span style="width:85%"></span></div>
                  </div>
                </td>
                <td>
                  <span class="mo-applicant-status mo-applicant-status--pending">
                    <span class="mo-applicant-status__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    Pending
                  </span>
                </td>
                <td class="mo-applicants-table__right">
                  <div class="mo-applicant-actions">
                    <a class="mo-applicant-actions__button" href="<%= contextPath %>/mo-applicant-details-preview.jsp">View Profile</a>
                    <a class="mo-applicant-actions__button mo-applicant-actions__button--primary" href="<%= contextPath %>/mo-applicant-details-preview.jsp#decision-panel">Make Decision</a>
                  </div>
                </td>
              </tr>

              <tr>
                <td class="mo-applicants-table__checkbox"><input type="checkbox" aria-label="Select Li Si"></td>
                <td>
                  <div class="mo-applicant-cell">
                    <span class="mo-applicant-cell__avatar" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <div class="mo-applicant-cell__meta">
                      <strong>Li Si</strong>
                      <p>li.si@univ.edu</p>
                    </div>
                  </div>
                </td>
                <td>
                  <div class="mo-academic-cell">
                    <strong>Computer Science</strong>
                    <p>Senior (Year 4)</p>
                  </div>
                </td>
                <td>
                  <div class="mo-match-cell">
                    <div class="mo-match-chip mo-match-chip--strong">92% Match</div>
                    <div class="mo-match-bar"><span class="is-strong" style="width:92%"></span></div>
                  </div>
                </td>
                <td>
                  <span class="mo-applicant-status mo-applicant-status--pending">
                    <span class="mo-applicant-status__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    Pending
                  </span>
                </td>
                <td class="mo-applicants-table__right">
                  <div class="mo-applicant-actions">
                    <a class="mo-applicant-actions__button" href="<%= contextPath %>/mo-applicant-details-preview.jsp">View Profile</a>
                    <a class="mo-applicant-actions__button mo-applicant-actions__button--primary" href="<%= contextPath %>/mo-applicant-details-preview.jsp#decision-panel">Make Decision</a>
                  </div>
                </td>
              </tr>

              <tr>
                <td class="mo-applicants-table__checkbox"><input type="checkbox" aria-label="Select Wang Wu"></td>
                <td>
                  <div class="mo-applicant-cell">
                    <span class="mo-applicant-cell__avatar" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <div class="mo-applicant-cell__meta">
                      <strong>Wang Wu</strong>
                      <p>wang.wu@univ.edu</p>
                    </div>
                  </div>
                </td>
                <td>
                  <div class="mo-academic-cell">
                    <strong>Software Engineering</strong>
                    <p>Sophomore (Year 2)</p>
                  </div>
                </td>
                <td>
                  <div class="mo-match-cell">
                    <div class="mo-match-chip">70% Match</div>
                    <div class="mo-match-bar"><span style="width:70%"></span></div>
                  </div>
                </td>
                <td>
                  <span class="mo-applicant-status mo-applicant-status--pending">
                    <span class="mo-applicant-status__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    Pending
                  </span>
                </td>
                <td class="mo-applicants-table__right">
                  <div class="mo-applicant-actions">
                    <a class="mo-applicant-actions__button" href="<%= contextPath %>/mo-applicant-details-preview.jsp">View Profile</a>
                    <a class="mo-applicant-actions__button mo-applicant-actions__button--primary" href="<%= contextPath %>/mo-applicant-details-preview.jsp#decision-panel">Make Decision</a>
                  </div>
                </td>
              </tr>

              <tr>
                <td class="mo-applicants-table__checkbox"><input type="checkbox" aria-label="Select Zhao Liu"></td>
                <td>
                  <div class="mo-applicant-cell">
                    <span class="mo-applicant-cell__avatar" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <div class="mo-applicant-cell__meta">
                      <strong>Zhao Liu</strong>
                      <p>zhao.liu@univ.edu</p>
                    </div>
                  </div>
                </td>
                <td>
                  <div class="mo-academic-cell">
                    <strong>Data Science</strong>
                    <p>Postgraduate</p>
                  </div>
                </td>
                <td>
                  <div class="mo-match-cell">
                    <div class="mo-match-chip">88% Match</div>
                    <div class="mo-match-bar"><span style="width:88%"></span></div>
                  </div>
                </td>
                <td>
                  <span class="mo-applicant-status mo-applicant-status--pending">
                    <span class="mo-applicant-status__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    Pending
                  </span>
                </td>
                <td class="mo-applicants-table__right">
                  <div class="mo-applicant-actions">
                    <a class="mo-applicant-actions__button" href="<%= contextPath %>/mo-applicant-details-preview.jsp">View Profile</a>
                    <a class="mo-applicant-actions__button mo-applicant-actions__button--primary" href="<%= contextPath %>/mo-applicant-details-preview.jsp#decision-panel">Make Decision</a>
                  </div>
                </td>
              </tr>

              <tr>
                <td class="mo-applicants-table__checkbox"><input type="checkbox" aria-label="Select Sun Qi"></td>
                <td>
                  <div class="mo-applicant-cell">
                    <span class="mo-applicant-cell__avatar" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    <div class="mo-applicant-cell__meta">
                      <strong>Sun Qi</strong>
                      <p>sun.qi@univ.edu</p>
                    </div>
                  </div>
                </td>
                <td>
                  <div class="mo-academic-cell">
                    <strong>Software Engineering</strong>
                    <p>Junior (Year 3)</p>
                  </div>
                </td>
                <td>
                  <div class="mo-match-cell">
                    <div class="mo-match-chip">65% Match</div>
                    <div class="mo-match-bar"><span style="width:65%"></span></div>
                  </div>
                </td>
                <td>
                  <span class="mo-applicant-status mo-applicant-status--pending">
                    <span class="mo-applicant-status__icon" aria-hidden="true">
                      <svg viewBox="0 0 24 24" focusable="false">
                        <path d="M12 6.25v5.5l3.25 1.75M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                      </svg>
                    </span>
                    Pending
                  </span>
                </td>
                <td class="mo-applicants-table__right">
                  <div class="mo-applicant-actions">
                    <a class="mo-applicant-actions__button" href="<%= contextPath %>/mo-applicant-details-preview.jsp">View Profile</a>
                    <a class="mo-applicant-actions__button mo-applicant-actions__button--primary" href="<%= contextPath %>/mo-applicant-details-preview.jsp#decision-panel">Make Decision</a>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
    </main>
  </div>
</body>
</html>
