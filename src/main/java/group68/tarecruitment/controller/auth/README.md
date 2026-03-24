# Auth Controllers

Owns pages:

- `login.jsp`
- `register.jsp`

Suggested Servlet responsibilities:

- open login page
- open register page
- handle TA registration submission
- handle login submission
- handle logout
- check active session

Suggested classes:

- `LoginServlet`
- `RegisterTaServlet`
- `LogoutServlet`
- `SessionCheckServlet`

Important details:

- login must validate role, email, and password
- successful login should create session attributes for `userId`, `role`, and `fullName`
- redirect target should depend on role

