# AI 协作开发提示词

这份文档给小组成员在使用 AI 辅助开发当前仓库时直接复制使用。目标是让 AI 在**当前真实目录结构**下继续开发，避免出现：

- 新旧目录混用
- 重复创建 `src/main/webapp` / `src/main/java`
- JSP 页面命名被改乱
- `data/*.json` 字段定义被随意修改
- 不必要地新增目录、迁移文件、重命名模块

## 1. 推荐使用方式

把下面“通用强约束提示词”完整复制给 AI，再在最后补充你本次的具体任务。

如果任务涉及：

- 改目录结构
- 改数据字段
- 改路由
- 新增页面

要让 AI 先阅读仓库再动手，不要让它直接凭经验生成一套“标准 Java Web 项目结构”。

## 2. 通用强约束提示词

```text
你现在是本仓库的协作开发 AI，请严格基于当前仓库真实结构工作，不要套用你熟悉的默认 Maven / Spring / React 项目结构。

在开始任何改动前，请先阅读并遵守以下事实：

一、项目技术边界
1. 这是一个 Java Servlet / JSP 项目，不是 Spring Boot 项目。
2. 数据存储必须使用文本文件，当前以 JSON 文件为主。
3. 不允许引入数据库。
4. 不要擅自改成 Maven/Gradle 标准目录结构，除非我明确要求你做结构迁移。

二、当前唯一有效的主目录结构
1. 后端主代码目录：src/com/bupt/ta/
2. 测试辅助代码目录：src/test/java/
3. 前端根目录：web/
4. JSP 视图目录：web/WEB-INF/views/
5. 静态资源目录：web/assets/
6. 数据目录：data/
7. 文档目录：docs/

三、当前已存在的 JSP 页面命名，必须优先复用，不要擅自重命名
1. auth: login.jsp, register.jsp
2. ta: dashboard.jsp, profile.jsp, positions.jsp, position-details.jsp, applications.jsp
3. mo: dashboard.jsp, profile-edit.jsp, post-position.jsp, postings.jsp, applicants.jsp, applicant-details.jsp
4. admin: dashboard.jsp, create-mo.jsp, all-mos.jsp, all-jobs.jsp
5. common: header.jsp, sidebar.jsp, footer.jsp, layout.jsp, layout-placeholder.jsp

四、当前部分 controller 仍保留但页面未落地，除非我明确要求，否则不要自动补齐这些页面
1. /WEB-INF/views/auth/role-select.jsp
2. /WEB-INF/views/common/403.jsp
3. /WEB-INF/views/common/error.jsp
4. /WEB-INF/views/admin/mo-detail.jsp
5. /WEB-INF/views/admin/ta-workload.jsp
6. /WEB-INF/views/ta/apply-confirm.jsp

五、当前 Java 包结构必须保持一致
1. controller
2. service
3. service/impl
4. repository/file
5. resume
6. match
7. config
8. dto
9. model
10. filter
11. exception
12. util

六、数据文件定义必须谨慎处理，不要随意改字段名
1. data/users/ta.json
   关键字段示例：
   - taId
   - fullName
   - studentId
   - majorProgram
   - academicYear
   - email
   - phone
   - intro
   - skills
   - resumeFileName
   - resumeUploadedAt
   - extractedResume
2. data/postings/postings.json
   关键字段示例：
   - postingId
   - courseCode
   - courseName
   - moId
   - moName
   - vacancies
   - applicationCount
   - deadline
   - description
   - requiredSkills
   - estimatedWorkloadHours
   - status
3. data/applications/applications.json
   关键字段示例：
   - applicationId
   - postingId
   - postingTitle
   - taId
   - taName
   - appliedAt
   - status
   - statement
   - feedback
   - skillMatchScore
   - skillMatchExplanation

七、如果任务不是“重构目录结构”，请遵守这些约束
1. 不要新增平行目录树。
2. 不要把已有页面从 web/ 再复制一份到别处。
3. 不要把 com.bupt.ta 改成别的包名。
4. 不要引入新的全局命名体系。
5. 不要擅自批量重命名 JSP 文件。
6. 不要在未确认的情况下修改 JSON 数据结构。

八、你的工作流程必须是
1. 先阅读与任务直接相关的真实文件。
2. 明确指出你将修改哪些文件。
3. 仅在当前目录结构中实施最小必要改动。
4. 如果你发现任务会影响目录结构、页面命名、数据定义、路由约定，请先暂停并说明影响，再等待确认。
5. 修改后进行基本验证。

九、验证方式
如果改动涉及 Java 代码，请优先使用这条编译检查命令验证：
powershell -ExecutionPolicy Bypass -Command "javac -encoding UTF-8 -cp 'lib/pdfbox-app-3.0.2.jar;lib/gson-2.11.0.jar;lib/javax.servlet-api-4.0.1.jar' -d build/module-check (Get-ChildItem -Recurse -Filter *.java src/com/bupt/ta | ForEach-Object { $_.FullName })"

十、文档同步要求
如果你的改动影响以下内容，也请同步更新对应文档：
1. README.md
2. requirements.txt
3. docs/代码结构树.md
4. docs/简历输入模块中文说明手册.md
5. work-log.md（仅追加新的阶段记录，不要篡改旧阶段事实）

十一、输出要求
1. 最终请明确列出你修改了哪些文件。
2. 如果有未解决风险或仓库中本来就缺失的页面/路由，请单独指出。

现在请先阅读与我任务直接相关的文件，再开始工作。

我的本次任务是：
[把这里替换成你的具体需求]
```

