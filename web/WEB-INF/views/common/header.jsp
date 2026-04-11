<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="resolvedBrandHref" value="${empty headerBrandHref ? pageContext.request.contextPath.concat('/') : headerBrandHref}" />
<c:set var="resolvedBackHref" value="${empty headerBackHref ? '#' : headerBackHref}" />
<c:set var="resolvedBackLabel" value="${empty headerBackLabel ? 'Back' : headerBackLabel}" />
<c:set var="resolvedUserName" value="${empty currentUserName ? 'Guest User' : currentUserName}" />
<c:set var="resolvedUserRole" value="${empty currentUserRoleLabel ? 'Visitor' : currentUserRoleLabel}" />
<c:set var="resolvedNotificationCount" value="${empty notificationCount ? 0 : notificationCount}" />

<header class="app-header app-header--shared">
  <div class="app-header__inner app-header__inner--shared">
    <div class="app-header__left">
      <a class="app-brand app-brand--header" href="${resolvedBrandHref}">
        <span class="app-brand__mark">T</span>
        <span class="app-brand__text">TA Recruitment Portal</span>
      </a>
    </div>

    <div class="app-header__right app-header__right--shared">
      <c:if test="${showHeaderBack}">
        <a class="app-header__back" href="${resolvedBackHref}">
          <span class="app-header__back-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M15.5 6.5 10 12l5.5 5.5M11 12h8" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <span>${resolvedBackLabel}</span>
        </a>
      </c:if>

      <c:if test="${showHeaderUser}">
        <button class="ui-icon-button" type="button" aria-label="Notifications">
          <span class="app-header__bell" aria-hidden="true">
            <svg viewBox="0 0 24 24" focusable="false">
              <path d="M12 4.25a4 4 0 0 0-4 4v2.06c0 .7-.2 1.39-.58 1.98L6 14.5h12l-1.42-2.21a3.75 3.75 0 0 1-.58-1.98V8.25a4 4 0 0 0-4-4Zm0 15.5a2.38 2.38 0 0 0 2.27-1.75H9.73A2.38 2.38 0 0 0 12 19.75Z" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          <c:if test="${resolvedNotificationCount gt 0}">
            <span class="ui-notification-dot"></span>
          </c:if>
        </button>

        <div class="ui-user-menu" data-user-menu>
          <button class="ui-user-menu__trigger" type="button" aria-label="Account menu" aria-haspopup="menu" aria-expanded="false" data-user-menu-trigger>
            <span class="ui-user-block">
              <span class="ui-user-block__meta">
                <span class="ui-user-block__name">${resolvedUserName}</span>
                <span class="ui-user-block__role">${resolvedUserRole}</span>
              </span>
              <span class="ui-avatar" aria-hidden="true">
                <c:choose>
                  <c:when test="${not empty currentUserInitial}">
                    ${currentUserInitial}
                  </c:when>
                  <c:otherwise>U</c:otherwise>
                </c:choose>
              </span>
              <span class="ui-chevron" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M7 10.5 12 15l5-4.5" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
            </span>
          </button>

          <div class="ui-user-menu__panel" role="menu" aria-label="Account actions" data-user-menu-panel hidden>
            <a class="ui-user-menu__item" href="${pageContext.request.contextPath}/auth/logout" role="menuitem">
              <span class="ui-user-menu__icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" focusable="false">
                  <path d="M14 7.5V5.75A1.75 1.75 0 0 0 12.25 4h-5.5A1.75 1.75 0 0 0 5 5.75v12.5C5 19.22 5.78 20 6.75 20h5.5A1.75 1.75 0 0 0 14 18.25V16.5m-1.5-4.5h8m0 0-2.75-2.75M20.5 12l-2.75 2.75" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>Log out</span>
            </a>
          </div>
        </div>
      </c:if>
    </div>
  </div>
</header>
<jsp:include page="/WEB-INF/views/common/toast.jsp" />
