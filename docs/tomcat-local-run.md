# 本地 Tomcat 启动说明（最小版）

本文档用于在 IntelliJ IDEA 中本地运行本项目（Servlet/JSP）。

## 1. 前置准备
- JDK 8+（建议 17）
- Apache Tomcat 9.x（支持 Servlet 3.1）
- IntelliJ IDEA Ultimate（社区版对 Web 支持有限）

## 2. 项目结构约定
- Java 源码目录：`src`
- Web 资源目录：`web`
- 部署描述文件：`web/WEB-INF/web.xml`
- JSP 目录：`web/WEB-INF/views`

## 3. 在 IDEA 配置 Artifact
1. 打开 `File -> Project Structure -> Artifacts`
2. 点击 `+`，选择 `Web Application: Exploded`
3. 选择当前模块（`TA recruitment system`）
4. 确认输出内容中包含：
   - `WEB-INF/classes`（编译后的 `src`）
   - `web` 下静态资源与 `WEB-INF`

## 4. 在 IDEA 配置 Tomcat
1. 打开 `Run -> Edit Configurations`
2. 新建 `Tomcat Server -> Local`
3. 在 `Application Server` 里选择本地 Tomcat 路径
4. 在 `Deployment` 中添加上一步的 exploded artifact
5. 设定 `Application context`，例如：`/ta-system`

## 5. 启动与访问
- 启动 Tomcat 后，访问：
  - `http://localhost:8080/ta-system/auth/login`
- 或访问 context 根路径（会走 welcome-file）：
  - `http://localhost:8080/ta-system/`

## 6. 常见问题
- 404（页面找不到）：
  - 确认 URL 包含 context path
  - 确认 Servlet 有 `@WebServlet` 注解并已编译进部署产物
- JSP 404：
  - 确认 JSP 在 `web/WEB-INF/views`
  - 确认由 Servlet `forward` 访问，而不是浏览器直接访问 `WEB-INF`
- 类找不到：
  - 重新 Build 项目，确认 artifact 中存在 `WEB-INF/classes/com/...`
