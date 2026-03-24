# Repository Layer

Purpose:

- abstract file persistence from business logic
- provide read/write operations for JSON files

Main rule:

- repositories should not contain page logic
- repositories should not know JSP or Servlet details
- repositories should return domain objects

Suggested repository groups:

- user repositories
- posting repositories
- application repositories
- file metadata repositories

