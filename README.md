# 点评项目（自己实现）

先用最小可运行骨架把项目立起来：明确分层、核心依赖、基础配置和首批模块边界。

## 当前分层（MVP）

- `controller`：接口层（接收请求、参数校验、统一返回）
- `service`：业务层（业务编排、事务边界）
- `mapper`：数据访问层（基础 CRUD + 后续复杂 SQL）
- `entity`：实体层（与数据库表结构对应）

> 当前仅创建以上 4 个核心包，其他包（`config`、`util`、`dto` 等）按需补充。

## 第一阶段验收结果

已打通链路：`Controller -> Service -> Mapper -> MySQL -> JSON返回`

- 验收接口：`GET /user/{id}`
- 验收结论：请求可达、数据库可查、结果可返回
- 已知依赖：MySQL 库名/表名/字段约定

## 当前已验收接口

- `GET /user/{id}`
- `POST /user/code`
- `POST /user/login`

## 本地联调（Postman）

当前服务端口以 `application.properties` 为准（示例：`8081`）。

1. 发送验证码  
   `POST http://localhost:8081/user/code?phone=13800000000`  
   预期：`{"success":true,"message":"验证码发送成功（mock）"}`

2. 登录  
   `POST http://localhost:8081/user/login?phone=13800000000&code=123456`  
   预期：`{"success":true,"message":"登录成功（mock）"}`

## 第二阶段目标（继续）

从“登录成功（mock）”升级到“可校验登录态”：

1. 登录成功后，把用户信息写入 `HttpSession`
2. 新增接口：`GET /user/me`（返回当前登录用户）
3. 先用单机 Session 跑通，再迁移到 Redis 共享 Session（Option A）

### 第二阶段验收标准

- 未登录访问 `GET /user/me`：返回未登录提示
- 登录后访问 `GET /user/me`：返回当前用户信息
- 同一 Postman 会话下可持续识别登录状态（Cookie 生效）
