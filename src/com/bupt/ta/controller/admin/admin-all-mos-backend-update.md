# 我改了什么

这次只围绕文档 `docs/Admin模块后端对接需求.md` 中 **4.3 All MOs 页面** 做后端最小必要改动，保持现有项目真实结构不变：

- 后端主代码仍然只在 `src/com/bupt/ta/`
- 前端目录仍然只在 `web/`
- JSP 目录仍然只在 `web/WEB-INF/views/`
- 继续使用 JSON 文件，不引入数据库
- 不改包名、不改目录结构、不批量改 JSP

---

## 1. 对齐了 `GET /admin/mos` 需要的后端返回结构

目标是让后端能够稳定提供文档里要求的：

- `mosPage.records`
- `mosPage.page`
- `mosPage.size`
- `mosPage.total`
- `query`

我保留了原有的 `UserService.searchMOs(query)` 入口，没有额外引入新的 DTO 层，而是用最小改动把现有 `PageResult<User>` 补齐到足够支撑 All MOs 列表联调。

---

## 2. 修改了 `User`，让它能覆盖 All MOs 列表需要的字段

修改文件：`src/com/bupt/ta/model/User.java`

根据文档 4.3，All MOs 每条记录建议至少包含：

- `moId`
- `fullName`
- `staffId`
- `email`
- `department`
- `phone`
- `status`

因为当前返回类型就是 `User`，所以这次直接在 `User` 上补齐了这些字段，并保留已有字段：

- `id`
- `username`
- `displayName`
- `role`

这样可以不改 service 接口签名，也不引入新的目录和类型。

---

## 3. 修改了 `UserServiceImpl.searchMOs()`

修改文件：`src/com/bupt/ta/service/impl/UserServiceImpl.java`

这次把 `searchMOs()` 从“只做简单过滤并把全部结果直接返回”补成了真正可用于 All MOs 页的后端能力：

### 已支持的查询能力

- 关键字筛选：`keyword`
- 状态筛选：`status`
- 排序：`sortBy`
- 分页：`page`、`size`

### 当前返回语义

- `records`：当前页记录
- `page`：当前页码
- `size`：请求页大小
- `total`：筛选后的总记录数

### 每条记录当前可返回字段

- `moId`
- `fullName`
- `staffId`
- `email`
- `department`
- `phone`
- `status`
- 以及已有的 `id / username / displayName / role`

### 状态处理方式

当前仓库里的 MO 数据实际存的是：

- `active: true/false`

文档里希望页面层看到统一状态字段，所以我在 service 层做了兼容映射：

- `active=true` -> `status=ACTIVE`
- `active=false` -> `status=INACTIVE`

如果未来 JSON 里直接出现 `status` 字段，也会优先读取并标准化。

### 排序兼容值

当前补的排序兼容值包括：

- `fullNameAsc`
- `fullNameDesc`
- `nameAsc`
- `nameDesc`
- `staffIdAsc`
- `staffIdDesc`
- `emailAsc`
- `emailDesc`
- `departmentAsc`
- `departmentDesc`
- `createdAtAsc`
- `createdAtDesc`

默认排序使用：

- `createdAtDesc`

---

## 4. 修改了 `AdminMOListServlet`

修改文件：`src/com/bupt/ta/controller/admin/AdminMOListServlet.java`

原来这个 servlet 会把下面这些参数放进 `query`：

- `keyword`
- `status`
- `sortBy`

这次为了让分页真正能从请求透传到后端，我补加了：

- `page`
- `size`

因此现在 `/admin/mos` 这条链路在后端已经可以处理：

- 关键字筛选
- 状态筛选
- 排序
- 分页

并继续保留：

- `request.setAttribute("mosPage", userService.searchMOs(query))`
- `request.setAttribute("query", query)`

我没有改 JSP。

---

## 5. 数据仍然直接落在现有 `data/users/mo.json`

这次没有新增任何数据目录，也没有新建额外用户文件夹。

MO 数据仍然直接使用项目现有路径：

- `data/users/mo.json`

符合你要求的：

- 直接创建在 `C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\data\users`
- 不需要另创建一个文件夹来存放

---

## 6. 补了终端测试入口

新增文件：

- `scripts/AdminAllMOsConsoleRunner.java`
- `scripts/test-admin-all-mos.ps1`

目的：

- 不启动 Tomcat，也能直接在终端验证 All MOs 的后端逻辑
- 用项目真实 `data/users/mo.json` 做验证

### 终端测试覆盖内容

这个 runner 会：

1. 在真实 `data/users/mo.json` 中创建 3 条唯一 MO 测试数据
2. 将其中 1 条标记为 `inactive`
3. 验证关键字筛选
4. 验证 `ACTIVE / INACTIVE` 状态筛选
5. 验证 `fullNameAsc / fullNameDesc` 排序
6. 验证 `page / size` 分页
7. 验证 `User` 返回字段里确实包含：
   - `moId`
   - `fullName`
   - `staffId`
   - `email`
   - `department`
   - `phone`
   - `status`

脚本成功时会输出：

- `ADMIN_ALL_MOS_TEST=PASS`

---

## 7. 我没有改的部分

因为你要求 **只完成后端功能代码任务，不去改变前端**，所以这次没有动：

- `web/WEB-INF/views/admin/all-mos.jsp`
- 任何前端静态页面、表单结构、按钮、命名

也没有改：

- 包名
- 目录结构
- 数据文件命名
- JSON 根路径

---

## 8. 实际改动文件清单

### 已修改

- `src/com/bupt/ta/model/User.java`
- `src/com/bupt/ta/service/impl/UserServiceImpl.java`
- `src/com/bupt/ta/controller/admin/AdminMOListServlet.java`

### 已新增

- `scripts/AdminAllMOsConsoleRunner.java`
- `scripts/test-admin-all-mos.ps1`
- `src/com/bupt/ta/controller/admin/admin-all-mos-backend-update.md`

---

## 9. 怎么在终端测试

在 PowerShell 里运行：

```powershell
& "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\scripts\test-admin-all-mos.ps1"
```

如果 Java 不在 PATH，可以显式指定：

```powershell
& "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\scripts\test-admin-all-mos.ps1" -JavaHome "E:\jdk-21.0.2"
```

如果你想指定数据目录，也可以传：

```powershell
& "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\scripts\test-admin-all-mos.ps1" -DataDir "C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\data"
```

默认情况下，脚本直接使用项目真实数据目录，也就是：

- `C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\data`

因此测试读写的 MO 数据文件就是：

- `C:\Users\18540\Desktop\group68\TA-Recruitment-System-Group68\data\users\mo.json`

