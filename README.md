# TA-Recruitment-System-Group68

## 小组成员

| GitHub Username | QMID |
| --- | --- |
| `Liu-2806` | `231226532` |
| `ffelaine` | `231226347` |
| `deer-ice` | `231226406` |
| `ShaoyangZhu` | `231226370` |
| `skywalker11111` | `231226439` |
| `NoveAmberic` | `231226495` |


cd F:\softwareengineering\TA-Recruitment-System-Group68
powershell -ExecutionPolicy Bypass -File .\scripts\run-web-app.ps1

http://localhost:8080/TA-Recruitment-System-Group68/

## 项目背景与约束

本项目是 EBU6304 课程的 TA Recruitment System 小组项目。当前仓库中的实现和后续开发必须遵守 handout 中的硬性要求：

- 技术路线必须使用 `Java`，Web 方向可使用 `Java Servlet / JSP`
- 数据必须使用文本文件存储，例如 `.txt`、`.csv`、`.json`、`.xml`
- 不能使用数据库
- 项目需要体现模块化设计，便于 sprint 迭代和持续开发
- AI 可以使用，但必须能够解释 AI 具体做了什么、没有做什么

因此，本仓库当前的实现思路是：

- 使用 `Servlet + JSP` 作为主要 Web 架构
- 使用 `JSON` 文件作为当前业务数据存储格式
- 使用本地文件夹保存上传的 PDF 简历
- 将 AI 能力限制在“岗位匹配分析与解释生成”这一层

## 仓库整体架构说明

当前仓库已经收敛出一套可以继续开发的主结构，但仍保留少量阶段性痕迹，例如旧 demo 的编译产物和历史工作日志。理解仓库时，最重要的是区分“当前真正承载业务逻辑的主框架”与“仅用于保留历史上下文的材料”。

### 1. 当前主框架

当前真正承载控制器、服务层、数据访问层以及输入/匹配模块逻辑的是：

- `src/com/bupt/ta/`

这一层采用典型的分层结构：

- `controller/`
  负责接收请求、组织参数、调用 service、转发 JSP
- `service/`
  负责业务接口定义
- `service/impl/`
  负责业务实现
- `repository/file/`
  负责基于 JSON 文本文件的数据读写
- `resume/`
  负责 PDF 文本提取与结构化处理
- `match/`
  负责岗位匹配、LLM 调用与 fallback 逻辑
- `config/`
  负责统一装配 service 和 repository
- `util/`
  放会话常量等通用工具类

### 2. 当前主框架使用的 JSP 页面

当前控制器实际转发到的 JSP 页面目录是：

- `web/WEB-INF/views/`

也就是说，`src/com/bupt/ta/controller/...` 里的 Servlet 目前 forward 的都是这套页面，例如：

- `/WEB-INF/views/ta/profile.jsp`
- `/WEB-INF/views/ta/position-details.jsp`
- `/WEB-INF/views/mo/applicants.jsp`
- `/WEB-INF/views/mo/applicant-details.jsp`

这套页面是当前业务联动时真正对应的视图层。

### 3. 当前前端资源目录

当前前端资源已经统一收敛到：

- `web/`

该目录下同时包含：

- preview 页面入口
- `assets` 静态资源
- 当前使用的 `WEB-INF/views`
- 当前使用的 `WEB-INF/lib`
- 当前使用的 `WEB-INF/web.xml`

因此，当前已接入业务逻辑的前后端主结构是：

- `src/com/bupt/ta/`
- `web/`

如果你继续在当前业务框架上开发，应优先看这两部分。`work-log.md` 中出现的 `src/main/webapp` 是阶段性历史记录，不再作为当前目录结构说明。

### 4. 数据层结构

当前仓库的数据层目录是：

- `data/`

其中与现有业务最相关的目录包括：

- `data/users/`
  保存 TA 用户资料与结构化简历信息
