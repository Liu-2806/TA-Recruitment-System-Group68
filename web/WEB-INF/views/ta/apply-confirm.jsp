<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%
    Map<String, Object> job = (Map<String, Object>) request.getAttribute("job");
    Map<String, Object> eligibility = (Map<String, Object>) request.getAttribute("eligibilityResult");
%>
<!DOCTYPE html>
<html>
<head>
    <title>TA Apply Confirm</title>
</head>
<body>
<h1>TA Apply Confirm</h1>

<% if (request.getAttribute("errorMessage") != null) { %>
<p style="color:red;"><%= request.getAttribute("errorMessage") %></p>
<% } %>

<% if (job != null) { %>
<h2><%= job.get("courseName") %></h2>
<p><strong>Course Code:</strong> <%= job.get("courseCode") %></p>
<p><strong>Required Skills:</strong> <%= job.get("requiredSkills") %></p>
<% } %>

<% if (eligibility != null) { %>
<p><strong>Eligible:</strong> <%= eligibility.get("eligible") %></p>
<p><strong>Resume Uploaded:</strong> <%= eligibility.get("hasResume") %></p>
<p><strong>Duplicate Application:</strong> <%= eligibility.get("duplicateApplication") %></p>
<% } %>

<form method="post" action="<%= request.getContextPath() %>/ta/applications">
    <input type="hidden" name="jobId" value="<%= job == null ? "" : job.get("postingId") %>">
    <p>Application Statement:<br><textarea name="statement" rows="5" cols="80"></textarea></p>
    <button type="submit">Confirm and Submit</button>
</form>
</body>
</html>
