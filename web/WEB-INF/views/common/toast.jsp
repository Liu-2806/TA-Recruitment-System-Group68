<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.bupt.ta.util.SessionKeys" %>
<%
  String toastType = request.getAttribute("toastType") == null ? "" : String.valueOf(request.getAttribute("toastType")).trim();
  String toastTitle = request.getAttribute("toastTitle") == null ? "" : String.valueOf(request.getAttribute("toastTitle")).trim();
  String toastMessage = request.getAttribute("toastMessage") == null ? "" : String.valueOf(request.getAttribute("toastMessage")).trim();

  Object requestError = request.getAttribute("errorMessage");
  if (toastMessage.isBlank() && requestError != null) {
    toastType = "error";
    toastMessage = String.valueOf(requestError).trim();
  }

  javax.servlet.http.HttpSession toastSession = request.getSession(false);
  if (toastMessage.isBlank() && toastSession != null) {
    Object flashSuccess = toastSession.getAttribute(SessionKeys.FLASH_SUCCESS);
    Object flashError = toastSession.getAttribute(SessionKeys.FLASH_ERROR);
    if (flashSuccess != null && !String.valueOf(flashSuccess).trim().isEmpty()) {
      toastType = "success";
      toastMessage = String.valueOf(flashSuccess).trim();
      toastSession.removeAttribute(SessionKeys.FLASH_SUCCESS);
    } else if (flashError != null && !String.valueOf(flashError).trim().isEmpty()) {
      toastType = "error";
      toastMessage = String.valueOf(flashError).trim();
      toastSession.removeAttribute(SessionKeys.FLASH_ERROR);
    }
  }

  if (!toastMessage.isBlank() && toastTitle.isBlank()) {
    if ("success".equalsIgnoreCase(toastType)) {
      toastTitle = "Success";
    } else if ("error".equalsIgnoreCase(toastType)) {
      toastTitle = "Something went wrong";
    } else {
      toastTitle = "Notice";
    }
  }
%>
<%
  if (!toastMessage.isBlank()) {
%>
<div class="app-toast-stack" aria-live="polite" aria-atomic="true">
  <div class="app-toast app-toast--<%= toastType.isBlank() ? "info" : toastType.toLowerCase() %>" data-toast data-toast-timeout="4200">
    <div class="app-toast__body">
      <p class="app-toast__title"><%= toastTitle %></p>
      <p class="app-toast__message"><%= toastMessage %></p>
    </div>
    <button class="app-toast__close" type="button" aria-label="Dismiss notification" data-toast-close>
      <span aria-hidden="true">×</span>
    </button>
  </div>
</div>
<%
  }
%>
