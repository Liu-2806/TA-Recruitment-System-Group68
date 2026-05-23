<p align="center">
  <a href="README.md">中文</a> · <a href="README_EN.md">English</a>
</p>

<p align="center">
  <img src="docs/images/hero-banner.svg" alt="TA Recruitment System — Hero Banner" width="1000">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/Servlet-4.0-5DB75D?style=for-the-badge&logo=apache&logoColor=white" alt="Servlet 4.0">
  <img src="https://img.shields.io/badge/JSP-2.3-6DB33F?style=for-the-badge&logo=apache&logoColor=white" alt="JSP 2.3">
  <img src="https://img.shields.io/badge/Tomcat-9.x-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black" alt="Tomcat 9">
  <img src="https://img.shields.io/badge/PDFBox-3.0.2-FF6F61?style=for-the-badge&logo=apache&logoColor=white" alt="PDFBox">
  <img src="https://img.shields.io/badge/Gson-2.11.0-4FC3F7?style=for-the-badge&logo=json&logoColor=white" alt="Gson">
  <img src="https://img.shields.io/badge/No%20Database-JSON%20Only-64748B?style=for-the-badge&logo=json&logoColor=white" alt="No Database">
  <img src="https://img.shields.io/badge/AI%20Matching-LLM%20%2B%20Rules-AB47BC?style=for-the-badge&logo=openai&logoColor=white" alt="AI Matching">
</p>

<h2 align="center">
  Intelligent Web System for the Full TA Recruitment Lifecycle
</h2>

<p align="center">
  Connecting three roles: <code>TA Applicants</code> · <code>Module Organisers (MO)</code> · <code>System Administrators (Admin)</code><br>
  A complete closed loop from <b>Job Posting</b> → <b>Resume Submission</b> → <b>AI Intelligent Matching</b> → <b>Manual Review</b> → <b>Workload Analysis</b>
</p>

<p align="center">
  <a href="#-team-members">👥 Team</a> ·
  <a href="#-quick-start">🚀 Quick Start</a> ·
  <a href="#-system-architecture">🏗 Architecture</a> ·
  <a href="#-core-features">🎯 Features</a> ·
  <a href="#-ai-intelligent-matching">🤖 AI Matching</a> ·
  <a href="#-innovations--highlights">💡 Innovations</a> ·
  <a href="#-data-model">📊 Data Model</a> ·
  <a href="#-application-lifecycle">🔄 Lifecycle</a> ·
  <a href="#-project-structure">📂 Structure</a> ·
  <a href="#-tech-stack">🛠 Tech Stack</a> ·
  <a href="#-development-workflow">⚡ Dev Workflow</a> ·
  <a href="#-testing-strategy">🧪 Testing</a> ·
  <a href="#-demo-accounts">🎮 Demo</a> ·
  <a href="#-security--privacy">🔐 Security</a> ·
  <a href="#-documentation">📚 Docs</a>
</p>

---

## 👥 Team Members

