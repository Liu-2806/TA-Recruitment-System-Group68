# Stage 3 Interface Design

## Date

2026-03-24

## Purpose

This document is the main deliverable for phase 3: interface design and data preparation.

It combines:

- frontend interface specification
- page data dependency mapping
- field naming conventions

The scope of this document is based on the pages already implemented in phase 2.

## 1. Design Scope

Current implemented page groups:

- Auth
- TA
- MO
- Admin

Covered pages:

- `login.jsp`
- `register.jsp`
- `ta/dashboard.jsp`
- `ta/profile.jsp`
- `ta/positions.jsp`
- `ta/position-details.jsp`
- `ta/applications.jsp`
- `mo/dashboard.jsp`
- `mo/profile-edit.jsp`
- `mo/post-position.jsp`
- `mo/postings.jsp`
- `mo/applicants.jsp`
- `mo/applicant-details.jsp`
- `admin/dashboard.jsp`
- `admin/create-mo.jsp`
- `admin/all-mos.jsp`
- `admin/all-jobs.jsp`

## 2. Interface Strategy

Recommended backend style:

- JSP pages are rendered by Servlet controllers
- business data for dynamic parts is provided through JSON endpoints
- file data is stored in JSON and files under the project data directory
- no database is used

Recommended route split:

- page routes: used to open JSP pages
- API routes: used to return JSON data or accept form submissions

Examples:

- page route: `/ta/dashboard`
- API route: `/api/ta/dashboard`

## 3. General Conventions

### 3.1 Request / Response format

Recommended JSON response wrapper:

```json
{
  "success": true,
  "message": "OK",
  "data": {}
}
```

Recommended error format:

```json
{
  "success": false,
  "message": "Validation failed",
  "errors": {
    "email": "Email format is invalid"
  }
}
```

### 3.2 Field naming convention

Use `camelCase` for JSON fields.

Examples:

- `studentId`
- `staffId`
- `courseCode`
- `applicationStatus`
- `requiredSkills`
- `createdAt`

### 3.3 ID convention

Recommended id fields:

- `userId`
- `taId`
- `moId`
- `postingId`
- `applicationId`

All ids should be treated as strings in the interface layer, even if internally generated from numbers.

### 3.4 Time / date convention

Recommended date format:

- date only: `YYYY-MM-DD`
- datetime: `YYYY-MM-DD HH:mm:ss`

Examples:

- `2026-03-30`
- `2026-03-24 14:30:00`

### 3.5 Role values

Recommended role values:

- `TA`
- `MO`
- `ADMIN`

### 3.6 Status values

Recommended application status values:

- `PENDING`
- `ACCEPTED`
- `REJECTED`
- `WITHDRAWN`
- `REVOCATION_REQUESTED`

Recommended posting status values:

- `OPEN`
- `CLOSED`
- `DRAFT`

## 4. Core Data Models

### 4.1 TA Profile

```json
{
  "taId": "TA001",
  "fullName": "Zhang San",
  "studentId": "2021001234",
  "majorProgram": "Software Engineering",
  "academicYear": "Year 3",
  "email": "zhangsan@bupt.edu",
  "skills": ["Java", "Python"],
  "resumeFileName": "resume_zhang_san.pdf",
  "resumeUploadedAt": "2026-03-12 10:20:00"
}
```

### 4.2 MO Profile

```json
{
  "moId": "MO001",
  "fullName": "Prof. Wang",
  "staffId": "M001",
  "email": "wang@bupt.edu",
  "department": "Software Engineering",
  "phone": "123-4567-8901",
  "description": "Main research interests..."
}
```

### 4.3 Job Posting

```json
{
  "postingId": "POST001",
  "courseCode": "SE3001",
  "courseName": "Software Engineering TA",
  "moId": "MO001",
  "moName": "Prof. Wang",
  "vacancies": 3,
  "applicationCount": 5,
  "deadline": "2026-03-30",
  "description": "Assist in lab session teaching...",
  "requiredSkills": ["Java", "Object-Oriented Programming"],
  "estimatedWorkloadHours": 6,
  "status": "OPEN"
}
```

