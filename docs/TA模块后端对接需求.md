# TA 模块后端对接需求

## 1. 文档目的

本文档用于 TA 模块与后端逻辑对接时的任务说明。重点回答两件事：

1. TA 模块当前有哪些功能页面和业务动作
2. 每个功能需要后端提供哪些数据、接收哪些参数

本项目当前是 `Servlet + JSP + JSON 文本文件` 架构，因此这里的“后端传输数据”默认有两种落点：

- 若继续沿用当前架构：由 Servlet 写入 `request attribute`
- 若后续抽成接口：由后端返回等价 JSON 结构

## 2. 当前 TA 模块范围

当前 TA 模块主要包括以下页面和入口：

- `GET /ta/dashboard`
- `GET /ta/profile`
- `POST /ta/profile`
- `POST /ta/profile/resume`
- `GET /ta/jobs`
- `GET /ta/jobs/detail`
- `GET /ta/applications/confirm`
- `POST /ta/applications`
- `GET /ta/applications/my`

对应页面文件：

- `web/WEB-INF/views/ta/dashboard.jsp`
- `web/WEB-INF/views/ta/profile.jsp`
- `web/WEB-INF/views/ta/positions.jsp`
- `web/WEB-INF/views/ta/position-details.jsp`
- `web/WEB-INF/views/ta/applications.jsp`

## 3. 当前后端接口与服务现状

当前代码里已经存在的相关 service / servlet 能力：

- `ProfileService.getTAProfile`
- `ProfileService.updateTAProfile`
- `ProfileService.listAllSkillTags`
- `ResumeService.saveOrReplaceTAResume`
- `JobService.searchOpenJobs`
- `JobService.getJobById`
- `ApplicationService.checkEligibility`
- `ApplicationService.createApplication`
- `ApplicationService.listApplicationsByTA`
- `RecommendationService.buildJobMatchForTA`

当前已存在但仍需进一步完善的点：

- `TADashboardServlet` 当前只转发页面，还没有把真实 dashboard 数据塞进页面
- `TA positions` 页面 UI 里有 `major` 过滤，但当前 `JobQuery` 还没有 `major` 字段
- `TA applications` 页面 UI 里有关键词搜索，但当前 `ApplicationQuery` 还没有 `keyword` 字段
- `TA dashboard` 新增了 timetable 展示需求，但当前还没有专门的数据结构或数据文件
- `TA profile` 提交参数名与现有 JSON 字段名存在不完全一致的问题，后端需要统一映射

## 4. 分功能开发需求

### 4.1 Dashboard 页面

页面功能：

- 左侧展示 TA 基本个人信息
- 左侧展示申请统计
- 左侧展示简历状态
- 右侧展示 TA timetable
- 右侧展示简化岗位列表
- 右侧展示最近申请列表

#### 4.1.1 左侧个人信息卡片

功能说明：

- 显示 TA 姓名、学号、专业
- 点击 `View / Edit Profile` 进入 profile 页

后端需要提供的数据：

- `profileSummary.fullName`
- `profileSummary.studentId`
- `profileSummary.majorProgram`

建议结构：

```json
{
  "profileSummary": {
    "taId": "TA001",
    "fullName": "Alex Chen",
    "studentId": "2021001234",
    "majorProgram": "Software Engineering"
  }
}
```

#### 4.1.2 左侧申请统计

功能说明：

- 展示 Pending / Accepted / Rejected 数量

后端需要提供的数据：

- `applicationStats.pendingCount`
- `applicationStats.acceptedCount`
- `applicationStats.rejectedCount`

建议结构：

```json
{
  "applicationStats": {
    "pendingCount": 3,
    "acceptedCount": 1,
    "rejectedCount": 0
  }
}
```

#### 4.1.3 左侧简历状态

功能说明：

- 展示当前简历文件名
- 展示简历状态说明
- 点击 `Upload / Replace` 进入 profile 页上传区
- 点击 `Download` 下载当前简历

后端需要提供的数据：

- `resumeSummary.resumeFileName`
- `resumeSummary.resumeUploadedAt`
- `resumeSummary.hasResume`
- `resumeSummary.downloadUrl`
- `resumeSummary.statusLabel`

