<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%
    Map<String, Object> profile = (Map<String, Object>) request.getAttribute("profile");
    List<String> allSkillTags = (List<String>) request.getAttribute("allSkillTags");
    Map<String, Object> extractedResume = profile == null ? null : (Map<String, Object>) profile.get("extractedResume");
%>
<!DOCTYPE html>
<html>
<head>
    <title>TA Profile</title>
</head>
<body>
<h1>TA Profile</h1>

<% if (request.getAttribute("errorMessage") != null) { %>
<p style="color:red;"><%= request.getAttribute("errorMessage") %></p>
<% } %>

<% if (profile != null) { %>
<h2>Basic Information</h2>
<form method="post" action="<%= request.getContextPath() %>/ta/profile">
    <p>Full Name: <input type="text" name="name" value="<%= profile.get("fullName") %>"></p>
    <p>Student ID: <input type="text" value="<%= profile.get("studentId") %>" readonly></p>
    <p>Major: <input type="text" name="major" value="<%= profile.get("majorProgram") %>"></p>
    <p>Academic Year: <input type="text" name="grade" value="<%= profile.get("academicYear") %>"></p>
    <p>Email: <input type="email" name="email" value="<%= profile.get("email") %>"></p>
    <p>Phone: <input type="text" name="phone" value="<%= profile.get("phone") %>"></p>
    <p>Introduction:<br><textarea name="intro" rows="4" cols="80"><%= profile.get("intro") %></textarea></p>
    <p>Skill Tags:
        <select name="skillTags" multiple size="8">
            <% for (String skill : allSkillTags) { %>
            <option value="<%= skill %>" <%= ((List<?>) profile.get("skills")).contains(skill) ? "selected" : "" %>><%= skill %></option>
            <% } %>
        </select>
    </p>
    <button type="submit">Save Profile</button>
</form>

<h2>Resume Upload</h2>
<p>Current File: <strong><%= profile.get("resumeFileName") == null ? "No resume uploaded" : profile.get("resumeFileName") %></strong></p>
<p>Uploaded At: <%= profile.get("resumeUploadedAt") == null ? "-" : profile.get("resumeUploadedAt") %></p>
<form method="post" action="<%= request.getContextPath() %>/ta/profile/resume" enctype="multipart/form-data">
    <input type="file" name="resumeFile" accept="application/pdf" required>
    <button type="submit">Upload / Replace PDF Resume</button>
</form>
<p>Only PDF files are accepted. Maximum size: 5MB.</p>

<h2>Extracted Resume Information</h2>
<% if (extractedResume != null) { %>
<p><strong>Name:</strong> <%= extractedResume.get("name") %></p>
<p><strong>Email:</strong> <%= extractedResume.get("email") %></p>
<p><strong>Phone:</strong> <%= extractedResume.get("phone") %></p>
<p><strong>Education:</strong> <%= extractedResume.get("education") %></p>
<p><strong>Skills:</strong> <%= extractedResume.get("skills") %></p>
<p><strong>Experience Highlights:</strong> <%= extractedResume.get("experienceHighlights") %></p>
<% } else { %>
<p>No structured resume data is available yet.</p>
<% } %>
<% } else { %>
<p>No profile data available.</p>
<% } %>
</body>
</html>
