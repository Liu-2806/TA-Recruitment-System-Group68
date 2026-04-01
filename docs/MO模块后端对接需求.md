# MO 模块后端对接需求

## 1. 文档目的

本文档用于 MO 模块与后端逻辑对接时的开发说明，重点明确：

1. MO 模块当前有哪些页面与业务动作
2. 每个页面/动作需要后端提供哪些数据
3. 当前代码里已经有什么接口
4. 当前还缺什么，需要后端优先补什么

本项目当前架构是 `Servlet + JSP + JSON 文本文件`，因此这里的“后端传输数据”默认有两种实现方式：

- 若继续沿用当前架构：由 Servlet 写入 `request attribute`
- 若后续局部改为接口：返回等价 JSON 结构

## 2. 当前 MO 模块范围

当前 MO 模块主要包括以下页面和入口：

- `GET /mo/dashboard`
- `GET /mo/profile`
- `POST /mo/profile`
- `GET /mo/jobs/create`
- `POST /mo/jobs/create`
- `GET /mo/jobs/my`
- `GET /mo/jobs/applicants?jobId=...`
- `GET /mo/applicants/detail?applicationId=...`
- `POST /mo/applications/status`
- `GET /mo/applicants/resume?applicationId=...`

当前前端还新增了一个待审核队列页面，但后端尚未正式接入对应 Servlet：

- `GET /mo/review-queue`
  目前只有前端预览页和 JSP，后端逻辑还没有正式实现

对应页面文件：

- `web/WEB-INF/views/mo/dashboard.jsp`
- `web/WEB-INF/views/mo/profile-edit.jsp`
- `web/WEB-INF/views/mo/post-position.jsp`
- `web/WEB-INF/views/mo/postings.jsp`
- `web/WEB-INF/views/mo/applicants.jsp`
- `web/WEB-INF/views/mo/applicant-details.jsp`
- `web/WEB-INF/views/mo/review-queue.jsp`

## 3. 当前后端能力现状

当前代码里已存在的相关 controller / service 能力：

- `ProfileService.getMOProfile`
- `ProfileService.updateMOProfile`
- `JobService.createJob`
- `JobService.listJobsByMO`
- `JobService.getJobById`
- `ApplicationService.listApplicationsByJob`
- `ApplicationService.getApplicationDetailForMO`
- `ApplicationService.updateStatusByMO`
- `RecommendationService.buildApplicationMatchForMO`
- `ResumeService.openResumeStreamForMO`

当前已知缺口：

- `MODashboardServlet` 现在只转发页面，没有注入真实 dashboard 数据
- 当前没有正式的 `/mo/review-queue` Servlet
- `ProfileService` 当前没有改密码接口，但前端已加“Change Password”对话框
- MO profile 的数据存储落点还不清晰，`data/users/` 目前只有 `ta.json`
- `MOJobCreateServlet` 当前接收的参数名与前端页面字段语义不完全一致
- `ApplicationQuery` 当前只支持 `status/page/size/sortBy`，但 MO 待审核队列和申请人列表未来大概率还需要 `keyword`

## 4. 分功能后端需求

### 4.1 Dashboard 页面

接口：

- `GET /mo/dashboard`

页面功能：

- 左侧展示 MO 基本个人信息
- 左侧展示“待审核申请数量”
- 左侧展示提醒信息
- 右侧展示快捷操作卡片
- 右侧展示当前 MO 发布的岗位摘要
- 右侧展示最近招聘活动

#### 4.1.1 左侧个人信息卡片

功能说明：

- 展示 MO 姓名、工号、院系
- 点击进入 profile 编辑页

后端需要提供的数据：

- `profileSummary.moId`
- `profileSummary.fullName`
- `profileSummary.staffId`
- `profileSummary.department`

建议结构：

```json
{
  "profileSummary": {
    "moId": "MO001",
    "fullName": "Prof. Wang",
    "staffId": "M001",
    "department": "School of Software Engineering"
  }
}
```

#### 4.1.2 Awaiting Review 卡片

功能说明：