## 3. 适合直接复制的简短版

如果你不想每次都贴完整版本，可以先用这个简短版：

```text
请严格基于当前仓库真实结构开发：
- 后端主代码只在 src/com/bupt/ta/
- 前端根目录只在 web/
- JSP 只在 web/WEB-INF/views/
- 不要引入数据库
- 不要擅自改包名、改目录结构、改 JSON 字段名、批量重命名 JSP
- 先阅读相关文件，再做最小必要改动
- 如果改动会影响目录结构、路由、页面命名、数据定义，先停下来说明影响
- 修改后做基本验证，并告诉我改了哪些文件

我的任务是：
[把这里替换成你的具体需求]
```

## 4. 常用任务追加语句

你可以把下面这些句子接在提示词最后，帮助 AI 更稳定地执行。

### 4.1 继续做现有页面功能

```text
请只在现有页面和现有 controller/service 范围内继续接功能，不要新建平行页面，不要新增另一套命名。
```

### 4.2 修复 bug

```text
请以最小修改修复问题，优先保持现有目录、现有数据结构、现有页面命名不变。
```

### 4.3 新增一个页面

```text
如果必须新增页面，请严格放在 web/WEB-INF/views/ 对应角色目录下，并沿用当前命名风格；新增前先检查是否已有语义等价页面。
```

### 4.4 新增数据字段

```text
如果你判断必须新增 JSON 字段，请先说明新增原因、影响文件和兼容性影响，未经确认不要直接改 data 文件结构。
```

### 4.5 做目录整理

```text
这次任务涉及目录整理。请先列出详细改动计划，再等我确认。未经确认不要移动目录或批量重命名。
```

## 5. 建议组员避免对 AI 说的话

下面这些说法很容易让 AI 擅自重构：

- “把这个项目整理成标准 Java Web 项目”
- “按最佳实践重构整个目录”
- “顺便把命名统一一下”
- “把旧结构清理掉”
- “你看着改得更合理一点”

更好的说法是：

- “只在当前目录结构下继续开发”
- “不要新增平行目录”
- “不要改数据字段定义”
- “如需改结构先给详细计划”
- “以当前 web 目录和 src/com/bupt/ta 为唯一主结构”

## 6. 给组员的最终建议

如果任务比较大，最好分三段发给 AI：

1. 先让 AI 阅读相关文件并复述当前结构理解
2. 再让 AI 给改动计划
3. 确认后再让 AI 动手改

这样最能避免它脱离当前仓库现状，擅自生成另一套目录体系。
