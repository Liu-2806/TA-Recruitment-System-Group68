# Admin Controllers

Owns pages:

- `admin/dashboard.jsp`
- `admin/create-mo.jsp`
- `admin/all-mos.jsp`
- `admin/all-jobs.jsp`

Suggested Servlet responsibilities:

- load admin dashboard statistics
- create MO account
- list all MO accounts
- reset MO password
- list all jobs in the system
- later load workload overview page

Suggested classes:

- `AdminDashboardServlet`
- `AdminCreateMoServlet`
- `AdminAllMosServlet`
- `AdminResetMoPasswordServlet`
- `AdminAllJobsServlet`
- `AdminWorkloadServlet`

Important details:

- enforce admin-only access
- password generation logic should be reusable
- admin list pages should support keyword and status filters

