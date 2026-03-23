<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="resolvedSystemName" value="${empty systemName ? 'TA Recruitment System' : systemName}" />
<c:set var="resolvedRole" value="${empty currentUserRole ? 'ta' : currentUserRole}" />
<c:set var="resolvedPortalLabel" value="${empty sidebarPortalLabel ? 'Workspace' : sidebarPortalLabel}" />
<c:set var="resolvedActiveNav" value="${empty activeNav ? '' : activeNav}" />
<c:set var="resolvedRoleLabel" value="${empty currentUserRoleLabel ? 'User' : currentUserRoleLabel}" />

<aside class="app-sidebar">
  <div class="app-sidebar__brand">
    <span class="app-brand__mark">T</span>
    <div>
      <div class="app-brand__text">${resolvedPortalLabel}</div>
      <div class="app-header__eyebrow">${resolvedSystemName}</div>
    </div>
  </div>

  <nav class="app-sidebar__nav" aria-label="Primary Navigation">
    <div class="app-sidebar__group">
      <p class="app-sidebar__group-title">Main</p>

      <c:choose>
        <c:when test="${resolvedRole eq 'admin'}">
          <a class="app-sidebar__link ${resolvedActiveNav eq 'admin-dashboard' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">DB</span>Dashboard</span>
          </a>
          <a class="app-sidebar__link ${resolvedActiveNav eq 'admin-create-mo' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">MO</span>Create MO Account</span>
          </a>
          <a class="app-sidebar__link ${resolvedActiveNav eq 'admin-mos' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">AM</span>All MOs</span>
          </a>
          <a class="app-sidebar__link ${resolvedActiveNav eq 'admin-positions' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">AP</span>All Positions</span>
          </a>
          <a class="app-sidebar__link ${resolvedActiveNav eq 'admin-workload' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">WL</span>All TA Workload</span>
          </a>
        </c:when>

        <c:when test="${resolvedRole eq 'mo'}">
          <a class="app-sidebar__link ${resolvedActiveNav eq 'mo-dashboard' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">DB</span>Dashboard</span>
          </a>
          <a class="app-sidebar__link ${resolvedActiveNav eq 'mo-profile' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">PF</span>My Profile</span>
          </a>
          <a class="app-sidebar__link ${resolvedActiveNav eq 'mo-post-position' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">PP</span>Post New Position</span>
          </a>
          <a class="app-sidebar__link ${resolvedActiveNav eq 'mo-postings' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">MP</span>My Postings</span>
          </a>
          <a class="app-sidebar__link ${resolvedActiveNav eq 'mo-revocations' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">RV</span>Revocation Requests</span>
            <c:if test="${not empty revocationCount}">
              <span class="ui-chip ui-chip--primary">${revocationCount}</span>
            </c:if>
          </a>
        </c:when>

        <c:otherwise>
          <a class="app-sidebar__link ${resolvedActiveNav eq 'ta-dashboard' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">DB</span>Dashboard</span>
          </a>
          <a class="app-sidebar__link ${resolvedActiveNav eq 'ta-profile' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">PF</span>My Profile</span>
          </a>
          <a class="app-sidebar__link ${resolvedActiveNav eq 'ta-positions' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">BP</span>Browse Positions</span>
          </a>
          <a class="app-sidebar__link ${resolvedActiveNav eq 'ta-applications' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">AP</span>My Applications</span>
            <c:if test="${not empty applicationCount}">
              <span class="ui-chip ui-chip--primary">${applicationCount}</span>
            </c:if>
          </a>
          <a class="app-sidebar__link ${resolvedActiveNav eq 'ta-notifications' ? 'is-active' : ''}" href="#">
            <span class="app-sidebar__link-main"><span class="app-sidebar__icon">NT</span>Notifications</span>
          </a>
        </c:otherwise>
      </c:choose>
    </div>

    <div class="app-sidebar__group">
      <p class="app-sidebar__group-title">Utilities</p>
      <a class="app-sidebar__link ${resolvedActiveNav eq 'settings' ? 'is-active' : ''}" href="#">
        <span class="app-sidebar__link-main"><span class="app-sidebar__icon">ST</span>Settings</span>
      </a>
      <a class="app-sidebar__link" href="#">
        <span class="app-sidebar__link-main"><span class="app-sidebar__icon">LO</span>Logout</span>
      </a>
    </div>
  </nav>

  <div class="app-sidebar__footer">
    <div class="ui-user-block__name">${resolvedRoleLabel}</div>
    <div class="ui-user-block__role">Role Workspace</div>
  </div>
</aside>
