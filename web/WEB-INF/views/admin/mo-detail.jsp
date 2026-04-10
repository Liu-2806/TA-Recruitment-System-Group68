<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  Object moObj = request.getAttribute("mo");
  com.bupt.ta.model.User mo = moObj instanceof com.bupt.ta.model.User ? (com.bupt.ta.model.User) moObj : new com.bupt.ta.model.User();
  String errorMessage = String.valueOf(request.getAttribute("errorMessage") == null ? "" : request.getAttribute("errorMessage"));
  request.setAttribute("headerBrandHref", contextPath + "/admin/dashboard");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", contextPath + "/admin/mos");
  request.setAttribute("headerBackLabel", "Back to MO List");
  request.setAttribute("showHeaderUser", Boolean.TRUE);
  request.setAttribute("currentUserName", "Super Admin");
  request.setAttribute("currentUserRoleLabel", "Online");
  request.setAttribute("currentUserInitial", "A");
  request.setAttribute("notificationCount", Integer.valueOf(1));
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>MO Detail | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/admin-dashboard.css">
</head>
<body>
  <div class="admin-allmos-shell">
    <aside class="admin-sidebar">
      <div class="admin-sidebar__brand">
        <span class="app-brand__mark">T</span>
        <span class="admin-sidebar__brand-text">Admin Portal</span>
      </div>
      <nav class="admin-sidebar__nav" aria-label="Admin Navigation">
        <a class="admin-sidebar__link" href="<%= contextPath %>/admin/dashboard"><span>Dashboard</span></a>
        <a class="admin-sidebar__link" href="<%= contextPath %>/admin/mos/create"><span>Create MO Account</span></a>
        <a class="admin-sidebar__link is-active" href="<%= contextPath %>/admin/mos"><span>All MOs</span></a>
        <a class="admin-sidebar__link" href="<%= contextPath %>/admin/jobs"><span>All Postings</span></a>
        <a class="admin-sidebar__link" href="<%= contextPath %>/admin/analytics/ta-workload"><span>All TA Workload</span></a>
      </nav>
    </aside>

    <div class="admin-allmos-main">
      <jsp:include page="/WEB-INF/views/common/header.jsp" />

      <main class="admin-allmos-content" style="max-width: 960px;">
        <section class="admin-allmos-heading">
          <div>
            <h1>MO Account Detail</h1>
            <p>Review and update this module organizer's core profile information.</p>
          </div>
        </section>

        <section class="admin-create-card" style="background:#fff;border:1px solid #d0d5dd;border-radius:24px;padding:24px;">
          <form action="<%= contextPath %>/admin/mos/detail" method="post" style="display:grid;gap:16px;">
            <input type="hidden" name="moUserId" value="<%= mo.getId() == null ? "" : mo.getId() %>">
            <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(240px,1fr));gap:16px;">
              <label style="display:grid;gap:8px;">
                <span>Full Name</span>
                <input name="fullName" type="text" value="<%= mo.getFullName() == null ? "" : mo.getFullName() %>" style="padding:12px 14px;border-radius:12px;border:1px solid #d0d5dd;" required>
              </label>
              <label style="display:grid;gap:8px;">
                <span>Email</span>
                <input name="email" type="email" value="<%= mo.getEmail() == null ? "" : mo.getEmail() %>" style="padding:12px 14px;border-radius:12px;border:1px solid #d0d5dd;" required>
              </label>
              <label style="display:grid;gap:8px;">
                <span>Department</span>
                <input name="department" type="text" value="<%= mo.getDepartment() == null ? "" : mo.getDepartment() %>" style="padding:12px 14px;border-radius:12px;border:1px solid #d0d5dd;">
              </label>
              <label style="display:grid;gap:8px;">
                <span>Phone</span>
                <input name="phone" type="text" value="<%= mo.getPhone() == null ? "" : mo.getPhone() %>" style="padding:12px 14px;border-radius:12px;border:1px solid #d0d5dd;">
              </label>
              <label style="display:grid;gap:8px;">
                <span>Status</span>
                <select name="status" style="padding:12px 14px;border-radius:12px;border:1px solid #d0d5dd;">
                  <option value="ACTIVE" <%= "ACTIVE".equalsIgnoreCase(mo.getStatus()) ? "selected" : "" %>>Active</option>
                  <option value="INACTIVE" <%= "INACTIVE".equalsIgnoreCase(mo.getStatus()) ? "selected" : "" %>>Inactive</option>
                </select>
              </label>
              <label style="display:grid;gap:8px;">
                <span>Staff ID</span>
                <input type="text" value="<%= mo.getStaffId() == null ? "" : mo.getStaffId() %>" style="padding:12px 14px;border-radius:12px;border:1px solid #d0d5dd;background:#f8fafc;" readonly>
              </label>
            </div>

            <label style="display:grid;gap:8px;">
              <span>Description</span>
              <textarea name="description" rows="5" style="padding:12px 14px;border-radius:12px;border:1px solid #d0d5dd;resize:vertical;"><%= mo.getDescription() == null ? "" : mo.getDescription() %></textarea>
            </label>

            <div style="display:flex;gap:12px;flex-wrap:wrap;">
              <button type="submit" style="padding:12px 18px;border:none;border-radius:999px;background:#111827;color:#fff;cursor:pointer;">Save MO Profile</button>
              <button type="button" onclick="window.location.href='<%= contextPath %>/admin/mos'" style="padding:12px 18px;border-radius:999px;border:1px solid #d0d5dd;background:#fff;cursor:pointer;">Back to MO List</button>
            </div>
          </form>

          <form action="<%= contextPath %>/admin/mos/reset-password" method="post" style="margin-top:16px;">
            <input type="hidden" name="moUserId" value="<%= mo.getId() == null ? "" : mo.getId() %>">
            <input type="hidden" name="newPassword" value="Temp123!">
            <button type="submit" style="padding:12px 18px;border-radius:999px;border:1px solid #d0d5dd;background:#fff;cursor:pointer;">Reset Password To Temp123!</button>
          </form>
        </section>
      </main>

      <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </div>
  </div>
</body>
</html>
