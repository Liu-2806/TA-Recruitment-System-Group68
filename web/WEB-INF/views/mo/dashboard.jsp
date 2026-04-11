<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  Object currentUserObj = request.getSession(false) == null ? null : request.getSession(false).getAttribute("currentUser");
  com.bupt.ta.model.User currentUser = currentUserObj instanceof com.bupt.ta.model.User ? (com.bupt.ta.model.User) currentUserObj : null;
  String currentUserName = currentUser == null || currentUser.getDisplayName() == null || currentUser.getDisplayName().trim().isEmpty()
      ? "MO"
      : currentUser.getDisplayName().trim();
  String currentUserInitial = currentUserName.isEmpty() ? "M" : currentUserName.substring(0, 1).toUpperCase();
  Object overviewObj = request.getAttribute("overview");
  java.util.Map overview = overviewObj instanceof java.util.Map ? (java.util.Map) overviewObj : java.util.Collections.emptyMap();
  Object profileObj = overview.get("profileCard");
  java.util.Map profile = profileObj instanceof java.util.Map ? (java.util.Map) profileObj : java.util.Collections.emptyMap();
  String moName = String.valueOf(profile.getOrDefault("fullName", profile.getOrDefault("displayName", currentUserName)));
  String moStaffId = String.valueOf(profile.getOrDefault("staffId", "-"));
  String moDepartment = String.valueOf(profile.getOrDefault("department", "-"));

  Object activePostingsObj = overview.get("activePostings");
  java.util.List activePostings = activePostingsObj instanceof java.util.List ? (java.util.List) activePostingsObj : java.util.Collections.emptyList();
  Object awaitingObj = overview.get("awaitingReviewCount");
  int awaitingReviewCount = awaitingObj instanceof Number ? ((Number) awaitingObj).intValue() : 0;
  Object alertsObj = overview.get("alerts");
  java.util.List alerts = alertsObj instanceof java.util.List ? (java.util.List) alertsObj : java.util.Collections.emptyList();

  request.setAttribute("headerBrandHref", contextPath + "/mo/dashboard");
  request.setAttribute("showHeaderBack", Boolean.FALSE);
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", currentUserName);
  request.setAttribute("currentUserRoleLabel", "Module Organizer");
  request.setAttribute("currentUserInitial", currentUserInitial);
  request.setAttribute("notificationCount", Integer.valueOf(Math.max(awaitingReviewCount, 0)));
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
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="mo-dashboard-main">
      <div class="mo-dashboard-grid">
        <aside class="mo-dashboard-sidebar">
          <section class="mo-card mo-card--profile mo-profile-card">
            <div class="mo-card__accent"></div>
            <div class="mo-profile-card__avatar">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 12a4 4 0 1 0-4-4 4 4 0 0 0 4 4Zm0 1.5c-3.3 0-6 1.97-6 4.4V19h12v-1.1c0-2.43-2.7-4.4-6-4.4Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <h2><%= moName %></h2>
            <p class="mo-profile-card__staff-id">Staff ID: <%= moStaffId %></p>
            <p class="mo-profile-card__dept"><%= moDepartment %></p>
            <a class="mo-profile-card__button" href="<%= contextPath %>/mo/profile">
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
              <strong><%= awaitingReviewCount %></strong>
              <span>applications found</span>
            </div>
            <a class="mo-metric-card__button" href="<%= contextPath %>/mo/review-queue">
              <span>Manage Review</span>
              <span aria-hidden="true">-></span>
            </a>
          </section>

          <section class="mo-card mo-alert-card">
            <div class="mo-alert-card__header">
              <h3>System Alerts</h3>
            </div>

            <% if (alerts.isEmpty()) { %>
            <div class="mo-alert-card__critical">
              <p>No deadline warnings for your open postings.</p>
            </div>
            <% } else { %>
            <div class="mo-alert-card__critical">
              <p><%= alerts.size() %> <%= alerts.size() == 1 ? "position is" : "positions are" %> closing soon. Finalize selections.</p>
            </div>
            <% } %>
          </section>
        </aside>

        <section class="mo-dashboard-content">
          <div class="mo-dashboard-actions">
            <a class="mo-action-card mo-action-card--primary" href="<%= contextPath %>/mo/jobs/create">
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

            <a class="mo-action-card mo-action-card--secondary" href="<%= contextPath %>/mo/jobs/my">
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
                <h3>My Active Postings <span>(<%= activePostings.size() %>)</span></h3>
              </div>
            </div>

            <div class="mo-postings-list">
              <%
                if (activePostings.isEmpty()) {
              %>
              <p style="margin:0;color:#5c5f6a;font-size:0.95rem;">No open postings yet. Use <strong>Post New Position</strong> to create one.</p>
              <%
                } else {
                  for (Object po : activePostings) {
                    java.util.Map posting = (java.util.Map) po;
                    String courseName = String.valueOf(posting.getOrDefault("courseName", "Posting"));
                    String postingId = String.valueOf(posting.getOrDefault("postingId", ""));
                    Object ac = posting.get("applicationCount");
                    int appCount = ac instanceof Number ? ((Number) ac).intValue() : 0;
                    String cn = courseName.trim();
                    String initial = cn.isEmpty() ? "?" : cn.substring(0, 1).toUpperCase();
              %>
              <article class="mo-posting-item">
                <div class="mo-posting-item__identity">
                  <span class="mo-posting-item__avatar"><%= initial %></span>
                  <div>
                    <h4><%= courseName %></h4>
                    <p>Applicants: <span><%= appCount %></span></p>
                  </div>
                </div>
                <a class="mo-posting-item__action" href="<%= contextPath %>/mo/jobs/applicants?jobId=<%= postingId %>">
                  <span>Review Candidates</span>
                  <span aria-hidden="true">-></span>
                </a>
              </article>
              <%
                  }
                }
              %>
            </div>
          </section>
        </section>
      </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
