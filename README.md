<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/Servlet-4.0-5DB75D?style=for-the-badge&logo=apache&logoColor=white" alt="Servlet 4.0">
  <img src="https://img.shields.io/badge/JSP-2.3-6DB33F?style=for-the-badge&logo=apache&logoColor=white" alt="JSP 2.3">
  <img src="https://img.shields.io/badge/Tomcat-9.x-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=black" alt="Tomcat 9">
  <img src="https://img.shields.io/badge/PDFBox-3.0.2-FF6F61?style=for-the-badge&logo=apache&logoColor=white" alt="PDFBox">
  <img src="https://img.shields.io/badge/No%20Database-JSON%20Only-4FC3F7?style=for-the-badge&logo=json&logoColor=white" alt="No Database">
  <img src="https://img.shields.io/badge/AI%20Matching-LLM%20%2B%20Rules-AB47BC?style=for-the-badge&logo=openai&logoColor=white" alt="AI Matching">
</p>

<h1 align="center">
  <br>
  🎓 TA Recruitment System
  <br>
  <sup><sub>BUPT International School — Group 68 | EBU6304 Software Engineering</sub></sup>
  <br>
</h1>

<p align="center">
  <b>一个覆盖助教招聘全流程的智能 Web 系统</b><br>
  连接 <code>TA 申请者</code> · <code>课程组织者 (MO)</code> · <code>系统管理员 (Admin)</code> 三大角色<br>
  从岗位发布 → 简历投递 → <b>AI 智能匹配</b> → 人工审核 → 工作量分析的完整闭环
</p>

<p align="center">
  <a href="#-team-members">Team</a> ·
  <a href="#-quick-start">Quick Start</a> ·
  <a href="#-architecture">Architecture</a> ·
  <a href="#-core-features">Features</a> ·
  <a href="#-ai-intelligent-matching">AI Matching</a> ·
  <a href="#-innovations--highlights">Innovations</a> ·
  <a href="#-project-structure">Structure</a> ·
  <a href="#-tech-stack">Tech Stack</a> ·
  <a href="#-demo-accounts">Demo</a>
</p>

---

## 👥 Team Members

| GitHub Username | QMID |
| :--- | :--- |
| `Liu-2806` | `231226532` |
| `ffelaine` | `231226347` |
| `deer-ice` | `231226406` |
| `ShaoyangZhu` | `231226370` |
| `skywalker11111` | `231226439` |
| `NoveAmberic` | `231226495` |

---

## 🚀 Quick Start

```powershell
# 1. 克隆项目
git clone https://github.com/Liu-2806/TA-Recruitment-System-Group68.git
cd TA-Recruitment-System-Group68

# 2. 确保 JDK 21 已安装
java -version  # 应输出 21.x

# 3. 一键启动（需本地安装 Tomcat 9）
powershell -ExecutionPolicy Bypass -File .\scripts\run-web-app.ps1

# 4. 浏览器访问
# http://localhost:8080/TA-Recruitment-System-Group68/
```

> **环境要求：** Windows 10/11 · JDK 21 · Apache Tomcat 9.x · PowerShell 5.x+

---

## 🏗 Architecture

