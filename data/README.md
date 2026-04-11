# Data Folder

Purpose:

- hold the simple text-based backend data required by the coursework rules

Recommended subfolders:

- `users`
- `postings`
- `applications`
- `resumes`
- `system`

Rules:

- do not use a database
- keep all stored data human-readable where possible

Prototype seed data:

- `data/users/ta.json`, `data/users/mo.json`, and `data/users/admin.json` contain the sample accounts used by the current runnable prototype
- these sample records belong in `data/`, because the live app reads from this folder at runtime
- `demo-data/` is better reserved for optional fixtures or one-off demo assets, not the main runtime user store

