<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.bupt.ta.dto.PageResult" %>
<%
    Map<String, Object> job = (Map<String, Object>) request.getAttribute("job");
    PageResult<Map<String, Object>> applicationsPage = (PageResult<Map<String, Object>>) request.getAttribute("applicationsPage");
%>
<!DOCTYPE html>
<html>
<head>
    <title>MO Applicant List</title>
</head>
<body>
<h1>MO Applicant List</h1>

<% if (job != null) { %>
<h2><%= job.get("courseName") %> (<%= job.get("courseCode") %>)</h2>
<p>Required Skills: <%= job.get("requiredSkills") %></p>
<% } %>

<table border="1" cellpadding="6">
    <thead>
    <tr>
        <th>Application ID</th>
        <th>TA Name</th>
        <th>Status</th>
        <th>Match Score</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <% if (applicationsPage != null) {
           for (Map<String, Object> row : applicationsPage.getRecords()) { %>
    <tr>
        <td><%= row.get("applicationId") %></td>
        <td><%= row.get("taName") %></td>
        <td><%= row.get("status") %></td>
        <td><%= row.get("skillMatchScore") %>%</td>
        <td><a href="<%= request.getContextPath() %>/mo/applicants/detail?applicationId=<%= row.get("applicationId") %>">View Detail</a></td>
    </tr>
    <%     }
       } %>
    </tbody>
</table>
</body>
</html>
