# Frontend Structure

This folder contains the JSP frontend structure for the TA Recruitment System.

## Main folders

- `assets/css`: shared stylesheets
- `assets/js`: shared scripts
- `assets/images`: static images and icons
- `WEB-INF/lib`: JSP tag libraries and local web dependencies
- `WEB-INF/views/common`: shared JSP fragments
- `WEB-INF/views/auth`: authentication pages
- `WEB-INF/views/ta`: TA role pages
- `WEB-INF/views/mo`: MO role pages
- `WEB-INF/views/admin`: Admin role pages

## Naming conventions

- Role page folders use lowercase role names: `auth`, `ta`, `mo`, `admin`
- Page file names use lowercase kebab-case when there are multiple words
- Shared layout fragments stay under `WEB-INF/views/common`
- Page-specific CSS files go under `assets/css/pages`
- Page-specific JS files go under `assets/js/pages`

Examples:

- `WEB-INF/views/auth/login.jsp`
- `WEB-INF/views/ta/dashboard.jsp`
- `WEB-INF/views/mo/post-position.jsp`
- `WEB-INF/views/admin/workload.jsp`
- `assets/css/pages/login.css`
- `assets/js/pages/login.js`

## Role entry structure

- Public entry: `index.jsp`
- Login preview entry: `login-preview.jsp`
- Auth pages: `WEB-INF/views/auth`
- TA pages: `WEB-INF/views/ta`
- MO pages: `WEB-INF/views/mo`
- Admin pages: `WEB-INF/views/admin`

Planned role-based page entry points:

- TA: dashboard, profile, positions, applications
- MO: dashboard, profile, post-position, postings, applicants
- Admin: dashboard, mo-list, ta-list, workload

## Shared layout

- `WEB-INF/views/common/header.jsp`: top bar with branding, page context, notifications, and user info
- `WEB-INF/views/common/sidebar.jsp`: role-aware navigation for TA, MO, and Admin
- `WEB-INF/views/common/footer.jsp`: system, group, and version info
- `WEB-INF/views/common/layout.jsp`: shared page shell for content pages

The shared `layout.jsp` expects request attributes such as:

- `bodyPage`
- `showSidebar`
- `pageTitle`
- `pageEyebrow`
- `currentUserName`
- `currentUserRole`
- `currentUserRoleLabel`
- `currentUserInitial`
- `activeNav`
- `notificationCount`

## Notes

- JSP pages under `WEB-INF/views` are intended to be rendered through servlets.
- Shared CSS and JS files are placed in `assets`.
- Empty role/page folders use `.gitkeep` so the structure can be tracked in Git.
- JSTL dependencies for shared JSP fragments are stored in `WEB-INF/lib`.
