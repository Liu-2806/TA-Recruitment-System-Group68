# Controller Layer

Purpose:

- implement Servlet endpoints
- receive page requests and form submissions
- call services
- forward to JSP pages or return JSON

Development rule:

- `doGet` should mainly load page data
- `doPost` should mainly handle create/update/delete actions
- use `request.setAttribute(...)` before forwarding to JSP
- keep controllers thin

Suggested split:

- `auth`
- `ta`
- `mo`
- `admin`