建议结构：

```json
{
  "resumeSummary": {
    "hasResume": true,
    "resumeFileName": "sample_resume.pdf",
    "resumeUploadedAt": "2026-03-24 21:10:00",
    "statusLabel": "Verified System PDF",
    "downloadUrl": "/mo/applicants/resume?taId=TA001"
  }
}
```

#### 4.1.4 右侧 timetable

功能说明：

- 展示一个“课程助教”固定条目
- 展示“活动助教”日历
- 日历支持周切换
- 今日蓝色边框高亮
- 过去日期自动变暗
- 点击有任务的日期显示任务详情，并跳转对应岗位详情页

当前状态：

- 前端交互已写在 `web/assets/js/pages/ta-dashboard.js`
- 后端当前还没有真实 timetable 数据输入

后端需要提供的数据：

- `timetable.currentWeekLabel`
- `timetable.courseAssignment`
- `timetable.activityEvents`

`courseAssignment` 需要字段：

- `postingId`
- `courseCode`
- `courseName`
- `label`
- `dayOfWeek`
- `startTime`
- `endTime`
- `location`
- `description`
- `relatedApplicationId`

`activityEvents` 每条需要字段：

- `eventId`
- `postingId`
- `applicationId`
- `title`
- `type`
  可选值建议统一为：`lab` / `exam` / `checkoff`
- `date`
- `startTime`
- `endTime`
- `location`
- `description`

建议结构：

```json
{
  "timetable": {
    "currentWeekLabel": "Week of 25 Mar - 31 Mar",
    "courseAssignment": {
      "postingId": "POST001",
      "courseCode": "SE3001",
      "courseName": "Software Engineering TA",
      "label": "Course TA",
      "dayOfWeek": "TUE",
      "startTime": "14:00",
      "endTime": "16:00",
      "location": "QB-302",
      "description": "Weekly support session",
      "relatedApplicationId": "APP001"
    },
    "activityEvents": [
      {
        "eventId": "EVT001",
        "postingId": "POST001",
        "applicationId": "APP001",
        "title": "SE3001 Lab Support",
        "type": "lab",
        "date": "2026-03-25",
        "startTime": "10:00",
        "endTime": "12:00",
        "location": "QB-302",
        "description": "Guide students through the weekly lab and answer implementation questions."
      }
    ]
  }
}
```

后端特别注意：

- 当前仓库没有数据库，因此 timetable 不能依赖数据库表
- 建议新增一个文本数据文件，例如：
  - `data/system/ta-timetable.json`
  或
  - `data/system/ta-schedule.json`
- 该文件应能按 `taId + week` 返回记录

#### 4.1.5 右侧简化岗位列表

功能说明：

- 在 dashboard 里显示推荐/开放岗位列表
- 每条可点击 `View Details` 进入岗位详情页

后端需要提供的数据：

- `recommendedJobs` 列表

每条岗位建议字段：

- `postingId`
- `courseCode`
- `courseName`
- `moName`
- `deadline`
- `requiredSkills`
- `status`

建议结构：

```json
{
  "recommendedJobs": [
    {
      "postingId": "POST001",
      "courseCode": "SE3001",
      "courseName": "Software Engineering TA",
      "moName": "Prof. Wang",
      "deadline": "2026-03-30",
      "requiredSkills": ["Java", "Testing"],
      "status": "OPEN"
    }
  ]
}
```

#### 4.1.6 右侧最近申请列表

功能说明：

- dashboard 中只展示最近几条申请
- 点击 `View History` 跳到我的申请页对应位置

后端需要提供的数据：

- `recentApplications` 列表

每条申请建议字段：

- `applicationId`
- `postingId`
- `postingTitle`
- `courseCode`
- `status`
- `appliedAt`
- `statusLabel`
- `summaryText`

### 4.2 Profile 页面

页面功能：

- 查看与编辑 TA 基本资料
- 维护技能标签
- 查看当前简历
- 上传 / 替换简历

#### 4.2.1 GET /ta/profile

后端需要提供的数据：

- `profile`
- `allSkillTags`

`profile` 需要字段：

