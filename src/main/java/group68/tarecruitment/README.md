# Backend Structure Overview

This package is the main backend root for the TA Recruitment System.

Suggested responsibility split:

- `config`: application constants and path configuration
- `controller`: Servlet layer
- `service`: business logic
- `repository`: JSON file access
- `model`: domain entities
- `dto`: request/response transfer objects
- `validation`: form and request validation
- `security`: session and role checks
- `util`: shared helper code

Development rule:

- controllers should not contain complex business logic
- services should not render JSPs
- repositories should focus on file persistence only

