<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  String errorMessage = String.valueOf(request.getAttribute("errorMessage") == null ? "Something went wrong." : request.getAttribute("errorMessage"));
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Error | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
</head>
<body style="background:#f8fafc;">
  <main style="max-width:720px;margin:72px auto;padding:0 20px;">
    <section style="background:#fff;border:1px solid #f5c2c7;border-radius:24px;padding:32px;box-shadow:0 18px 50px rgba(15,23,42,0.08);">
      <p style="margin:0 0 8px;color:#b42318;font-weight:700;">System Error</p>
      <h1 style="margin:0 0 16px;font-size:32px;">We couldn't complete that request.</h1>
      <p style="margin:0 0 24px;color:#475467;"><%= errorMessage %></p>
      <div style="display:flex;gap:12px;flex-wrap:wrap;">
        <a href="<%= contextPath %>/auth/login" style="padding:12px 18px;border-radius:999px;background:#111827;color:#fff;text-decoration:none;">Back to Login</a>
        <a href="javascript:history.back()" style="padding:12px 18px;border-radius:999px;border:1px solid #d0d5dd;color:#111827;text-decoration:none;">Go Back</a>
      </div>
    </section>
  </main>
</body>
</html>
