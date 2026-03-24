# Config Module

Purpose:

- define application-wide constants
- centralize file paths and folder names
- hold route names and default values used by multiple modules

Suggested classes:

- `AppPaths`
- `RoutePaths`
- `SessionKeys`
- `JsonFileNames`

Important details:

- all file storage paths should point to simple text-based data folders
- avoid hardcoding the same path string in multiple Servlets
- keep role names and status values centralized here when reused widely

