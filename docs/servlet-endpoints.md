# TA 招聘系统 Servlet 接口清单（框架版）

本文档用于前后端和数据层联调时快速对照。

## 公共模块
- `GET/POST /auth/login`：登录
- `POST/GET /auth/logout`：登出
- `GET/POST /auth/role-select`：角色选择（可选）

## TA 模块
- `GET/POST /ta/register`：TA 注册
- `GET/POST /ta/profile`：资料查看/编辑
- `POST /ta/profile/resume`：简历上传/替换
- `GET /ta/dashboard`：TA 仪表盘
- `GET /ta/jobs`：岗位列表
- `GET /ta/jobs/detail`：岗位详情
- `GET /ta/applications/confirm`：申请确认页
- `POST /ta/applications`：提交申请
- `GET /ta/applications/my`：我的申请列表

## MO 模块
- `GET /mo/dashboard`：MO 仪表盘
- `GET/POST /mo/profile`：MO 资料
- `GET/POST /mo/jobs/create`：发布岗位
- `GET /mo/jobs/my`：我的岗位
- `GET /mo/jobs/applicants`：岗位申请人列表
- `GET /mo/applicants/detail`：申请人详情
- `GET /mo/applicants/resume`：简历下载
- `POST /mo/applications/status`：更新申请状态

## Admin 模块
- `GET /admin/dashboard`：系统概览
- `GET/POST /admin/mos/create`：创建 MO
- `GET /admin/mos`：MO 列表
- `POST /admin/mos/reset-password`：重置密码
- `GET/POST /admin/mos/detail`：MO 详情编辑
- `GET /admin/jobs`：全系统岗位
- `GET /admin/analytics/ta-workload`：TA 工作量分析

## 系统级
- `GET /error`：统一错误页
- `GET /403`：无权限页
