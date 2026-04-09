<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  String contextPath = request.getContextPath();
  Object currentUserObj = request.getSession(false) == null ? null : request.getSession(false).getAttribute("currentUser");
  com.bupt.ta.model.User currentUser = currentUserObj instanceof com.bupt.ta.model.User ? (com.bupt.ta.model.User) currentUserObj : null;
  String currentUserName = currentUser == null || currentUser.getDisplayName() == null || currentUser.getDisplayName().trim().isEmpty()
      ? "MO"
      : currentUser.getDisplayName().trim();
  String currentUserInitial = currentUserName.isEmpty() ? "M" : currentUserName.substring(0, 1).toUpperCase();
  Object pc = request.getAttribute("pendingCount");
  int pendingCount = pc instanceof Number ? ((Number) pc).intValue() : 0;
  Object pqc = request.getAttribute("postingsInQueueCount");
  int postingsInQueueCount = pqc instanceof Number ? ((Number) pqc).intValue() : 0;

  request.setAttribute("headerBrandHref", contextPath + "/mo/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/mo/dashboard");
  request.setAttribute("headerBackLabel", "Back to Dashboard");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", currentUserName);
  request.setAttribute("currentUserRoleLabel", "Module Organizer");
  request.setAttribute("currentUserInitial", currentUserInitial);
  request.setAttribute("notificationCount", Integer.valueOf(Math.max(pendingCount, 0)));
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
              <strong><%= pendingCount %> <span>Pending</span></strong>
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
              <strong><%= postingsInQueueCount %> <span>Positions</span></strong>
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
              <c:choose>
                <c:when test="${empty pendingRows}">
                  <tr>
                    <td colspan="7" style="padding:1.25rem;text-align:center;color:#5c5f6a;">No applications pending review.</td>
                  </tr>
                </c:when>
                <c:otherwise>
                  <c:forEach var="row" items="${pendingRows}">
                    <tr data-name="${row.taNameSortKey}" data-course="${row.courseSortKey}" data-submitted="${row.submittedIso}" data-match="${row.skillMatchScore}">
                      <td>
                        <div class="mo-applicant-cell">
                          <span class="mo-applicant-cell__avatar" aria-hidden="true">
                            <svg viewBox="0 0 24 24" focusable="false">
                              <path d="M12 12a3.75 3.75 0 1 0-3.75-3.75A3.75 3.75 0 0 0 12 12Zm0 1.5c-3.17 0-5.75 1.89-5.75 4.22V19h11.5v-.28c0-2.33-2.58-4.22-5.75-4.22Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                            </svg>
                          </span>
                          <div class="mo-applicant-cell__meta">
                            <strong><c:out value="${row.taName}" /></strong>
                            <p><c:out value="${row.taEmail}" /></p>
                          </div>
                        </div>
                      </td>
                      <td>
                        <div class="mo-academic-cell">
                          <strong><c:out value="${row.postingTitle}" /></strong>
                          <p><c:out value="${row.positionSubtitle}" /></p>
                        </div>
                      </td>
                      <td class="mo-review-queue__time">
                        <strong><c:out value="${row.submittedDateDisplay}" /></strong>
                        <p><c:out value="${row.submittedTimeDisplay}" /></p>
                      </td>
                      <td>
                        <div class="mo-match-cell">
                          <div class="mo-match-chip${row.matchStrong ? ' mo-match-chip--strong' : ''}"><c:out value="${row.skillMatchScore}" />% Match</div>
                          <div class="mo-match-bar"><span class="${row.matchStrong ? 'is-strong' : ''}" style="width:${row.skillMatchScore}%"></span></div>
                        </div>
                      </td>
                      <td><span class="mo-review-priority mo-review-priority--${row.priorityKey}"><c:out value="${row.priorityLabel}" /></span></td>
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
                          <a class="mo-applicant-actions__button" href="${pageContext.request.contextPath}/mo/applicants/detail?applicationId=${row.applicationId}">View Profile</a>
                          <a class="mo-applicant-actions__button mo-applicant-actions__button--primary" href="${pageContext.request.contextPath}/mo/applicants/detail?applicationId=${row.applicationId}#decision-panel">Review Now</a>
                        </div>
                      </td>
                    </tr>
                  </c:forEach>
                </c:otherwise>
              </c:choose>
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