- `data/postings/`
  保存岗位信息
- `data/applications/`
  保存申请记录
- `data/resumes/`
  保存上传后的 PDF 简历文件
- `data/system/`
  保存系统级辅助数据，例如技能标签
  当前也承载 TA timetable 与岗位时段辅助数据

这种设计符合 handout 中“不能使用数据库”的要求。

### 5. 文档层结构

- `docs/`

主要保存：

- 课程 handout
- backlog / prototype / report 等阶段性交付物
- 当前模块中文说明手册
- 当前代码结构树

### 6. 依赖与环境层

- `lib/`

当前项目依赖的核心 jar 包直接保存在仓库中，例如：

- `pdfbox-app-3.0.2.jar`
- `gson-2.11.0.jar`
- `javax.servlet-api-4.0.1.jar`

这样做的目的，是保证组员在没有 Maven/Gradle 统一构建的情况下，仍然可以在相同环境下完成编译与调试。

## 当前主框架的调用关系

如果只看当前已经接好的业务主链路，可以把调用过程理解为：

1. 浏览器请求进入 `controller`
2. `controller` 从 session 中读取当前用户，提取参数
3. `controller` 调用 `ServiceRegistry`
4. `ServiceRegistry` 返回对应 service 实例
5. service 实现类调用 `repository/file/` 读取或写入 JSON 文件
6. 若是简历上传场景，则还会调用 `resume/` 处理 PDF
7. 若是匹配场景，则还会调用 `match/` 做岗位匹配分析
8. 最终 controller 将结果写入 request attribute 并转发到 JSP

### ServiceRegistry 的作用

当前业务层统一通过：

- `src/com/bupt/ta/config/ServiceRegistry.java`

来装配 service 与 repository。

这意味着：

- controller 不自己 new repository
- controller 不自己拼装复杂依赖
- 业务依赖关系集中在一处，便于后续重构

当前 `ServiceRegistry` 已统一装配：

- `ResumeService`
- `RecommendationService`
- `ProfileService`
- `JobService`
- `ApplicationService`

## 当前已完成的模块任务

本次我负责并已经完成的是两个模块：

- 输入模块
- 匹配模块

下面按模块分别说明。

## 一、输入模块

### 模块目标

输入模块负责完成 TA 上传简历后的基础处理链路，包括：

- 接收 PDF 简历文件
- 保存原始文件
- 提取 PDF 文本
- 生成结构化候选人信息
- 将结果写入文本型数据文件
- 在 TA 页面展示提取后的结果

### 代码落点

输入模块主要写在以下位置：

- `src/com/bupt/ta/controller/ta/TAResumeUploadServlet.java`
- `src/com/bupt/ta/controller/ta/TAResumeDownloadServlet.java`
- `src/com/bupt/ta/controller/ta/TAProfileServlet.java`
- `src/com/bupt/ta/service/ResumeService.java`
- `src/com/bupt/ta/service/impl/ResumeServiceImpl.java`
- `src/com/bupt/ta/resume/PdfResumeExtractor.java`
- `src/com/bupt/ta/resume/ResumeStructurer.java`
- `web/WEB-INF/views/ta/profile.jsp`

### 当前输入模块的处理流程

1. TA 在个人资料页上传 PDF
2. `TAResumeUploadServlet` 接收文件请求
3. `ResumeServiceImpl` 校验上传内容：
   - 只允许 `.pdf`
   - 文件不能为空
   - 文件不能超过 `5MB`
4. PDF 文件保存到 `data/resumes/`
5. `PdfResumeExtractor` 使用 PDFBox 提取文本
6. `ResumeStructurer` 将原始文本结构化
7. 将结构化结果写回 `data/users/ta.json`
8. `TAProfileServlet` 再次读取 profile 并交给 `profile.jsp` 展示

### 当前已提取的结构化字段

当前版本已经支持提取：