| GitHub Username | QMID |
| :--- | :--- |
| [`Liu-2806`](https://github.com/Liu-2806) | `231226532` |
| [`ffelaine`](https://github.com/ffelaine) | `231226347` |
| [`deer-ice`](https://github.com/deer-ice) | `231226406` |
| [`ShaoyangZhu`](https://github.com/ShaoyangZhu) | `231226370` |
| [`skywalker11111`](https://github.com/skywalker11111) | `231226439` |
| [`NoveAmberic`](https://github.com/NoveAmberic) | `231226495` |

---

## 🚀 Quick Start

### Environment Requirements

| Item | Requirement |
| :--- | :--- |
| OS | Windows 10 / 11 |
| JDK | 21 ([Adoptium](https://adoptium.net/temurin/releases/?version=21)) |
| Servlet Container | Apache Tomcat 9.x ([Download](https://tomcat.apache.org/download-90.cgi)) |
| Shell | PowerShell 5.x+ |
| Encoding | UTF-8 |

### One-Command Startup

```powershell
# 1. Clone the project
git clone https://github.com/Liu-2806/TA-Recruitment-System-Group68.git
cd TA-Recruitment-System-Group68

# 2. Verify JDK 21
java -version   # Should output 21.x

# 3. One-command startup (Tomcat must be in parent directory, or specify via -TomcatPath)
powershell -ExecutionPolicy Bypass -File .\scripts\run-web-app.ps1

# 4. Open in browser
# http://localhost:8080/TA-Recruitment-System-Group68/
```

> **Default Tomcat lookup path:** `apache-tomcat-9.0.116/` in the parent directory of the repository. If installed elsewhere, use the `-TomcatPath "C:\path\to\tomcat"` parameter.

---

## 🏗 System Architecture

<p align="center">
  <img src="docs/images/architecture.svg" alt="System Architecture Diagram" width="1000">
</p>

The system follows a classic **Three-Tier Layered Architecture**, strictly separating the presentation layer, business logic layer, and data persistence layer:

### Core Entities (5 Core Entities)

| Entity | Description | Persistence Location |
| :--- | :--- | :--- |
| **User** | Authentication + Role (TA/MO/ADMIN) | `data/users/*.json` |
| **TAProfile** | Candidate info + Resume + Timetable | `data/users/ta.json` |
| **JobPosting** | Position + Skill requirements + Schedule | `data/postings/postings.json` |
| **Application** | TA-to-Posting link + Status + AI match score | `data/applications/applications.json` |
| **Notification** | Event-driven messages | `data/notifications/notifications.json` |

### Core Components Explained

<details>
<summary><b>ServiceRegistry — DI Container</b></summary>

`ServiceRegistry.java` is the central dependency injection container. At class-load time, it instantises in dependency order:
1. First creates **7 Repository** singletons (TADataRepository, PostingDataRepository, ApplicationDataRepository, SystemDataRepository, TATimetableDataRepository, etc.)
2. Then creates **7 Service** singletons via **constructor injection** (AuthService, ProfileService, JobService, ApplicationService, ResumeService, RecommendationService, AnalyticsService)

Controllers never `new` services themselves — they obtain them via static methods on `ServiceRegistry`, ensuring one shared instance per service, visible dependency graph, and no circular dependencies.
</details>

<details>
<summary><b>AuthFilter — RBAC Three-Stage Pipeline</b></summary>

`@WebFilter("/*")` intercepts all requests with three-stage processing:
1. **Whitelist passthrough:** Public paths like `/auth/login`, `/auth/register`, `/assets/*`, `/dev/login-as`
2. **Authentication check:** Unauthenticated requests are redirected to `/auth/login`
3. **Role-based access control:** `/ta/*` requires `Role.TA`, `/mo/*` requires `Role.MO`, `/admin/*` requires `Role.ADMIN`. Unauthorised access forwards to `403.jsp`

The `Role` enum provides compile-time type safety — no string comparison needed.
</details>

<details>
<summary><b>Application Lifecycle Engine — Six-State Engine</b></summary>

`ApplicationServiceImpl.java` (~760 lines) implements a complete application lifecycle:

**6-Condition Eligibility Gate:**
| # | Condition | Check |
| :--- | :--- | :--- |
| 1 | Profile complete | studentId, majorProgram, academicYear are filled |
| 2 | Resume uploaded | resumeFileName is non-empty |
| 3 | No duplicate | No existing application for the same TA + same posting |
| 4 | Deadline valid | Current date ≤ posting.deadline |
| 5 | Posting open | posting.status == OPEN |
| 6 | No timetable conflict | Minute-level interval overlap comparison (lines 485-510) |

Only when all 6 conditions pass is the application created, simultaneously executing AI match scoring and sending a notification to the MO.
</details>

<details>
<summary><b>Notification System — 7 Event Types</b></summary>

| Event Type | Recipient | Triggered When | Action Required |
| :--- | :--- | :--- | :---: |
| `NEW_APPLICATION` | MO | TA submits a new application | ❌ |
| `APPLICATION_ACCEPTED` | TA | MO accepts the application | ❌ |
| `APPLICATION_REJECTED` | TA | MO rejects the application | ❌ |
| `APPLICATION_AUTO_WITHDRAWN` | TA | MO edits posting, causing cascade withdrawal | ❌ |
| `REVOCATION_REQUEST` | MO | TA requests revocation of an accepted posting | ✅ |
| `REVOCATION_APPROVED` | TA | MO approves the revocation | ❌ |
| `REVOCATION_REJECTED` | TA | MO declines the revocation | ❌ |

Each notification carries recipientId, entityId, timestamp, and resolved flag. Revocation request notifications remain **unresolved** until the MO responds.
</details>

<details>
<summary><b>View Layer — Shared Layout Template System</b></summary>

All pages use `layout.jsp` as a shared template. Controllers set `bodyPage` and `pageTitle`; the layout automatically embeds the header, role-specific sidebar, and footer.

- **CSS modularisation:** base.css + components.css + layout.css + **19** page-specific CSS files
- **JS modularisation:** common.js + toast.js + **9** page-specific scripts
- **JSTL rendering:** All views use JSTL for conditional rendering and iteration — zero scriptlet code
- **Shared error pages:** 403.jsp and error.jsp are reused across all modules

This template architecture allowed **6 developers to work in parallel** on their respective modules while maintaining visual consistency across the entire site.
</details>

---

## 🎯 Core Features

### Three-Role Feature Matrix

| Feature | TA Applicant | Module Organiser (MO) | Admin |
| :--- | :---: | :---: | :---: |
| Register / Login | ✅ | ✅ (Created by Admin) | ✅ |
| Profile Management | ✅ | ✅ | — |
| PDF Resume Upload & Smart Extraction | ✅ | — | — |
| Job Browsing & Multi-dimensional Filtering | ✅ | — | Global Monitoring |
| **AI Intelligent Matching Analysis** | ✅ | ✅ | — |
| Online Application & 6-Condition Gate | ✅ | — | — |
| Application Status Tracking (6 States) | ✅ | — | — |
| Withdraw Application | ✅ | — | — |
| Revocation Request | ✅ | — | — |
| Job CRUD + Cascade Withdrawal | — | ✅ | — |
| Activity Posting Creation | — | ✅ | — |
| Applicant Review (Accept/Reject) | — | ✅ | — |
| Review Queue & Priority Sorting | — | ✅ | — |
| Inline Decision + Review Feedback | — | ✅ | — |
| Revocation Request Approval | — | ✅ | — |
| Real-time Notification System (7 types) | ✅ | ✅ | — |
| MO Account Management (Create/Reset) | — | — | ✅ |
| Global Job Monitoring | — | — | ✅ |
| TA Workload Analysis & Alerts | — | — | ✅ |
| Workload Distribution Chart | — | — | ✅ |
| Intelligent Management Suggestions | — | — | ✅ |

---

### 🎓 TA — Teaching Assistant Applicant

```
Register → Complete Profile → Upload PDF Resume → Browse Jobs → View Match Analysis → Submit Application → Track Status
```

| Feature | Details |
| :--- | :--- |
| **Smart Resume Extraction** | PDFBox text extraction → Heuristic structuring → 7 fields (name, email, phone, education, skills, experienceHighlights, rawText) → JSON persistence |
| **Multi-dimensional Job Filtering** | Keyword search + Major/Department/Module type filtering + 4 sorting options (Latest/Deadline/Match score/Name A-Z) |
| **Match Score Preview** | Each job card directly displays match band labels (Strong ≥ 80 / Moderate 60-79 / Weak < 60) |
| **6-Condition Application Gate** | Profile completeness + Resume uploaded + No duplicate + Deadline + Posting status + **Timetable conflict detection** (minute-level interval overlap) |
| **Pre-application Confirmation** | Displays job info, match score, checklist, and personal statement input |
| **Application Status Tracking** | 6-state real-time transitions + coloured status labels + match explanations |
| **Withdraw** | One-click withdrawal for SUBMITTED applications — irreversible |
| **Revocation Request** | Submit revocation request with reason for ACCEPTED postings — requires MO approval |

---

### 📋 MO — Module Organiser

```
Create Posting → View Applicants → AI-Assisted Review → Accept/Reject → Handle Revocation Requests
```

| Feature | Details |
| :--- | :--- |
| **Full Job Lifecycle** | Create → Edit → Close → Reopen; supports both TA course assistant and Activity one-off event postings |
| **Activity Postings** | Project assessment / Invigilation / Lab support, with date, time, and location fields |
| **Cascade Withdrawal** | When editing posting description or required skills, automatically withdraws all SUBMITTED applications and notifies affected TAs |
| **AI-Assisted Review** | Applicant list sorted by match score descending; inline Accept/Reject takes immediate effect |
| **Review Queue** | Aggregates all pending applications with priority labels (High ≥ 80 / Medium 60-79 / Low < 60) |
| **Resume Download** | Direct download of applicant's original PDF resume for comprehensive review alongside AI analysis |
| **Revocation Handling** | Approve/Reject directly from notifications; approval releases TA timetable slots |
| **MO Dashboard** | Pending count + Active postings + Deadline alerts + Active job list + Recent activity |

---

### ⚙️ Admin — System Administrator

```
System Overview → MO Management → Global Job Monitoring → TA Workload Analysis
```

| Feature | Details |
| :--- | :--- |
| **Global Dashboard** | Total TAs, Total MOs, Total Postings, Open Postings, Pending Applications, Active Recruitments + Recent 8 system-wide activities |
| **MO Account Management** | Create MO accounts (username/name/staff ID/email/department/password) + View details + Password reset |
| **Global Job Monitoring** | View all MOs' postings, filter by MO/status/keyword, sort by creation date/deadline/application count |
| **TA Workload Analysis** | Hours statistics + Active posting count + Load grading (NORMAL < 12h / HIGH_ALERT ≥ 12h) |
| **Workload Distribution Chart** | Hour buckets (0-4h / 4-8h / 8-12h / 12h+) + High workload alert count + Peak workload group |
| **Risk Level Assessment** | LOW (< 4h) / MEDIUM (4-8h) / HIGH (≥ 12h), each with auto-generated management suggestions |
| **Intelligent Suggestions** | HIGH: Avoid extra tasks, coordinate redistribution; MEDIUM: Prioritise short tasks; LOW: Can take on short tasks |

---

## 🤖 AI Intelligent Matching

<p align="center">
  <img src="docs/images/matching-flow.svg" alt="AI Matching Flow Diagram" width="900">
</p>

### Dual-Engine Architecture

The system uses the **Strategy design pattern** to dynamically select the matching engine:

```
RecommendationServiceImpl checks LLM configuration
    │
    ├── Config complete → OpenAiCompatibleMatcher (LLM path)
    │                       │
    │                       ├── Success → Return MatchResult (method = "api-llm")
    │                       │
    │                       └── Failure → Auto-fallback to LocalRuleMatcher
    │                                     (method = "local-rule-fallback")
    │
    └── Not configured → LocalRuleMatcher (Local rules)
                         (method = "local-rule")
```

**Key design: Regardless of whether the external AI service is available, the system always produces a match result — zero single point of failure.**

### LLM Five-Dimension Evaluation

When LLM API configuration is complete, the system sends structured resume and job posting data to the AI endpoint. The prompt includes a strict five-dimension evaluation framework:

| Dimension | Description |
| :--- | :--- |
| 🧠 **Skill Overlap** | Match between extracted resume skills and posting's requiredSkills |
| 🎓 **Education Relevance** | Relevance of education and major to the posting |
| 👨‍🏫 **Teaching/Academic Support Experience** | Whether experienceHighlights contains TA, tutoring, or teaching experience |
| 📋 **Role Responsibility Fit** | Match between experience highlights and posting description responsibilities |
| ⚠️ **Critical Gaps** | Key skills or experience from the posting not reflected in the resume |

#### Explainability Constraints

- AI does **not** read the raw PDF, and does **not** introduce external knowledge
- Only analyses the structured fields passed by the system (skills, education, experienceHighlights)
- If evidence for an item is absent from input, must label **"insufficient evidence"** rather than speculate
- `explanation` must cover: matching evidence + critical gaps + fit assessment
- Must explicitly state **"advisory review only, not an automated hiring decision"**

### Local Rule Algorithm (LocalRuleMatcher)

When LLM is unavailable, local rules provide deterministic scoring:

```
baseScore     = 30                              (Base score)
skillScore    = 55 × (matched / required)       (Skill match score)
teachingBonus = 10  (Contains "assistant"/"teaching"/"tutor" keywords)
courseBonus   = 5   (Contains course code or course name)

finalScore    = min(100, max(0, Σ))
```

#### Skill Matching Strategies

| Strategy | Description |
| :--- | :--- |
| **Exact Match** | Skill names normalised (lowercase, strip spaces/special chars) before comparison |
| **Alias Match** | QA ↔ Testing ↔ QualityAssurance · Lab Support ↔ Lab · Tutoring ↔ Tutor · Marking ↔ Grading |
| **Contains Match** | When one skill name contains another, treated as a match |

### Unified Output Format

Regardless of which engine is used, the output format is strictly consistent:

| Field | Type | Description |
| :--- | :--- | :--- |
| `score` | int (0-100) | Match score |
| `explanation` | string | Match explanation (full analysis text) |
| `method` | string | Matching method: `api-llm` / `local-rule` / `local-rule-fallback` |
| `matchedSkills` | string[] | Matched skills list |
| `missingSkills` | string[] | Missing skills list |

#### Runtime-Derived Fields (UI rendering only, not persisted)

| Field | Description |
| :--- | :--- |
| `scoreBand` | Strong Match (≥80) / Moderate Match (60-79) / Weak Match (<60) |
| `strengthSummary` | Strength summary text |
| `riskSummary` | Risk summary text |
| `nextStepSuggestion` | Suggested next steps |
| `confidenceHint` | Result confidence indication |
| `methodLabel` | User-friendly label for the matching method |
| `methodHint` | Explanatory text for the matching method |

### LLM Configuration Guide (Optional)

```bash
# 1. Copy the configuration template
cp .env.example .env.local

# 2. Edit .env.local
LLM_API_URL=https://api.deepseek.com/v1
LLM_API_KEY=your_api_key_here
LLM_MODEL=deepseek-chat

# 3. .env.local is already in .gitignore and will not be committed
# 4. Without configuration, local rule matching is used automatically — fully functional
```

> **DeepSeek compatibility:** Supports both `https://api.deepseek.com` and `https://api.deepseek.com/v1` formats. Code auto-completes the `/v1` suffix.

---

## 💡 Innovations & Highlights

### 🌟 Top 10 Core Innovations

| # | Innovation | Description |
| :--- | :--- | :--- |
| 1 | **Dual-Engine AI Matching** | LLM + local rules dual-path architecture, Strategy pattern for dynamic selection, auto-degradation when API unavailable, **zero single point of failure** |
| 2 | **Explainable AI Decisions** | Five-dimension evaluation framework + evidence/gaps/confidence annotations, strictly complying with handout explainability requirements; AI results are **not used as automated hiring decisions** |
| 3 | **6-Condition Application Gate** | Profile completeness + Resume + Deduplication + Deadline + Posting status + **Minute-level timetable conflict detection** (interval overlap, lines 485-510) |
| 4 | **Cascade Withdrawal Mechanism** | MO editing key info (description/skills) auto-withdraws all submitted applications, resets `applicationCount` to zero, notifies affected TAs to reapply |
| 5 | **Revocation Request Workflow** | TA can request revocation (with reason) for accepted postings; MO approval releases timetable slots; bidirectional notifications enable **multi-role collaboration** |
| 6 | **Intelligent Resume Extraction Pipeline** | PDFBox 3.0.2 extraction → Heuristic rule-based structuring → 7-field output → JSON persistence, **fully automated processing** |
| 7 | **Real-time Notification System** | 7 event types, including **actionable notifications** (Revocation Request); MO can Approve/Reject directly within the notification panel |
| 8 | **TA Workload Monitoring** | Hours statistics + Load grading (NORMAL/HIGH_ALERT) + Risk levels (LOW/MEDIUM/HIGH) + **Intelligent management suggestions** |
| 9 | **Multi-Role RBAC** | AuthFilter three-stage pipeline: whitelist → authentication → role authorisation; `Role` enum provides **compile-time type safety** |
| 10 | **Shared Layout Templates** | layout.jsp template + 19 page CSS files + 9 page JS scripts; **6 developers working in parallel** while maintaining site-wide visual consistency |

### 🏗 Engineering Highlights

| Highlight | Description |
| :--- | :--- |
| **Database-free Design** | All JSON file storage, Gson serialisation, `synchronized` methods ensure concurrent safety |
| **ServiceRegistry DI** | Manual DI container, constructor injection for 7 Repos + 7 Services, clear dependency graph |
| **Annotation-based Routing** | `@WebServlet` annotations replace XML, zero configuration maintenance for route changes |
| **Zero Build Framework** | Pure `javac` + PowerShell scripts, committed JARs guarantee consistent environments for all 6 developers |
| **Risk Mitigation** | Phase 1 Console prototype validates data model → Phase 2 Static JSP → Phase 3 Servlet integration → Phase 4 AI + Notifications |
| **Documentation-First** | 694-line `interface-design.md` defines all API contracts, keeping 6 developers aligned on interfaces |

---

## 📊 Data Model

The system models **5 core entities**, persisted as JSON arrays in separate files under `data/`. Entities cross-reference each other via ID fields:

```
User (userId)
  │
  ├── TAProfile (taId → User.userId)
  │     └── extractedResume (Structured resume, 7 fields)
  │     └── timetable (Weekly schedule data)
  │
  ├── MOProfile (moId → User.userId)
  │     └── JobPosting[] (postingId, moId)
  │           └── schedule[] ({dayOfWeek, startTime, endTime})
  │
  └── Application (applicationId)
        ├── taId → User.userId
        ├── postingId → JobPosting.postingId
        ├── status → ApplicationStatus enum (6 states)
        ├── skillMatchScore (0-100)
        └── matchedSkills[] / missingSkills[]
```

### Data File Inventory

| File Path | Format | Description |
| :--- | :--- | :--- |
| `data/users/ta.json` | JSON array | TA user data (with extracted resume + skill tags) |
| `data/users/mo.json` | JSON array | MO user data (with department, staff ID) |
| `data/users/admin.json` | JSON array | Admin user data |
| `data/postings/postings.json` | JSON array | All job postings (with schedule[]) |
| `data/applications/applications.json` | JSON array | All application records (with AI match results) |
| `data/notifications/notifications.json` | JSON array | All notification records (with resolved flag) |
| `data/system/skill-tags.json` | JSON array | Pre-defined skill tags (Java, Python, Git, etc.) |
| `data/system/ta-timetable.json` | JSON object | TA timetable data (weekly schedule + posting time blocks) |
| `data/resumes/` | PDF files | Uploaded original resume files |

---

## 🔄 Application Lifecycle

```
                          ┌─────────────┐
                          │  SUBMITTED   │
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
       TA Revocation      │
       Request            │
                         │
                  ┌──────▼───────────┐
                  │REVOCATION_REQUESTED│
                  │  (Revocation Req.) │
                  └──┬───────────┬────┘
                     │           │
      MO Approve     │           │   MO Reject
                     │           │
              ┌──────▼──┐   ┌────▼─────┐
              │WITHDRAWN │   │ ACCEPTED │
              │(Withdrawn)│   │(Restored)│
              └──────────┘   └──────────┘

  TA Withdraw:     SUBMITTED ──Withdraw──→ WITHDRAWN
  MO Cascade:      SUBMITTED ──Edit Key Info──→ WITHDRAWN + Notify TA
```

| Status | Meaning | TA Actions | MO Actions |
| :--- | :--- | :---: | :---: |
| `SUBMITTED` | Submitted, awaiting review | Withdraw | Accept / Reject |
| `UNDER_REVIEW` | MO is reviewing | — | Accept / Reject |
| `ACCEPTED` | MO has accepted | Revocation Request | — |
| `REJECTED` | MO has rejected | — | — |
| `WITHDRAWN` | Application withdrawn | — | — |
| `REVOCATION_REQUESTED` | TA requests revocation | — | Approve / Reject |

---

## 📂 Project Structure

```
TA-Recruitment-System-Group68/
│
├── src/com/bupt/ta/              ← Java source code (main framework)
│   ├── config/                   │  ServiceRegistry (DI container)
│   ├── controller/               │  Servlet controllers (30+)
│   │   ├── auth/                 │  │  LoginServlet · Register · Logout · RoleSelect
│   │   ├── ta/                   │  │  Dashboard · Profile · Jobs · Apply · MyApplications
│   │   ├── mo/                   │  │  Dashboard · Jobs CRUD · Applicants · ReviewQueue
│   │   ├── admin/                │  │  Dashboard · MO Mgmt · Job Monitor · Workload
│   │   └── common/               │  │  DevLogin · Error · Legal · AccessDenied
│   ├── service/                  │  Business interfaces (7)
│   │   └── impl/                 │  Business implementations (7)
│   ├── repository/file/          │  JSON file-based data access layer (7 repos)
│   ├── resume/                   │  PDF resume extraction pipeline
│   │   ├── PdfResumeExtractor    │  │  PDFBox 3.0.2 text extraction
│   │   └── ResumeStructurer      │  │  Heuristic rule-based structuring (7 fields)
│   ├── match/                    │  AI dual-engine matching
│   │   ├── RecommendationServiceImpl  │  Engine dispatcher
│   │   ├── OpenAiCompatibleMatcher    │  LLM matching (5-dimension evaluation)
│   │   ├── LocalRuleMatcher           │  Local rule-based matching
│   │   ├── EnvConfigLoader            │  Environment configuration loader
│   │   └── MatchResult                │  Unified output DTO
│   ├── model/                    │  Data models
│   │   ├── User                  │  │  Base user class
│   │   ├── Role                  │  │  Role enum (TA/MO/ADMIN)
│   │   └── ApplicationStatus     │  │  Application status enum (6 states)
│   ├── dto/                      │  Data transfer objects
│   │   ├── PageResult            │  │  Pagination wrapper
│   │   ├── JobQuery              │  │  Job query conditions
│   │   └── ApplicationQuery      │  │  Application query conditions
│   ├── filter/                   │  AuthFilter (RBAC three-stage pipeline)
│   ├── exception/                │  BusinessException · UnauthorizedException
│   └── util/                     │  SessionKeys and other utilities
│
├── web/                          ← Web frontend root (Tomcat web root)
│   ├── WEB-INF/
│   │   ├── views/                │  JSP views (34 pages)
│   │   │   ├── auth/             │  │  login.jsp · register.jsp
│   │   │   ├── ta/               │  │  dashboard · profile · positions · position-details · applications · apply-confirm
│   │   │   ├── mo/               │  │  dashboard · postings · post-position · applicants · applicant-details · review-queue · profile-edit
│   │   │   ├── admin/            │  │  dashboard · create-mo · all-mos · all-jobs · ta-workload
│   │   │   └── common/           │  │  layout.jsp · sidebar · header · footer · legal
│   │   ├── lib/                  │  Runtime JARs (taglibs · pdfbox · gson · commons-logging)
│   │   └── web.xml               │  Web app configuration (session timeout · welcome file · AuthFilter)
│   ├── assets/
│   │   ├── css/                  │  base.css · components.css · layout.css + 19 page-specific stylesheets
│   │   └── js/                   │  common.js + 9 page-specific scripts
│   └── index.jsp                 │  Homepage entry (redirects to /auth/login)
│
├── data/                         ← Data files directory (No Database)
│   ├── users/                    │  ta.json · mo.json · admin.json
│   ├── postings/                 │  postings.json
│   ├── applications/             │  applications.json
│   ├── notifications/            │  notifications.json
│   ├── system/                   │  skill-tags.json · ta-timetable.json
│   └── resumes/                  │  Uploaded PDF resume files
│
├── lib/                          ← Compile-time JAR dependencies
│   ├── pdfbox-app-3.0.2.jar      │  PDF text extraction
│   ├── gson-2.11.0.jar           │  JSON serialisation
│   └── javax.servlet-api-4.0.1.jar │ Servlet API
│
├── scripts/                      ← Deployment & run scripts (PowerShell)
│   ├── run-web-app.ps1           │  One-command startup
│   ├── deploy-to-local-tomcat.ps1│  Four-stage deployment
│   └── start-local-tomcat.ps1    │  Tomcat startup
│
├── docs/                         ← Project documentation
│   ├── images/                   │  README assets (hero-banner · architecture · matching-flow)
│   ├── submit/                   │  Course submission materials
│   │   ├── Report_group68_final.docx  │ Final report
│   │   ├── ProductBacklog_group68.xlsx │ Product backlog
│   │   └── Prototype_group68.pdf       │ Prototype design
│   ├── 用户手册_中文.md           │  Full Chinese user manual
│   ├── User_Manual.md            │  English User Manual
│   ├── 代码结构树.md              │  Code structure tree
│   ├── 简历输入模块中文说明手册.md │  Resume input & matching module manual
│   ├── interface-design.md       │  API interface design contract (694 lines)
│   ├── page-navigation.md        │  Page navigation design
│   └── AI协作开发提示词.md        │  AI-assisted development prompt documentation
│
├── .env.example                  ← LLM API configuration template
├── .gitignore                    ← Git ignore rules
├── README.md                     ← Chinese README
└── README_EN.md                  ← English README (this file)
```

---

## 🛠 Tech Stack

| Layer | Technology | Version | Rationale |
| :--- | :--- | :--- | :--- |
| **Backend Framework** | Java Servlet + JSP | Servlet 4.0 / JSP 2.3 | Course requirement; mature and stable; annotation-based config eliminates XML |
| **Runtime Container** | Apache Tomcat | 9.x (9.0.116) | Servlet 4.0 compatible; widely adopted |
| **Data Serialisation** | Gson | 2.11.0 | Lightweight API; better suited than Jackson for straightforward serialisation |
| **PDF Processing** | Apache PDFBox | 3.0.2 | Reliable text extraction for multi-column, special-font, and embedded-font PDF resumes |
| **Frontend** | HTML5 + CSS3 + JSTL | — | Template-based rendering; JSTL replaces scriptlets for zero embedded Java code |
| **Data Storage** | JSON text files | — | Hard course constraint: no database |
| **AI Service** | OpenAI-compatible API (optional) | DeepSeek, etc. | LLM-assisted matching analysis with graceful degradation |
| **Runtime Environment** | JDK | 21 | Latest LTS version |
| **Build Tool** | javac + PowerShell | — | Zero build framework dependency; committed JARs ensure consistent environments |

---

## ⚡ Development Workflow

### Four-Phase Implementation Strategy

```
Phase 1 ── Console Prototype ──→ Data model + JSON persistence validated
   │
Phase 2 ── Static JSP Pages ──→ 34 views + 19 CSS files + interface-design.md (694 lines)
   │
Phase 3 ── Servlet Integration ──→ ServiceRegistry + AuthFilter + Module Servlets
   │                                  Integration order: auth → TA → MO → admin
   │
Phase 4 ── AI + Notification Integration ──→ Dual-engine matching + Resume pipeline + Notification system
```

### Deployment Script (Four Stages)

`deploy-to-local-tomcat.ps1` executes four stages:
1. Create staging directory under Tomcat `webapps/`
2. Copy `web/` (JSPs, CSS, JS, images) to staging
3. `javac` compile Java source code, output `.class` files to `WEB-INF/classes/`
4. Verify deployment structure integrity

### Git Collaboration

| Metric | Data |
| :--- | :--- |
| Workflow | Git feature-branch + PR merge |
| Commits | 106+ commits |
| PR Merges | 20+ pull requests |
| Dev Branches | dev-wpz/Admin · dev-zjy/MO · dev-zsy/architecture · dev-syy/Servlet · dev-fyl/TA |
| Commit Convention | Conventional Commits (feat: / fix: / refactor: / docs:) |

---

## 🧪 Testing Strategy

### Four-Level Testing Approach (No JUnit Framework)

| Level | Type | Tools | Coverage |
| :--- | :--- | :--- | :--- |
| **1** | Console Smoke Tests | Phase1ConsoleApp, MOConsoleApp, 7 admin runners | JSON persistence + data model serialisation |
| **2** | Scripted Integration Tests | PowerShell automation scripts (test-admin-*.ps1) | Reproducible regression via compile + execution |
| **3** | PDF Pipeline Validation | SampleResumePdfGenerator | Structured extraction accuracy |
| **4** | Browser E2E Tests | Dev login shortcuts + seed data | Full-path UI validation |

### Formal Test Programs

Formal test programs for the software submission are provided under `test-programs/`. Run them with:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\test-programs\run-all-tests.ps1
```

The runner compiles the Java source, copies `data/` into `.acceptance-test-data/`, and verifies authentication, TA workflow, MO workflow, Admin workflow, suitability analysis, resume access, and JSON persistence without modifying the normal demonstration data.

The live acceptance testing task guide is available at `docs/testing/acceptance-testing-tasks.md`.

### Verified Critical Paths

- ✅ Resume pipeline: PDF → PDFBox text extraction → structured fields → JSON persistence
- ✅ Application flow: 6-condition gate → application creation → AI match scoring → MO notification
- ✅ Timetable conflict: WED 10:00-12:00 vs 10:30-12:30 → 90-minute overlap detected
- ✅ AuthFilter: All cross-role access blocked, 403.jsp displayed
- ✅ AI matching: Local rule scoring + LLM 5-dimension evaluation + API failure fallback
- ✅ Notification workflow: Correct notification generated at each lifecycle transition + MO unresolved-until-responded semantics

---

## 🎮 Demo Accounts

| Role | Username | Password | User ID | Notes |
| :--- | :--- | :--- | :--- | :--- |
| **TA** | `syy` | `1234567890syy` | TA001 | Resume uploaded, has application records |
| **TA** | `zjy` | `1234567890zjy` | TA002 | Resume uploaded, has application records |
| **MO** | `mo_demo` | `Temp123!` | MO001 | Prof. Wang, has multiple postings |
| **MO** | `18540371119` | `111111` | MO002 | Puze Wang |

### Development Login Shortcuts

```
http://localhost:8080/TA-Recruitment-System-Group68/dev/login-as?role=TA
http://localhost:8080/TA-Recruitment-System-Group68/dev/login-as?role=MO
http://localhost:8080/TA-Recruitment-System-Group68/dev/login-as?role=ADMIN
```

> For local development and debugging only. `DevSessionLoginServlet` should be disabled or protected in production deployments.

---

## 🔐 Security & Privacy

| Security Measure | Implementation |
| :--- | :--- |
| **RBAC Access Control** | AuthFilter three-stage pipeline; `Role` enum compile-time type safety; unauthorised → 403.jsp |
| **API Key Protection** | `.env.local` is in `.gitignore` — never enters version control |
| **Session Management** | 30-minute timeout; old session invalidated on new login |
| **File Upload Security** | PDF only, max 5MB, stored in server local directory |
| **AI Data Privacy** | Only structured resume data (skills/education/experience summary) is sent — **raw PDF is never transmitted** |
| **Password Security** | Plain text in test environment; production should use `PasswordUtils` hash + salt |

---

## 📚 Documentation

| Document | Description |
| :--- | :--- |
| [`docs/User_Manual.md`](docs/User_Manual.md) | Complete English user manual (with screenshots, flowcharts, FAQ, appendices) |
| [`docs/用户手册_中文.md`](docs/用户手册_中文.md) | Complete Chinese user manual (1700+ lines) |
| [`docs/代码结构树.md`](docs/代码结构树.md) | Code structure tree + directory responsibility descriptions |
| [`docs/简历输入模块中文说明手册.md`](docs/简历输入模块中文说明手册.md) | Resume input & matching module detailed manual (11 chapters) |
| [`docs/interface-design.md`](docs/interface-design.md) | API interface design contract (694 lines, defines all API contracts) |
| [`docs/page-navigation.md`](docs/page-navigation.md) | Page navigation design document |
| [`docs/AI协作开发提示词.md`](docs/AI协作开发提示词.md) | AI-assisted development prompt documentation (transparency requirements) |
| [`docs/submit/`](docs/submit/) | Course submission materials (Report / Backlog / Prototype) |
| [`README.md`](README.md) | Chinese README |

---

<p align="center">
  <img src="docs/images/logo.svg" alt="TA Recruitment System Logo" width="80">
</p>

<p align="center">
  <sub>Built with ❤️ by <b>Group 68</b> · EBU6304 Software Engineering · BUPT International School</sub><br>
  <sub>© 2026 TA Recruitment System Group 68</sub>
</p>
