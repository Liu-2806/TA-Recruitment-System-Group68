# Service Layer

Purpose:

- hold business rules
- connect controllers to repositories
- keep business logic out of Servlets

Main rule:

- one service should reflect one business area
- services can combine data from multiple repositories
- services should return clean domain objects or DTOs

Suggested split:

- `auth`
- `ta`
- `mo`
- `admin`

