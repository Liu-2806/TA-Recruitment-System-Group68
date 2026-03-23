<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="resolvedSystemName" value="${empty systemName ? 'TA Recruitment System' : systemName}" />
<c:set var="resolvedPageTitle" value="${empty pageTitle ? 'Workspace' : pageTitle}" />
<c:set var="resolvedEyebrow" value="${empty pageEyebrow ? 'Current Section' : pageEyebrow}" />
<c:set var="resolvedUserName" value="${empty currentUserName ? 'Guest User' : currentUserName}" />
<c:set var="resolvedUserRole" value="${empty currentUserRoleLabel ? 'Visitor' : currentUserRoleLabel}" />
<c:set var="resolvedNotificationCount" value="${empty notificationCount ? 0 : notificationCount}" />
<c:set var="showBrand" value="${empty headerShowBrand ? true : headerShowBrand}" />

<header class="app-header">
  <div class="app-header__inner">
    <div class="app-header__left">
      <c:if test="${showBrand}">
        <a class="app-brand" href="${pageContext.request.contextPath}/">
          <span class="app-brand__mark">T</span>
          <span class="app-brand__text">${resolvedSystemName}</span>
        </a>
      </c:if>

      <div class="app-header__context">
        <span class="app-header__eyebrow">${resolvedEyebrow}</span>
        <span class="app-header__title">${resolvedPageTitle}</span>
      </div>
    </div>

    <div class="app-header__right">
      <button class="ui-icon-button" type="button" aria-label="Notifications">
        <span aria-hidden="true">N</span>
        <c:if test="${resolvedNotificationCount gt 0}">
          <span class="ui-notification-dot"></span>
        </c:if>
      </button>

      <div class="ui-user-block">
        <div class="ui-user-block__meta">
          <p class="ui-user-block__name">${resolvedUserName}</p>
          <p class="ui-user-block__role">${resolvedUserRole}</p>
        </div>
        <span class="ui-avatar" aria-hidden="true">
          <c:choose>
            <c:when test="${not empty currentUserInitial}">
              ${currentUserInitial}
            </c:when>
            <c:otherwise>U</c:otherwise>
          </c:choose>
        </span>
        <span class="ui-chevron" aria-hidden="true">v</span>
      </div>
    </div>
  </div>
</header>