- `taId`
- `fullName`
- `studentId`
- `majorProgram`
- `academicYear`
- `email`
- `phone`
- `intro`
- `skills`
- `resumeFileName`
- `resumeUploadedAt`
- `extractedResume`

`allSkillTags` 需要字段：

- 字符串数组即可，例如 `["Java", "Python", "Testing"]`

当前代码中的字段映射风险：

- 页面与数据文件更偏向：
  - `fullName`
  - `majorProgram`
  - `academicYear`
  - `skills`
- 但 `TAProfileServlet.doPost()` 当前提交参数是：
  - `name`
  - `major`
  - `grade`
  - `skillTags`

建议后端统一做映射：

- `name -> fullName`
- `major -> majorProgram`
- `grade -> academicYear`
- `skillTags -> skills`

否则前后端联调时很容易出现：

- 页面改了但 JSON 文件没更新
- JSON 有数据但页面回显不出来

#### 4.2.2 POST /ta/profile

前端会提交的数据：

- `name`
- `email`
- `phone`
- `major`
- `grade`
- `intro`
- `skillTags[]`

后端处理要求：

- 根据当前登录用户 `taUserId` 更新资料
- 成功后重定向回 `/ta/profile`
- 失败时回填：
  - `errorMessage`
  - `profile`
  - `allSkillTags`

### 4.3 简历上传

接口：

- `POST /ta/profile/resume`

功能说明：

- 上传 PDF 简历
- 保存原文件
- 更新 `resumeFileName`
- 更新 `resumeUploadedAt`
- 解析并回填 `extractedResume`

前端提交的数据：

- multipart file part name: `resumeFile`

后端需要返回/更新的关键结果：

- `resumeFileName`
- `resumeUploadedAt`
- `extractedResume.name`
- `extractedResume.email`
- `extractedResume.phone`
- `extractedResume.education`
- `extractedResume.skills`
- `extractedResume.experienceHighlights`
- `extractedResume.rawText`

验证要求：

- 仅允许 `.pdf`
- 文件不能为空
- 文件大小不超过 `5MB`

### 4.4 岗位列表页面

接口：

- `GET /ta/jobs`

页面功能：

- 搜索岗位
- 按状态筛选
- 按排序方式排序
- 分页浏览岗位
- 进入岗位详情

当前前端 UI 字段：

- `keyword`
- `major`
- `sortBy`

当前后端 `JobQuery` 已支持：

- `keyword`
- `status`
- `sortBy`
- `page`
- `size`

当前缺口：

- 前端已经有 `major` 过滤，但 `JobQuery` 里还没有 `major`

建议后端扩展：

- `major`
  或者更通用地用
- `category`

后端需要返回的数据：

- `jobsPage.records`
- `jobsPage.page`
- `jobsPage.size`
- `jobsPage.total`
- `query`

每条岗位建议字段：

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

### 4.5 岗位详情页

接口：

- `GET /ta/jobs/detail?jobId=...`

页面功能：

- 查看单个岗位详情
- 查看岗位说明
- 查看 required skills
- 查看 estimated workload
- 查看 AI / rule-based match 结果
- 发起申请

后端需要提供两个核心对象：

- `job`
- `matchAnalysis`

#### 4.5.1 job 对象

建议字段：

- `postingId`
- `courseCode`
- `courseName`
- `moId`
- `moName`
- `vacancies`
- `deadline`
- `description`
- `requiredSkills`
- `estimatedWorkloadHours`
- `status`

#### 4.5.2 matchAnalysis 对象

建议字段：

- `score`
- `explanation`
- `method`
- `matchedSkills`
- `missingSkills`

示例：

```json
{
  "matchAnalysis": {
    "score": 85,
    "explanation": "Strong fit for Java, testing, and lab communication requirements.",
    "method": "LOCAL_RULE",
    "matchedSkills": ["Java", "Testing", "Communication"],
    "missingSkills": ["Object-Oriented Programming"]
  }
}
```

### 4.6 申请资格检查与投递

接口：

- `GET /ta/applications/confirm?jobId=...`
- `POST /ta/applications`

#### 4.6.1 资格检查

功能说明：

- 在正式提交前，检查 TA 是否可以申请该岗位

后端需要提供的数据：

- `job`
- `eligibilityResult`

