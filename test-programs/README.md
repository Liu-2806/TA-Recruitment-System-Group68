# Test Programs

This directory contains the formal test programs for the TA Recruitment System software submission.

Run all test programs from the project root:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\test-programs\run-all-tests.ps1
```

The runner compiles the main Java source and these console test programs, copies `data/` into `.acceptance-test-data/`, and runs the tests with:

```text
-Dta.data.dir=.acceptance-test-data
```

This means the tests perform real JSON writes and workflow state transitions without changing the demonstration data used by the web app.

The test suite covers:

- `AuthAndProfileTest`: TA/MO/Admin login, failed login, TA profile read/update.
- `TAWorkflowTest`: student-side job browsing, suitability analysis, application, withdrawal, and revocation request.
- `MOWorkflowTest`: MO posting create/edit, applicant review, resume download, accept/reject decisions, and revocation response.
- `AdminWorkflowTest`: admin dashboard, MO creation/search/detail, all-job overview, and TA workload analytics.
- `DataIntegrityTest`: JSON files, key IDs, resume file existence, application status persistence, and repository re-read consistency.

By default, the runner clears AI environment variables and runs deterministic local matching. The product itself can invoke an AI model when `LLM_API_URL`, `LLM_API_KEY`, and `LLM_MODEL` are configured; otherwise it falls back to local rule matching.
