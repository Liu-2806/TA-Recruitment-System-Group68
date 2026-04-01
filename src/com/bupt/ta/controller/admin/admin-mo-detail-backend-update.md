# 我改了什么

这次只围绕文档 `docs/Admin模块后端对接需求.md` 中 **4.4 MO Detail 页面** 做后端最小必要改动，保持当前仓库真实结构不变：

- 后端主代码仍然只在 `src/com/bupt/ta/`
- 前端目录仍然只在 `web/`
- JSP 目录仍然只在 `web/WEB-INF/views/`
- 继续使用 JSON 文件，不引入数据库
- 不改包名、不改目录结构、不批量改 JSP

---

## 1. 对齐了 `GET /admin/mos/detail?moUserId=...` 的返回契约

文档要求后端给页面提供一个 `mo` 对象，建议字段包括：

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

我没有新增 DTO 目录和额外接口，而是用最小改动继续沿用当前的 `UserService.getMOById(moUserId)`，让它返回的 `User` 已经能覆盖上述字段。

---

## 2. 修改了 `User`，补齐 MO Detail 需要的字段

修改文件：`src/com/bupt/ta/model/User.java`

在不改变原有 `User` 用法的前提下，补充了：

- `description`
- `createdAt`
- `postingCount`

这样当前 `User` 不仅能覆盖 All MOs 页面，也能继续覆盖 MO Detail 页面未来联调所需的数据结构。

---

## 3. 修改了 `UserServiceImpl.getMOById()`

修改文件：`src/com/bupt/ta/service/impl/UserServiceImpl.java`

这次给 `getMOById()` 补了 detail 场景真正需要的字段映射：

- 从 `data/users/mo.json` 读取并映射：
  - `moId`
  - `fullName`
  - `staffId`
  - `email`
  - `phone`
  - `department`
  - `description`
  - `createdAt`
  - `status`
- 额外聚合 `postingCount`

### `postingCount` 的处理方式

`postingCount` 没有直接存放在 `mo.json` 里，所以这次是在 service 层按当前 MO 的 `moId` 去统计：

- `data/postings/postings.json`

里属于该 MO 的岗位数量。

也就是说：

- 不改 JSON 字段名
- 不新增数据库
- 不新增新的数据目录
- 直接复用现有 `postings.json`

---

## 4. 修改了 `UserServiceImpl.updateMOByAdmin()`

修改文件：`src/com/bupt/ta/service/impl/UserServiceImpl.java`

文档里对 `POST /admin/mos/detail` 的要求是：

前端预计提交：

- `moUserId`
- `name`
- `email`
- `phone`
- `description`
- `status`

同时建议后端统一：

- `name -> fullName`

这次我把管理员更新 MO 的逻辑补成了：

### 已支持的更新字段

- `fullName`（兼容 `name`）
- `email`
- `phone`
- `description`
- `status`

### 已补的校验

- 校验 `moUserId` 非空
- 校验目标 MO 存在
- 校验 `fullName` 非空
- 校验 `email` 非空
- 校验邮箱格式合法
- 校验修改后的邮箱唯一性
- 校验 `status` 只接受可识别值

### 状态映射规则

当前仓库真实数据里 MO 状态本质上还是存：

- `active: true/false`

所以这次统一在 service 层做转换：

- `ACTIVE` / `ENABLED` -> `active=true`
- `INACTIVE` / `DISABLED` -> `active=false`

页面侧后续只需要看统一后的：

- `status=ACTIVE`
- `status=INACTIVE`

---

## 5. 修改了 `AdminMODetailServlet`

修改文件：`src/com/bupt/ta/controller/admin/AdminMODetailServlet.java`

这次做了几个最小但必要的修复：

### 5.1 修复了 `userService` 未初始化问题

原来的 servlet 里：

- `private UserService userService;`

但没有赋值，运行时有空指针风险。

现在改为通过现有：

- `ServiceRegistry.userService()`

完成初始化。

### 5.2 对齐了 POST 参数接收

现在 servlet 会接收并传递：

- `moUserId`
- `fullName`
- `name`
- `email`
- `phone`
- `description`
- `status`

其中：

- `fullName` / `name` 会做兼容读取

### 5.3 对齐了失败回填要求

文档要求失败时回填：

- `errorMessage`
- `mo`

这次在异常分支里，不再简单把原始 `Map` 直接塞给页面，而是回填成一个更接近 detail 页契约的 `User` 对象，便于后续 JSP 接页面时直接取值。

---

## 6. 数据仍然直接使用现有路径

这次没有新建 MO 专属文件夹，也没有改数据目录结构。

仍然直接使用：

- `data/users/mo.json`
- `data/postings/postings.json`

符合你之前强调的：

- 直接创建在 `C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\data\users`
- 不需要另创建一个文件夹来存放

---

## 7. 补了终端测试入口

新增文件：

- `scripts/AdminMODetailConsoleRunner.java`
- `scripts/test-admin-mo-detail.ps1`

目的：

- 不启动 Tomcat，也能直接验证 MO Detail 后端逻辑
- 覆盖 GET detail / POST update / 持久化 / 失败场景

### 终端测试当前覆盖内容

#### GET 场景

验证 `getMOById()` 返回：

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

#### POST 场景

验证 `updateMOByAdmin()` 能更新：

- `name -> fullName`
- `email`
- `phone`
- `description`
- `status`

并验证更新结果真实写回 `mo.json`。

#### 失败场景

验证以下错误能被正确拦截：

- 邮箱重复
- 状态非法
- 目标 MO 不存在

脚本成功时会输出：

- `ADMIN_MO_DETAIL_TEST=PASS`

---

## 8. 关于测试数据目录，我额外做了一个安全修正

在第一次终端验证后，我发现如果脚本默认直接使用项目真实 `data/`，会污染仓库里的真实 JSON 文件。

所以我额外把：

- `scripts/test-admin-mo-detail.ps1`

修正为：

- **默认复制项目 `data/` 到隔离测试目录再执行**

这样你直接跑脚本时：

- 不会覆盖真实 `data/users/mo.json`
- 不会覆盖真实 `data/postings/postings.json`

如果你确实想指定自定义测试目录，仍然可以显式传 `-DataDir`。

---

## 9. 我没有改的部分

因为你要求 **只完成后端功能代码任务，不去改变前端**，所以这次没有动：

- `web/WEB-INF/views/admin/mo-detail.jsp`
- 任何前端静态预览页面
- 任何 JSP 路由命名
- 任何目录结构

也没有引入：

- 数据库
- 新包名
- 新接口层目录

---

## 10. 实际改动文件清单

### 已修改

- `src/com/bupt/ta/model/User.java`
- `src/com/bupt/ta/service/impl/UserServiceImpl.java`
- `src/com/bupt/ta/controller/admin/AdminMODetailServlet.java`
- `scripts/test-admin-mo-detail.ps1`

### 已新增

- `scripts/AdminMODetailConsoleRunner.java`
- `src/com/bupt/ta/controller/admin/admin-mo-detail-backend-update.md`

---

## 11. 怎么在终端测试

直接在 PowerShell 里运行：

```powershell
& "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\scripts\test-admin-mo-detail.ps1"
```

如果 Java 不在 PATH，可以显式指定：

```powershell
& "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\scripts\test-admin-mo-detail.ps1" -JavaHome "E:\jdk-21.0.2"
```

如果你想指定自定义测试数据目录，也可以传：

```powershell
& "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\scripts\test-admin-mo-detail.ps1" -DataDir "C:\your\temp\admin-mo-detail-data"
```

默认情况下，这个脚本现在会使用隔离测试目录，而不是直接写仓库真实 `data/`。

