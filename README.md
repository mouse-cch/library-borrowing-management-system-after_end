[README.md](https://github.com/user-attachments/files/29508710/README.md)
# 图书借阅管理系统 - 后端

基于 Spring Boot 3.5 + MyBatis-Plus + MySQL 8.0 的图书借阅管理系统后端，提供 40+ 个 RESTful 接口，JWT 鉴权，支持学生、教师、图书管理员、系统管理员四种角色。

> **前端仓库**：[点击跳转](https://github.com/你的用户名/library-vue) ← 推 GitHub 后替换链接

## 技术栈

| 类别 | 技术 |
|------|------|
| 框架 | Spring Boot 3.5.16 |
| ORM | MyBatis-Plus 3.5 |
| 数据库 | MySQL 8.0 |
| 鉴权 | JWT（jjwt 0.12） |
| API 文档 | SpringDoc OpenAPI 2.8（Swagger UI） |
| JSON | FastJSON2 2.0 |
| 工具库 | Lombok、Apache Commons Lang3 |
| 构建工具 | Maven 3.8+ |
| 运行环境 | JDK 21 |

## 接口概览

| 模块 | Controller | 接口数 | 说明 |
|------|-----------|--------|------|
| 认证 | AuthController | 3 | 登录、注册、获取当前用户 |
| 图书 | BookController | 6 | 增删改查、分类查询、库存管理 |
| 借阅 | BorrowController | 7 | 借阅申请、审批、归还、续借、逾期查询 |
| 预约 | ReserveController | 5 | 预约申请、审批、取消、查询 |
| 费用 | FeeController | 4 | 费用查询、缴纳、罚金计算 |
| 库存 | StockController | 4 | 入库、盘点、遗失登记、报废 |
| 统计 | StatsController | 3 | 借阅统计、逾期统计、库存统计 |
| 系统 | SysController | 6 | 角色管理、冻结、密码重置、操作日志 |
| 用户 | UserController | 4 | 用户查询、档案、信息修改 |

> 启动后访问 **http://localhost:8080/swagger-ui.html** 查看完整接口文档。

## 数据库设计（10 张表）

| 表名 | 说明 |
|------|------|
| `user` | 用户表（账号、密码、角色、状态） |
| `role` | 角色表（学生/教师/图书管理员/系统管理员） |
| `book` | 图书表（书名、作者、分类、库存、状态） |
| `category` | 图书分类表 |
| `borrow` | 借阅记录表（借出、归还、逾期状态） |
| `reservation` | 预约记录表 |
| `fee` | 费用记录表（逾期罚金） |
| `inventory` | 库存盘点表 |
| `operation_log` | 操作日志表 |
| `password_reset` | 密码重置审批表 |

## 环境要求

- **JDK** >= 21
- **Maven** >= 3.8
- **MySQL** >= 8.0

## 快速启动

### 1. 初始化数据库

在 MySQL 中创建数据库并导入建表脚本：

```sql
CREATE DATABASE IF NOT EXISTS library_db DEFAULT CHARACTER SET utf8mb4;
USE library_db;
SOURCE 项目设计文档/04-数据库建表SQL.sql;
```

导入测试数据（可选，20 本示例图书）：

```sql
SOURCE 项目设计文档/05-图书测试数据.sql;
```

### 2. 修改数据库配置

编辑 `src/main/resources/application.yml`，改数据库密码：

```yaml
spring:
  datasource:
    username: root
    password: 你的密码   # ← 改这里
```

### 3. 启动项目

```bash
mvn spring-boot:run
```

看到 `Started LibraryServerApplication` 即启动成功，端口 **8080**。

### 4. 验证

```bash
curl http://localhost:8080/api/auth/login
# 或浏览器打开 http://localhost:8080/swagger-ui.html
```

## 打包部署

```bash
mvn clean package -DskipTests
```

生成的 JAR 包在 `target/library-server-1.0.0.jar`，直接运行：

```bash
java -jar target/library-server-1.0.0.jar
```

## 项目结构

```
after-end/
├── src/main/java/com/example/afterend/
│   ├── config/                     # 配置类
│   │   ├── CorsConfig.java         # 跨域配置
│   │   ├── JwtConfig.java          # JWT 配置
│   │   ├── MybatisPlusConfig.java  # MyBatis-Plus 配置 + 自动填充
│   │   └── SpringDocConfig.java    # API 文档配置
│   ├── controller/                 # 控制器（9 个）
│   ├── dto/                        # 数据传输对象
│   │   ├── BorrowVO.java           # 借阅视图（含书名、用户名）
│   │   ├── OverdueVO.java          # 逾期视图（含罚金）
│   │   ├── ReserveVO.java          # 预约视图
│   │   └── Result.java             # 统一返回格式 {code, msg, data}
│   ├── entity/                     # 实体类（10 张表对应）
│   ├── interceptor/
│   │   └── JwtInterceptor.java     # JWT 拦截器
│   ├── mapper/                     # MyBatis 映射接口
│   ├── service/                    # 服务接口
│   ├── serviceimpl/                # 服务实现
│   └── utils/
│       ├── JwtUtil.java            # JWT 工具类
│       └── OverdueUtil.java        # 逾期检测工具
├── src/main/resources/
│   ├── application.yml             # 主配置
│   └── mapper/                     # MyBatis XML 映射文件
├── 项目设计文档/
│   ├── 04-数据库建表SQL.sql
│   └── 05-图书测试数据.sql
└── pom.xml
```

## 业务规则

| 规则项 | 学生 | 教师 |
|--------|------|------|
| 最大借阅数 | 5 本 | 10 本 |
| 借阅期限 | 30 天 | 60 天 |
| 最大续借次数 | 3 次 | 3 次 |
| 逾期罚金 | ¥1.00/天 | ¥1.00/天 |

可在 `application.yml` 中修改：

```yaml
borrow:
  student-max: 5
  teacher-max: 10
  student-days: 30
  teacher-days: 60
  max-renew-count: 3
  fine-per-day: 1.00
```

## 常见问题

**Q: 启动报 "Communications link failure"？**

A: MySQL 没启动，或 `application.yml` 中密码错误。

**Q: 注册时报 "create_time cannot be null"？**

A: `MybatisPlusConfig` 中的 `MetaObjectHandler` 未生效，检查该类是否在 Spring 扫描路径下。

**Q: 接口返回 401 "未登录"？**

A: 请求头缺少 `Authorization: Bearer <token>`，先调登录接口获取 token。

**Q: Swagger 页面打不开？**

A: 本项目已从 Knife4j 迁移至 SpringDoc，访问 `http://localhost:8080/swagger-ui.html`。

## 开发者

陈成豪 · 软件工程 24-2 班
