# Auth Services

Responsibilities:

- validate login
- register TA accounts
- create session-ready user identity results
- check duplicate email or student ID during registration

Suggested classes:

- `AuthService`
- `RegistrationService`
- `PasswordService`

Important details:

- password rules should be centralized
- login should not know JSP view paths
- return clear validation errors for controllers to show in the UI