### 4.4 Application

```json
{
  "applicationId": "APP001",
  "postingId": "POST001",
  "postingTitle": "Software Engineering TA",
  "taId": "TA001",
  "taName": "Zhang San",
  "appliedAt": "2026-03-10 11:10:00",
  "status": "PENDING",
  "skillMatchScore": 85,
  "feedback": ""
}
```

## 5. Page Route Recommendations

Recommended page routes:

- `/login`
- `/register`
- `/ta/dashboard`
- `/ta/profile`
- `/ta/positions`
- `/ta/positions/{postingId}`
- `/ta/applications`
- `/mo/dashboard`
- `/mo/profile`
- `/mo/post-position`
- `/mo/postings`
- `/mo/postings/{postingId}/applicants`
- `/mo/applicants/{applicationId}`
- `/admin/dashboard`
- `/admin/mos/create`
- `/admin/mos`
- `/admin/jobs`

## 6. API Specification

### 6.1 Auth module

#### `POST /api/auth/login`

Purpose:

- authenticate user and create session

Request:

```json
{
  "role": "TA",
  "email": "zhangsan@bupt.edu",
  "password": "123456"
}
```

Response data:

- `userId`
- `fullName`
- `role`
- `redirectPath`

#### `POST /api/auth/logout`

Purpose:

- destroy session

#### `GET /api/auth/session`

Purpose:

- check whether current session is valid

Response data:

- `loggedIn`
- `userId`
- `role`
- `fullName`

#### `POST /api/auth/register-ta`

Purpose:

- create TA account

Request fields:

- `fullName`
- `studentId`
- `majorProgram`
- `academicYear`
- `email`
- `password`
- `confirmPassword`

### 6.2 TA module

#### `GET /api/ta/dashboard`

Purpose:

- provide dashboard summary data

Response data:

- `profileSummary`
- `applicationStats`
- `resumeInfo`
- `skills`
- `pendingActionCount`
- `latestPostings`
- `recentApplications`
- `deadlines`
- `alerts`

#### `GET /api/ta/profile`

Purpose:

- load full TA profile

#### `PUT /api/ta/profile`

Purpose:

- update TA profile

Request fields:

- `fullName`
- `majorProgram`
- `academicYear`
- `email`
- `skills`

#### `POST /api/ta/resume`

Purpose:

- upload or replace CV

Request type:

- multipart form data

Main fields:

- `resumeFile`

#### `GET /api/ta/positions`

Purpose:

- list available positions

Query params:

- `keyword`
- `majorProgram`
- `sortBy`

Response data:

- array of posting summaries

#### `GET /api/ta/positions/{postingId}`

Purpose:

- get vacancy details

Response data:

- posting detail
- match analysis summary

#### `POST /api/ta/positions/{postingId}/apply`

Purpose:

- submit application

Request fields:

- `postingId`

Response data:

- `applicationId`
- `status`

#### `GET /api/ta/applications`

Purpose:

- list all applications for current TA

Query params:

- `status`

#### `POST /api/ta/applications/{applicationId}/withdraw`

Purpose:

- withdraw or revoke application

Request fields:

- `reason`

### 6.3 MO module

#### `GET /api/mo/dashboard`

Purpose:

- load MO dashboard data

Response data:

- `profileCard`
- `awaitingReviewCount`
- `alerts`
- `activePostings`
- `recentActivity`

#### `GET /api/mo/profile`

Purpose:

- load MO profile

#### `PUT /api/mo/profile`

Purpose:

- update MO profile

Request fields:

- `fullName`
- `email`
- `department`
- `phone`
- `description`

#### `POST /api/mo/postings`

Purpose:

- create new posting

Request fields:

- `courseName`
- `courseCode`
- `vacancies`
- `deadline`
- `description`
- `requiredSkills`
- `estimatedWorkloadHours`

#### `GET /api/mo/postings`

Purpose:

- list current MO postings

Query params:

- `status`
- `keyword`

#### `GET /api/mo/postings/{postingId}/applicants`