- `name`
- `email`
- `phone`
- `education`
- `skills`
- `experienceHighlights`
- `rawText`

### 当前输入模块的数据输出

输入模块完成后，TA 数据会新增或更新这些字段：

- `resumeFileName`
- `resumeUploadedAt`
- `extractedResume`

这些信息保存在：

- `data/users/ta.json`

上传后的 PDF 文件保存在：

- `data/resumes/`

### 当前输入模块页面结果

在：

- `web/WEB-INF/views/ta/profile.jsp`

中已经能够展示：

- 当前简历文件名
- 上传时间
- 结构化简历信息
- 简历下载入口

## 二、匹配模块

### 模块目标

匹配模块负责将候选人的结构化简历信息与岗位要求进行分析，并生成对 MO 有参考价值的结果，包括：

- 匹配分数
- 一段解释文本
- 已匹配技能
- 缺失技能
- 匹配方法标记

### 代码落点

匹配模块主要写在以下位置：

- `src/com/bupt/ta/service/RecommendationService.java`
- `src/com/bupt/ta/match/RecommendationServiceImpl.java`
- `src/com/bupt/ta/match/LocalRuleMatcher.java`
- `src/com/bupt/ta/match/OpenAiCompatibleMatcher.java`
- `src/com/bupt/ta/match/EnvConfigLoader.java`
- `src/com/bupt/ta/controller/ta/TAJobDetailServlet.java`
- `src/com/bupt/ta/controller/ta/TAApplyConfirmServlet.java`
- `src/com/bupt/ta/controller/ta/TAApplicationWithdrawServlet.java`
- `src/com/bupt/ta/controller/mo/MOApplicantDetailServlet.java`
- `src/com/bupt/ta/controller/mo/MOJobApplicantsServlet.java`
- `web/WEB-INF/views/ta/position-details.jsp`
- `web/WEB-INF/views/mo/applicants.jsp`
- `web/WEB-INF/views/mo/applicant-details.jsp`

### 当前匹配模块的处理流程

1. 从 `data/users/ta.json` 读取 TA 的结构化简历
2. 从 `data/postings/postings.json` 读取岗位信息
3. `RecommendationServiceImpl` 判断是否存在完整的 LLM 配置
4. 如果存在：
   - 调用 `OpenAiCompatibleMatcher`
   - 向配置的 OpenAI-compatible / DeepSeek-compatible 接口发送请求
5. 如果不存在，或 LLM 请求失败：
   - 自动退回 `LocalRuleMatcher`
6. 生成统一格式的匹配结果，返回给 controller 和页面

### 当前 LLM 在系统中的职责

当前版本里，AI 不负责 PDF 提取，也不负责直接读原始简历文件。

AI 当前只负责：

- 接收已经结构化好的候选人数据
- 接收单个岗位要求
- 输出：
  - `score`
  - `explanation`

这种边界设计有两个好处：

- 更符合 handout 对 AI 可解释性的要求
- 更容易在 demo / viva 中说明 AI 做了什么，没有做什么

当前 LLM prompt 还额外遵守以下解释性约束：

- 只允许基于输入的结构化候选人信息与岗位信息做判断
- 不允许补充外部事实或臆测缺失信息
- 按技能重合度、教育背景相关性、教学/学术支持经历、岗位职责适配性、关键缺口五个维度综合评分
- `explanation` 必须同时说明支持证据、关键缺口和“仅供人工决策参考”的边界

### 当前匹配结果的统一输出字段

当前匹配模块统一输出：

- `score`
- `explanation`
- `method`
- `matchedSkills`
- `missingSkills`

对于申请记录，还会额外同步：

- `skillMatchScore`
- `skillMatchExplanation`
- `matchMethod`

Current TA-side completion notes:

