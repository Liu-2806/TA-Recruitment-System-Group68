<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="resolvedSystemName" value="${empty systemName ? 'TA Recruitment System' : systemName}" />
<c:set var="resolvedVersion" value="${empty systemVersion ? 'Version 0.1' : systemVersion}" />
<c:set var="resolvedGroupName" value="${empty groupName ? 'Group 68' : groupName}" />

<footer class="app-footer">
  <div class="app-footer__inner">
    <div>
      <strong>${resolvedSystemName}</strong>
      <span> | EBU6304 Software Engineering Group Project</span>
    </div>
    <div>
      <span>${resolvedGroupName}</span>
      <span> | ${resolvedVersion}</span>
    </div>
  </div>
</footer>
