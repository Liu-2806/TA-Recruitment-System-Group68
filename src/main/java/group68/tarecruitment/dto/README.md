# DTO Layer

Purpose:

- define input and output structures between controllers and services
- reduce direct exposure of domain models in form handling

Suggested subfolders:

- `request`
- `response`

Use DTOs when:

- request fields differ from stored model fields
- one page combines data from multiple entities

