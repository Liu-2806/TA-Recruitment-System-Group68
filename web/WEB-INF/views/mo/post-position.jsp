<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  Object currentUserObj = request.getSession(false) == null ? null : request.getSession(false).getAttribute("currentUser");
  com.bupt.ta.model.User currentUser = currentUserObj instanceof com.bupt.ta.model.User ? (com.bupt.ta.model.User) currentUserObj : null;
  String currentUserName = currentUser == null || currentUser.getDisplayName() == null || currentUser.getDisplayName().trim().isEmpty()
      ? "MO"
      : currentUser.getDisplayName().trim();
  String currentUserInitial = currentUserName.isEmpty() ? "M" : currentUserName.substring(0, 1).toUpperCase();
  String errorMessage = String.valueOf(request.getAttribute("errorMessage") == null ? "" : request.getAttribute("errorMessage"));
  Object formDataObj = request.getAttribute("formData");
  java.util.Map formData = formDataObj instanceof java.util.Map ? (java.util.Map) formDataObj : java.util.Collections.emptyMap();
  String normalizedActivityType = com.bupt.ta.util.ActivityTypeUtils.normalize(String.valueOf(formData.getOrDefault("activityType", "")));
  String assetVersion = "20260410-selectfix-3";

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
  <title>Post New Position | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/mo-post-position.css?v=<%= assetVersion %>">
  <style>
    .mo-post-input--select select {
      display: block;
      width: 100%;
      height: 44px;
      padding: 0 40px 0 16px;
      border: 1px solid #e2e8f0;
      border-radius: 14px;
      background: #ffffff;
      color: #334155;
      font-size: 14px;
      font-weight: 600;
      outline: none;
      appearance: none;
      -webkit-appearance: none;
      -moz-appearance: none;
      cursor: pointer;
    }

    .mo-post-input--select select:focus {
      border-color: #60a5fa;
      box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.08);
    }

    .mo-post-input--select select::-ms-expand {
      display: none;
    }

    .mo-post-input input[type="number"] {
      appearance: textfield;
      -moz-appearance: textfield;
    }

    .mo-post-input input[type="number"]::-webkit-outer-spin-button,
    .mo-post-input input[type="number"]::-webkit-inner-spin-button {
      -webkit-appearance: none;
      margin: 0;
    }

    .mo-post-select__caret {
      position: absolute;
      right: 14px;
      top: 50%;
      transform: translateY(-50%);
      width: 0;
      height: 0;
      border-left: 5px solid transparent;
      border-right: 5px solid transparent;
      border-top: 7px solid #94a3b8;
      pointer-events: none;
    }
  </style>