`eligibilityResult` 建议字段：

- `eligible`
- `profileCompleted`
- `resumeUploaded`
- `alreadyApplied`
- `beforeDeadline`
- `reasons`

示例：

```json
{
  "eligibilityResult": {
    "eligible": true,
    "profileCompleted": true,
    "resumeUploaded": true,
    "alreadyApplied": false,
    "beforeDeadline": true,
    "reasons": []
  }
}
```

#### 4.6.2 提交申请

前端提交的数据：

- `jobId`
- `statement`

后端需要做的事：

- 创建 application 记录
- 绑定 `taId`
- 绑定 `postingId`
- 写入 `appliedAt`
- 初始化 `status`
- 可选同步写入匹配结果字段

建议写入字段：

- `applicationId`
- `postingId`
- `postingTitle`
- `taId`
- `taName`
- `appliedAt`
- `status`
- `statement`
- `feedback`
- `skillMatchScore`
- `skillMatchExplanation`
- `matchMethod`

当前状态建议统一：

- `SUBMITTED`
- `UNDER_REVIEW`
- `ACCEPTED`
- `REJECTED`
- `WITHDRAWN`

### 4.7 我的申请页面

接口：

- `GET /ta/applications/my`

页面功能：

- 查看当前所有申请
- 按状态筛选
- 按时间或状态排序
- 查看申请详情
- 查看历史轨迹
- 查看反馈

当前后端 `ApplicationQuery` 已支持：

- `status`
- `page`
- `size`
- `sortBy`

当前前端 UI 已出现但后端未支持的字段：

- `keyword`

建议后端扩展：

- `keyword`

后端需要返回的数据：

- `applicationsPage.records`
- `applicationsPage.page`
- `applicationsPage.size`
- `applicationsPage.total`
- `query`

每条申请建议字段：

- `applicationId`
- `postingId`
- `postingTitle`
- `courseCode`
- `moName`
- `appliedAt`
- `updatedAt`
- `status`
- `statusLabel`
- `statement`
- `feedback`
- `skillMatchScore`
- `skillMatchExplanation`
- `historyLogs`

其中 `historyLogs` 建议结构：

```json
[
  {
    "time": "2026-03-24 20:50:00",
    "action": "SUBMITTED",
    "description": "Application submitted with supporting statement and resume."
  }
]
```

如果短期不打算在后端实现完整时间线，至少应先支持：

- `applicationId`
- `postingTitle`
- `status`
- `appliedAt`
- `feedback`

## 5. 建议统一的数据定义

### 5.1 TA profile 统一字段

建议后端最终统一使用以下字段作为 canonical naming：

- `taId`
- `fullName`
- `studentId`
- `majorProgram`
- `academicYear`
- `email`
- `phone`
- `intro`
- `skills`
- `resumeFileName`
- `resumeUploadedAt`
- `extractedResume`

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

### 5.4 matchAnalysis 统一字段

- `score`
- `explanation`
- `method`
- `matchedSkills`
- `missingSkills`

### 5.5 timetable 新增字段建议

由于 dashboard 新增 timetable 功能，建议新增一类统一数据结构：

- `courseAssignment`
- `activityEvents`

不要把 timetable 逻辑零散塞进：

- `applications.json`
- `postings.json`

否则后续会出现：

- 申请数据和排班数据职责混乱
- 同一岗位多周活动难以维护

## 6. 当前最值得后端优先完成的部分

建议后端按以下顺序推进：

1. 完善 `TA dashboard` 的真实数据注入
   - profile summary
   - application stats
   - resume summary
   - recommended jobs
   - recent applications
2. 建立 timetable 数据源与读取逻辑
3. 补齐 `JobQuery.major` 与 `ApplicationQuery.keyword`
4. 统一 profile 字段映射
5. 增加 application history / feedback 结构

## 7. 给后端同学的简短结论

如果只看 TA 模块，当前对接的核心就是五类数据：

1. `TA profile`
2. `resume + extractedResume`
3. `open postings`
4. `job match analysis`
5. `TA applications + application history`

新增的 dashboard timetable 需要单独数据源，这是当前 TA 模块里**唯一明确新增的数据域**。