- `data/postings/postings.json` keeps the original required fields and now also allows optional filtering metadata: `department`, `moduleType`, `roleResponsibilities`
- `/ta/jobs` can filter by `keyword`, `major`, `department`, `moduleType`, and `responsibilityKeyword`
- TA `withdraw / revocation request` updates the application record, decrements `applicationCount`, and releases timetable blocks in `data/system/ta-timetable.json` when the withdrawn record was already accepted
- `/ta/profile` now shows explicit success feedback after profile save and resume replacement, and the API-LLM matcher is expected to return `score`, `explanation`, `matchedSkills`, and `missingSkills`

### 当前匹配模块页面结果

#### TA 侧

在：

- `web/WEB-INF/views/ta/position-details.jsp`

中已经能够展示：

- 岗位信息
- 当前 TA 与岗位的匹配分数
- 匹配解释
- 已匹配技能
- 缺失技能
- 当前使用的匹配方法
- 申请前确认入口

#### MO 侧

在：

- `web/WEB-INF/views/mo/applicants.jsp`

中已经能够展示：

- 某岗位下的申请人列表
- 每个申请人的匹配分数

在：

- `web/WEB-INF/views/mo/applicant-details.jsp`

中已经能够展示：

- 申请人基本信息
- 投递岗位信息
- 匹配分数
- 匹配解释
- 已匹配技能
- 缺失技能
- PDF 简历下载入口

## 本地私有 API 配置方案

为了避免在团队协作中暴露 API 密钥，当前仓库不将密钥写入源码。

推荐方式：

1. 仓库保留 `.env.example`
2. 每个组员在本地创建 `.env.local`
3. 在 `.env.local` 中填写：
   - `LLM_API_URL`
   - `LLM_API_KEY`
   - `LLM_MODEL`

当前代码支持：

- 从系统环境变量读取配置
- 从仓库根目录 `.env.local` 读取配置
- 自动处理带引号的值
- 兼容 DeepSeek base URL 自动补全

`.env.local` 已加入 `.gitignore`，不会被提交。

## 当前用于测试的示例数据

为了让输入模块与匹配模块可以独立验证，仓库中已提供一组文本型 seed 数据：

- `data/users/ta.json`
- `data/postings/postings.json`
- `data/applications/applications.json`
- `data/system/skill-tags.json`
- `data/system/ta-timetable.json`
- `data/resumes/sample_resume.pdf`

当前默认测试身份包括：

- `TA001`
- `MO001`

当前默认测试数据包括：

- `POST001`
- `APP001`

当前 timetable / 冲突校验示例数据还包括：

- `POST002`
- `POST003`

这使得在正式登录尚未接好之前，仍然可以验证你负责的业务主链路。

## 当前如何验证我负责的模块

### 1. 服务层验证

当前最稳定的验证方式是直接跑服务层 smoke test，因为它不依赖完整登录模块。

编译检查命令：

```powershell
javac -encoding UTF-8 -cp "lib/pdfbox-app-3.0.2.jar;lib/gson-2.11.0.jar;lib/javax.servlet-api-4.0.1.jar" -d build/module-check (Get-ChildItem -Recurse -Filter *.java src/com/bupt/ta | ForEach-Object { $_.FullName })
```

你之前已经实际验证过：

- PDF 提取成功
- 结构化简历生成成功
- TA 岗位匹配成功
- MO 申请匹配成功

### 2. 本地网页联调验证

由于正式登录服务尚未完成，当前提供了一个开发专用入口：

- `GET /dev/login-as?role=TA`
- `GET /dev/login-as?role=MO`

其代码位于：

- `src/com/bupt/ta/controller/common/DevSessionLoginServlet.java`

该入口仅用于本地联调，作用是：

- 直接写入测试 session
- 跳过尚未完成的正式登录逻辑
- 快速验证 TA 与 MO 页面是否能读到你的模块结果

推荐测试路径：

#### TA 视角