```
┌──────────────────────────────────────────────────────────────────────┐
│                          Browser (用户端)                             │
│              TA Applicant  │  Module Organizer  │  Admin              │
└────────────────────────────┬─────────────────────────────────────────┘
                             │ HTTP / HTTPS
┌────────────────────────────▼─────────────────────────────────────────┐
│                      Apache Tomcat 9.x                                │
│                                                                       │
│  ┌───────────────────────────────────────────────────────────────┐   │
│  │  Presentation Layer (JSP + JSTL + CSS3 + JS)                  │   │
│  │  layout.jsp │ sidebar.jsp │ header.jsp │ footer.jsp            │   │
│  │  ──────────────────────────────────────────────────────────    │   │
│  │  auth/ │ ta/ │ mo/ │ admin/ │ common/  (34 JSP Views)         │   │
│  └───────────────────────────────────────────────────────────────┘   │
│                              │                                        │
│  ┌───────────────────────────▼───────────────────────────────────┐   │
│  │  Controller Layer (Servlet 4.0 — Annotation-based)            │   │
│  │                                                               │   │
│  │  auth/          LoginServlet │ Register │ Logout │ RoleSelect │   │
│  │  ta/            Dashboard │ Profile │ Jobs │ Apply │ MyApps   │   │
│  │  mo/            Dashboard │ Jobs CRUD │ Applicants │ Review   │   │
│  │  admin/         Dashboard │ MO Mgmt │ Job Monitor │ Workload  │   │
│  │  common/        AuthFilter │ Error │ Legal │ DevLogin │ Notif  │   │
│  └───────────────────────────────────────────────────────────────┘   │
│                              │                                        │
│  ┌───────────────────────────▼───────────────────────────────────┐   │
│  │  Service Layer (Interface + Impl, DI via ServiceRegistry)     │   │
│  │                                                               │   │
│  │  AuthService │ ProfileService │ JobService                    │   │
│  │  ApplicationService (6-state lifecycle engine)                │   │
│  │  ResumeService │ RecommendationService (Dual-engine)          │   │
│  │  AnalyticsService │ UserService │ NotificationService         │   │
│  └──────────┬──────────────────────────────┬────────────────────┘   │
│             │                              │                         │
│  ┌──────────▼──────────┐    ┌──────────────▼──────────────────┐     │
│  │  Repository Layer    │    │  Specialized Modules             │     │
│  │  (JSON File-based)   │    │                                  │     │
│  │  TADataRepository    │    │  resume/                          │     │
│  │  PostingDataRepo     │    │  ├─ PdfResumeExtractor (PDFBox)  │     │
│  │  ApplicationDataRepo │    │  └─ ResumeStructurer (Heuristic) │     │
│  │  SystemDataRepo      │    │                                  │     │
│  │  TimetableDataRepo   │    │  match/                           │     │
│  │  + Gson serialization│    │  ├─ OpenAiCompatibleMatcher      │     │
│  │  + synchronized I/O  │    │  ├─ LocalRuleMatcher             │     │
│  └──────────┬───────────┘    │  └─ RecommendationServiceImpl    │     │
│             │                └──────────────────────────────────┘     │
│  ┌──────────▼───────────────────────────────────────────────────┐    │
│  │  Data Persistence Layer (File-based, No Database)             │    │
│  │                                                              │    │
│  │  data/users/       ta.json │ mo.json │ admin.json            │    │
│  │  data/postings/    postings.json                             │    │
│  │  data/applications/ applications.json                        │    │
│  │  data/notifications/ notifications.json                      │    │
│  │  data/system/      skill-tags.json │ ta-timetable.json       │    │
│  │  data/resumes/     *.pdf (uploaded CV files)                 │    │
│  └──────────────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────────────┘
```

---

## 🎯 Core Features

### 三大角色功能矩阵

| 功能 | TA 申请者 | MO 课程组织者 | Admin 管理员 |
| :--- | :---: | :---: | :---: |
| 注册 / 登录 | ✅ | ✅ (Admin 创建) | ✅ |
| 个人资料管理 | ✅ | ✅ | — |
| PDF 简历上传与智能提取 | ✅ | — | — |
| 岗位浏览与多维筛选 | ✅ | — | 全局监控 |
| **AI 智能匹配分析** | ✅ | ✅ | — |
| 在线申请与状态追踪 | ✅ | — | — |
| 申请撤回 / 撤销请求 | ✅ | — | — |
| 岗位 CRUD + 级联撤回 | — | ✅ | — |
| 申请人审核 (Accept/Reject) | — | ✅ | — |
| 审核队列与优先级 | — | ✅ | — |
| 实时通知系统 (7 种类型) | ✅ | ✅ | — |
| MO 账号管理 | — | — | ✅ |
| TA 工作量分析与预警 | — | — | ✅ |
| 系统仪表盘与全局监控 | — | ✅ (MO 范围) | ✅ (全局) |

---

### 🔹 TA — 助教申请者

