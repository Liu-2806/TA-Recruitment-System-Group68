# Frontend Structure

This folder contains the JSP frontend structure for the TA Recruitment System.

## Main folders

- `assets/css`: shared stylesheets
- `assets/js`: shared scripts
- `assets/images`: static images and icons
- `WEB-INF/views/common`: shared JSP fragments
- `WEB-INF/views/auth`: authentication pages
- `WEB-INF/views/ta`: TA role pages
- `WEB-INF/views/mo`: MO role pages
- `WEB-INF/views/admin`: Admin role pages

## Notes

- JSP pages under `WEB-INF/views` are intended to be rendered through servlets.
- Shared CSS and JS files are placed in `assets`.
- Empty role/page folders use `.gitkeep` so the structure can be tracked in Git.
