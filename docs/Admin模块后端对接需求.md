# Admin 模块后端对接需求

## 1. 文档目的

本文档用于 Admin 模块与后端逻辑对接时的开发说明，重点明确：

1. Admin 模块当前有哪些页面与业务动作
2. 每个页面/动作需要后端提供哪些数据
3. 当前代码里已有的 controller / service 能力
4. 当前还缺什么，需要后端补哪些接口或数据结构

本项目当前架构是 `Servlet + JSP + JSON 文本文件`，所以这里提到的“后端传输数据”默认有两种落点：

- 继续沿用当前架构：由 Servlet 写入 `request attribute`
- 若后续局部接口化：返回等价 JSON 结构

## 2. 当前 Admin 模块范围

当前 Admin 模块涉及的主要入口：

- `GET /admin/dashboard`
- `GET /admin/mos/create`
- `POST /admin/mos/create`
- `GET /admin/mos`
- `GET /admin/mos/detail?moUserId=...`
- `POST /admin/mos/detail`
- `POST /admin/mos/reset-password`
- `GET /admin/jobs`
- `GET /admin/analytics/ta-workload`

当前对应页面文件：

- `web/WEB-INF/views/admin/dashboard.jsp`
- `web/WEB-INF/views/admin/create-mo.jsp`
- `web/WEB-INF/views/admin/all-mos.jsp`
- `web/WEB-INF/views/admin/all-jobs.jsp`
- `web/WEB-INF/views/admin/ta-workload.jsp`

当前已知结构现状：

- `AdminMODetailServlet` 已存在，但对应的 `web/WEB-INF/views/admin/mo-detail.jsp` 目前并未落地
- `AdminTAWorkloadServlet` 已存在，且对应页面已补齐
- TA workload 页里的 `Details` 弹窗目前是前端静态数据，还没有真实后端详情接口

## 3. 当前后端能力现状

当前代码里已存在的相关 service / controller 能力：

- `AnalyticsService.getSystemOverview()`
- `AnalyticsService.getTAWorkloadReport(query)`
- `UserService.createMO(params)`
- `UserService.searchMOs(query)`
- `UserService.getMOById(moUserId)`
- `UserService.updateMOByAdmin(params)`
- `UserService.resetPasswordByAdmin(userId, rawPassword)`
- `JobService.searchAllJobsForAdmin(query)`

当前已知缺口：

- `Admin dashboard` 只有 `overview` 概览对象，还没有更细粒度的 activity / alerts 数据契约
- `All MOs` 页面已有列表，但 `MO detail` 页面未落地
- `All TA Workload` 页面已有主表格和详情弹窗，但后端目前只有 `getTAWorkloadReport(query)`，没有单个 TA workload detail 接口
- `data/users/` 当前只看到 `ta.json`，还没有明确的 `mo` 数据文件结构
- `AnalyticsService` 目前对 TA workload 的字段定义仍比较宽泛，缺少统一 contract

## 4. 分功能后端需求

### 4.1 Admin Dashboard 页面

接口：

- `GET /admin/dashboard`

页面功能：

- 展示系统总览统计
- 展示快捷操作
- 展示近期系统活动

当前 controller 已做：

- `request.setAttribute("overview", analyticsService.getSystemOverview())`

#### 4.1.1 overview 需要包含的核心数据

建议 `overview` 至少包含：

- `totalTAs`
- `totalMOs`
- `totalPostings`
- `openPostings`
- `pendingApplications`
- `activeRecruitmentCount`

建议结构：

```json
{
  "overview": {
    "totalTAs": 25,
    "totalMOs": 8,
    "totalPostings": 12,
    "openPostings": 9,
    "pendingApplications": 18,
    "activeRecruitmentCount": 6
  }
}
```

#### 4.1.2 recent activity 建议补充

当前 dashboard 页面已经有 “Recent Activity” 区域，但 controller 还没单独给这块喂数据。

建议后端在 `getSystemOverview()` 里直接补：

- `recentActivities[]`

每条活动建议字段：

- `activityType`
- `message`
- `timeLabel`
- `relatedEntityType`
- `relatedEntityId`

示例：

```json
{
  "recentActivities": [
    {
      "activityType": "POST_CREATED",
      "message": "Prof. Wang posted Software Engineering TA",
      "timeLabel": "2026-03-26 10:15",
      "relatedEntityType": "POSTING",
      "relatedEntityId": "POST001"
    }
  ]
}
```

### 4.2 Create MO Account 页面

接口：

- `GET /admin/mos/create`
- `POST /admin/mos/create`

页面功能：

- 创建新的 MO 账号
- 设置初始密码
- 输入基本资料

当前 servlet 已接收字段：

- `username`
- `name`
- `email`
- `tempPassword`

当前前端页面语义上还包含：

- `staffId`
- `department`
- `phone`
- `confirmPassword`

后端建议统一的创建参数：

