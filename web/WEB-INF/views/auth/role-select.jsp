<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Select Role | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
</head>
<body style="background:#f8fafc;">
  <jsp:include page="/WEB-INF/views/common/toast.jsp" />
  <main style="max-width:640px;margin:72px auto;padding:0 20px;">
    <section style="background:#fff;border:1px solid #d0d5dd;border-radius:24px;padding:32px;box-shadow:0 18px 50px rgba(15,23,42,0.08);">
      <h1 style="margin:0 0 16px;">Continue As</h1>
      <p style="margin:0 0 20px;color:#475467;">Choose the role you want to use in this session.</p>
      <form action="<%= contextPath %>/auth/role-select" method="post" style="display:grid;gap:12px;">
        <button type="submit" name="role" value="TA" style="padding:14px 18px;border-radius:16px;border:1px solid #d0d5dd;background:#fff;text-align:left;cursor:pointer;">TA Applicant</button>
        <button type="submit" name="role" value="MO" style="padding:14px 18px;border-radius:16px;border:1px solid #d0d5dd;background:#fff;text-align:left;cursor:pointer;">Module Organizer</button>
        <button type="submit" name="role" value="ADMIN" style="padding:14px 18px;border-radius:16px;border:1px solid #d0d5dd;background:#fff;text-align:left;cursor:pointer;">Admin</button>
      </form>
    </section>
  </main>
  <script src="<%= contextPath %>/assets/js/toast.js"></script>
</body>
</html>
