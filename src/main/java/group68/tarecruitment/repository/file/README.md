# File Repository Implementation

Purpose:

- implement JSON-based persistence
- read and write plain files under the project data folders

Suggested classes:

- `JsonUserRepository`
- `JsonPostingRepository`
- `JsonApplicationRepository`
- `JsonFileStorageHelper`

Important details:

- always read and write using UTF-8
- consider write safety to avoid corrupting JSON files
- centralize object-to-JSON conversion
- keep file paths configurable through the config module

