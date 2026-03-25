<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String contextPath = request.getContextPath();
  String legalType = (String) request.getAttribute("legalType");
  String referer = request.getHeader("Referer");
  String backHref = (referer != null && !referer.trim().isEmpty())
    ? referer
    : contextPath + "/register-preview.jsp";

  request.setAttribute("headerBrandHref", contextPath + "/login-preview.jsp");
  request.setAttribute("showHeaderBack", Boolean.TRUE);
  request.setAttribute("headerBackHref", backHref);
  request.setAttribute("headerBackLabel", "Back");
  request.setAttribute("showHeaderUser", Boolean.FALSE);
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Legal Information | TA Recruitment System</title>
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/base.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/layout.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/components.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/login.css">
  <link rel="stylesheet" href="<%= contextPath %>/assets/css/pages/legal.css">
</head>
<body>
  <div class="auth-shell legal-shell">
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="auth-stage legal-stage">
      <section class="auth-card legal-card" aria-labelledby="legal-heading">
        <div class="auth-card__hero legal-card__hero">
          <p class="legal-card__eyebrow">Placeholder Content</p>
          <h1 id="legal-heading" class="auth-card__title">User Agreement and Privacy Policy</h1>
          <p class="auth-card__subtitle">This page is reserved for the formal legal content that will be completed in a later iteration.</p>
        </div>

        <div class="auth-card__body legal-card__body">
          <p class="legal-card__notice">
            This is a temporary placeholder page for coursework development. Do not treat the text below as final legal language.
          </p>

          <nav class="legal-nav" aria-label="Legal sections">
            <a class="legal-nav__link <%= "agreement".equals(legalType) ? "is-active" : "" %>" href="<%= contextPath %>/legal?type=agreement#user-agreement">User Agreement</a>
            <a class="legal-nav__link <%= "privacy".equals(legalType) ? "is-active" : "" %>" href="<%= contextPath %>/legal?type=privacy#privacy-policy">Privacy Policy</a>
          </nav>

          <section id="user-agreement" class="legal-section">
            <div class="legal-section__header">
              <span class="legal-section__badge">Section A</span>
              <h2 class="legal-section__title">User Agreement</h2>
            </div>
            <p class="legal-section__text">
              This section will later describe account usage expectations, acceptable behavior, role responsibilities, and the rules for using the TA Recruitment System.
            </p>
            <ul class="legal-section__list">
              <li>Account creation and eligibility requirements</li>
              <li>Role-based access and acceptable use expectations</li>
              <li>Submission accuracy and responsibility for uploaded information</li>
              <li>Platform availability, maintenance, and limitation statements</li>
            </ul>
          </section>

          <section id="privacy-policy" class="legal-section">
            <div class="legal-section__header">
              <span class="legal-section__badge">Section B</span>
              <h2 class="legal-section__title">Privacy Policy</h2>
            </div>
            <p class="legal-section__text">
              This section will later explain what personal data is collected, how uploaded files and profile details are used, and how long recruitment-related records are retained.
            </p>
            <ul class="legal-section__list">
              <li>Personal profile information collected during registration</li>
              <li>Resume upload, storage, and review-related processing</li>
              <li>Role-based visibility of applicant and job data</li>
              <li>Data retention, update, and deletion policy placeholders</li>
            </ul>
          </section>

          <div class="legal-card__actions">
            <a class="auth-button auth-button--secondary legal-card__button" href="<%= backHref %>">Return to Previous Page</a>
          </div>
        </div>
      </section>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
  </div>
</body>
</html>
