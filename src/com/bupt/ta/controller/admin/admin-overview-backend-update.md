## 1) 细化了 `AnalyticsServiceImpl.getSystemOverview()`
修改文件：`src/com/bupt/ta/service/impl/AnalyticsServiceImpl.java`

现在返回结构已经对齐文档里的 admin overview 统一字段：

- `totalTAs`
- `totalMOs`
- `totalPostings`
- `openPostings`
- `pendingApplications`
- `activeRecruitmentCount`
- `recentActivities`

## 2) 统计口径
当前后端口径如下：

- `totalTAs`  
  `Role.TA` 用户总数

- `totalMOs`  
  `Role.MO` 用户总数

- `totalPostings`  
  `data/postings/postings.json` 总条数

- `openPostings`  
  `status = OPEN` 的岗位数

- `pendingApplications`  
  `status = SUBMITTED` 的申请数

- `activeRecruitmentCount`  
  `status = OPEN` 且 `deadline >= 今天` 的岗位数  
  （如果没有 `deadline`，也算 active）

## 3) `recentActivities` 已补齐
每条 activity 现在都包含文档要求字段：

- `activityType`
- `message`
- `timeLabel`
- `relatedEntityType`
- `relatedEntityId`

当前聚合来源：

- TA 注册：`TA_REGISTERED`
- MO 创建：`MO_CREATED`
- 岗位创建/发布：`POST_CREATED`  
  仅当 posting 数据里存在 `createdAt / postedAt / publishedAt / updatedAt`
- 申请提交：`APPLICATION_SUBMITTED`

`timeLabel` 统一格式为：

- `yyyy-MM-dd HH:mm`

## 4) 增加了命令行测试入口
新增文件：

- `scripts/AdminOverviewConsoleRunner.java`
- `scripts/test-admin-overview.ps1`

现在可以直接在命令行里跑 overview，而不用起 Tomcat。

## 5) 顺手补了一个 Windows 下很实用的兼容性修复
修改文件：`src/com/bupt/ta/util/JsonUtils.java`

现在 JSON 解析会自动忽略 UTF-8 BOM。  
这个是因为在 Windows PowerShell 5.1 下，手工生成 JSON 测试数据时很容易带 BOM，不处理的话会导致解析异常。

---

# 我实际验证过的结果

## 场景 1：项目当前真实数据
已成功跑通，输出结果为：

- `totalTAs = 3`
- `totalMOs = 0`
- `totalPostings = 3`
- `openPostings = 3`
- `pendingApplications = 1`
- `activeRecruitmentCount = 3`

并且 `recentActivities` 正常返回了 TA 注册和申请提交记录。

## 场景 2：空数据目录
已成功跑通：

- 所有计数为 `0`
- `recentActivities = []`

说明缺省/初始化场景没问题。

## 场景 3：定制混合数据
已成功验证：

- `openPostings = 2`
- `activeRecruitmentCount = 1`
- `pendingApplications = 1`

同时验证了多种 activity 类型排序正常：

- `APPLICATION_SUBMITTED`
- `POST_CREATED`
- `TA_REGISTERED`
- `MO_CREATED`

---

#怎么测

在 PowerShell 里运行：

```powershell
& "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\scripts\test-admin-overview.ps1"
```

如果要指定测试数据目录：

```powershell
& "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\scripts\test-admin-overview.ps1" -DataDir "C:\your\test\data"
```

如果本机 Java 不在 PATH，也可以指定：

```powershell
& "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\scripts\test-admin-overview.ps1" -JavaHome "E:\jdk-21.0.2"
```

---

# 现在的返回结构示意
实际结构已经会是这种形状：

```json
{
  "totalTAs": 3,
  "totalMOs": 0,
  "totalPostings": 3,
  "openPostings": 3,
  "pendingApplications": 1,
  "activeRecruitmentCount": 3,
  "recentActivities": [
    {
      "activityType": "TA_REGISTERED",
      "message": "deer registered as TA",
      "timeLabel": "2026-03-25 16:32",
      "relatedEntityType": "TA",
      "relatedEntityId": "TA003"
    }
  ]
}
```

---

# 这次涉及的文件

已修改：
- `src/com/bupt/ta/service/impl/AnalyticsServiceImpl.java`
- `src/com/bupt/ta/util/JsonUtils.java`

已新增：
- `scripts/AdminOverviewConsoleRunner.java`
- `scripts/test-admin-overview.ps1`

补充说明：
- `scripts/test-admin-overview.ps1` 当前被仓库的忽略规则忽略了，所以 `git status` 默认不会把它列成未跟踪文件。
- 如果要把这个脚本提交进仓库，后续需要调整 `.gitignore` 或手动强制 add。