- 展示当前所有“未处理”的申请数
- 点击 `Manage Review` 进入统一待审核队列页

后端需要提供的数据：

- `reviewSummary.pendingCount`
- `reviewSummary.pendingPostingCount`
- `reviewSummary.reviewQueueUrl`

建议结构：

```json
{
  "reviewSummary": {
    "pendingCount": 8,
    "pendingPostingCount": 3,
    "reviewQueueUrl": "/mo/review-queue"
  }
}
```

#### 4.1.3 System Alerts

功能说明：

- 展示和 MO 当前岗位相关的提醒
- 例如：即将截止、申请人暴增、岗位无人申请等

后端需要提供的数据：

- `alerts.critical[]`
- `alerts.minor[]`

每条提醒建议字段：

- `type`
- `message`
- `relatedPostingId`
- `actionUrl`

#### 4.1.4 我的岗位摘要

功能说明：

- 在 dashboard 中显示当前 MO 发布的若干岗位
- 显示岗位名称与申请人数
- 点击进入对应申请人列表页

后端需要提供的数据：

- `myPostingSummaries[]`

每条岗位建议字段：

- `postingId`
- `courseCode`
- `courseName`
- `applicationCount`
- `status`
- `applicantsUrl`

#### 4.1.5 最近招聘活动

功能说明：

- 展示最近发生的申请、录取、岗位变更等活动

后端需要提供的数据：

- `recentActivities[]`

每条活动建议字段：

- `activityType`
- `postingId`
- `postingTitle`
- `message`
- `timeLabel`

## 4.2 MO Profile 页面

接口：

- `GET /mo/profile`
- `POST /mo/profile`

页面功能：

- 查看与编辑 MO 个人资料
- 修改个人简介
- 修改密码

### 4.2.1 GET /mo/profile

后端需要提供的数据：

- `profile`

建议字段：

- `moId`
- `fullName`
- `staffId`
- `email`
- `department`
- `phone`
- `description`

建议结构：

```json
{
  "profile": {
    "moId": "MO001",
    "fullName": "Prof. Wang",
    "staffId": "M001",
    "email": "wang@bupt.edu",
    "department": "School of Software Engineering",
    "phone": "123-4567-8901",
    "description": "Research interests and teaching background..."
  }
}
```

### 4.2.2 POST /mo/profile

前端需要提交的数据：

- `name`
- `email`
- `phone`
- `description`
- `department`
  当前 servlet 还没有接这个字段，但前端页面已有院系选择，建议一并加入

后端处理要求：

- 成功更新后重定向回 `/mo/profile`
- 失败时回填：
  - `errorMessage`
  - `profile`

### 4.2.3 Change Password

当前状态：

- 前端弹窗已加
- 后端尚无对应接口

建议新增接口：

- `POST /mo/profile/password`

前端提交字段建议：

- `currentPassword`
- `newPassword`
- `confirmNewPassword`

后端需要完成：

- 校验当前密码
- 校验新密码与确认密码一致
- 更新密码
- 返回成功 / 错误信息

建议错误场景：

- 当前密码错误
- 新密码长度不符合要求
- 两次新密码不一致

## 4.3 发布岗位页面

接口：

- `GET /mo/jobs/create`
- `POST /mo/jobs/create`

页面功能：

- 填写岗位信息
- 选择截止日期和时间
- 填写技能要求
- 填写 workload
- 提交创建岗位

### 4.3.1 前端页面需要提交的字段

建议统一为以下 canonical 参数名：

- `courseName`
- `courseCode`
- `vacancies`
- `deadline`
- `description`
- `requiredSkills`
- `estimatedWorkload`

当前 servlet 里已有但建议统一调整的参数映射问题：

当前 `MOJobCreateServlet` 使用：

- `title`
- `courseCode`
- `courseName`
- `description`
- `requirements`
- `workloadHours`
- `headcount`
- `deadline`

当前前端页面语义更接近：

- `courseName`
- `courseCode`
- `vacancies`
- `requiredSkills`
- `estimatedWorkload`

建议后端统一映射为：

