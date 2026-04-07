<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  Object currentUserObj = request.getSession(false) == null ? null : request.getSession(false).getAttribute("currentUser");
  com.bupt.ta.model.User currentUser = currentUserObj instanceof com.bupt.ta.model.User ? (com.bupt.ta.model.User) currentUserObj : null;
  String currentUserName = currentUser == null || currentUser.getDisplayName() == null || currentUser.getDisplayName().trim().isEmpty()
      ? "MO"
      : currentUser.getDisplayName().trim();
  String currentUserInitial = currentUserName.isEmpty() ? "M" : currentUserName.substring(0, 1).toUpperCase();

  request.setAttribute("headerBrandHref", contextPath + "/mo/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/mo/dashboard");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", currentUserName);
  request.setAttribute("currentUserRoleLabel", "Module Organizer");
  request.setAttribute("currentUserInitial", currentUserInitial);
  request.setAttribute("notificationCount", Integer.valueOf(1));
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Pending Review Queue | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/mo-applicants.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/mo-review-queue.css">
</head>
<body>
  <div class="mo-applicants-shell mo-review-queue-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="mo-applicants-main">
      <section class="mo-applicants-toolbar mo-review-queue-toolbar">
        <div class="mo-applicants-header__title-group">
          <h1>Pending Review Queue</h1>
          <p>All unprocessed applications across your active postings</p>
        </div>

        <div class="mo-review-queue-toolbar__actions">
          <div class="mo-review-queue-sort">
            <label for="reviewQueueSort">Sort Queue</label>
            <div class="mo-review-queue-sort__select">
              <select id="reviewQueueSort">
                <option value="submitted-desc">Latest Submitted</option>
                <option value="match-desc">Highest Match</option>
                <option value="course-asc">Course Code</option>
                <option value="name-asc">Applicant Name</option>
              </select>
              <span class="mo-review-queue-sort__caret" aria-hidden="true">v</span>
            </div>
          </div>
        </div>
      </section>

      <section class="mo-applicants-summary mo-review-queue-summary">
        <div class="mo-applicants-summary__stats">
          <div class="mo-applicants-summary__item">
            <span class="mo-applicants-summary__icon mo-applicants-summary__icon--blue" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <div>
              <p>Unprocessed Applications</p>
              <strong>8 <span>Pending</span></strong>
            </div>
          </div>

          <div class="mo-applicants-summary__item">
            <span class="mo-applicants-summary__icon mo-applicants-summary__icon--purple" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <div>
              <p>Open Postings In Queue</p>
              <strong>3 <span>Positions</span></strong>
            </div>
          </div>
        </div>

        <div class="mo-review-queue-summary__note">
          <strong>Review Tip:</strong> Start with high-match candidates or the latest submissions to keep decisions moving.
        </div>
      </section>

      <section class="mo-applicants-table-card">
        <div class="mo-applicants-table__wrap">
          <table class="mo-applicants-table">
            <thead>
              <tr>
                <th>Applicant</th>
                <th>Position</th>
                <th>Submitted</th>
                <th>Skill Match</th>
                <th>Priority</th>
                <th>Status</th>
                <th class="mo-applicants-table__right">Action</th>
              </tr>
            </thead>
            <tbody id="reviewQueueBody">
              <tr data-name="Zhang San" data-course="SE3001" data-submitted="2026-03-25T09:20:00" data-match="85">
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
                    <strong>Software Engineering TA</strong>
                    <p>SE3001 · Prof. Wang</p>
                  </div>
                </td>
                <td class="mo-review-queue__time">
                  <strong>25 Mar 2026</strong>
                  <p>09:20</p>
                </td>
                <td>
                  <div class="mo-match-cell">
                    <div class="mo-match-chip">85% Match</div>
                    <div class="mo-match-bar"><span style="width:85%"></span></div>
                  </div>
                </td>
                <td><span class="mo-review-priority mo-review-priority--high">High</span></td>
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
                    <a class="mo-applicant-actions__button mo-applicant-actions__button--primary" href="<%= contextPath %>/mo-applicant-details-preview.jsp#decision-panel">Review Now</a>
                  </div>
                </td>
              </tr>

              <tr data-name="Li Si" data-course="CS2202" data-submitted="2026-03-24T14:10:00" data-match="92">
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
                    <strong>Data Structures TA</strong>
                    <p>CS2202 · Prof. Li</p>
                  </div>
                </td>
                <td class="mo-review-queue__time">
                  <strong>24 Mar 2026</strong>
                  <p>14:10</p>
                </td>
                <td>
                  <div class="mo-match-cell">
                    <div class="mo-match-chip mo-match-chip--strong">92% Match</div>
                    <div class="mo-match-bar"><span class="is-strong" style="width:92%"></span></div>
                  </div>
                </td>
                <td><span class="mo-review-priority mo-review-priority--high">High</span></td>
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
                    <a class="mo-applicant-actions__button mo-applicant-actions__button--primary" href="<%= contextPath %>/mo-applicant-details-preview.jsp#decision-panel">Review Now</a>
                  </div>
                </td>
              </tr>

              <tr data-name="Wang Wu" data-course="DB1101" data-submitted="2026-03-22T16:45:00" data-match="70">
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
                    <strong>Database Systems TA</strong>
                    <p>DB1101 · Prof. Zhang</p>
                  </div>
                </td>
                <td class="mo-review-queue__time">
                  <strong>22 Mar 2026</strong>
                  <p>16:45</p>
                </td>
                <td>
                  <div class="mo-match-cell">
                    <div class="mo-match-chip">70% Match</div>
                    <div class="mo-match-bar"><span style="width:70%"></span></div>
                  </div>
                </td>
                <td><span class="mo-review-priority mo-review-priority--medium">Medium</span></td>
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
                    <a class="mo-applicant-actions__button mo-applicant-actions__button--primary" href="<%= contextPath %>/mo-applicant-details-preview.jsp#decision-panel">Review Now</a>
                  </div>
                </td>
              </tr>

              <tr data-name="Zhao Liu" data-course="SE3001" data-submitted="2026-03-21T11:35:00" data-match="88">
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
                    <strong>Software Engineering TA</strong>
                    <p>SE3001 · Prof. Wang</p>
                  </div>
                </td>
                <td class="mo-review-queue__time">
                  <strong>21 Mar 2026</strong>
                  <p>11:35</p>
                </td>
                <td>
                  <div class="mo-match-cell">
                    <div class="mo-match-chip">88% Match</div>
                    <div class="mo-match-bar"><span style="width:88%"></span></div>
                  </div>
                </td>
                <td><span class="mo-review-priority mo-review-priority--high">High</span></td>
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
                    <a class="mo-applicant-actions__button mo-applicant-actions__button--primary" href="<%= contextPath %>/mo-applicant-details-preview.jsp#decision-panel">Review Now</a>
                  </div>
                </td>
              </tr>

              <tr data-name="Sun Qi" data-course="AI1101" data-submitted="2026-03-20T18:05:00" data-match="65">
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
                    <strong>AI Foundations TA</strong>
                    <p>AI1101 · Prof. Zhang</p>
                  </div>
                </td>
                <td class="mo-review-queue__time">
                  <strong>20 Mar 2026</strong>
                  <p>18:05</p>
                </td>
                <td>
                  <div class="mo-match-cell">
                    <div class="mo-match-chip">65% Match</div>
                    <div class="mo-match-bar"><span style="width:65%"></span></div>
                  </div>
                </td>
                <td><span class="mo-review-priority mo-review-priority--low">Low</span></td>
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
                    <a class="mo-applicant-actions__button mo-applicant-actions__button--primary" href="<%= contextPath %>/mo-applicant-details-preview.jsp#decision-panel">Review Now</a>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>

  <script src="<%= contextPath %>/assets/js/pages/mo-review-queue.js"></script>
</body>
</html>