```
注册 → 完善资料 → 上传 PDF 简历 → 浏览岗位 → 查看匹配分析 → 提交申请 → 追踪状态
```

- **简历智能提取** — 上传 PDF 后系统自动通过 PDFBox 提取文本并结构化（姓名、邮箱、电话、学历、技能、经历亮点）
- **多维岗位筛选** — 关键词搜索 + 专业/部门/模块类型筛选 + 4 种排序方式（最新/截止日期/匹配度/名称）
- **匹配度预览** — 每个岗位卡片直接展示匹配等级标签（Strong / Moderate / Weak Match）
- **申请前资格检查** — 6 条件门控：资料完整度、简历上传、重复申请、截止日期、岗位状态、**时间冲突检测**
- **申请状态追踪** — 6 种状态实时流转：SUBMITTED → UNDER_REVIEW → ACCEPTED / REJECTED / WITHDRAWN
- **撤销请求机制** — 对已接受岗位可提交撤销请求（Revocation Request），需 MO 审批

---

### 🔹 MO — 课程组织者

```
创建岗位 → 查看申请人 → AI 辅助审核 → Accept/Reject → 处理撤销请求
```

- **岗位全生命周期管理** — 创建、编辑、关闭、重新开放；编辑关键信息自动触发**级联撤回**
- **Activity 活动岗位** — 支持创建一次性活动岗位（监考 / 项目评审 / 实验支持），含日期、时间、地点
- **AI 辅助审核** — 申请人列表按匹配分数排序，行内 Accept/Reject 决策即时生效
- **审核队列** — 汇总所有待审核申请，按优先级标注（High ≥ 80 / Medium 60-79 / Low < 60）
- **简历下载** — 直接下载申请人原始 PDF 简历，结合 AI 分析综合判断
- **撤销请求处理** — 在通知中直接 Approve/Reject TA 的撤销请求

---

### 🔹 Admin — 系统管理员

```
系统概览 → MO 管理 → 全局岗位监控 → TA 工作量分析
```

- **全局仪表盘** — 关键指标一览：TA 总数、MO 总数、岗位数、开放岗位、待审核申请、活跃招聘
- **MO 账号管理** — 创建 MO 账号、查看详情、密码重置
- **全局岗位监控** — 查看所有 MO 创建的岗位，支持按 MO/状态/关键词筛选
- **TA 工作量分析** — 工时统计、负载预警（NORMAL / HIGH_ALERT）、风险等级评估
- **工作量分布图** — 工时分布桶（0-4h / 4-8h / 8-12h / 12h+）+ 高工时预警人数
- **智能管理建议** — 根据风险等级自动生成分配建议

---

## 🤖 AI Intelligent Matching

### 双引擎架构 (Dual-Engine Architecture)

系统采用 **LLM 主路 + 本地规则 Fallback** 的智能匹配策略，确保任何情况下都能产出匹配结果：

```
                    ┌─────────────────────────────┐
                    │  RecommendationServiceImpl   │
                    │                              │
                    │  检查 LLM 配置完整性?         │
                    │         │                     │
                    │    ┌────┴────┐                │
                    │    │         │                │
                    │  完整      不完整              │
                    │    │         │                │
                    │    ▼         ▼                │
                    │ ┌──────┐ ┌──────────┐        │
                    │ │ LLM  │ │  Local   │        │
                    │ │ Path │ │  Rule    │        │
                    │ └──┬───┘ │ Matcher  │        │
                    │    │     └──────────┘        │
                    │  成功 │ 失败                  │
                    │    │  └─────┐                │
                    │    │        ▼                 │
                    │    │  Auto Fallback            │
                    │    │  (local-rule-fallback)    │
                    │    ▼                           │
                    │  统一 MatchResult 输出          │
                    │  score │ explanation           │
                    │  matchedSkills │ missingSkills │
                    │  method                        │
                    └─────────────────────────────────┘
```

### LLM 五维评分