- `vacancies -> headcount`
- `requiredSkills -> requirements`
- `estimatedWorkload -> workloadHours`

更理想的方案是直接统一 service / repository 的字段命名，避免前后端长期各叫各的。

### 4.3.2 createJob 返回结果

建议后端返回岗位详情对象：

- `postingId`
- `courseCode`
- `courseName`
- `moId`
- `moName`
- `vacancies`
- `applicationCount`
- `deadline`
- `description`
- `requiredSkills`
- `estimatedWorkloadHours`
- `status`

## 4.4 My Job Postings 页面

接口：

- `GET /mo/jobs/my`

页面功能：

- 查看当前 MO 自己发布的岗位
- 按状态筛选
- 关键字搜索
- 排序
- 查看某岗位申请人
- 未来可能支持编辑、关闭、删除

后端当前已有查询对象：

- `JobQuery`
  已支持：
  - `keyword`
  - `status`
  - `page`
  - `size`
  - `sortBy`

后端需要返回：

- `jobsPage.records`
- `jobsPage.page`
- `jobsPage.size`
- `jobsPage.total`
- `query`

每条岗位建议字段：

- `postingId`
- `courseCode`
- `courseName`
- `deadline`
- `vacancies`
- `applicationCount`
- `status`
- `estimatedWorkloadHours`
- `applicantsUrl`
- `editUrl`
- `canDelete`

## 4.5 Applicants 页面

接口：

- `GET /mo/jobs/applicants?jobId=...`

页面功能：

- 展示某岗位下所有申请人
- 显示申请人基础信息
- 显示 match score
- 显示当前状态
- 进入申请人详情页

后端当前已有：

- `jobService.getJobById(jobId)`
- `applicationService.listApplicationsByJob(jobId, query)`

后端需要提供两个核心对象：

- `job`
- `applicationsPage`

### 4.5.1 job 对象

建议字段：

- `postingId`
- `courseCode`
- `courseName`
- `moId`
- `moName`
- `vacancies`
- `applicationCount`
- `deadline`
- `description`
- `requiredSkills`
- `estimatedWorkloadHours`
- `status`

### 4.5.2 applicationsPage.records 每条申请

建议字段：

- `applicationId`
- `postingId`
- `taId`
- `taName`
- `taEmail`
- `majorProgram`
- `academicYear`
- `status`
- `skillMatchScore`
- `skillMatchExplanation`
- `matchMethod`
- `appliedAt`

当前前端未来大概率还需要：

- `keyword`
  用于按名字搜索

但当前 `ApplicationQuery` 还不支持，建议后端扩展：

- `keyword`

## 4.6 Applicant Detail 页面

接口：

- `GET /mo/applicants/detail?applicationId=...`

页面功能：

- 查看申请人完整信息
- 查看技能标签
- 查看 / 下载简历
- 查看 AI / rule-based match 分析
- 在底部执行 Hire / Reject

当前 controller 已有：

- `applicationService.getApplicationDetailForMO(applicationId, moUserId)`

建议后端返回一个聚合对象 `application`，至少包含以下字段：

### 4.6.1 申请基础信息

- `applicationId`
- `postingId`
- `postingTitle`
- `status`
- `appliedAt`
- `statement`
- `feedback`

### 4.6.2 TA 基础信息

- `taId`
- `taName`
- `studentId`
- `majorProgram`
- `academicYear`
- `email`
- `skills`

### 4.6.3 Resume 信息

- `resumeFileName`
- `resumeUploadedAt`
- `resumeDownloadUrl`

### 4.6.4 Match 分析

- `matchAnalysis.score`
- `matchAnalysis.explanation`
- `matchAnalysis.method`
- `matchAnalysis.matchedSkills`
- `matchAnalysis.missingSkills`

如果不想嵌套，也可以直接平铺：

- `skillMatchScore`
- `skillMatchExplanation`
- `matchMethod`
- `matchedSkills`
- `missingSkills`

## 4.7 更新申请状态

接口：

- `POST /mo/applications/status`

页面动作：

- Hire / Accept
- Reject + Feedback

前端需要提交的数据：

