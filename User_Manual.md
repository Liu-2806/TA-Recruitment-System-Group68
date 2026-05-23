# TA Recruitment System (Group 68) - User Manual

## Table of Contents

1. [System Overview](#1-system-overview)
2. [Quick Start](#2-quick-start)
3. [Common Features](#3-common-features)
4. [TA User Manual](#4-ta-user-manual)
5. [MO User Manual](#5-mo-user-manual)
6. [Admin User Manual](#6-admin-user-manual)
7. [Intelligent Matching System](#7-intelligent-matching-system)
8. [System Deployment and Operations Guide](#8-system-deployment-and-operations-guide)
9. [Frequently Asked Questions (FAQ)](#9-frequently-asked-questions-faq)
10. [Appendix A: URL Routing Table](#appendix-a-complete-url-routing-table)
11. [Appendix B: Data File Structures](#appendix-b-data-file-structures)
12. [Appendix C: Application Status Flow Diagram](#appendix-c-application-status-flow-diagram)

---

## 1. System Overview

### 1.1 System Introduction

TA Recruitment System (Group 68) is a web application designed for Teaching Assistant (TA) recruitment in university courses. The system connects three types of users—Teaching Assistant applicants (TA), Module Organizers (MO), and System Administrators (Admin)—covering the complete recruitment workflow from job posting, resume submission, intelligent matching, to manual review.

The system complies with the EBU6304 course project requirements: using the Java + Servlet/JSP technology stack, JSON text files for data storage, and no relational databases.

### 1.2 Technology Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Backend Framework | Java Servlet + JSP | Servlet 4.0 / JSP 2.3 |
| Runtime Container | Apache Tomcat | 9.x+ |
| Data Serialization | Gson | 2.11.0 |
| PDF Processing | Apache PDFBox | 3.0.2 |
| Frontend | HTML5 + CSS3 + JSTL | -- |
| Data Storage | JSON text files | -- |
| Optional AI Service | OpenAI Compatible API (DeepSeek, etc.) | -- |
| Runtime Environment | JDK | 21 |

### 1.3 System Architecture

```
┌─────────────────────────────────────────────────────┐
│                    Browser                            │
└──────────────────────┬──────────────────────────────┘
                       │ HTTP Request
┌──────────────────────▼──────────────────────────────┐
│                   Tomcat Container                    │
│  ┌────────────────────────────────────────────────┐  │
│  │              controller/ (Servlet)              │  │
│  │  auth/ │ ta/ │ mo/ │ admin/ │ common/          │  │
│  └──────────────────┬─────────────────────────────┘  │
│                     │ calls                           │
│  ┌──────────────────▼─────────────────────────────┐  │
│  │            service/impl/ (Business Layer)       │  │
│  │  AuthService │ JobService │ ApplicationService  │  │
│  │  ResumeService │ ProfileService │ Analytics     │  │
│  │  NotificationService │ RecommendationService   │  │
│  └──────┬──────────────────────┬──────────────────┘  │
│         │                      │                     │
│  ┌──────▼──────┐    ┌──────────▼────────────────┐    │
│  │ repository/  │    │ resume/ │ match/          │    │
│  │ file/ (DAL)  │    │ PDF Extract │ LLM/Local   │    │
│  └──────┬───────┘    │           Rule Matcher    │    │
│         │             └──────────────────────────┘    │
│  ┌──────▼───────┐                                   │
│  │  data/ (JSON) │                                   │
│  │  users/ │ postings/ │ applications/ │ resumes/   │  │
│  │  notifications/ │ system/                        │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────┘
```

### 1.4 Role and Feature Comparison

| Feature | TA (Applicant) | MO (Module Organizer) | Admin (System Admin) |
|---------|:---:|:---:|:---:|
| Register Account | ✅ | ❌ (Created by Admin) | ❌ |
| Login System | ✅ | ✅ | ✅ |
| Manage Profile | ✅ | ✅ | ❌ |
| Upload/Download Resume (PDF) | ✅ | ❌ | ❌ |
| Browse and Search Positions | ✅ | ❌ | Monitor |
| View Position Match Analysis | ✅ | ✅ | ❌ |
| Submit Application | ✅ | ❌ | ❌ |
| Withdraw Application / Revocation | ✅ | ❌ | ❌ |
| View My Applications | ✅ | ❌ | ❌ |
| Create/Edit/Close Positions | ❌ | ✅ | ❌ |
| View Applicant List | ❌ | ✅ | ❌ |
| Review Applications (Accept/Reject) | ❌ | ✅ | ❌ |
| Handle Revocation Requests | ❌ | ✅ | ❌ |
| Review Queue Management | ❌ | ✅ | ❌ |
| Cascade Withdrawal (on edit) | ❌ | ✅ | ❌ |
| System Overview Dashboard | ❌ | ✅ (MO scope) | ✅ (Global) |
| MO Account Management | ❌ | ❌ | ✅ |
| TA Workload Analysis | ❌ | ❌ | ✅ |
| Global Position Monitoring | ❌ | ❌ | ✅ |
| Receive Notifications | ✅ | ✅ | ❌ |

### 1.5 Module Architecture

```
┌────────────────┐   ┌────────────────┐   ┌────────────────┐
│  Auth Module    │   │  Input Module  │   │  Match Module   │
│  login/logout   │   │  resume upload │   │  LLM + local    │
│  register       │   │  PDF extract   │   │  rule matcher   │
│  role select    │   │  structure     │   │  score/explain  │
└────────────────┘   └────────────────┘   └────────────────┘

┌────────────────┐   ┌────────────────┐   ┌────────────────┐
│ Apply Module   │   │ Review Module  │   │ Notify Module  │
│ apply/withdraw │   │ accept/reject  │   │ 7 notif. types │
│ revocation     │   │ review queue   │   │ real-time push │
│ status track   │   │ inline decision│   │ action feedback│
└────────────────┘   └────────────────┘   └────────────────┘

┌────────────────┐   ┌────────────────┐   ┌────────────────┐
│ Admin Module   │   │ Analytics Mod. │   │ Common UI Mod. │
│ MO management  │   │ TA workload    │   │ layout/sidebar │
│ job monitoring │   │ system overview│   │ toast/pagination│
│ password reset │   │ distribution   │   │ responsive     │
└────────────────┘   └────────────────┘   └────────────────┘
```

---

## 2. Quick Start

### 2.1 Environment Requirements

| Item | Requirement | Download Link |
|------|------------|---------------|
| Operating System | Windows 10 / 11 | -- |
| JDK | 21 | https://adoptium.net/temurin/releases/?version=21 |
| Servlet Container | Apache Tomcat 9.x+ | https://tomcat.apache.org/download-90.cgi |
| PowerShell | 5.x+ | https://github.com/PowerShell/PowerShell/releases |
| Encoding | UTF-8 | -- |

**Project Dependency JARs (included under `lib/`):**

- `lib/pdfbox-app-3.0.2.jar` -- PDF text extraction
- `lib/gson-2.11.0.jar` -- JSON serialization/deserialization
- `lib/javax.servlet-api-4.0.1.jar` -- Servlet API

### 2.2 Startup Steps

#### Method 1: Using Deployment Scripts (Recommended)

1. Clone or pull the project locally, e.g., to `{PROJECT_ROOT}`.
2. Ensure JDK 21 is installed; `java -version` should output 21.x.
3. Run the deployment scripts:

```powershell
cd {PROJECT_ROOT}
powershell -ExecutionPolicy Bypass -File .\scripts\deploy-to-local-tomcat.ps1
powershell -ExecutionPolicy Bypass -File .\scripts\start-local-tomcat.ps1
```

4. Access in browser: `http://localhost:8080/TA-Recruitment-System-Group68/`

#### Method 2: Using Run Script

```powershell
cd {PROJECT_ROOT}
powershell -ExecutionPolicy Bypass -File .\scripts\run-web-app.ps1
```

#### Method 3: IDE Manual Deployment

1. Configure Tomcat run configuration in your IDE (e.g., IntelliJ IDEA).
2. Set the Artifact deployment directory to point to `web/`.
3. Compile Java source code under `src/com/bupt/ta/`, output to `WEB-INF/classes/`.
4. Start Tomcat and access the URL above.

### 2.3 Test Accounts

| Role | Username | Password | User ID | Description |
|------|----------|----------|---------|-------------|
| TA | syy | 1234567890syy | TA001 | Resume uploaded, has applications |
| TA | zjy | 1234567890zjy | TA002 | Resume uploaded, has applications |
| MO | mo_demo | Temp123! | MO001 | Prof. Wang, has multiple positions |
| MO | 18540371119 | 111111 | MO002 | Puze Wang |

> **Note:** These are test environment accounts. In production, passwords should be encrypted and more secure authentication strategies should be used.

### 2.4 Development Debug URLs

In the local development environment, the system provides shortcut login entries (for development and debugging only; should be disabled in production):

| URL | Description |
|-----|-------------|
| `/dev/login-as?role=TA` | Direct login as TA (TA001), redirect to profile page |
| `/dev/login-as?role=MO` | Direct login as MO (MO001), redirect to applicant list |
| `/dev/login-as?role=ADMIN` | Direct login as Admin, redirect to admin dashboard |
| `/dev/login-as?role=TA&redirect=/ta/jobs` | Custom post-login redirect path |

---

## 3. Common Features

### 3.1 Login

- **Entry:** The system home page automatically redirects to `/auth/login`.
- **Process:**
  1. Select role (TA / MO / Admin).
  2. Enter username (or email/student ID/staff ID) and password.
  3. System validates credentials and creates a session on success.
  4. Automatically redirects to the corresponding home page based on role (TA Dashboard / MO Dashboard / Admin Dashboard).
- **Failure Handling:** If the password is incorrect or the role does not match, an error message is displayed and the form data is preserved.
- **Session Management:** Session timeout is set to 30 minutes. Old sessions are automatically invalidated upon new login.

![Login Page](截图1/sys/7ab2bb50dd263f61443c89fc0fd3e426.png)

*Figure 3-1: System login page, supporting TA / MO / Admin three role login*

### 3.2 Registration (TA Only)

- **Entry:** "Register as TA" link at the bottom of the login page.
- **Required Fields:** Username, email, password (with strength validation), confirm password.
- **Optional Fields:** Full name, student ID, major, academic year, phone, intro.
- **Skill Tags:** Select from a predefined list (Java, Python, Git, Testing, Communication, SQL, Machine Learning, Data Analysis), multiple selections supported.
- **Validation Rules:**
  - Username must be unique
  - Email format validation
  - Passwords must match
  - Password should be at least 8 characters
- **After Completion:** Auto-login and redirect to TA Dashboard.

![TA Registration Page](截图1/sys/e20bf14efa982202ef1d49c36406a819.png)

*Figure 3-2: TA registration page, including basic info, skill tag selection, and password strength validation*

### 3.3 Logout

- **Location:** Top-right corner of the page, user avatar/username area, click "Logout".
- **Behavior:** Destroys the current session and redirects to the login page.

### 3.4 Notification System

The system supports **7 notification types**, displayed via a bell icon in the top-right corner, with unread counts marked by a red dot:

| Notification Type | Recipient | Trigger | Action Required |
|-------------------|-----------|---------|:---:|
| `NEW_APPLICATION` | MO | TA submitted a new application | ❌ |
| `APPLICATION_ACCEPTED` | TA | MO accepted the application | ❌ |
| `APPLICATION_REJECTED` | TA | MO rejected the application | ❌ |
| `APPLICATION_AUTO_WITHDRAWN` | TA | MO edited position, causing cascade withdrawal | ❌ |
| `REVOCATION_REQUEST` | MO | TA submitted a revocation request for an accepted position | ✅ (Approve/Reject) |
| `REVOCATION_APPROVED` | TA | MO approved the revocation request | ❌ |
| `REVOCATION_REJECTED` | TA | MO rejected the revocation request | ❌ |

**Notification Interaction:**
- Click the bell icon to expand the notification list.
- Each notification shows title, message content, and timestamp.
- Click the dismiss button next to a notification to mark as read and remove it.
- Notifications requiring action (e.g., revocation requests) display "Approve" / "Reject" buttons directly in the notification card.

![Notification System](截图1/sys/762b1c83d794e48de97130e353b1ea4b.png)

*Figure 3-3: Notification system, click the bell icon to expand the notification list, supporting read marking and inline actions*

### 3.5 Page Layout

The system uses a unified layout system:

```
┌──────────────────────────────────────────────┐
│  Header: Logo | Nav | Notification Bell | User │
├────────┬─────────────────────────────────────┤
│        │                                     │
│ Sidebar│  Main Content Area                  │
│ (opt.) │  (JSP page body)                    │
│        │                                     │
├────────┴─────────────────────────────────────┤
│  Footer: Copyright | Legal Links              │
└──────────────────────────────────────────────┘
```

- **Layout Template:** `web/WEB-INF/views/common/layout.jsp`
- **Sidebar:** `sidebar.jsp` -- dynamically displays different navigation items based on role
- **CSS Resources:** `web/assets/css/` contains base.css, layout.css, components.css, and page-specific styles

### 3.6 Toast Message Feedback

The system uses Toast components to provide instant feedback on operation results:

- **Success Toast:** Green style, used for successful operations (e.g., "Profile saved", "Application submitted").
- **Error Toast:** Red style, used for failed operations (e.g., "Upload failed: invalid PDF").
- **Position:** Top-right corner of the page, auto fade-in/fade-out.
- **Implementation:** Uses the `FlashMessages` utility class to temporarily store messages in the Session, which JSP pages read and display when rendering.

### 3.7 Pagination

Multiple list pages in the system support pagination:

- **Default per page:** 10 records (6 for TA application list).
- **Pagination Parameters:** `page` (page number) and `size` (items per page).
- **UI Display:** Bottom of the page shows "Showing X-Y of Z" with page number navigation buttons.
- **Implementation Class:** `com.bupt.ta.dto.PageResult` encapsulates pagination data.

---

## 4. TA User Manual

### 4.1 TA Dashboard

**Access Path:** Auto-redirect after login, or `/ta/dashboard`

The dashboard is the TA user's work center, containing the following modules:

#### 4.1.1 Personal Information Card

Displays the TA's basic information:
- Name, student ID, major, academic year
- Resume upload status (uploaded/not uploaded), with file name and upload time
- Click "Edit Profile" to navigate to the profile editing page

#### 4.1.2 Application Statistics

Three statistics cards:
- **Pending (Under Review):** Number of applications with status SUBMITTED or REVOCATION_REQUESTED
- **Accepted:** Number of applications accepted by MOs
- **Rejected:** Number of applications rejected by MOs

#### 4.1.3 Recent Applications

Displays the 3 most recent application records, including:
- Position name and course code
- Application status (with color tag)
- Application time
- Match score

#### 4.1.4 Recommended Positions

Displays the 3 most recent open positions not yet applied to:
- Position name, course code
- Deadline
- Match score preview
- Click to view details and apply

#### 4.1.5 Timetable/Schedule

Displays the current week's TA work schedule:
- **Course TA Assignments:** Fixed teaching periods for accepted positions (from `ta-timetable.json`)
- **Activity Events:** Dates, times, and locations for one-time activity positions (invigilation, project review, etc.)
- Displays "No ongoing TA assignment" when no positions are accepted

![TA Dashboard](截图1/ta/2a6983268410fe5dc800ec2b8cdd29a8.png)

*Figure 4-1: TA Dashboard, showing personal info, application statistics, recent applications, recommended positions, and timetable*

### 4.2 Profile Management

**Access Path:** `/ta/profile`

#### 4.2.1 Basic Information Editing

Editable fields:
- Full Name
- Student ID
- Email
- Phone
- Major Program
- Academic Year
- Intro -- multiline text

#### 4.2.2 Skill Tag Management

- Select multiple skills from the predefined list
- Current predefined skills: Java, Python, Git, Testing, Communication, SQL, Machine Learning, Data Analysis
- Selected skills are displayed as tags
- After saving, skill information is used for position matching analysis

#### 4.2.3 Resume Upload

**Upload Process:**

```
TA selects PDF file
    │
    ▼
File Validation ──┤ Format: .pdf only
                  │ Size: ≤ 5MB
                  │ Content: non-empty
    │
    ▼ (validation passed)
Save to data/resumes/{TA_ID}_{timestamp}_resume_{original filename}.pdf
    │
    ▼
PDFBox extracts text ──┤ Failure → keep original file, show warning
                       │ Success → continue
    │
    ▼
Resume structuring ──┤ Extract: name, email, phone,
                     │          education, skills,
                     │          experienceHighlights, rawText
    │
    ▼
Write to extractedResume field in data/users/ta.json
    │
    ▼
Page refreshes, showing success Toast and structured resume info
```

**Resume Display:**
After successful upload, the profile page displays:
- Resume file name
- Upload time
- Structured resume information (name, email, phone, education, skill list, experience highlights)
- Raw text preview
- Download button

**Re-upload:** Supports overwriting existing resumes; old files are retained on disk but the database points to the new file.

![TA Profile and Resume Upload](截图1/ta/e71314dd8d91921faf3b10ce783add31.png)

*Figure 4-2: TA Profile page, supporting basic info editing, skill tag management, and PDF resume upload*

#### 4.2.4 Resume Download

- Click the "Download Resume" button to download the original uploaded PDF file.
- Corresponding endpoint: `GET /ta/resume/download`

### 4.3 Position Browsing

**Access Path:** `/ta/jobs`

#### 4.3.1 Search and Filter

The system provides multi-dimensional search and filtering:

| Filter Option | Parameter | Description |
|---------------|-----------|-------------|
| Keyword Search | `keyword` | Search position name, course code, description |
| Major Filter | `major` | Filter by related major |
| Department Filter | `department` | Filter by department |
| Module Type | `moduleType` | Course TA / Activity |
| Responsibility Keyword | `responsibilityKeyword` | Search responsibility description |

#### 4.3.2 Sorting

| Sort Option | Parameter Value | Description |
|-------------|-----------------|-------------|
| Latest | `latest` (default) | Reverse chronological by posting time |
| Deadline Soon | `deadline-asc` | Deadline from nearest to farthest |
| Match Score High to Low | `match-desc` | Descending by intelligent match score |
| Position Name A-Z | `name-asc` | Alphabetical order |

#### 4.3.3 Position Card List

Each position card displays:
- Course code and position name
- MO name
- Open status and deadline
- Vacancies
- Estimated Workload Hours
- Match score tag (Strong Match / Moderate Match / Weak Match)
- Required skills list (displayed as tags)
- Position type (TA / Activity)
- "View Details" button

Positions already applied to show an "Already Applied" marker on the card.

#### 4.3.4 Empty State

When no matching positions are found, an empty state prompt is displayed:
- "No open positions match your current filters."
- Suggestions to clear filters or improve resume for better matching results.

![Position Browsing](截图1/ta/ec646829d392f5b653162c551d2b2407.png)

*Figure 4-3: Position browsing page, supporting keyword search, multi-condition filtering, and multiple sort options*

### 4.4 Position Details and Intelligent Matching

**Access Path:** `/ta/jobs/detail?jobId=POST001`

#### 4.4.1 Position Information

Complete position details display:
- Course code, course name
- MO name
- Position description (responsibility details)
- Required skills list
- Vacancies
- Estimated workload hours
- Deadline
- Position type (TA / Activity)
- For Activity type, additionally displays: activity type, date, start/end time, location

#### 4.4.2 Intelligent Matching Analysis

The system automatically matches the TA's resume against the position, displaying:

**Match Score Area:**
- Large percentage score (0-100)
- Match level tag:
  - `Strong Match` (≥ 80%) -- green
  - `Moderate Match` (60-79%) -- yellow
  - `Weak Match` (< 60%) -- orange/red

**Analysis Details:**
- **Strength Summary:** Evidence of the best-matched skills
- **Matched Skills:** Skills that overlap with position requirements
- **Risk Summary:** Missing or unclear skills
- **Missing Skills:** Required skills not found in the resume
- **Next Step Suggestion:** Recommended actions based on the score
- **Confidence Hint:** Reliability explanation of the match result
- **Method Label:** AI-assisted review / Rule-based review / Fallback rule review

![Position Details and Intelligent Matching](截图1/ta/1c6398a278f758657080ca0cfc14b39b.png)

*Figure 4-4: Position details page, showing complete position information and intelligent matching analysis results*

#### 4.4.3 Application Entry

At the bottom of the position details page, an "Apply Now" button is provided:
- Positions already applied to show "Already Applied" and cannot be applied to again
- Clicking redirects to the application confirmation page

### 4.5 Application Process

#### 4.5.1 Application Confirmation Page

**Access Path:** `/ta/applications/confirm?jobId=POST001`

The confirmation page displays:
- Position information summary
- Match score summary
- Pre-application checklist:
  - [ ] I have read the position requirements
  - [ ] I have uploaded my latest resume
  - [ ] My skills match the position requirements
  - [ ] I can fulfill the responsibilities of this position on time
- **Statement:** Optional personal statement explaining application motivation and strengths
- **Time Conflict Detection:** The system automatically checks for time conflicts with accepted positions (based on `ta-timetable.json`)
  - If a conflict exists, a warning message is displayed on the page
- Action buttons: "Submit Application" / "Cancel"

![Application Confirmation Page](截图1/ta/bc2cea4fb8f72fa1d605c823349634e6.png)

*Figure 4-5: Application confirmation page, showing position info, match score, checklist, and personal statement input*

#### 4.5.2 Submit Application

**Access Path:** `POST /ta/applications/submit`

After submission, the system:
1. Validates application legality (position exists and is open, no duplicate applications)
2. Records application time
3. Performs intelligent matching analysis and stores results in the application record
4. Updates the position's `applicationCount`
5. Sends `NEW_APPLICATION` notification to the MO
6. Redirects to "My Applications" page and displays a success Toast

### 4.6 My Applications

**Access Path:** `/ta/applications/my`

#### 4.6.1 Application List

The list displays all application records, each containing:
- Application ID
- Position name and course code
- MO name
- Application time
- Current status (SUBMITTED / UNDER_REVIEW / ACCEPTED / REJECTED / WITHDRAWN / REVOCATION_REQUESTED)
- Match score
- Status tag colors:
  - Pending Review: blue
  - Accepted: green
  - Rejected: red
  - Withdrawn: grey
  - Revocation Requested: orange

![My Applications](截图1/ta/3d576283c27125f864268ce349b11d0e.png)

*Figure 4-6: My Applications list page, showing all application records with their status, match scores, and action buttons*

#### 4.6.2 Application Details (Inline Expansion)

Clicking on an application expands its details:
- Complete position information
- Personal statement
- MO feedback (if any)
- Match analysis details (score, matched/missing skills, explanation)
- Action buttons (varies by status)

#### 4.6.3 Withdraw Application

- **Applicable Status:** SUBMITTED (under review)
- **Action:** Click the "Withdraw" button, confirm to execute
- **Effect:**
  - Application status changes to WITHDRAWN
  - Position `applicationCount` decreases by 1
  - Action is irreversible

#### 4.6.4 Revocation Request

- **Applicable Status:** ACCEPTED
- **Process:**
  1. Click "Request Revocation" button
  2. Enter revocation reason (required)
  3. After confirmation, application status changes to REVOCATION_REQUESTED
  4. MO receives `REVOCATION_REQUEST` notification (requires action)
  5. Wait for MO review:
     - If MO approves: status becomes WITHDRAWN, timetable slots are released, TA receives `REVOCATION_APPROVED` notification
     - If MO rejects: status reverts to ACCEPTED, TA receives `REVOCATION_REJECTED` notification

### 4.7 Complete Operation Example

**Scenario:** A new TA user completes registration, uploads a resume, browses positions, and submits an application.

1. **Register Account**
   - Visit `/auth/login`, click "Register as TA"
   - Fill in username `zhangsan`, email, password
   - Select skill tags: Java, Python, Git
   - Click "Register", auto-login to Dashboard

2. **Complete Profile and Upload Resume**
   - Click "Edit Profile" on the Dashboard
   - Fill in name "Zhang San", student ID "2023210001", major "Computer Science"
   - Select a local PDF resume file to upload
   - System automatically extracts and displays resume information
   - Click "Save Profile" to save

3. **Browse Positions**
   - Navigate to "Browse Positions" (`/ta/jobs`)
   - Enter "software" in the search box to filter software engineering related positions
   - Sort by "Match Score" to view the best-matched positions

4. **View Position Details**
   - Click "View Details" for "Software engineering" (POST001)
   - View position description and required skills
   - View intelligent matching analysis: score 57%, Moderate Match
   - Read match explanation and missing skill suggestions

5. **Submit Application**
   - Click "Apply Now" to enter the confirmation page
   - Check all checklist items
   - Fill in personal statement: "I have strong Java skills and previous TA experience."
   - Click "Submit Application"
   - System prompts "Application submitted successfully"

6. **View Application Status**
   - Navigate to "My Applications"
   - View the newly submitted application, status is "Pending Review"
   - Wait for MO review

---

## 5. MO User Manual

### 5.1 MO Dashboard

**Access Path:** `/mo/dashboard`

The MO dashboard provides an overview of the recruitment work managed by this MO:

#### 5.1.1 Personal Information Card

- MO name, staff ID, department
- Contact email and phone
- "Edit Profile" entry

#### 5.1.2 Key Metrics

- **Awaiting Review:** Total number of applications with SUBMITTED status across all positions
- **Active Postings:** Number of positions with OPEN status
- **Deadline Alerts:** Warnings for open positions closing within 3 days

#### 5.1.3 Active Position List

Displays all OPEN positions for this MO:
- Course code, course name
- Vacancies / applicants
- Deadline
- Click to view applicant list or edit position

#### 5.1.4 Recent Activity

Displays the 5 most recent activities related to this MO:
- New application submissions
- Position creation/updates
- Application review results
- Each activity shows time, related position, and TA name

![MO Dashboard](截图1/mo/721f769baca0603ed12218f9d2cee0bd.png)

*Figure 5-1: MO Dashboard, showing personal info, key metrics, active positions, and recent activity*

### 5.2 Position Management

**Access Path:** `/mo/jobs` (list), `/mo/jobs/create` (create), `/mo/jobs/edit?jobId=xxx` (edit)

#### 5.2.1 Position List

Displays all positions created by this MO, supporting:
- Status filter (Open / Closed / All)
- Keyword search
- Sorting (post date, deadline, name)
- Paginated browsing

Each position card has action buttons:
- **View Applicants:** View the applicant list
- **Edit:** Edit position information
- **Close / Reopen:** Close or reopen a position

![MO Position Management List](截图1/mo/378e6c14c434676dd559c4e66b9fb372.png)

*Figure 5-2: MO position management list, showing all positions with search, filter, and sort capabilities*

#### 5.2.2 Create Position

**Form Fields:**

| Field | Type | Required | Description |
|-------|------|:---:|-------------|
| Course Code | Text | ✅ | Course code, e.g., X5414 |
| Course Name | Text | ✅ | Course/position name |
| Description | Multiline text | ✅ | Detailed position description |
| Required Skills | Multi-select tags | ✅ | Required skills (from predefined list or manual input) |
| Vacancies | Number | ✅ | Number of vacancies |
| Estimated Workload Hours | Number | ✅ | Estimated weekly workload hours |
| Deadline | Date | ✅ | Application deadline |
| Posting Type | Dropdown | ✅ | TA (course TA) / ACTIVITY (activity) |

**Fields shown only when Posting Type = ACTIVITY:**

| Field | Type | Required | Description |
|-------|------|:---:|-------------|
| Activity Type | Dropdown | ✅ | project-assessment / invigilation / lab-support / other |
| Activity Date | Date | ✅ | Activity date |
| Start Time | Time | ✅ | Start time |
| End Time | Time | Optional | End time |
| Location | Text | ✅ | Activity location |

After creation, the position status defaults to OPEN, making it visible and available for TA applicants.

![Create Position](截图1/mo/d5b5dd32ec5d97ff81869bca8abee8d8.png)

*Figure 5-3: Create position form, including course info, required skills, vacancies, and other required fields*

#### 5.2.3 Edit Position

The edit page has the same form as the create page, additionally displaying:
- Position ID
- Creation time and last update time
- Current applicant count

**Cascade Withdrawal:**
When the MO edits key position information (position description or required skills), the system automatically:
1. Withdraws all submitted applications (SUBMITTED status) to WITHDRAWN
2. Resets the position `applicationCount` to zero
3. Sends `APPLICATION_AUTO_WITHDRAWN` notifications to affected TAs
4. The notification states: "Your application was automatically withdrawn because the module organizer updated the posting. Please review the new requirements and reapply if you remain eligible."

> **Note:** Cascade withdrawal is only triggered when description or skill fields are modified. Changes to non-core fields such as deadline or vacancy count do not trigger it.

![Edit Position](截图1/mo/4dfe4692a5f40e36deead95352e79f46.png)

*Figure 5-4: Edit position page, same form as creation, additionally showing position ID and current applicant count*

### 5.3 Application Review

**Access Path:** `/mo/jobs/applicants?jobId=POST001` (applicant list), `/mo/applicants/detail?applicationId=APP001` (application details)

#### 5.3.1 Applicant List

Displays all applicants for a single position:

**List Information:**
- TA name and email
- Application time
- Match score (with color tag)
- Match level (Strong / Moderate / Weak)
- Application status
- Inline action buttons: Accept / Reject / View Details

**Sorting:**
- Default: descending by match score, helping MOs prioritize high-match applicants

**Inline Decisions:**
- Click "Accept" or "Reject" directly on the list row
- Optionally fill in review feedback
- Decisions take effect immediately, sending corresponding notifications to TAs
- List status refreshes in real time

![Applicant List](截图1/mo/d5be790a996c05d53b1af2f7f8d35eb3.png)

*Figure 5-5: Applicant list page, sorted by match score, supporting inline Accept/Reject decisions*

#### 5.3.2 Application Details

Click "View Details" to enter the application details page, which displays:

**Applicant Information:**
- Name, student ID, email, phone
- Major, academic year
- Personal intro

**Position Information:**
- Position name, course code
- Position description, required skills

**Match Analysis:**
- Match score (large display)
- Match level tag
- Strength Summary
- Matched Skills
- Risk Summary
- Missing Skills
- Skill Match Explanation (full explanation)
- Method Label (matching method source)
- Confidence Hint

**Resume Download:**
- Provides a link to download the original PDF resume
- MOs can combine AI matching results with the original resume for comprehensive judgment

**Review Actions:**
- Accept button: Accept the application, status becomes ACCEPTED
- Reject button: Reject the application, status becomes REJECTED
- Feedback input: Review comments (optional, visible to the TA)

![Application Details](截图1/mo/00d841e7059b5e41b1f405e3a17ca1ad.png)

*Figure 5-6: Application details page, showing applicant info, match analysis, resume download, and review actions*

### 5.4 Review Queue

**Access Path:** `/mo/review-queue`

The review queue aggregates all pending applications (SUBMITTED status) across all positions under this MO:

**Queue Overview:**
- Total pending applications
- Number of positions involved
- Sorted by application time (descending)

**Each Application Shows:**
- TA name and email
- Position name and course code
- Match score
- Priority tags:
  - High (match score ≥ 80) -- red highlight
  - Medium (match score 60-79) -- yellow
  - Low (match score < 60) -- green
- Submission time (date + time)
- Action button: View Details

**Purpose:**
- Suitable for MOs to centrally batch-review all pending applications
- No need to enter each position individually
- Priority tags help MOs quickly identify high-match applicants

![Review Queue](截图1/mo/71a9d456739687a5c4546d7c0dd9dec9.png)

*Figure 5-7: Review queue page, aggregating all pending applications with priority labeling*

### 5.5 Revocation Handling

**Trigger Scenario:** TA submits a revocation request for an accepted position.

**Processing Flow:**

```
TA submits revocation request (with reason)
    │
    ▼
Application status → REVOCATION_REQUESTED
    │
    ▼
MO receives REVOCATION_REQUEST notification (requiresAction = true)
    │
    ▼
MO views request details in notification popup
    │
    ├─→ Approve
    │     │
    │     ▼
    │   Application status → WITHDRAWN
    │   Position applicationCount decreases by 1
    │   Release TA timetable slots (ta-timetable.json)
    │   TA receives REVOCATION_APPROVED notification
    │
    └─→ Reject
          │
          ▼
        Application status reverts → ACCEPTED
        TA receives REVOCATION_REJECTED notification
        TA continues as TA for this position
```

MOs can also see all pending revocation requests in the notification list and handle them directly via the Approve/Reject buttons on the notification cards.

### 5.6 MO Profile Management

**Access Path:** `/mo/profile`

Editable fields:
- Full Name
- Staff ID
- Email
- Phone
- Department
- Description

### 5.7 Complete Operation Example

**Scenario:** MO creates a new position and reviews received applications.

1. **View Dashboard**
   - Login and navigate to `/mo/dashboard`
   - View pending applications: 3
   - View deadline alerts: 1 position closing soon

2. **Create New Position**
   - Navigate to "My Postings" (`/mo/jobs`)
   - Click "Create New Position"
   - Fill in the form:
     - Course Code: EBU6304
     - Course Name: Software Engineering Lab TA
     - Description: Assist with weekly lab sessions...
     - Required Skills: Java, Git, Communication, Testing
     - Vacancies: 4
     - Workload Hours: 5
     - Deadline: 2026-06-01
     - Posting Type: TA
   - Click "Publish", position is successfully published

3. **View Applicants**
   - Click "View Applicants" for a position in the list
   - Applicant list is sorted by match score
   - View the top-scoring applicant: 85% Strong Match

4. **Review Application**
   - Click "View Details" to enter application details
   - View match analysis: matched Java, Git, Communication
   - Download PDF resume to view original content
   - Read AI match explanation and risk summary
   - After comprehensive evaluation, click "Accept"
   - Fill in Feedback: "Excellent match. Welcome aboard!"
   - TA immediately receives acceptance notification

5. **Handle Revocation Request**
   - Notification bell shows a red number
   - Expand notification list, see "Revocation request"
   - View request details: TA requests revocation due to course conflict
   - Click "Approve"
   - TA receives revocation approval notification, position vacancy is restored

---

## 6. Admin User Manual

### 6.1 Admin Dashboard (System Overview)

**Access Path:** `/admin/dashboard`

The Admin dashboard provides a global overview of system operation status:

#### 6.1.1 Key Metric Cards

| Metric | Description |
|--------|-------------|
| Total TAs | Total number of registered TAs in the system |
| Total MOs | Total number of registered MOs in the system |
| Total Postings | Total number of positions created by all MOs |
| Open Postings | Number of currently open positions |
| Pending Applications | Total number of pending applications |
| Active Recruitments | Number of active recruitment processes (open positions not yet past deadline) |

#### 6.1.2 Recent Activity

Displays the 8 most recent system-level activities:
- TA registrations
- MO account creations
- New position postings
- New application submissions
- Sorted in reverse chronological order, showing activity type, message, and time

![Admin Dashboard](截图1/admin/b894ac5ef5dc8d81e9947518f4cbf66b.png)

*Figure 6-1: Admin system overview dashboard, showing key metrics and recent activity*

### 6.2 MO Account Management

**Access Path:** `/admin/mos` (list), `/admin/mos/create` (create), `/admin/mos/detail?moId=xxx` (details)

#### 6.2.1 MO List

Displays all MO accounts in the system:
- Name, staff ID, email, department
- Position count (number of positions created by this MO)
- Action button: View Details

![MO Account Management](截图1/admin/9dcb4835c058821e281905e3e59f518d.png)

*Figure 6-2: MO account management page, supporting search and filtering, showing all MOs and their position counts*

#### 6.2.2 Create MO Account

Admins can create accounts for new module organizers:

**Form Fields:**

| Field | Type | Required | Description |
|-------|------|:---:|-------------|
| Username | Text | ✅ | Login username |
| Full Name | Text | ✅ | Full name |
| Staff ID | Text | ✅ | Staff ID |
| Email | Text | ✅ | Email |
| Phone | Text | Optional | Phone number |
| Department | Text | Optional | Department |
| Password | Password | ✅ | Initial password |

After creation, the MO can immediately log in with these credentials.

![Create MO Account](截图1/admin/218f8b97345fbce059e4bfd5d6c66b7b.png)

*Figure 6-3: Create MO account form, including username, name, staff ID, email, and other required fields*

#### 6.2.3 MO Details

**Access Path:** `/admin/mos/detail?moId=MO001`

Displays detailed MO information:
- Basic info (name, staff ID, email, department, phone)
- List of created positions
- Application count for each position
- Status (Active / Inactive)

**Password Reset:**
- Provides "Reset Password" functionality on the details page
- Enter new password to take effect immediately
- Corresponding endpoint: `POST /admin/mos/password-reset`

![MO Details](截图1/admin/53e4d45faa50a890725e5367df9d0f77.png)

*Figure 6-4: MO details page, showing basic info, created positions list, and password reset functionality*

### 6.3 Global Position Monitoring

**Access Path:** `/admin/jobs`

Displays a consolidated view of all positions in the system:
- Course code, course name
- Associated MO name
- Status (Open / Closed)
- Vacancies / applicants
- Deadline
- Supports filtering by MO, status, and keyword
- Supports sorting (post date, deadline, application count)

### 6.4 TA Workload Analysis

**Access Path:** `/admin/workload` (list), `/admin/workload/detail?taId=TA001` (details)

#### 6.4.1 Workload List

Displays the workload overview for all TAs:

| Column | Description |
|--------|-------------|
| TA ID | User identifier |
| Name | TA's full name |
| Student ID | Student ID number |
| Major | Major program |
| Active Positions | Number of currently ACCEPTED open positions |
| Total Hours | Sum of estimated hours across all accepted positions |
| Load Status | NORMAL / HIGH_ALERT / NOT_APPLIED |

**Filtering:**
- Keyword search (name/student ID/TA ID)
- Filter by major
- Filter by load status
- Sorting: name asc/desc, major asc/desc, hours asc/desc

**Load Status Determination:**
- `NOT_APPLIED`: No active positions (hours = 0)
- `NORMAL`: Total hours < 12
- `HIGH_ALERT`: Total hours ≥ 12 (red alert)

![TA Workload Analysis](截图1/admin/d168882aeb891cf3b8e320886a841923.png)

*Figure 6-5: TA workload analysis list, showing each TA's active positions, total hours, and load status*

#### 6.4.2 TA Workload Details

**Access Path:** `/admin/workload/detail?taId=TA001`

Displays detailed workload information for a specific TA:

**TA Personal Information:**
- Name, student ID, major, academic year, email, phone

**Current Position Assignments:**
- Course code, course name
- Position type (COURSE_TA / LAB / INVIGILATION / CHECKOFF)
- Hours
- Position status (ACTIVE / INACTIVE)

**Workload Analysis:**
- Total hours
- Active position count
- Peak Day
- Risk Level: LOW (hours < 4) / MEDIUM (4-8) / HIGH (≥ 12)
- Status tag

**Admin Suggestions:**
The system automatically generates suggestions based on risk level:

| Risk Level | Suggestion |
|------------|------------|
| HIGH | Avoid assigning additional tasks; coordinate with MOs to redistribute workload |
| MEDIUM | Monitor workload trends before assigning new long-duration tasks; prioritize short tasks |
| LOW | Current allocation is acceptable; can take on one short operational task |
| No Active Positions | No current TA assignments; consider recommending suitable open positions |

![TA Workload Details](截图1/admin/23c7a123b2aa945618c28137289d09ba.png)

*Figure 6-6: TA workload details page, showing personal info, current positions, workload analysis, and risk level*

#### 6.4.3 Workload Distribution Chart

The bottom of the page displays TA workload distribution statistics:

**Hour Buckets:**
- 0-4h: Number of low-hour TAs
- 4-8h: Number of medium-hour TAs
- 8-12h: Number of higher-hour TAs
- 12h+: Number of high-hour alert TAs

**Summary Metrics:**
- Critical Alert Count (high-hour TAs)
- Peak Workload Group (major group with highest workload)

![Workload Distribution Chart](截图1/admin/350ad50b824de63cf8f9f0c3fa5b0f4d.png)

*Figure 6-7: TA workload distribution chart, showing hour buckets and summary metrics*

---

## 7. Intelligent Matching System

### 7.1 Dual Engine Architecture

The system's intelligent matching adopts a **Dual Engine** architecture:

```
┌─────────────────────────────┐
│   RecommendationServiceImpl  │
│                              │
│   Check LLM config complete? │
│         │                    │
│    ┌────┴────┐               │
│    │         │               │
│ Complete  Incomplete         │
│    │         │               │
│    ▼         ▼               │
│  OpenAI   Local              │
│  Compatible Rule             │
│  Matcher  Matcher            │
│    │         │               │
│    │    ┌────┴────┐          │
│    │    │         │          │
│ Success Failure  Return      │
│    │    │         directly   │
│    │    ▼                    │
│    │  Fallback to Local      │
│    │  Rule Matcher           │
│    │  (method = local-rule-  │
│    │   fallback)             │
│    │                         │
│    ▼                         │
│  Unified MatchResult         │
│  (score, explanation,        │
│   matchedSkills,             │
│   missingSkills, method)     │
└─────────────────────────────┘
```

### 7.2 LLM Scoring Dimensions

When LLM API configuration is complete, the system sends structured resume and position data to the AI endpoint. The prompt includes the following scoring dimensions:

| Dimension | Description |
|-----------|-------------|
| Skill Overlap | Match degree between resume skills and position requirements |
| Education Relevance | Relevance of degree and major to the position |
| Teaching/Academic Support Experience | Whether there is TA, tutoring, or teaching-related experience |
| Responsibility Fit | Match between experience highlights and position responsibilities |
| Key Gaps | Missing critical skills or experience |

**Prompt Constraints (ensuring explainability):**
- Base judgment only on the input structured resume data and position information
- Do not supplement with external facts or speculate about missing information
- Must provide both supporting evidence and key gaps
- Must label as "for human decision reference only"
- AI results are not used for automatic hiring decisions

### 7.3 Local Rule Algorithm (Local Rule Matcher)

When the LLM is unavailable or not configured, the system uses local rule matching:

**Scoring Formula:**

```
baseScore = 30
skillScore = 55 × (matched skills count / required skills total)
teachingBonus = 10 (resume contains "assistant" / "teaching" / "tutor" keywords)
courseBonus = 5 (resume contains course code or course name)

finalScore = min(100, max(0, baseScore + skillScore + teachingBonus + courseBonus))
```

**Skill Matching Strategy:**
1. Exact match: Normalize skill names (lowercase, remove spaces and special characters) then compare
2. Alias match: Built-in skill alias groups
   - QA / Testing / QualityAssurance are mutually recognized
   - Lab Support / Lab / LaboratorySupport are mutually recognized
   - Tutoring / Tutor / TeachingSupport are mutually recognized
   - Marking / Grading / Assessment are mutually recognized
3. Contains match: When one skill name contains another, it is considered a match

### 7.4 Match Result Fields

Regardless of which matching engine is used, the output format is unified:

| Field | Type | Description |
|-------|------|-------------|
| score | int (0-100) | Match score |
| explanation | string | Match explanation (full analysis text) |
| method | string | Matching method: api-llm / local-rule / local-rule-fallback |
| matchedSkills | string[] | List of matched skills |
| missingSkills | string[] | List of missing skills |

**User display auxiliary fields (derived at runtime, not persisted):**

| Field | Description |
|-------|-------------|
| scoreBand | Strong Match (≥80) / Moderate Match (60-79) / Weak Match (<60) |
| strengthSummary | Strength summary text |
| riskSummary | Risk summary text |
| nextStepSuggestion | Next step suggestion |
| confidenceHint | Result confidence explanation |
| methodLabel | User-friendly label for the matching method |
| methodHint | Description text for the matching method |

### 7.5 Configuration Guide

**Enable LLM Matching:**

1. Copy `.env.example` to `.env.local`
2. Edit `.env.local`, fill in:

```
LLM_API_URL=https://api.deepseek.com/v1
LLM_API_KEY=your_api_key_here
LLM_MODEL=deepseek-chat
```

3. `.env.local` is in `.gitignore` and will not be committed
4. Configuration is automatically loaded on system startup

**DeepSeek Compatibility:**
- Supports both `https://api.deepseek.com` and `https://api.deepseek.com/v1` formats
- Code automatically appends the `/v1` suffix

**Degradation Strategy:**
- Automatic fallback to local rules when LLM call fails
- Fallback results are labeled `local-rule-fallback` with the reason noted in the explanation
- Degradation does not affect functional completeness, only matching accuracy

---

## 8. System Deployment and Operations Guide

### 8.1 Project Directory Structure

```
TA-Recruitment-System-Group68/
├── src/
│   └── com/bupt/ta/
│       ├── controller/          # Servlet controllers
│       │   ├── auth/            # Authentication related
│       │   ├── ta/              # TA features
│       │   ├── mo/              # MO features
│       │   ├── admin/           # Admin features
│       │   └── common/          # Common controllers
│       ├── service/             # Business interfaces
│       │   └── impl/            # Business implementations
│       ├── repository/file/     # File data access layer
│       ├── resume/              # PDF resume processing
│       ├── match/               # Intelligent matching
│       ├── model/               # Data models
│       ├── dto/                 # Data transfer objects
│       ├── filter/              # Filters
│       ├── exception/           # Custom exceptions
│       ├── config/              # Configuration and dependency injection
│       └── util/                # Utility classes
├── web/                         # Web frontend root
│   ├── WEB-INF/
│   │   ├── views/               # JSP views
│   │   │   ├── auth/            # Login/register pages
│   │   │   ├── ta/              # TA pages
│   │   │   ├── mo/              # MO pages
│   │   │   ├── admin/           # Admin pages
│   │   │   └── common/          # Common component pages
│   │   ├── lib/                 # Runtime JAR dependencies
│   │   └── web.xml              # Web application configuration
│   ├── assets/
│   │   └── css/                 # Stylesheets
│   └── index.jsp                # Home page entry
├── data/                        # Data file directory
│   ├── users/                   # User data
│   ├── postings/                # Position data
│   ├── applications/            # Application data
│   ├── resumes/                 # Uploaded resume PDFs
│   ├── notifications/           # Notification data
│   └── system/                  # System auxiliary data
├── lib/                         # Compile-time JAR dependencies
├── scripts/                     # Deployment and run scripts
├── docs/                        # Documentation
└── build/                       # Build output
```

### 8.2 Data File Description

| File Path | Format | Description |
|-----------|--------|-------------|
| `data/users/ta.json` | JSON array | TA user data (including resume extraction results) |
| `data/users/mo.json` | JSON array | MO user data |
| `data/users/admin.json` | JSON array | Admin user data |
| `data/postings/postings.json` | JSON array | All position data |
| `data/applications/applications.json` | JSON array | All application records |
| `data/notifications/notifications.json` | JSON array | All notification records |
| `data/system/skill-tags.json` | JSON array | Predefined skill tags |
| `data/system/ta-timetable.json` | JSON object | TA timetable data |
| `data/resumes/` | PDF files | Original uploaded resume files |

### 8.3 Data Backup and Recovery

**Backup:**

```powershell
# Backup all data files
Copy-Item -Path "{PROJECT_ROOT}\data" -Destination "{BACKUP_PATH}\data_backup_{DATE}" -Recurse
```

**Recovery:**

```powershell
# Recover data files
Remove-Item -Path "{PROJECT_ROOT}\data" -Recurse -Force
Copy-Item -Path "{BACKUP_PATH}\data_backup_{DATE}" -Destination "{PROJECT_ROOT}\data" -Recurse
```

> **Recommendation:** Back up the data/ directory regularly, especially before system upgrades or data migrations.

### 8.4 Environment Variable Configuration

| Variable | Description | Required |
|----------|-------------|:---:|
| `LLM_API_URL` | AI API endpoint URL | No (uses local rules if absent) |
| `LLM_API_KEY` | AI API key | No |
| `LLM_MODEL` | AI model name | No |

Configuration methods (choose one):
1. System environment variables
2. `.env.local` file (project root directory)

### 8.5 Security Considerations

1. **API Key Protection:**
   - `.env.local` is in `.gitignore`
   - Never commit API keys to version control
   - Use `.env.example` as a template for team collaboration

2. **Password Security:**
   - Current test environment stores passwords in plain text
   - Production should use hashed and salted storage (`PasswordUtils` provides basic utilities)

3. **Session Security:**
   - Session timeout: 30 minutes
   - Old sessions automatically invalidated on new login
   - HTTPS should be enabled in production

4. **File Upload Security:**
   - Only PDF format allowed
   - File size limited to 5MB
   - Uploaded files stored on the server's local directory

5. **Development Debug Entry:**
   - `/dev/login-as` is for local development only
   - Should be removed or protected in production deployment

---

## 9. Frequently Asked Questions (FAQ)

### Login and Authentication

**Q: What should I do if the login prompt shows "Invalid username, password, or role"?**
A: Please check: (1) Is the username spelled correctly? (2) Is the password correct (case-sensitive)? (3) Does the selected role match the account's actual role?

**Q: Why can't I login after selecting the MO role?**
A: MO accounts are created by Admins and cannot be self-registered. Please contact an Admin to create your MO account.

**Q: How long does a session last before expiring?**
A: By default, sessions expire after 30 minutes of inactivity. You will need to log in again after expiration.

### TA Users

**Q: I get "Invalid file format" when uploading my resume?**
A: The system only supports PDF files. Please confirm the file extension is `.pdf`.

**Q: My resume was uploaded successfully but no information was extracted?**
A: Possible reasons: (1) The PDF is a scanned/image-based PDF that PDFBox cannot extract text from; (2) The PDF uses special fonts or is encrypted. We recommend using standard text-based PDFs.

**Q: Why is my match score so low?**
A: The match score is based on skills extracted from your resume compared to position requirements. Suggestions: (1) Ensure your resume contains relevant skill keywords; (2) Complete the skill tags in your profile; (3) Match results are for reference only; final review is done by the MO.

**Q: Can I apply to the same position multiple times?**
A: No. The system detects duplicate applications; each TA can only submit one application per position.

**Q: Can I reapply after withdrawing my application?**
A: Yes. After withdrawal, the status becomes WITHDRAWN, and you can browse the position again and submit a new application.

**Q: What is the difference between a Revocation Request and a Withdraw?**
A: Withdraw is for pending (SUBMITTED) applications and can be done immediately. A Revocation Request is for accepted (ACCEPTED) applications and requires MO approval to revoke.

### MO Users

**Q: Why does editing a position show "Applications will be withdrawn"?**
A: When you modify the position description or required skills, the system triggers a cascade withdrawal, automatically withdrawing all submitted applications. This ensures applicants are re-evaluated against the updated requirements.

**Q: Is the match score the final decision?**
A: No. The match score and AI analysis are for reference only. MOs should combine the original PDF resume with their own judgment to make the final review decision. AI results are clearly labeled as "Advisory review" with confidence hints.

**Q: How are priorities determined in the review queue?**
A: Based on match score: ≥ 80 is High Priority, 60-79 is Medium, and < 60 is Low.

**Q: What happens when I approve or reject a revocation request?**
A: After approval: the TA's application becomes WITHDRAWN, the position vacancy is restored, and the TA's timetable slots are released. After rejection: the TA's application reverts to ACCEPTED and they continue as TA for the position.

**Q: Can I close a published position?**
A: Yes. In the position management list, you can close open positions. After closing, TAs can no longer apply. You can also reopen (Reopen) closed positions.

### Admin Users

**Q: How do I reset an MO's password?**
A: Navigate to "MO Management" -> find the target MO -> click "View Details" -> use the "Reset Password" feature on the details page.

**Q: What does "HIGH_ALERT" mean in TA workload analysis?**
A: It means the TA's total workload hours have reached or exceeded 12 hours/week, indicating a risk of overload. We recommend coordinating with relevant MOs to avoid assigning additional tasks to this TA.

**Q: What is the data source for the workload distribution chart?**
A: It is calculated based on all TAs' accepted (ACCEPTED) applications and the estimated workload hours of their corresponding positions.

### Data and Security

**Q: Where is the system data stored?**
A: All business data is stored as JSON files in the `data/` directory. Uploaded PDF resumes are stored in the `data/resumes/` directory.

**Q: What happens if a data file is corrupted?**
A: We recommend regularly backing up the `data/` directory. If a file is corrupted, it can be restored from backup. The system has fault tolerance for empty JSON array files.

**Q: Will AI matching expose resume data?**
A: The data sent to the AI endpoint only includes the structured resume (skill list, education, experience summary) and position information; the original PDF file is not included. We recommend using AI services that comply with data security requirements.

---

## Appendix A: Complete URL Routing Table

### Authentication and Common

| Method | URL | Servlet | Description |
|--------|-----|---------|-------------|
| GET | `/` | index.jsp → redirect | Home page, redirects to login |
| GET | `/auth/login` | LoginServlet | Login page |
| POST | `/auth/login` | LoginServlet | Handle login submission |
| GET | `/auth/register` | TARegisterServlet | TA registration page |
| POST | `/auth/register` | TARegisterServlet | Handle TA registration |
| GET | `/auth/logout` | LogoutServlet | Logout |
| GET | `/auth/role-select` | RoleSelectServlet | Role selection page |
| GET | `/dev/login-as` | DevSessionLoginServlet | Development debug login |
| GET | `/access-denied` | AccessDeniedServlet | Access denied page |
| GET | `/error` | ErrorPageServlet | General error page |
| GET | `/legal` | LegalPageServlet | Legal terms page |
| POST | `/notifications/dismiss` | NotificationDismissServlet | Mark notification as read |

### TA Features

| Method | URL | Servlet | Description |
|--------|-----|---------|-------------|
| GET | `/ta/dashboard` | TADashboardServlet | TA Dashboard |
| GET | `/ta/profile` | TAProfileServlet | Profile page |
| POST | `/ta/profile` | TAProfileServlet | Save profile |
| POST | `/ta/resume/upload` | TAResumeUploadServlet | Upload resume PDF |
| GET | `/ta/resume/download` | TAResumeDownloadServlet | Download resume PDF |
| GET | `/ta/jobs` | TAJobListServlet | Position list (search/filter) |
| GET | `/ta/jobs/detail` | TAJobDetailServlet | Position details + match analysis |
| GET | `/ta/applications/confirm` | TAApplyConfirmServlet | Application confirmation page |
| POST | `/ta/applications/submit` | TAApplySubmitServlet | Submit application |
| GET | `/ta/applications/my` | TAMyApplicationsServlet | My applications list |
| POST | `/ta/applications/withdraw` | TAApplicationWithdrawServlet | Withdraw application |

### MO Features

| Method | URL | Servlet | Description |
|--------|-----|---------|-------------|
| GET | `/mo/dashboard` | MODashboardServlet | MO Dashboard |
| GET | `/mo/profile` | MOProfileServlet | MO Profile |
| POST | `/mo/profile` | MOProfileServlet | Save MO profile |
| GET | `/mo/jobs` | MOJobListServlet | Position management list |
| GET | `/mo/jobs/create` | MOJobCreateServlet | Create position page |
| POST | `/mo/jobs/create` | MOJobCreateServlet | Submit position creation |
| GET | `/mo/jobs/edit` | MOJobEditServlet | Edit position page |
| POST | `/mo/jobs/edit` | MOJobEditServlet | Submit position edit |
| GET | `/mo/jobs/applicants` | MOJobApplicantsServlet | Position applicant list |
| GET | `/mo/applicants/detail` | MOApplicantDetailServlet | Application details |
| GET | `/mo/applicants/resume` | MOApplicantResumeDownloadServlet | Download applicant resume |
| POST | `/mo/applications/status` | MOApplicationStatusUpdateServlet | Update application status |
| GET | `/mo/review-queue` | MOReviewQueueServlet | Review queue |
| POST | `/mo/revocation/respond` | MORevocationResponseServlet | Handle revocation request |

### Admin Features

| Method | URL | Servlet | Description |
|--------|-----|---------|-------------|
| GET | `/admin/dashboard` | AdminDashboardServlet | System overview dashboard |
| GET | `/admin/mos` | AdminMOListServlet | MO account list |
| GET | `/admin/mos/create` | AdminMOCreateServlet | Create MO page |
| POST | `/admin/mos/create` | AdminMOCreateServlet | Submit MO creation |
| GET | `/admin/mos/detail` | AdminMODetailServlet | MO details |
| POST | `/admin/mos/password-reset` | AdminMOPasswordResetServlet | Reset MO password |
| GET | `/admin/jobs` | AdminJobListServlet | Global position monitoring |
| GET | `/admin/workload` | AdminTAWorkloadServlet | TA workload analysis |
| GET | `/admin/workload/detail` | AdminTAWorkloadDetailServlet | TA workload details |

---

## Appendix B: Data File Structures

### B.1 TA Users (data/users/ta.json)

```json
[
  {
    "id": "TA001",
    "taId": "TA001",
    "username": "syy",
    "email": "syy@qmul.ac.uk",
    "fullName": "Yueyang Sun",
    "displayName": "Yueyang Sun",
    "studentId": "2023213458",
    "phone": "123456",
    "intro": "I love learning software engineering",
    "skills": ["Java", "Git"],
    "majorProgram": "Computer",
    "academicYear": "Year 3",
    "role": "TA",
    "password": "...",
    "active": true,
    "resumeFileName": "TA001_xxx_resume_syy.pdf",
    "resumeUploadedAt": "2026-04-12 00:22:05",
    "extractedResume": {
      "name": "Alex Chen",
      "email": "alex.chen@example.com",
      "phone": "+44 7123 456789",
      "education": "MSc Computer Science, QMUL",
      "skills": ["Java", "Python", "Git", "Testing"],
      "experienceHighlights": ["TA for programming labs..."],
      "rawText": "..."
    },
    "createdAt": "2026-04-12 00:20:15",
    "updatedAt": "2026-04-12 00:20:15"
  }
]
```

### B.2 MO Users (data/users/mo.json)

```json
[
  {
    "id": "MO001",
    "moId": "MO001",
    "username": "mo_demo",
    "email": "wang@tarecruitment",
    "fullName": "Prof. Wang",
    "displayName": "Prof. Wang",
    "staffId": "STAFF001",
    "department": "School of Software Engineering",
    "phone": "+44 20 7000 1001",
    "description": "welcome",
    "role": "MO",
    "password": "...",
    "active": true,
    "createdAt": "2026-04-09 13:55:31",
    "updatedAt": "2026-04-14 15:30:56"
  }
]
```

### B.3 Positions (data/postings/postings.json)

```json
[
  {
    "postingId": "POST001",
    "courseCode": "X5414",
    "courseName": "Software engineering",
    "moId": "MO001",
    "moName": "Prof. Wang",
    "vacancies": 8,
    "applicationCount": 2,
    "deadline": "2026-04-19",
    "description": "As a Software Engineering Teaching Assistant...",
    "requiredSkills": ["Java", "Git Version Control", "..."],
    "postingType": "TA",
    "estimatedWorkloadHours": 6,
    "status": "OPEN",
    "createdAt": "2026-04-12 00:14:11",
    "updatedAt": "2026-04-12 00:14:11"
  }
]
```

ACTIVITY type positions have additional fields: `activityType`, `activityDate`, `activityStartTime`, `activityEndTime`, `activityLocation`, `moduleType`

### B.4 Applications (data/applications/applications.json)

```json
[
  {
    "applicationId": "APP-7500AC6A",
    "postingId": "POST001",
    "postingTitle": "Software engineering",
    "taId": "TA001",
    "taName": "Yueyang Sun",
    "appliedAt": "2026-04-12 00:25:54",
    "updatedAt": "2026-04-12 00:27:25",
    "status": "ACCEPTED",
    "statement": "I love software engineering!!!",
    "feedback": "good student!",
    "courseCode": "X5414",
    "moName": "Prof. Wang",
    "skillMatchScore": 57,
    "skillMatchExplanation": "The applicant shows a 57% advisory match...",
    "matchMethod": "local-rule",
    "matchedSkills": ["Java", "Git Version Control"],
    "missingSkills": ["Solid Software Engineering Fundamentals", "..."],
    "scoreBand": "Weak Match",
    "strengthSummary": "...",
    "riskSummary": "...",
    "nextStepSuggestion": "...",
    "confidenceHint": "...",
    "methodLabel": "Rule-based review",
    "methodHint": "..."
  }
]
```

### B.5 Notifications (data/notifications/notifications.json)

```json
[
  {
    "notificationId": "NOTIF-001",
    "recipientId": "MO001",
    "recipientRole": "MO",
    "type": "NEW_APPLICATION",
    "title": "New application received",
    "message": "Yueyang Sun has applied for Software engineering (X5414).",
    "relatedApplicationId": "APP-7500AC6A",
    "relatedPostingId": "POST001",
    "status": "UNREAD",
    "requiresAction": false,
    "actionStatus": "",
    "createdAt": "2026-04-12 00:25:54"
  }
]
```

### B.6 Skill Tags (data/system/skill-tags.json)

```json
[
  { "name": "Java" },
  { "name": "Python" },
  { "name": "Git" },
  { "name": "Testing" },
  { "name": "Communication" },
  { "name": "SQL" },
  { "name": "Machine Learning" },
  { "name": "Data Analysis" }
]
```

### B.7 Timetable (data/system/ta-timetable.json)

Stores TAs' weekly course schedules and position time slots, used for time conflict detection and dashboard display.

---

## Appendix C: Application Status Flow Diagram

```
                        ┌─────────────┐
                        │   SUBMITTED  │
                        │  (Submitted) │
                        └──┬─────┬─────┘
                           │     │
              MO Accept     │     │    MO Reject
                           │     │
                    ┌──────▼─┐ ┌─▼────────┐
                    │ACCEPTED │ │ REJECTED  │
                    │(Accepted)│ │(Rejected) │
                    └──┬─────┘ └───────────┘
                       │
         TA Revocation │
                       │
              ┌────────▼──────────┐
              │REVOCATION_REQUESTED│
              │ (Revocation Req.)  │
              └──┬───────────┬────┘
                 │           │
   MO Approve    │           │   MO Reject
                 │           │
          ┌──────▼──┐   ┌────▼─────┐
          │WITHDRAWN │   │ ACCEPTED │
          │(Withdrawn)│   │(Restored)│
          └──────────┘   └──────────┘

   TA Withdraw (SUBMITTED status only):

   SUBMITTED ──TA Withdraw──→ WITHDRAWN

   MO Edit triggers cascade withdrawal:

   SUBMITTED ──MO Edit (key info)──→ WITHDRAWN
   (Affected TAs are notified)
```

**Status Description Table:**

| Status | English Name | Meaning | TA Actionable | MO Actionable |
|--------|-------------|---------|:---:|:---:|
| SUBMITTED | Submitted | Application submitted, awaiting review | Withdraw | Accept / Reject |
| UNDER_REVIEW | Under Review | MO is reviewing the application | -- | Accept / Reject |
| ACCEPTED | Accepted | MO accepted the application | Revocation Request | -- |
| REJECTED | Rejected | MO rejected the application | -- | -- |
| WITHDRAWN | Withdrawn | Application has been withdrawn or revoked | -- | -- |
| REVOCATION_REQUESTED | Revocation Requested | TA requested revocation of an accepted position | -- | Approve / Reject |

---

*This manual applies to TA Recruitment System (Group 68) v1.0, last updated: 2026-05-21*