Purpose:

- list applicants for one posting

Response data:

- posting summary
- target recruitment count
- applicant list

#### `GET /api/mo/applications/{applicationId}`

Purpose:

- load applicant detail page

Response data:

- applicant profile
- skill tags
- resume info
- skill match analysis

#### `POST /api/mo/applications/{applicationId}/decision`

Purpose:

- accept or reject applicant

Request fields:

- `decision`
- `feedback`

Allowed decision values:

- `ACCEPT`
- `REJECT`

### 6.4 Admin module

#### `GET /api/admin/dashboard`

Purpose:

- load admin overview dashboard

Response data:

- `totalTas`
- `totalMos`
- `totalPostings`
- `recentActivities`

#### `POST /api/admin/mos`

Purpose:

- create MO account

Request fields:

- `fullName`
- `staffId`
- `email`
- `department`
- `phone`
- `initialPassword`

#### `POST /api/admin/mos/{moId}/reset-password`

Purpose:

- reset MO password

Response data:

- `temporaryPassword`

#### `GET /api/admin/mos`

Purpose:

- list all MO accounts

Query params:

- `keyword`
- `department`

#### `GET /api/admin/jobs`

Purpose:

- list all job postings across system

Query params:

- `courseKeyword`
- `postingMo`
- `status`

#### `GET /api/admin/workload`

Purpose:

- provide workload overview for all TAs

Response data:

- TA workload list
- summary metrics

## 7. Page Data Dependency Table

### Auth pages

- `login.jsp`
  - `POST /api/auth/login`
  - `GET /api/auth/session`

- `register.jsp`
  - `POST /api/auth/register-ta`

### TA pages

- `ta/dashboard.jsp`
  - `GET /api/ta/dashboard`

- `ta/profile.jsp`
  - `GET /api/ta/profile`
  - `PUT /api/ta/profile`
  - `POST /api/ta/resume`

- `ta/positions.jsp`
  - `GET /api/ta/positions`

- `ta/position-details.jsp`
  - `GET /api/ta/positions/{postingId}`
  - `POST /api/ta/positions/{postingId}/apply`

- `ta/applications.jsp`
  - `GET /api/ta/applications`
  - `POST /api/ta/applications/{applicationId}/withdraw`

### MO pages

- `mo/dashboard.jsp`
  - `GET /api/mo/dashboard`

- `mo/profile-edit.jsp`
  - `GET /api/mo/profile`
  - `PUT /api/mo/profile`

- `mo/post-position.jsp`
  - `POST /api/mo/postings`

- `mo/postings.jsp`
  - `GET /api/mo/postings`

- `mo/applicants.jsp`
  - `GET /api/mo/postings/{postingId}/applicants`

- `mo/applicant-details.jsp`
  - `GET /api/mo/applications/{applicationId}`
  - `POST /api/mo/applications/{applicationId}/decision`

### Admin pages

- `admin/dashboard.jsp`
  - `GET /api/admin/dashboard`

- `admin/create-mo.jsp`
  - `POST /api/admin/mos`
  - `POST /api/admin/mos/{moId}/reset-password`

- `admin/all-mos.jsp`
  - `GET /api/admin/mos`
  - `POST /api/admin/mos/{moId}/reset-password`

- `admin/all-jobs.jsp`
  - `GET /api/admin/jobs`

## 8. Recommended File Storage Mapping

Suggested file structure for backend data:

- `data/users/ta.json`
- `data/users/mo.json`
- `data/users/admin.json`
- `data/postings/postings.json`
- `data/applications/applications.json`
- `data/resumes/`

Suggested relation keys:

- TA profile links to resume by `resumeFileName`
- Application links TA and posting by `taId` and `postingId`
- Posting links MO by `moId`

## 9. Current Phase 3 Deliverable Status

This document completes the first concrete output of phase 3:

- interface design draft
- page data dependency mapping
- field naming conventions

Later phase 3 work can continue with:

- validating field names with teammates
- refining request/response schemas
- mapping these contracts into actual Servlet controllers
