# 我改了什么

这次只围绕文档 `docs/Admin模块后端对接需求.md` 中 **4.5 Reset MO Password** 做后端最小必要改动，保持当前仓库真实结构不变：

- 后端主代码仍然只在 `src/com/bupt/ta/`
- 前端目录仍然只在 `web/`
- JSP 目录仍然只在 `web/WEB-INF/views/`
- 继续使用 JSON 文件，不引入数据库
- 不改包名、不改目录结构、不改 JSON 字段名

---

## 1. 对齐了 `POST /admin/mos/reset-password` 的后端处理要求

文档要求：

- 校验管理员权限
- 校验目标 MO 存在
- 重置密码
- 成功后跳回 `/admin/mos`
- 失败时进入 error 页面

这次实现重点就是把这条链路补齐并收敛为 MO 专用重置。

---

## 2. 修改了 `AdminMOPasswordResetServlet`

修改文件：`src/com/bupt/ta/controller/admin/AdminMOPasswordResetServlet.java`

主要改动：

- 在 `doPost` 中新增管理员权限校验：
  - `currentUser(request)` 不能为空
  - `currentUser.getRole()` 必须是 `Role.ADMIN`
- 无权限时：
  - 回填 `errorMessage`
  - forward 到 `/WEB-INF/views/common/error.jsp`
- 有权限时继续读取：
  - `moUserId`
  - `newPassword`
- 重置成功后保持原有跳转：
  - `response.sendRedirect(request.getContextPath() + "/admin/mos")`
- 异常时保持 error 页面处理：
  - `errorMessage`
  - forward 到 `/WEB-INF/views/common/error.jsp`

---

## 3. 修改了 `UserServiceImpl.resetPasswordByAdmin()`

修改文件：`src/com/bupt/ta/service/impl/UserServiceImpl.java`

主要改动：

- 保留基础参数校验：
  - `userId` 非空
  - `rawPassword` 非空
  - 最小密码长度校验
- 将目标账号范围限制为 **MO 账号**：
  - 只从 `Role.MO` 查找
  - 移除对 `Role.ADMIN` 的兜底查找
- 目标不存在时统一抛错：
  - `MO 账号不存在`
- 重置时更新 MO 记录：
  - 覆盖 `password`
  - 清理 `passwordSalt` / `passwordHash`
  - 更新 `updatedAt`
  - 持久化到 MO 数据文件

这样可以避免通过该接口误改管理员账号密码，契合“Reset MO Password”的业务边界。

---

## 4. 数据落地位置保持不变

这次没有新增任何用户数据目录，仍然使用现有路径：

- `data/users/mo.json`

符合“直接创建在 `data/users`，不另建文件夹”的要求。

---

## 5. 新增了终端测试入口

新增文件：

- `scripts/AdminResetMOPasswordConsoleRunner.java`
- `scripts/test-admin-reset-mo-password.ps1`

目的：

- 不启动 Tomcat，也能直接验证重置密码后端逻辑
- 使用项目现有数据目录进行验证

---

## 6. 终端测试覆盖内容

`AdminResetMOPasswordConsoleRunner` 当前覆盖：

1. 创建一条新的 MO 测试账号
2. 校验重置前旧密码可登录
3. 执行 `resetPasswordByAdmin(moUserId, newPassword)`
4. 校验重置后：
   - 旧密码登录失败
   - 新密码登录成功
5. 负例校验：
   - 重置 `ADMIN001` 返回 `MO 账号不存在`
   - 重置不存在的 MO ID 返回 `MO 账号不存在`

脚本成功标记：

- `NEGATIVE_CASES=PASS`
- `ADMIN_RESET_MO_PASSWORD_TEST=PASS`

---

## 7. 我实际执行过的测试命令

在 PowerShell 中：

```powershell
Set-Location "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68"
powershell -ExecutionPolicy Bypass -File ".\scripts\test-admin-reset-mo-password.ps1"
```

---

## 8. 可选测试参数

如果 Java 不在 PATH：

```powershell
& "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\scripts\test-admin-reset-mo-password.ps1" -JavaHome "E:\jdk-21.0.2"
```

如果想指定数据目录：

```powershell
& "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\scripts\test-admin-reset-mo-password.ps1" -DataDir "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\data"
```

---

## 9. 这次涉及的文件清单

### 已修改

- `src/com/bupt/ta/controller/admin/AdminMOPasswordResetServlet.java`
- `src/com/bupt/ta/service/impl/UserServiceImpl.java`

### 已新增

- `scripts/AdminResetMOPasswordConsoleRunner.java`
- `scripts/test-admin-reset-mo-password.ps1`
- `src/com/bupt/ta/controller/admin/admin-reset-mo-password-backend-update.md`