| 维度 | 说明 |
| :--- | :--- |
| 🧠 技能重合度 | 简历技能 vs 岗位所需技能 |
| 🎓 教育背景相关性 | 学历与专业的岗位匹配度 |
| 👨‍🏫 教学/学术支持经历 | 助教、辅导、教学相关经验 |
| 📋 岗位职责适配性 | 经历亮点与岗位描述匹配 |
| ⚠️ 关键缺口 | 缺失的关键技能或经验 |

### 本地规则算法

```
baseScore     = 30
skillScore    = 55 × (matched_skills / required_skills)
teachingBonus = 10 (含 "assistant" / "teaching" / "tutor")
courseBonus   = 5 (含课程编号或名称)
─────────────────────────────────────────────
finalScore    = min(100, max(0, Σ))
```

**技能别名匹配** — QA ↔ Testing ↔ QualityAssurance · Lab Support ↔ Lab · Marking ↔ Grading

### 可解释性约束

- AI **不**读取原始 PDF，只分析结构化简历数据
- Prompt 禁止臆测缺失信息，必须标注"缺少证据"
- 输出必须包含支持证据 + 关键缺口 + "仅供人工决策参考"
- AI 结果**不作为自动录用依据**，MO 综合判断后做出最终决定

---

## 💡 Innovations & Highlights

### 🌟 核心创新

| # | 创新点 | 说明 |
| :--- | :--- | :--- |
| 1 | **双引擎 AI 匹配** | LLM + 本地规则双路架构，API 不可用时自动降级，零单点故障 |
| 2 | **可解释 AI 决策** | 五维评分 + 证据/缺口/置信度说明，符合 handout 可解释性要求 |
| 3 | **6 条件申请门控** | 资料完整性 + 简历 + 去重 + 截止日期 + 岗位状态 + **时间冲突检测** |
| 4 | **级联撤回机制** | MO 编辑关键信息自动撤回所有申请，通知受影响 TA，保证公平 |
| 5 | **撤销请求工作流** | TA 对已接受岗位可请求撤销，MO 审批，双向保护 |
| 6 | **简历智能提取管线** | PDFBox 提取 → 启发式结构化 → JSON 持久化，全自动处理 |
| 7 | **实时通知系统** | 7 种事件类型，含需操作的通知（Revocation），MO 在通知内直接决策 |
| 8 | **TA 工作量预警** | 工时统计 + 负载分级 (NORMAL/HIGH_ALERT) + 风险等级 + 智能建议 |
| 9 | **多角色 RBAC** | AuthFilter 三阶段管道：白名单 → 认证 → 角色权限，编译期类型安全 |
| 10 | **共享布局模板** | layout.jsp + 19 页面 CSS + 9 页面 JS，6 人并行开发保持一致性 |

### 🏗 工程亮点

- **无数据库设计** — 全部 JSON 文件存储，Gson 序列化，synchronized 保证并发安全
- **ServiceRegistry DI 容器** — 手工依赖注入，构造器注入 7 个 Repository + 7 个 Service
- **Annotation-based 路由** — `@WebServlet` 注解替代 XML，路由变更零配置维护
- **零构建框架依赖** — 纯 javac 编译 + PowerShell 脚本，提交 JAR 保证环境一致
- **106+ 次提交，20+ PR 合并** — Git feature-branch 工作流，Conventional Commits 规范
- **4 级测试策略** — Console 冒烟 → 脚本集成 → PDF 管线 → 浏览器 E2E

---

## 📂 Project Structure