</head>
<body>
  <div class="mo-post-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="mo-post-main">
      <section class="mo-post-card">
        <div class="mo-post-card__header">
          <div class="mo-post-card__title">
            <span class="mo-post-card__icon" aria-hidden="true">
              <svg viewBox="0 0 24 24" focusable="false">
                <path d="M12 5v14M5 12h14" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </span>
            <h2>Position Details</h2>
          </div>
          <span class="mo-post-card__badge">* Required Fields</span>
        </div>
        <form class="mo-post-form" action="<%= contextPath %>/mo/jobs/create" method="post">
          <div class="mo-post-grid">
            <div class="mo-post-field">
              <label for="postingType">* Position Type</label>
              <div class="mo-post-input mo-post-input--select">
                <select id="postingType" name="postingType">
                  <option value="TA" <%= !"ACTIVITY".equalsIgnoreCase(String.valueOf(formData.getOrDefault("postingType", "TA"))) ? "selected" : "" %>>TA Position</option>
                  <option value="ACTIVITY" <%= "ACTIVITY".equalsIgnoreCase(String.valueOf(formData.getOrDefault("postingType", ""))) ? "selected" : "" %>>Activity</option>
                </select>
                <span class="mo-post-select__caret" aria-hidden="true"></span>
              </div>
            </div>

            <div class="mo-post-field">
              <label for="courseName">* Position Name</label>
              <div class="mo-post-input">
                <span class="mo-post-input__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M5.5 8h13v10h-13Zm3-2.5h7V8h-7Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </span>
                <input id="courseName" name="courseName" type="text" value="<%= String.valueOf(formData.getOrDefault("courseName", "")) %>" placeholder="e.g. Software Engineering">
              </div>
            </div>

            <div class="mo-post-field">
              <label for="courseCode">* Reference Code</label>
              <div class="mo-post-input">
                <span class="mo-post-input__icon mo-post-input__icon--text" aria-hidden="true">#</span>
                <input id="courseCode" name="courseCode" type="text" value="<%= String.valueOf(formData.getOrDefault("courseCode", "")) %>" placeholder="e.g. SE3001">
              </div>
            </div>

            <div class="mo-post-field">
              <label for="vacancies">* Number of Vacancies</label>
              <div class="mo-post-input">
                <input id="vacancies" name="vacancies" type="number" min="1" value="<%= String.valueOf(formData.getOrDefault("vacancies", "3")) %>">
                <span class="mo-post-input__suffix">Persons</span>
              </div>
            </div>

            <div class="mo-post-field">
              <label for="deadline">* Application Deadline</label>
              <div class="mo-post-input mo-post-input--picker" id="deadlinePickerField">
                <input id="deadline" name="deadline" type="date" value="<%= String.valueOf(formData.getOrDefault("deadline", "")) %>">
              </div>
            </div>

            <div class="mo-post-field mo-post-field--full" id="activityFields" <%= "ACTIVITY".equalsIgnoreCase(String.valueOf(formData.getOrDefault("postingType", ""))) ? "" : "hidden" %>>
              <div class="mo-post-field__label-row">
                <label>Activity Schedule</label>
                <span>Required for one-off activities</span>
              </div>
              <div class="mo-post-grid">
                <div class="mo-post-field">
                  <label for="activityType">Activity Type</label>
                  <div class="mo-post-input mo-post-input--select">
                    <select id="activityType" name="activityType">
                      <option value="lab-support" <%= (!formData.containsKey("activityType") || "lab-support".equalsIgnoreCase(normalizedActivityType)) ? "selected" : "" %>>Lab Support</option>
                      <option value="lab-assessment" <%= "lab-assessment".equalsIgnoreCase(normalizedActivityType) ? "selected" : "" %>>Lab Assessment</option>
                      <option value="project-assessment" <%= "project-assessment".equalsIgnoreCase(normalizedActivityType) ? "selected" : "" %>>Project Assessment</option>
                      <option value="invigilation" <%= "invigilation".equalsIgnoreCase(normalizedActivityType) ? "selected" : "" %>>Invigilation</option>
                      <option value="others" <%= "others".equalsIgnoreCase(normalizedActivityType) ? "selected" : "" %>>Others</option>
                    </select>
                    <span class="mo-post-select__caret" aria-hidden="true"></span>
                  </div>
                </div>

                <div class="mo-post-field">
                  <label for="activityDate">Activity Date</label>
                  <div class="mo-post-input">
                    <input id="activityDate" name="activityDate" type="date" value="<%= String.valueOf(formData.getOrDefault("activityDate", "")) %>">
                  </div>
                </div>

                <div class="mo-post-field">
                  <label for="activityStartTime">Start Time</label>
                  <div class="mo-post-input">
                    <input id="activityStartTime" name="activityStartTime" type="time" value="<%= String.valueOf(formData.getOrDefault("activityStartTime", "")) %>">
                  </div>
                </div>

                <div class="mo-post-field">
                  <label for="activityEndTime">End Time</label>
                  <div class="mo-post-input">
                    <input id="activityEndTime" name="activityEndTime" type="time" value="<%= String.valueOf(formData.getOrDefault("activityEndTime", "")) %>">
                  </div>
                </div>

                <div class="mo-post-field mo-post-field--full">
                  <label for="activityLocation">Location</label>
                  <div class="mo-post-input">
                    <input id="activityLocation" name="activityLocation" type="text" value="<%= String.valueOf(formData.getOrDefault("activityLocation", "")) %>" placeholder="e.g. Central Exam Hall">
                  </div>
                </div>
              </div>
            </div>

            <div class="mo-post-field mo-post-field--full">
              <label for="positionDescription">* Position Description</label>
              <textarea id="positionDescription" name="description" rows="5" placeholder="Briefly describe the responsibilities..."><%= String.valueOf(formData.getOrDefault("description", "")) %></textarea>
            </div>

            <div class="mo-post-field mo-post-field--full">
              <div class="mo-post-field__label-row">
                <label for="requiredSkills">* Required Skills</label>
                <span>Use commas to separate</span>
              </div>
              <div class="mo-post-input mo-post-input--skills">
                <span class="mo-post-input__icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" focusable="false">
                    <path d="M4.75 10.25 10.25 4.75H17l2.25 2.25v6.75L13.75 19.25 4.75 10.25Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                    <circle cx="14.5" cy="9.5" r="1" fill="currentColor"/>
                  </svg>
                </span>
                <input id="requiredSkills" name="requiredSkills" type="text" value="<%= String.valueOf(formData.getOrDefault("requiredSkills", "")) %>" placeholder="Java, Python, Communication Skills...">
              </div>
            </div>

            <div class="mo-post-field mo-post-field--full">
              <label for="estimatedWorkload">Estimated Workload</label>
              <div class="mo-post-input">
                <input id="estimatedWorkload" name="estimatedWorkloadHours" type="number" value="<%= String.valueOf(formData.getOrDefault("estimatedWorkloadHours", "6")) %>" min="1">
                <span class="mo-post-input__suffix">Hours / Week</span>
              </div>
            </div>
          </div>
          <input type="hidden" name="status" value="OPEN">

          <div class="mo-post-actions">
            <button class="mo-post-actions__primary" type="submit">
              <span class="mo-post-actions__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="m4.75 12 14.5-6.5-4.75 13-3-5.25L4.75 12Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>Post Position</span>
            </button>

            <a class="mo-post-actions__cancel" href="<%= contextPath %>/mo/dashboard">
              <span class="mo-post-actions__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="m7 7 10 10M17 7 7 17" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>Cancel</span>
            </a>
          </div>
        </form>
      </section>

      <div class="mo-post-tip">
        <span class="mo-post-tip__icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" focusable="false">
            <path d="M12 8.5v4.5m0 3h.01M12 20a8 8 0 1 0-8-8 8 8 0 0 0 8 8Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </span>
        <p><strong>Pro Tip:</strong> Once a position is posted, it will be visible to all eligible students immediately. You can track applicant progress from your dashboard.</p>
      </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
  <script src="<%= contextPath %>/assets/js/pages/mo-post-position.js"></script>
</body>
</html>
