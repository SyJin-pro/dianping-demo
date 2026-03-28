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
- `GET /user/me`

## 本地联调（Postman）

当前服务端口以 `application.properties` 为准（示例：`8081`）。

1. 发送验证码  
   `POST http://localhost:8081/user/code?phone=13800000000`  
   预期：`{"success":true,"message":"验证码发送成功（mock）"}`

2. 登录  
   `POST http://localhost:8081/user/login?phone=13800000000&code=123456`  
   预期：`{"success":true,"message":"登录成功（mock）"}`

3. 获取当前登录用户（同一 Postman 会话）  
   `GET http://localhost:8081/user/me`  
   预期：`{"success":true,"message":"查询成功","data":{...}}`

## 第二阶段验收结果（已完成）

从“登录成功（mock）”升级到“可校验登录态”：

1. 登录成功后，把用户信息写入 `HttpSession`
2. 新增接口：`GET /user/me`（返回当前登录用户）
3. 同一 Postman 会话下可持续识别登录状态（Cookie 生效）

### 第二阶段验收标准（已通过）

- 未登录访问 `GET /user/me`：返回未登录提示
- 登录后访问 `GET /user/me`：返回当前用户信息
- 同一 Postman 会话下可持续识别登录状态（Cookie 生效）

## 第三阶段目标（下一步）

将单机 Session 升级为 `Session + Redis`（Option A）：

1. 接入 Redis 与 Spring Session
2. 保持现有 `HttpSession` 代码写法不变，由框架托管到 Redis
3. 为后续多实例部署提供共享登录态能力

### 第三阶段验收标准（计划）

- 登录后访问 `GET /user/me` 正常
- 应用重启后（在会话有效期内）仍可读取登录态
- Redis 中可观察到 Session 相关键


## 阶段复盘（基础链路 + Redis 登录态）

### 目标

从零搭建黑马点评基础骨架，先打通 `controller` / `service` / `mapper` / `entity` 四层，完成用户查询、验证码登录（mock）与登录态维护。

### 技术决策（本阶段）

- 鉴权：`Session + Redis`
- 缓存策略：先走 Option A（后续再迭代）
- ORM：采用 MyBatis 注解方式快速起步

### 遇到的核心问题

- `org.apache.ibatis.annotations` 不存在 -> 依赖未就绪
- `Failed to configure a DataSource` -> 数据源配置不完整
- `Unknown database 'dianping'` -> 目标库未创建
- 登录 `500` -> 迁移到 Redis Session 后会话写入链路异常（最终通过让 `User` 可序列化解决）

### 关键修复动作

- 补齐 `mybatis-spring-boot-starter`、Redis、Spring Session 依赖
- 配置 MySQL 与 Redis 连接参数
- 创建并连接目标数据库
- `User` 实体实现 `Serializable`（见 `src/main/java/com/jj/dianpingdemo/entity/User.java`）以支持 Session 序列化入 Redis

### 验证结果

- `GET` 查询成功
- Postman 登录流程成功（发送验证码 mock -> 登录 mock -> 查询当前用户）
- Redis 连通性测试成功（可写可读）
- 应用重启后仍保持登录态，说明 Session 已由 Redis 托管，阶段目标达成

### 本阶段结论

你已经完成了“从 0 到 1”的最关键闭环：可运行 + 可登录 + 状态可持久化。  
当前项目已具备进入下一阶段（登出、TTL、拦截器鉴权、业务缓存细化）的基础条件。