```
TA-Recruitment-System-Group68/
│
├── src/com/bupt/ta/              ← Java 源码主框架
│   ├── config/                   │  ServiceRegistry (DI 容器)
│   ├── controller/               │  Servlet 控制器
│   │   ├── auth/                 │  │  Login │ Register │ Logout │ RoleSelect
│   │   ├── ta/                   │  │  Dashboard │ Profile │ Jobs │ Apply
│   │   ├── mo/                   │  │  Dashboard │ Jobs CRUD │ Applicants │ Review
│   │   ├── admin/                │  │  Dashboard │ MO Mgmt │ Job Monitor │ Workload
│   │   └── common/               │  │  AuthFilter │ Error │ DevLogin │ Legal
│   ├── service/                  │  业务接口
│   │   └── impl/                 │  业务实现
│   ├── repository/file/          │  JSON 文件数据访问层
│   ├── resume/                   │  PDF 简历提取管线
│   ├── match/                    │  AI 双引擎匹配
│   ├── model/                    │  数据模型 (User │ Role │ ApplicationStatus)
│   ├── dto/                      │  数据传输对象 (PageResult │ JobQuery │ ...)
│   ├── filter/                   │  AuthFilter (RBAC)
│   ├── exception/                │  自定义异常
│   └── util/                     │  工具类
│
├── web/                          ← 前端根目录
│   ├── WEB-INF/
│   │   ├── views/                │  JSP 视图 (34 页面)
│   │   │   ├── auth/             │  │  login.jsp │ register.jsp
│   │   │   ├── ta/               │  │  dashboard │ profile │ positions │ applications
│   │   │   ├── mo/               │  │  dashboard │ postings │ applicants │ review-queue
│   │   │   ├── admin/            │  │  dashboard │ MO mgmt │ job monitor │ workload
│   │   │   └── common/           │  │  layout.jsp │ sidebar │ header │ footer
│   │   ├── lib/                  │  运行时 JAR (taglibs │ pdfbox │ gson)
│   │   └── web.xml               │  Web 应用配置
│   └── assets/
│       ├── css/                  │  base.css │ components.css │ layout.css + 19 页面样式
│       └── js/                   │  common.js + 9 页面脚本
│
├── data/                         ← 数据文件目录 (No Database)
│   ├── users/                    │  ta.json │ mo.json │ admin.json
│   ├── postings/                 │  postings.json
│   ├── applications/             │  applications.json
│   ├── notifications/            │  notifications.json
│   ├── system/                   │  skill-tags.json │ ta-timetable.json
│   └── resumes/                  │  上传的 PDF 简历文件
│
├── lib/                          ← 编译依赖 JAR
├── scripts/                      ← 部署与运行脚本 (PowerShell)
├── docs/                         ← 项目文档
│   ├── submit/                   │  提交材料 (Report │ Backlog │ Prototype)
│   ├── 用户手册_中文.md           │  完整用户手册
│   ├── User_Manual.md            │  English User Manual
│   ├── 代码结构树.md              │  代码结构说明
│   └── 简历输入模块中文说明手册.md │  模块文档
├── .env.example                  ← LLM API 配置模板
└── README_EN.md                  ← English README
```

---

## 🛠 Tech Stack

| 层次 | 技术 | 版本 | 选择理由 |
| :--- | :--- | :--- | :--- |
| **后端框架** | Java Servlet + JSP | Servlet 4.0 / JSP 2.3 | 课程要求，成熟稳定 |
| **运行容器** | Apache Tomcat | 9.x | Servlet 4.0 兼容 |
| **数据序列化** | Gson | 2.11.0 | 轻量 API，适合简单序列化 |
| **PDF 处理** | Apache PDFBox | 3.0.2 | 可靠提取多栏/特殊字体 PDF |
| **前端** | HTML5 + CSS3 + JSTL | — | 模板化渲染，无 scriptlet |
| **数据存储** | JSON 文本文件 | — | 课程约束：不使用数据库 |
| **AI 服务** | OpenAI 兼容 API (可选) | DeepSeek 等 | LLM 辅助匹配分析 |
| **运行环境** | JDK | 21 | 最新 LTS |
| **构建** | javac + PowerShell | — | 零构建框架依赖 |

---

## 🎮 Demo Accounts

| 角色 | 用户名 | 密码 | 说明 |
| :--- | :--- | :--- | :--- |
| **TA** | `syy` | `1234567890syy` | 已上传简历，有申请记录 |
| **TA** | `zjy` | `1234567890zjy` | 已上传简历，有申请记录 |
| **MO** | `mo_demo` | `Temp123!` | Prof. Wang，有多个岗位 |
| **Admin** | `admin` | `Admin123!` | 系统管理员 |

### 开发调试快捷入口

