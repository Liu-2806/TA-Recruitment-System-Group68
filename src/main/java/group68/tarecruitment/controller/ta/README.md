# TA Controllers

Owns pages:

- `ta/dashboard.jsp`
- `ta/profile.jsp`
- `ta/positions.jsp`
- `ta/position-details.jsp`
- `ta/applications.jsp`

Suggested Servlet responsibilities:

- load dashboard summary
- load and update TA profile
- upload or replace CV
- list available positions
- load one position detail
- submit application
- list TA applications
- handle withdrawal or revocation later

Suggested classes:

- `TaDashboardServlet`
- `TaProfileServlet`
- `TaResumeServlet`
- `TaPositionsServlet`
- `TaPositionDetailsServlet`
- `TaApplyServlet`
- `TaApplicationsServlet`

Important details:

- enforce TA-only access
- block application submission when profile or CV is incomplete
- later add timetable conflict validation before final submit