- `username`
- `fullName`
- `staffId`
- `email`
- `department`
- `phone`
- `tempPassword`
- `confirmPassword`

当前代码缺口：

- `AdminMOCreateServlet` 还没有接 `staffId / department / phone / confirmPassword`
- 如果前端最终保留这些字段，后端需要同步补接收与校验

后端处理要求：

- 校验用户名 / staffId / email 唯一性
- 校验两次密码一致
- 创建 MO 账号后跳转 `/admin/mos`
- 出错时回填：
  - `errorMessage`
  - `formData`

### 4.3 All MOs 页面

接口：

- `GET /admin/mos`

页面功能：

- 分页查看全部 MO
- 按关键字筛选
- 按状态筛选
- 按排序规则排序

当前 servlet 已做：

- 组装 `query`
  - `keyword`
  - `status`
  - `sortBy`
- `request.setAttribute("mosPage", userService.searchMOs(query))`

后端需要返回：

- `mosPage.records`
- `mosPage.page`
- `mosPage.size`
- `mosPage.total`
- `query`

每条 MO 建议字段：

- `moId`
- `fullName`
- `staffId`
- `email`
- `department`
- `phone`
- `status`

如果当前返回的是 `User` 对象，建议确保 `User` 至少能覆盖以上字段，或在 service 层做 Map / DTO 转换。

### 4.4 MO Detail 页面

接口：

- `GET /admin/mos/detail?moUserId=...`
- `POST /admin/mos/detail`

当前状态：

- controller 已存在
- 页面 `admin/mo-detail.jsp` 尚未落地

后端仍需提供的数据契约，方便页面后续接入：

#### 4.4.1 GET /admin/mos/detail

需要返回：

- `mo`

建议字段：

- `moId`
- `fullName`
- `staffId`
- `email`
- `phone`
- `department`
- `description`
- `status`
- `createdAt`
- `postingCount`

#### 4.4.2 POST /admin/mos/detail

前端预计提交：

- `moUserId`
- `name`
- `email`
- `phone`
- `description`
- `status`

建议后端统一字段命名：

- `name -> fullName`

处理要求：

- 成功后重定向回当前 detail 页面
- 失败时回填：
  - `errorMessage`
  - `mo`

### 4.5 Reset MO Password

接口：

- `POST /admin/mos/reset-password`

当前 servlet 已接收：

- `moUserId`
- `newPassword`

后端处理要求：

- 校验管理员权限
- 校验目标 MO 存在
- 重置密码
- 成功后跳回 `/admin/mos`
- 失败时进入 error 页面

建议未来扩展：

- `confirmPassword`
- `forceResetOnNextLogin`

### 4.6 All Job Positions 页面

接口：

- `GET /admin/jobs`

页面功能：

- 查看全系统岗位
- 按关键字筛选
- 按状态筛选
- 按 MO 筛选
- 分页展示

当前后端 `JobQuery` 已支持：

- `keyword`
- `status`
- `page`
- `size`
- `sortBy`

当前前端页面语义还包括：

- `moFilter`

但 `JobQuery` 里还没有这个字段。建议后端扩展：

- `moId`
  或
- `ownerId`

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
- `moId`
- `moName`
- `vacancies`
- `applicationCount`
- `deadline`
- `status`
- `estimatedWorkloadHours`

### 4.7 All TA Workload 页面

接口：

- `GET /admin/analytics/ta-workload`

页面功能：

- 按姓名 / 学号筛选 TA
- 按专业筛选
- 按 workload status 筛选
- 展示 TA 当前岗位数和总工时
- 展示 workload 状态
- 查看单个 TA 的详情弹窗
- 展示底部 workload 分布分析

当前 servlet 已组装 query：

- `keyword`
- `term`
- `minHours`
- `maxHours`

当前页面实际更贴近的筛选维度是：

- `keyword`
- `major`
- `status`

建议后端统一 query 字段为：

- `keyword`
- `major`
- `status`
- `page`
- `size`
- `sortBy`

当前 `AnalyticsService.getTAWorkloadReport(query)` 建议返回：

- `reportPage.records`
- `reportPage.page`
- `reportPage.size`
- `reportPage.total`

每条 TA workload 记录建议字段：

- `taId`
- `fullName`
- `studentId`
- `majorProgram`
- `activePositionCount`
- `totalWorkloadHours`
- `workloadStatus`

示例：

```json
{
  "reportPage": {
    "records": [
      {
        "taId": "TA001",
        "fullName": "Alex Chen",
        "studentId": "2021001234",
        "majorProgram": "Software Engineering",
        "activePositionCount": 2,
        "totalWorkloadHours": 8,
        "workloadStatus": "NORMAL"
      }
    ],
    "page": 1,
    "size": 10,
    "total": 25
  }
}
```

#### 4.7.1 workload status 建议枚举

建议统一值：

- `NORMAL`
- `HIGH_ALERT`
- `NOT_APPLIED`

前端可再映射成人类可读文案：