```
/dev/login-as?role=TA      → 以 TA001 身份登录
/dev/login-as?role=MO      → 以 MO001 身份登录
/dev/login-as?role=ADMIN   → 以 Admin 身份登录
```

---

## 📖 Application Lifecycle

```
                          ┌─────────────┐
                          │  SUBMITTED   │
                          │  (已提交)     │
                          └──┬─────┬─────┘
                             │     │
               MO Accept     │     │    MO Reject
                             │     │
                      ┌──────▼─┐ ┌─▼────────┐
                      │ACCEPTED │ │ REJECTED  │
                      │ (已接受) │ │ (已拒绝)   │
                      └──┬─────┘ └───────────┘
                         │
           TA 撤销请求    │
                         │
                  ┌──────▼───────────┐
                  │REVOCATION_REQUESTED│
                  │  (请求撤销)         │
                  └──┬───────────┬────┘
                     │           │
      MO Approve     │           │   MO Reject
                     │           │
              ┌──────▼──┐   ┌────▼─────┐
              │WITHDRAWN │   │ ACCEPTED │
              │ (已撤回)  │   │ (恢复接受)│
              └──────────┘   └──────────┘

  TA 主动撤回:  SUBMITTED ──Withdraw──→ WITHDRAWN
  MO 级联撤回:  SUBMITTED ──Edit关键信息──→ WITHDRAWN + 通知 TA
```

---

## 🔐 Security & Privacy

- **RBAC 三阶段管道** — 白名单 → 认证 → 角色权限，`Role` 枚举编译期类型安全
- **API 密钥保护** — `.env.local` 在 `.gitignore` 中，不进入版本控制
- **Session 管理** — 30 分钟超时，登录时旧会话自动失效
- **文件上传安全** — 仅 PDF，最大 5MB，存储于服务器本地
- **AI 数据隐私** — 仅发送结构化简历（技能/学历/经历摘要），不发送原始 PDF

---

## 📊 Development Stats

| 指标 | 数据 |
| :--- | :--- |
| 团队规模 | 6 人 |
| Git 提交 | 106+ commits |
| PR 合并 | 20+ pull requests |
| JSP 视图 | 34 页面 |
| CSS 文件 | 19 页面样式 + 3 公共样式 |
| JS 脚本 | 9 页面脚本 + 公共脚本 |
| Servlet | 30+ 控制器 |
| Service | 7 接口 + 7 实现 |
| Repository | 7 数据访问类 |
| 文档 | 694 行 interface-design.md + 用户手册 + 报告 |

---

## 📚 Documentation

| 文档 | 说明 |
| :--- | :--- |
| [`docs/用户手册_中文.md`](docs/用户手册_中文.md) | 完整中文用户手册（含截图、流程图、FAQ） |
| [`docs/User_Manual.md`](docs/User_Manual.md) | English User Manual |
| [`docs/代码结构树.md`](docs/代码结构树.md) | 代码目录结构说明 |
| [`docs/简历输入模块中文说明手册.md`](docs/简历输入模块中文说明手册.md) | 简历输入与匹配模块详解 |
| [`docs/interface-design.md`](docs/interface-design.md) | API 接口设计契约（694 行） |
| [`docs/page-navigation.md`](docs/page-navigation.md) | 页面导航设计 |
| [`docs/AI协作开发提示词.md`](docs/AI协作开发提示词.md) | AI 辅助开发 prompt 文档 |
| [`docs/submit/`](docs/submit/) | 课程提交材料（Report / Backlog / Prototype） |

---

## 🏁 LLM Configuration (Optional)

```bash
# 1. 复制配置模板
cp .env.example .env.local

# 2. 编辑 .env.local，填入你的 API 信息
LLM_API_URL=https://api.deepseek.com/v1
LLM_API_KEY=your_api_key_here
LLM_MODEL=deepseek-chat

# 3. .env.local 已在 .gitignore 中，不会被提交
# 4. 不配置则自动使用本地规则匹配
```

---

<p align="center">
  <sub>Built with ❤️ by Group 68 · EBU6304 Software Engineering · BUPT International School</sub><br>
  <sub>© 2026 TA Recruitment System Group 68</sub>
</p>
