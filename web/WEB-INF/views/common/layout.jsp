<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  String bodyPage = (String) request.getAttribute("bodyPage");
  if (bodyPage == null || bodyPage.trim().isEmpty()) {
      bodyPage = "/WEB-INF/views/common/layout-placeholder.jsp";
  }

  Object sidebarFlag = request.getAttribute("showSidebar");
  boolean showSidebar = sidebarFlag instanceof Boolean && (Boolean) sidebarFlag;
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><%= request.getAttribute("pageTitle") != null ? request.getAttribute("pageTitle") : "TA Recruitment System" %></title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
</head>
<body>
  <div class="app-shell <%= showSidebar ? "app-shell--with-sidebar" : "" %>">
    <% if (showSidebar) { %>
      <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
    <% } %>

    <div class="app-main">
      <jsp:include page="/WEB-INF/views/common/header.jsp" />

      <main class="app-content">
        <jsp:include page="<%= bodyPage %>" />
      </main>

      <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </div>
  </div>
</body>
</html>
