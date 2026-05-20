# TA Recruitment System - Group 68

This repository contains Group 68's EBU6304 Software Engineering Group Project: a lightweight Java Servlet/JSP web application for the BUPT International School Teaching Assistant Recruitment System.

The system supports Teaching Assistants (TAs), Module Organisers (MOs), and Admin users through a file-based recruitment workflow. It uses Java, JSP, Servlets, JSON data files, and local JAR dependencies. It does not use a database.

## Group Members

| GitHub Username | QMID |
| --- | --- |
| `Liu-2806` | `231226532` |
| `ffelaine` | `231226347` |
| `deer-ice` | `231226406` |
| `ShaoyangZhu` | `231226370` |
| `skywalker11111` | `231226439` |
| `NoveAmberic` | `231226495` |

## Project Scope

The project addresses the TA recruitment workflow described in the EBU6304 handout. The implemented system focuses on core recruitment functions:

- TA users can maintain applicant profiles, upload CVs, browse available jobs, apply for jobs, and track application status.
- MO users can maintain their profile, post and edit jobs, review applicants, and make application decisions.
- Admin users can view system-level dashboards, manage MO accounts, review all positions, and monitor TA workload.
- The matching component combines local rule-based matching with optional LLM-assisted interpretation, so AI output is explainable and not required for normal local operation.

## Handout Compliance

This project follows the mandatory technical restrictions in the group project handout:

- It is implemented as a lightweight Java Servlet/JSP web application.
- All persistent input and output data is stored in text-based files, mainly JSON.
- No database is used.
- The code is organised into modular controller, service, repository, resume-processing, matching, and utility layers.
- AI-assisted features are optional and supported with local fallback logic.

## Environment

Recommended local environment:

- Operating system: Windows
- Shell: Windows PowerShell
- Java: JDK 17 or a compatible Java Development Kit
- Web server: Apache Tomcat 9
- Current helper scripts target Apache Tomcat `9.0.116`
- Build tool: no Maven or Gradle is required

The project keeps required JAR files in the repository, so it can be compiled and run with the provided scripts.

## Packages and Local Dependencies

Compile-time dependencies in `lib/`:

- `javax.servlet-api-4.0.1.jar`
- `gson-2.11.0.jar`
- `pdfbox-app-3.0.2.jar`

Web runtime dependencies in `web/WEB-INF/lib/`:

- `commons-logging-1.2.jar`
- `fontbox-3.0.3.jar`
- `gson-2.10.1.jar`
- `pdfbox-3.0.3.jar`
- `pdfbox-io-3.0.3.jar`
- `taglibs-standard-impl-1.2.5.jar`
- `taglibs-standard-spec-1.2.5.jar`

## Repository Structure

- `src/com/bupt/ta/` - Java source code, including controllers, services, repositories, resume extraction, matching, filters, models, DTOs, and utilities.
- `web/` - JSP views, static CSS/JS assets, `WEB-INF/web.xml`, and web runtime libraries.
- `data/` - JSON data files and uploaded resume files.
- `lib/` - local compile-time JAR dependencies.
- `scripts/` - PowerShell helper scripts for compilation, deployment, and local running.
- `docs/submit/` - project documents and submission materials.
- `output/` - sample output files for candidate profiles and matching results.

## Configuration

The main run script sets `TA_DATA_DIR` to the repository's `data/` directory. This makes the web application use the local JSON data files in the project folder.

Optional LLM configuration can be added by copying `.env.example` to `.env.local` and filling in:

```text
LLM_API_URL=
LLM_API_KEY=
LLM_MODEL=
```

These settings are only needed for LLM-assisted matching. If they are not provided, the system can still use local rule-based matching.

## Running the Web Application

From the repository root, run:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\run-web-app.ps1
```

Then open:

```text
http://localhost:8080/TA-Recruitment-System-Group68/
```

The run script compiles Java source files into `web/WEB-INF/classes`, deploys the `web/` folder into Tomcat, sets the data directory, and starts Tomcat unless `-SkipStart` is provided.

Optional script parameters:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\run-web-app.ps1 -TomcatPath "C:\path\to\apache-tomcat-9.0.116"
powershell -ExecutionPolicy Bypass -File .\scripts\run-web-app.ps1 -JavaHome "C:\path\to\jdk"
powershell -ExecutionPolicy Bypass -File .\scripts\run-web-app.ps1 -SkipStart
```

## Demo Accounts

The following demo accounts are available in the current JSON data:

| Role | Username | Password |
| --- | --- | --- |
| Admin | `admin` | `Admin123!` |
| TA | `syy` | `1234567890syy` |
| TA | `zjy` | `1234567890zjy` |
| MO | `mo_demo` | `Temp123!` |

Development login shortcuts are also available:

```text
/dev/login-as?role=TA
/dev/login-as?role=MO
/dev/login-as?role=ADMIN
```

For example:

```text
http://localhost:8080/TA-Recruitment-System-Group68/dev/login-as?role=ADMIN
```

## Main Workflows

### TA Workflow

1. Register or log in as a TA.
2. Edit the TA profile.
3. Upload a PDF CV.
4. Browse available positions.
5. View position details and matching information.
6. Apply for suitable positions.
7. Track submitted, accepted, rejected, or withdrawn applications.

### MO Workflow

1. Log in as an MO.
2. Maintain the MO profile.
3. Post new TA positions.
4. Edit existing positions.
5. Review applicants for each position.
6. View applicant details and CV information.
7. Accept or reject applications.

### Admin Workflow

1. Log in as Admin.
2. View the Admin dashboard.
3. Create and manage MO accounts.
4. Review all positions.
5. Inspect individual MO details.
6. Monitor all TA workload records and workload distribution.

## Data Storage

All persistent project data is stored as local files:

- `data/users/admin.json` - Admin accounts.
- `data/users/ta.json` - TA accounts, profile data, skills, and extracted resume data.
- `data/users/mo.json` - MO accounts and profile data.
- `data/postings/postings.json` - posted positions.
- `data/applications/applications.json` - application records.
- `data/notifications/notifications.json` - notification records.
- `data/system/skill-tags.json` - shared skill tags.
- `data/system/ta-timetable.json` - timetable support data.
- `data/resumes/` - uploaded PDF resume files.

## Build and Test Guidance

To compile the main Java source files manually, use the local JAR dependencies:

```powershell
javac -encoding UTF-8 -cp "lib/pdfbox-app-3.0.2.jar;lib/gson-2.11.0.jar;lib/javax.servlet-api-4.0.1.jar" -d build\classes (Get-ChildItem -Recurse -Filter *.java src\com\bupt\ta | ForEach-Object { $_.FullName })
```

Suggested manual acceptance tests:

- TA can log in, view/edit profile, upload a PDF resume, browse jobs, and submit an application.
- MO can log in, post a job, edit a job, review applicants, and update application status.
- Admin can log in, view dashboard metrics, create/manage MO accounts, inspect all jobs, and view TA workload data.
- Invalid or incomplete inputs show validation or error feedback instead of corrupting JSON data.

## Notes for Final Submission

According to the EBU6304 handout, the final software ZIP should include source code, test programs, code documentation, a user manual with key screenshots, and a README file with setup, configuration, and running instructions.

This `README_EN.md` is intended to satisfy the README requirement for the software package. The final project report, demonstration video, and user manual are separate deliverables.
