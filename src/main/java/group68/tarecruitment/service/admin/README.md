# Admin Services

Responsibilities:

- load dashboard statistics
- create MO accounts
- list and filter all MO accounts
- list and filter all job postings
- generate overview reports later
- reset passwords

Suggested classes:

- `AdminDashboardService`
- `MoAccountAdminService`
- `AdminPostingService`
- `AdminReportService`

Important details:

- dashboard numbers should be computed from system records, not hardcoded
- keep report export logic separate from page rendering