- `Normal`
- `High Alert`
- `Not Applied`

#### 4.7.2 单个 TA 详情弹窗

当前状态：

- 前端弹窗已加
- 但当前数据还是静态写死

建议新增接口：

- `GET /admin/analytics/ta-workload/detail?taId=...`

建议返回聚合对象：

- `taProfile`
- `workingPositions`
- `workloadAnalysis`
- `adminSuggestions`

##### `taProfile`

- `taId`
- `fullName`
- `studentId`
- `majorProgram`
- `academicYear`
- `email`
- `phone`

##### `workingPositions`

每条建议字段：

- `postingId`
- `courseCode`
- `courseName`
- `roleType`
  可选：`COURSE_TA` / `LAB` / `INVIGILATION` / `CHECKOFF`
- `workloadHours`
- `status`

##### `workloadAnalysis`

建议字段：

- `totalWorkloadHours`
- `activePositionCount`
- `peakDay`
- `riskLevel`
- `statusLabel`

##### `adminSuggestions`

- 字符串数组即可

示例：

```json
{
  "detail": {
    "taProfile": {
      "taId": "TA001",
      "fullName": "Alex Chen",
      "studentId": "2021001234",
      "majorProgram": "Software Engineering",
      "academicYear": "Year 3",
      "email": "alex.chen@bupt.edu.cn",
      "phone": "+44 7123 456789"
    },
    "workingPositions": [
      {
        "postingId": "POST001",
        "courseCode": "SE3001",
        "courseName": "Software Engineering TA",
        "roleType": "COURSE_TA",
        "workloadHours": 6,
        "status": "ACTIVE"
      }
    ],
    "workloadAnalysis": {
      "totalWorkloadHours": 8,
      "activePositionCount": 2,
      "peakDay": "Friday",
      "riskLevel": "LOW",
      "statusLabel": "Normal workload distribution"
    },
    "adminSuggestions": [
      "Safe to keep current assignments.",
      "Can absorb one short operational duty if needed."
    ]
  }
}
```

#### 4.7.3 底部 workload distribution analysis

当前页面下方已有整体分析区，建议后端补一个总览对象：

- `distributionSummary`

建议字段：

- `hourBuckets`
  例如：
  - `0-4`
  - `4-8`
  - `8-12`
  - `12+`
- `peakWorkloadGroup`
- `criticalAlertCount`

示例：

```json
{
  "distributionSummary": {
    "hourBuckets": [
      { "label": "0-4h", "count": 4 },
      { "label": "4-8h", "count": 7 },
      { "label": "8-12h", "count": 10 },
      { "label": "12h+", "count": 2 }
    ],
    "peakWorkloadGroup": "Software Dept",
    "criticalAlertCount": 2
  }
}
```

## 5. 建议统一的数据定义

### 5.1 admin overview 统一字段

- `totalTAs`
- `totalMOs`
- `totalPostings`
- `openPostings`
- `pendingApplications`
- `activeRecruitmentCount`
- `recentActivities`

### 5.2 MO 管理对象统一字段

- `moId`
- `fullName`
- `staffId`
- `email`
- `department`
- `phone`
- `description`
- `status`

### 5.3 岗位管理对象统一字段

- `postingId`
- `courseCode`
- `courseName`
- `moId`
- `moName`
- `vacancies`
- `applicationCount`
- `deadline`
- `status`
- `estimatedWorkloadHours`

### 5.4 TA workload 统一字段

- `taId`
- `fullName`
- `studentId`
- `majorProgram`
- `academicYear`
- `email`
- `phone`
- `activePositionCount`
- `totalWorkloadHours`
- `workloadStatus`

## 6. 当前最需要后端优先推进的部分

建议后端按以下顺序推进：

1. 细化 `AnalyticsService.getSystemOverview()` 的返回结构
   - 补 recent activities
   - 补 admin dashboard 真正需要的 overview 字段
2. 打通 `All MOs` 列表与 `createMO`
   - 补 staffId / department / phone 等字段
3. 明确 MO 用户数据源
   - 当前 `data/users/` 只看到 `ta.json`
4. 打通 `All Job Positions` 的管理员过滤能力
   - 尤其是按 MO 筛选
5. 扩展 `getTAWorkloadReport(query)`
   - 支持 major / status
   - 返回 workload status 统一枚举
6. 增加 `TA workload detail` 聚合接口
   - 供 `Details` 弹窗使用
7. 落地 `Admin MO detail` 页面所需数据

## 7. 给后端同学的简短结论

如果只看 Admin 模块，后端最核心的数据域有四块：

1. `Admin dashboard overview`
2. `MO account management`
3. `All system postings`
4. `TA workload analytics`

其中当前最关键的新增能力有两个：

- `Admin TA workload detail` 单个 TA 聚合详情
- `MO 数据文件 / 数据源` 的明确落地

这两个点补齐后，Admin 模块的大部分页面就能从静态预览顺利转成真实联调页。
