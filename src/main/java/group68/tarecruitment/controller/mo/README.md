# MO Controllers

Owns pages:

- `mo/dashboard.jsp`
- `mo/profile-edit.jsp`
- `mo/post-position.jsp`
- `mo/postings.jsp`
- `mo/applicants.jsp`
- `mo/applicant-details.jsp`

Suggested Servlet responsibilities:

- load MO dashboard
- load and update MO profile
- create new posting
- list current MO postings
- list applicants for a posting
- load one applicant detail page
- accept or reject applicant

Suggested classes:

- `MoDashboardServlet`
- `MoProfileServlet`
- `MoCreatePostingServlet`
- `MoPostingsServlet`
- `MoApplicantsServlet`
- `MoApplicantDetailsServlet`
- `MoDecisionServlet`

Important details:

- enforce MO-only access
- only allow MO to manage its own postings
- all decision updates must also update TA application status

