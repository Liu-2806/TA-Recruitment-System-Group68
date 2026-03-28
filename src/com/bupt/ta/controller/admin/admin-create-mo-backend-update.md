## 1. 对齐了 Create MO 的后端参数契约
聚焦文档 `docs/Admin模块后端对接需求.md` 中 **4.2 Create MO Account 页面** 的要求，后端现在统一支持这些创建参数：

- `username`
- `fullName`
- `staffId`
- `email`
- `department`
- `phone`
- `tempPassword`
- `confirmPassword`

同时保留了对旧键名的兼容：

- `name`
- `initialPassword`

这样既能满足文档口径，也不会直接打断仓库里已有的旧调用方式。

## 2. 修改了 `AdminMOCreateServlet`
修改文件：`src/com/bupt/ta/controller/admin/AdminMOCreateServlet.java`

这次补齐了 servlet 对以下字段的接收：

- `staffId`
- `department`
- `phone`
- `confirmPassword`

并补充了：

- `fullName` / `name` 兼容读取
- `tempPassword` / `initialPassword` 兼容读取
- 失败时继续回填：
  - `errorMessage`
  - `formData`
- 成功后跳转：`/admin/mos`

## 3. 修改了 `UserServiceImpl.createMO()`
修改文件：`src/com/bupt/ta/service/impl/UserServiceImpl.java`

现在 `createMO()` 已按文档要求补齐以下校验：

- 校验 `username` 唯一
- 校验 `staffId` 唯一
- 校验 `email` 唯一
- 校验 `confirmPassword` 非空
- 校验 `tempPassword` 与 `confirmPassword` 一致
- 继续保留邮箱格式校验和最小密码长度校验

同时持久化时会写入：

- `id`
- `moId`
- `username`
- `email`
- `fullName`
- `displayName`
- `staffId`
- `department`
- `phone`
- `description`
- `role`
- `password`
- `active`
- `createdAt`
- `updatedAt`

数据仍然落到 JSON 文件，不引入数据库。

## 4. 顺手修复了成功跳转后的 `/admin/mos` 问题
修改文件：`src/com/bupt/ta/controller/admin/AdminMOListServlet.java`

这个 servlet 原来 `userService` 没有初始化。
如果 Create MO 成功后直接重定向到 `/admin/mos`，会存在空指针风险。

这次做了最小修复：

- 给 `AdminMOListServlet` 正常注入 `ServiceRegistry.userService()`

这样至少能保证创建成功后的跳转链路是通的。

## 5. 补了终端测试入口
新增文件：

- `scripts/AdminCreateMOConsoleRunner.java`
- `scripts/test-admin-create-mo.ps1`

另外同步修改：

- `scripts/Phase1ConsoleApp.java`（已同步交互参数）

现在可以不启动 Tomcat，直接在终端验证 Create MO 的后端逻辑。

当前命令行测试覆盖了：

### 成功场景
- 成功创建 MO
- 成功写入项目真实目录 `data/users/mo.json`
- 成功用新 MO 账号登录
- 成功通过 `searchMOs()` 查到新账号

### 失败场景
- 用户名重复
- 工号重复
- 邮箱重复
- 两次密码不一致

## 6.实际验证
我做的是**后端最小必要改动**，没有改前端 JSP 结构、命名和页面目录。

已验证内容：

- 相关 Java 文件能够被脚本编译
- `AdminMOCreateServlet` / `AdminMOListServlet` 也参与了编译验证
- `createMO()` 会直接在项目真实 `data/users` 下生成或更新 `mo.json`
- 终端 runner 能输出成功标记：`ADMIN_CREATE_MO_TEST=PASS`

## 7. 目前仍然存在但这次没有改的前端现状
因为你要求**只做后端，不改前端**，所以我没有动 `web/WEB-INF/views/admin/create-mo.jsp`。

当前这个 JSP 仍然是静态预览状态，至少有两个现实限制：

- `<form action="#" method="post">` 还没有真正指向 `/admin/mos/create`
- 输入框目前大多没有 `name` 属性，浏览器提交时不会把这些字段发给后端

所以：

- **终端测试已经可以完整验证后端功能**
- **浏览器页面要真正把字段提交给 servlet，还需要前端后续接线**

这部分我这次没有改，符合你“只完成后端功能代码任务”的要求。

## 8. 这次涉及的文件
已修改：

- `src/com/bupt/ta/controller/admin/AdminMOCreateServlet.java`
- `src/com/bupt/ta/controller/admin/AdminMOListServlet.java`
- `src/com/bupt/ta/service/impl/UserServiceImpl.java`
- `scripts/Phase1ConsoleApp.java`

已新增：
用于测试和说明
- `scripts/AdminCreateMOConsoleRunner.java`
- `scripts/test-admin-create-mo.ps1`
- `src/com/bupt/ta/controller/admin/admin-create-mo-backend-update.md`

## 9. 怎么测
在 PowerShell 里运行：

```powershell
& "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\scripts\test-admin-create-mo.ps1"
```

如果 Java 不在 PATH，可以显式指定：

```powershell
& "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\scripts\test-admin-create-mo.ps1" -JavaHome "E:\jdk-21.0.2"
```

如果想把测试数据写到自定义目录：

```powershell
& "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\scripts\test-admin-create-mo.ps1" -DataDir "C:\your\temp\admin-create-mo-data"
```

默认情况下，这个脚本现在直接使用项目根目录下的 `data`，也就是最终 MO 数据直接写入：

- `C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\data\users\mo.json`


