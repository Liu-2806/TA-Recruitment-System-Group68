<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%
    Map<String, Object> application = (Map<String, Object>) request.getAttribute("application");
    Map<String, Object> taProfile = application == null ? null : (Map<String, Object>) application.get("taProfile");
    Map<String, Object> job = application == null ? null : (Map<String, Object>) application.get("job");
%>
<!DOCTYPE html>
<html>
<head>
    <title>MO Applicant Detail</title>
</head>
<body>
<h1>MO Applicant Detail</h1>

<% if (application != null && taProfile != null && job != null) { %>
<h2>Applicant Information</h2>
<p><strong>Name:</strong> <%= taProfile.get("fullName") %></p>
<p><strong>Student ID:</strong> <%= taProfile.get("studentId") %></p>
<p><strong>Major:</strong> <%= taProfile.get("majorProgram") %></p>
<p><strong>Year:</strong> <%= taProfile.get("academicYear") %></p>
<p><strong>Email:</strong> <%= taProfile.get("email") %></p>
<p><strong>Skills:</strong> <%= taProfile.get("skills") %></p>

<h2>Resume</h2>
<p><strong>Resume File:</strong> <%= taProfile.get("resumeFileName") %></p>
<p><strong>Uploaded At:</strong> <%= taProfile.get("resumeUploadedAt") %></p>
<p><a href="<%= request.getContextPath() %>/mo/applicants/resume?applicationId=<%= application.get("applicationId") %>">Download Resume PDF</a></p>

<h2>Applied Job</h2>
<p><strong>Course:</strong> <%= job.get("courseName") %> (<%= job.get("courseCode") %>)</p>
<p><strong>Required Skills:</strong> <%= job.get("requiredSkills") %></p>

<h2>AI Match Analysis for MO Review</h2>
<p><strong>Match Score:</strong> <%= application.get("skillMatchScore") %>%</p>
<p><strong>Matched Skills:</strong> <%= application.get("matchedSkills") %></p>
<p><strong>Missing Skills:</strong> <%= application.get("missingSkills") %></p>
<p><strong>Explanation:</strong> <%= application.get("skillMatchExplanation") %></p>
<p><strong>Method:</strong> <%= application.get("matchMethod") %></p>

<h2>Application Details</h2>
<p><strong>Status:</strong> <%= application.get("status") %></p>
<p><strong>Statement:</strong> <%= application.get("statement") %></p>
<p><strong>Applied At:</strong> <%= application.get("appliedAt") %></p>
<% } else { %>
<p>No application detail available.</p>
<% } %>
</body>
</html>
