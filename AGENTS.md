# 统一开发规范（spring-cloud-alibaba-demo）

> 本文档供 **AI 编码助手** 阅读。项目入口与快速开始请参阅 [`README.md`](README.md)。

本项目是 Spring Cloud Alibaba 微服务架构的 **技术演示与验证工程**（2026 全家桶升级版），用于展示 Nacos 注册中心/配置中心（`spring.config.import` 机制）、Sentinel 流控与规则持久化、Spring Cloud Gateway（WebFlux）、OpenFeign 声明式客户端及 SkyWalking 日志链路追踪的最小可用与最佳实践。

---

## 1. 技术栈

版本以根目录 `pom.xml` 为准；下表为当前锁定版本。

| 类别 | 技术 | 版本 | 说明 |
|------|------|:---:|------|
| 语言 | Java (Dragonwell / OpenJDK) | 21 | JDK 21 基线 |
| 核心框架 | Spring Boot | 4.1.1 | Jakarta EE 11 基线 |
| 微服务基座 | Spring Cloud | 2025.1.3 | LoadBalancer / OpenFeign / Gateway WebFlux 5.x |
| 微服务套件 | Spring Cloud Alibaba | 2025.1.0.0 | Nacos Client 3.1.1 / Sentinel 1.8.9 |
| 注册/配置中心 | Nacos Server | 3.2.3 | 控制台端口 8080，API 8848，gRPC 9848 |
| 流控/熔断 | Sentinel | 1.8.9 | 规则支持 Nacos 数据源动态持久化 |
| 链路埋点 | SkyWalking Toolkit | 9.6.0 | `apm-toolkit-logback-1.x` trace-id 埋点 |
| 辅助工具 | print-server-address | 4.0.0 | 服务启动输出地址（来自 netbuffer-github 仓库） |
| 辅助工具 | Lombok | 1.18.46 | 编译期代码生成 |
| 容器底座 | supervisord (Dragonwell 21) | v4.1.0-alijdk21 | 多模块整合单容器一键部署 |

---

## 2. 模块职责与端口分配

| 模块 | 端口 | 应用名 (`spring.application.name`) | 核心职责 |
|------|:---:|------|------|
| `user-service-provider` | 8700 | `user-service-provider` | 服务提供者：REST API、Nacos 服务注册、慢调用/重试测试桩 |
| `user-service-invoker` | 8701 | `user-service-invoker` | 服务消费者：LoadBalancer + RestTemplate、OpenFeign 客户端、服务发现探索 |
| `order-service` | 8702 | `order-service` | 配置中心与熔断：Nacos 配置加载（`spring.config.import`）、Sentinel 限流持久化 |
| `spring-cloud-gateway` | 8709 | `spring-cloud-gateway` | API 网关：WebFlux 响应式架构、自定义谓词/过滤器、Sentinel 网关限流 |

### 调用链路

```text
🌐 gateway :8709
   ├── /scadusp/** → user-service-provider :8700
   ├── /scadusi/** → user-service-invoker :8701 ──Feign/LB──→ user-service-provider :8700
   └── /scados/**  → order-service :8702 ──配置/规则──→ nacos :8848
```

---

## 3. 构建与部署

### 3.1 本地构建

- **JDK**：Alibaba Dragonwell 21 / OpenJDK 21
- **Maven**：3.9+

```bash
# 全工程打包（跳过测试执行）
mvn clean package -DskipTests --batch-mode

# 单独打包指定模块
mvn clean package -pl order-service -am -DskipTests
```

### 3.2 容器化交付

- Dockerfile 基于 `javawiki/supervisord:v4.1.0-alijdk21`，整合 4 个子模块的 jar 包由 supervisord 统一托管守护。
- 打包前必须先执行 `mvn clean package -DskipTests` 生成各模块 target jar：
  - `order-service/target/order-service.jar`
  - `spring-cloud-gateway/target/spring-cloud-gateway.jar`
  - `user-service-invoker/target/user-service-invoker.jar`
  - `user-service-provider/target/user-service-provider.jar`

---

## 4. 关键架构约束与避坑铁律（红线）

1. **javax → jakarta 命名空间全面迁移**：
   - 严禁引入任何 `javax.servlet.*` 或 `javax.validation.*` 依赖与导入，全链路使用 `jakarta.*`。
2. **Spring Cloud Gateway WebFlux 约束**：
   - 网关采用 Reactive 栈，依赖为 `spring-cloud-starter-gateway-server-webflux`。
   - 网关模块严禁引入 `spring-boot-starter-web`（Servlet 容器会导致网关启动崩溃或行为异常）。
   - 网关路由配置前缀为 `spring.cloud.gateway.server.webflux.*`。
3. **告别 bootstrap，统一采用 `spring.config.import`**：
   - Spring Cloud Alibaba 2025.x 移除传统 `bootstrap.yml` 机制，配置均置于 `application.yml`。
   - Nacos 配置导入使用标准语法：`spring.config.import: optional:nacos:${spring.application.name}.yaml?refreshEnabled=true`。

---

## 5. 包与代码规范

### 5.1 包结构约定

```text
cn.netbuffer.springclouddemo.<模块去除中划线>
  ├── config         # 自动配置与 Bean 声明
  ├── controller     # Web 控制器
  ├── client         # Feign 客户端与外部调用
  ├── filter         # 网关过滤器（仅 gateway 模块）
  ├── route          # 路由断言与工厂（仅 gateway 模块）
  └── ...
```

### 5.2 命名规范

- **类名**：大驼峰（`OrderController`、`TokenRoutePredicateFactory`）。
- **配置属性**：小写中划线（kebab-case，如 `print-server-address.ignore-parent-context`）。
- **环境变量**：大写下划线，规范命名前缀，例如 `SCAD_USP_NACOS_ADDR`、`SCAD_GW_NACOS_NS`。

### 5.3 Git 提交规范

遵循 Conventional Commits：
```text
<type>(<scope>): <subject>

# 示例：
feat(gateway): 增加令牌校验断言工厂
fix(order-service): 修复 nacos 配置动态刷新偶发空指针
ci: 增加 GitHub Actions 自动化构建工作流
```

---

## 6. 给 AI 助手的行动指南

1. **修改代码前**：核实对应模块是 WebMvc 还是 WebFlux，坚决避免在网关引入 WebMvc 依赖或代码。
2. **新增配置时**：优先通过环境变量支持动态注入（如 `${SCAD_...:默认值}`），确保与 Docker / Compose 环境契约一致。
3. **保持最小依赖**：公共依赖在父 `pom.xml` 中通过 `<dependencyManagement>` 维护，子模块不写版本号。
4. **避免无关重构**：严禁无故修改已有模块端口及 Nacos DataId/Group 规范。
