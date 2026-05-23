# Acceptance Testing Tasks / 现场验收测试任务

## 1. What "Test Programs" Means

In this software engineering submission, **test programs** means runnable code or scripts that verify the software behavior. They are not only a written testing report.

本项目的 test programs 是可运行的测试脚本和 Java 控制台测试程序，用来证明系统满足需求，而不只是文字版测试说明。

Run the full test suite:

```powershell
cd "E:\BUPT\25-26软件工程\TA-Recruitment-System-Group68"
powershell -NoProfile -ExecutionPolicy Bypass -File .\test-programs\run-all-tests.ps1
```

The runner copies `data/` into `.acceptance-test-data/` and runs tests against that isolated copy, so it does not damage the normal demonstration data.

## 2. Accounts for Live Acceptance

| Role | Username | Password | Main Purpose |
| --- | --- | --- | --- |
| TA Applicant | `syy` | `1234567890syy` | Student-side browsing, profile, resume, application, withdrawal |
| Module Organizer | `mo_demo` | `Temp123!` | Posting management, applicant review, suitability analysis, decisions |
| Admin | `admin` | `Admin123!` | System overview, MO management, all jobs, TA workload |

Start the web application:

```powershell
cd "E:\BUPT\25-26软件工程\TA-Recruitment-System-Group68"
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\run-web-app.ps1
```

Then open:

```text
http://localhost:8080/TA-Recruitment-System-Group68/auth/login
```

## 3. TA Applicant Testing Task

**Goal:** Verify that a student can prepare their profile, inspect roles, receive matching advice, and manage applications.

Suggested live steps:

1. Log in as `syy`.
2. Open the TA dashboard and point out the profile card, application statistics, resume status, weekly schedule, available positions, and recent applications.
3. Open `View / Edit Profile`, confirm profile fields and skills, and show resume download.
4. Open the position list, filter or search roles, and open a role detail page.
5. Explain the suitability analysis: the system can invoke an AI model when configured; otherwise it uses the local rule-based fallback. The analysis includes match score, strengths, missing evidence, and application advice.
6. Apply for one open role, then return to `My Applications`.
7. Demonstrate one normal withdrawal for a not-yet-accepted application.
8. Demonstrate a revocation request for an accepted application, if an accepted role is available.

Expected result:

- The student can see relevant jobs and profile information.
- A submitted application appears in the application list.
- A normal withdrawal becomes `WITHDRAWN`.
- Withdrawing an accepted application becomes `REVOCATION_REQUESTED`.
- The changes are stored in the JSON data files and remain after refresh.

Questions each member should be ready to answer:

- Which requirement does this verify? Student application management and recommendation support.
- Where is the data stored? Mainly `data/users/ta.json`, `data/postings/postings.json`, `data/applications/applications.json`, and `data/resumes/`.
- Is the AI analysis a final decision? No. It is advisory and supports human review.

## 4. MO Testing Task

**Goal:** Verify that a module organizer can manage jobs and review applications.

Suggested live steps:

1. Log in as `mo_demo`.
2. Open the MO dashboard and show active postings, alerts, and recent activity.
3. Open posting management and create or edit a job posting.
4. Open the applicants list for a posting.
5. Open an applicant detail page and show profile, resume access, suitability score, matched skills, missing evidence, and advice.
6. Accept one application and reject another.
7. If a TA has requested revocation, approve or decline the revocation request.

Expected result:

- A new or edited posting appears in the MO posting list.
- Applicant records are visible to the correct MO only.
- Resume download works when the stored PDF exists.
- Application status changes are saved and reflected in the TA side.

Questions each member should be ready to answer:

- Which requirement does this verify? Job posting, applicant review, decision recording, and role-based access.
- What happens after a decision? Status and feedback are saved in `data/applications/applications.json`.
- Why is human review still needed? Suitability analysis is advisory and should be checked against the original resume.

## 5. Admin Testing Task

**Goal:** Verify that the administrator can monitor the system and manage organizer accounts.

Suggested live steps:

1. Log in as `admin`.
2. Show dashboard metrics: total TAs, MOs, postings, open postings, pending applications, and recent activity.
3. Open MO management, search existing MOs, and create a test MO if needed.
4. Open all jobs and show filtering or overview information.
5. Open TA workload and show workload detail for a student.

Expected result:

- Admin can view cross-system data, not only one MO's data.
- Admin can create/search MO accounts.
- Admin can inspect all jobs and TA workload distribution.

Questions each member should be ready to answer:

- Which requirement does this verify? Administrative monitoring and account management.
- How is workload calculated? It is derived from accepted applications and posting workload hours.
- What should be checked if numbers look wrong? Confirm the relevant JSON data files and accepted application statuses.

## 6. Automated Test Programs Mapping

| Test Program | Requirement Area | What It Verifies |
| --- | --- | --- |
| `AuthAndProfileTest` | Authentication, profile management | Correct role login, failed login, TA profile read/update |
| `TAWorkflowTest` | Student-side workflow | Job browsing, suitability analysis, apply, withdraw, revocation request |
| `MOWorkflowTest` | MO-side workflow | Create/edit posting, applicant review, resume download, accept/reject, revocation response |
| `AdminWorkflowTest` | Admin workflow | Dashboard metrics, MO creation/search/detail, all jobs, TA workload |
| `DataIntegrityTest` | Persistence and file data | JSON readability, key IDs, resume existence, status persistence |

## 7. If Something Fails During Live Acceptance

- If the page has no data, rerun `scripts\run-web-app.ps1` so Tomcat points to this project's `data/` directory.
- If resume download returns 404, check that the TA's `resumeFileName` exists under `data/resumes/`.
- If an application cannot be submitted, check duplicate application, deadline, profile completeness, resume presence, and schedule conflict reasons.
- If AI matching is not available, explain that the system uses a local rule-based fallback unless `LLM_API_URL`, `LLM_API_KEY`, and `LLM_MODEL` are configured.
- If test programs fail, rerun `test-programs\run-all-tests.ps1` and read the `[FAIL]` line to identify the module.