- `applicationId`
- `newStatus`
- `comment`

后端需要完成：

- 校验该申请是否属于当前 MO 的岗位
- 更新申请状态
- 写入备注 / 反馈

当前枚举 `ApplicationStatus` 只有：

- `SUBMITTED`
- `ACCEPTED`
- `REJECTED`

这意味着“未处理”的语义目前只能用 `SUBMITTED` 表示。  
如果后续需要更细的流程，建议扩展：

- `UNDER_REVIEW`
- `WAITLISTED`
- `WITHDRAWN`

## 4.8 简历下载

接口：

- `GET /mo/applicants/resume?applicationId=...`

后端当前已有：

- `ResumeService.openResumeStreamForMO(applicationId, moUserId)`

后端需要返回：

- `contentType`
- `fileName`
- `stream`

后端要求：

- 必须校验当前 MO 是否有权限下载该申请人的简历
- 文件不存在时返回明确错误

## 4.9 Pending Review Queue 页面

当前状态：

- 前端页面和排序脚本已存在
- 后端还没有正式 Servlet

建议新增接口：

- `GET /mo/review-queue`

功能说明：

- 聚合展示当前 MO 所有“未处理”的申请
- 支持排序：
  - 最新提交
  - 最高匹配度
  - 课程代码
  - 申请人姓名

后端需要返回的数据：

- `reviewQueuePage.records`
- `reviewQueuePage.page`
- `reviewQueuePage.size`
- `reviewQueuePage.total`
- `query`

每条记录建议字段：

- `applicationId`
- `postingId`
- `postingTitle`
- `courseCode`
- `taId`
- `taName`
- `taEmail`
- `status`
- `appliedAt`
- `skillMatchScore`
- `priorityLevel`

其中 `priorityLevel` 建议可选值：

- `HIGH`
- `MEDIUM`
- `LOW`

当前前端排序脚本用的是：

- `submitted`
- `match`
- `course`
- `name`

如果后端未来接入真实排序，建议也沿用这四种能力。

## 5. 建议统一的数据定义

### 5.1 MO profile 统一字段

建议后端 canonical naming：

- `moId`
- `fullName`
- `staffId`
- `email`
- `department`
- `phone`
- `description`

### 5.2 posting 统一字段

- `postingId`
- `courseCode`
- `courseName`
- `moId`
- `moName`
- `vacancies`
- `applicationCount`
- `deadline`
- `description`
- `requiredSkills`
- `estimatedWorkloadHours`
- `status`

### 5.3 application 统一字段

- `applicationId`
- `postingId`
- `postingTitle`
- `taId`
- `taName`
- `appliedAt`
- `updatedAt`
- `status`
- `statement`
- `feedback`
- `skillMatchScore`
- `skillMatchExplanation`
- `matchMethod`

### 5.4 applicant detail 聚合字段

建议 `getApplicationDetailForMO` 最终返回一个聚合对象，包含：

- `application`
- `posting`
- `applicantProfile`
- `resume`
- `matchAnalysis`

这样最适合 applicant detail 页面直接渲染。

## 6. 当前最需要后端优先推进的部分

建议后端按下面顺序推进：

1. 给 `MO dashboard` 注入真实数据
   - profile summary
   - pending review stats
   - alerts
   - posting summary
   - recent activity
2. 建立 MO profile 的真实数据源
   - 当前 `data/users/` 里没有明确的 MO 数据文件，建议补充
3. 完善 create job 的参数命名统一
4. 打通 applicants 列表页与 applicant detail 页的真实数据
5. 补上 `/mo/review-queue` 后端逻辑
6. 增加 `change password` 接口

## 7. 给后端同学的简短结论

如果只看 MO 模块，当前最核心的后端数据域有五块：

1. `MO profile`
2. `my postings`
3. `applications by posting`
4. `applicant detail + resume + match analysis`
5. `pending review queue`

其中最关键的新增点有两个：

- `MO 用户数据源`
- `Pending Review Queue 聚合查询`

这两个点如果先补起来，MO 模块的大部分页面都能比较顺地接上真实后端数据。
