<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%
    Map<String, Object> job = (Map<String, Object>) request.getAttribute("job");
    Map<String, Object> matchAnalysis = (Map<String, Object>) request.getAttribute("matchAnalysis");
%>
<!DOCTYPE html>
<html>
<head>
    <title>TA Job Detail</title>
</head>
<body>
<h1>TA Job Detail</h1>

<% if (job != null) { %>
<h2><%= job.get("courseName") %> (<%= job.get("courseCode") %>)</h2>
<p><strong>MO:</strong> <%= job.get("moName") %></p>
<p><strong>Deadline:</strong> <%= job.get("deadline") %></p>
<p><strong>Vacancies:</strong> <%= job.get("vacancies") %></p>
<p><strong>Status:</strong> <%= job.get("status") %></p>
<p><strong>Required Skills:</strong> <%= job.get("requiredSkills") %></p>
<p><strong>Description:</strong> <%= job.get("description") %></p>
<p><strong>Estimated Workload:</strong> <%= job.get("estimatedWorkloadHours") %> hours/week</p>

<h2>AI Match Analysis</h2>
<% if (matchAnalysis != null) { %>
<p><strong>Match Score:</strong> <%= matchAnalysis.get("score") %>%</p>
<p><strong>Method:</strong> <%= matchAnalysis.get("method") %></p>
<p><strong>Matched Skills:</strong> <%= matchAnalysis.get("matchedSkills") %></p>
<p><strong>Missing Skills:</strong> <%= matchAnalysis.get("missingSkills") %></p>
<p><strong>Explanation:</strong> <%= matchAnalysis.get("explanation") %></p>
<% } else { %>
<p>No match analysis is available.</p>
<% } %>
<% } else { %>
<p>No job data available.</p>
<% } %>
</body>
</html>