- `/dev/login-as?role=TA`
- `/ta/profile`
- `/ta/jobs/detail?jobId=POST001`
- `/ta/applications/confirm?jobId=POST003`（可验证 timetable conflict 校验）
- `/ta/applications/my`（可验证 withdraw / revocation request 最小闭环）

#### MO 视角

- `/dev/login-as?role=MO`
- `/mo/jobs/applicants?jobId=POST001`
- `/mo/applicants/detail?applicationId=APP001`

## 当前仓库中需要特别注意的结构事实

为了便于后续组内协作，这里明确说明当前仓库的几个关键事实：

### 1. 当前业务主框架不是旧 demo

旧的 `edu/qmul/ta` demo 仍然保留在仓库中，但它不再是当前主开发路径。  
当前实际接入输入模块和匹配模块的主框架是：

- `src/com/bupt/ta`
- `web/WEB-INF/views`

### 2. 当前前端目录已统一到 `web`

当前前端资源、preview 页面、JSP 视图和 taglib 依赖统一位于：

- `web/`

当前 controller forward 的也是：

- `web/WEB-INF/views/...`

因此，后续协作时应直接以 `web/` 作为当前前端根目录。`work-log.md` 中保留的旧路径仅用于记录阶段过程。

### 3. 正式登录模块尚未完成

当前登录 controller 还没有接好完整的认证 service，因此：

- 不能把“网页登录未完全打通”理解成你负责的输入模块未完成
- 你负责的输入模块和匹配模块已经完成到可独立验证的状态

## 当前我负责模块的完成边界

本次我只完成并维护以下范围：

- PDF 简历上传与保存
- PDF 文本提取
- 简历结构化处理
- 岗位匹配分析
- TA 页面展示匹配结果
- MO 页面展示匹配结果
- 简历 PDF 下载
- 与上述模块直接相关的文档和环境说明

本次没有替其他成员完成：

- 正式登录模块
- 全站统一部署方案
- 其他角色页面的完整业务逻辑
- 整体 UI 美化或设计系统统一

## 环境要求

详见：

- `requirements.txt`

当前核心环境要求为：

- Windows 10 / 11
- JDK 21
- PowerShell
- 本地存在 `lib/` 下的依赖 jar

## 文档维护要求

从当前迭代开始，每次修改本模块相关代码后，必须同步更新：

- `README.md`
- `requirements.txt`
- `docs/简历输入模块中文说明手册.md`
- `docs/代码结构树.md`

这样做的目的，是保证：

- 环境一致
- 文档一致
- 模块边界清晰
- 后续组员能快速接手

## 提交与上传约定

为避免把测试辅助文件误上传到共享提交中，当前仓库默认忽略以下测试内容：

- `src/test/`
- `scripts/test-*.ps1`

如果后续确实需要共享测试代码或测试脚本，应先确认是否属于正式交付范围，再决定是否调整忽略规则。
## 2026-04 Runtime Update

Current runtime conventions for the active web application:

- Use `web/` as the Tomcat web root.
- Do not use historical `src/main/webapp` paths for deployment.
- Compile Java sources from `src/com/bupt/ta` into `WEB-INF/classes` before deploying to Tomcat.
- Use `/dev/login-as?role=TA` and `/dev/login-as?role=MO` for demo routing after deployment.

Current TA/input optimization status:

- `index.jsp` now redirects to the real `/auth/login` route instead of a preview page.
- The local Tomcat deployment scripts now stage `web/` and compiled classes together.
- TA profile and resume flows now keep success/error feedback inside the main JSP flow.
- TA application submit/withdraw flows now return to business pages with visible status feedback instead of raw error pages.

## 2026-04 Match Experience Update

The AI matching flow now keeps the existing core fields and also derives user-facing helper fields at runtime:

- `scoreBand`
- `strengthSummary`
- `riskSummary`
- `nextStepSuggestion`
- `confidenceHint`
- `methodLabel`
- `methodHint`

These helper fields are used only for JSP rendering and are not required as new stored JSON schema fields.
