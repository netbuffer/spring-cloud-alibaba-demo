# 🌱 spring-cloud-alibaba-demo

![](https://img.shields.io/static/v1?label=java&message=21&color=f89820)
![](https://img.shields.io/static/v1?label=maven&message=3.9.6&color=C71A36)
![](https://img.shields.io/static/v1?label=spring-boot&message=4.1.1&color=6DB33F)
![](https://img.shields.io/static/v1?label=spring-cloud&message=2025.1.3&color=6DB33F)
![](https://img.shields.io/static/v1?label=spring-cloud-alibaba&message=2025.1.0.0&color=F68243)
![](https://img.shields.io/static/v1?label=nacos&message=3.2.3&color=F68243)
![](https://img.shields.io/static/v1?label=lombok&message=1.18.46&color=blue)

> 🔬 Spring Cloud Alibaba 微服务组件技术验证工程（2026 全家桶升级版）

* https://github.com/netbuffer/spring-cloud-alibaba-demo
* https://gitee.com/netbuffer/spring-cloud-alibaba-demo
* https://github.com/netbuffer/spring-cloud-demo
* https://gitee.com/netbuffer/spring-cloud-demo

---

## 🧱 技术栈

| 组件 | 版本 | 说明 |
|---|---|---|
| ☕ Java | 21 | Dragonwell / OpenJDK 均可 |
| 🍃 Spring Boot | 4.1.1 | Jakarta EE 11 基线 |
| ☁️ Spring Cloud | 2025.1.3 | LoadBalancer / OpenFeign / Gateway Server WebFlux 5.x |
| 🐑 Spring Cloud Alibaba | 2025.1.0.0 | Nacos Client 3.1.1 / Sentinel 1.8.9 |
| 🛰️ Nacos Server | 3.2.3 | 注册中心 + 配置中心（`spring.config.import` 新机制） |
| 🛡️ Sentinel | 1.8.9 | 流控/熔断，规则持久化到 Nacos |
| 👣 SkyWalking Toolkit | 9.6.0 | logback trace-id 埋点（需配套 9.x agent） |

## 📦 模块结构

```
spring-cloud-alibaba-demo
├── 🧩 user-service-provider   # 服务提供者 :8700
├── 🧩 user-service-invoker    # 服务消费者（Feign + LoadBalancer RestTemplate）:8701
├── 🧩 order-service           # Nacos 配置中心 + Sentinel 规则持久化示例 :8702
└── 🧩 spring-cloud-gateway    # 网关（自定义 Filter/Predicate）:8709
```

调用链路：

```
🌐 gateway :8709
   ├── /scadusp/** → user-service-provider
   ├── /scadusi/** → user-service-invoker ──Feign/LB──→ user-service-provider
   └── /scados/**  → order-service ──配置/规则──→ nacos :8848
```

---

## 🚀 快速开始

### 1️⃣ 启动 Nacos

```shell
docker run -d --name nacos -e MODE=standalone \
  -e NACOS_AUTH_ENABLE=false \
  -e NACOS_AUTH_TOKEN=U0NBRC1kZW1vLXNlY3JldC1rZXktMDEyMzQ1Njc4OS0yMDI2 \
  -e NACOS_AUTH_IDENTITY_KEY=scadIdentity -e NACOS_AUTH_IDENTITY_VALUE=scadValue \
  -p 8848:8848 -p 9848:9848 -p 8080:8080 nacos/nacos-server:v3.2.3
```

或只启动 compose 中的 nacos 服务：`docker compose up -d scad-nacos`

> 💡 控制台地址 `http://localhost:8080`（3.x 起独立端口），API 在 `:8848/nacos`
>
> ⚠️ 3.x 镜像强制要求提供 Base64 的 `NACOS_AUTH_TOKEN`，即使关闭鉴权

### 2️⃣ 创建命名空间 & 配置

控制台创建命名空间，**ID 必须为** `3f21b39c-8476-4520-ac89-3ea4c0e1f47b`（也可用环境变量 `SCAD_*_NACOS_NS` 覆盖）。

order-service 需要以下配置（新版已改用 `spring.config.import` 加载）：

| dataId | Group | 内容示例 |
|---|---|---|
| `order-service.yaml` | DEFAULT_GROUP | `orderType: common-order` |
| `order-service-corp.yaml` | DEFAULT_GROUP | `orderType: corp-express` |
| `dictionaries.yaml` | order | `maxOrderCount: 100` |
| `order-service-sentinel.json` | order | Sentinel 流控规则 JSON |

参考截图见 [help/](help/) 目录 📷

### 3️⃣ 构建与启动

```shell
# JDK 21 + Maven 3.9+
mvn clean package -DskipTests

java -jar user-service-provider/target/user-service-provider.jar
java -jar user-service-invoker/target/user-service-invoker.jar
java -jar order-service/target/order-service.jar
java -jar spring-cloud-gateway/target/spring-cloud-gateway.jar
```

一键脚本（含 SkyWalking agent 参数）：[sh/startup.sh](sh/startup.sh)

### 4️⃣ （可选）启动 Sentinel Dashboard

```shell
docker run -d --name sentinel-dashboard -p 8800:8858 bladex/sentinel-dashboard:1.8.9
```

> 🔑 控制台 <http://localhost:8800>，账号/密码 `sentinel` / `sentinel`；规则持久化在 Nacos，详见 [sentinel-dashboard/README.md](sentinel-dashboard/README.md) 🛡️

---

## 🔌 接口速查

```shell
# 服务提供者
curl http://localhost:8700/user/1                          # 查询用户
curl -XPOST http://localhost:8700/user -H "Content-Type: application/json" -d '{"name":"alice"}'
curl "http://localhost:8700/user/retry-test?code=502"      # 熔断重试测试

# 服务消费者
curl http://localhost:8701/invoke/user/1                   # RestTemplate + LoadBalancer
curl http://localhost:8701/invoke/feign/user/1?s=6         # OpenFeign（s 秒模拟慢调用）
curl http://localhost:8701/invoke/instances/user-service-provider   # 服务发现
curl http://localhost:8701/randomorg/integers              # Feign 直连外部 URL

# 配置中心（支持 @RefreshScope 动态刷新）
curl http://localhost:8702/order/detail/1
curl "http://localhost:8702/order/dictionaries?key=maxOrderCount&className=java.lang.Integer"

# 网关路由
curl http://localhost:8709/scadusp/user/1                  # → provider
curl http://localhost:8709/scadusi/invoke/feign/user/1     # → invoker → provider
curl http://localhost:8709/scados/order/detail/1           # → order-service
curl http://localhost:8709/actuator/gateway/routes         # 查看路由
curl -H "X-Request-bd-id:1234" -v http://localhost:8709    # Header 谓词 → baidu
curl -v "http://localhost:8709/no-such-path?x-user-type=test"   # 自定义 Token 谓词 → taobao(SetStatus=400)
```

IDEA 用户可直接使用 [help/http-requests.http](help/http-requests.http) 🧪

---

## 🐳 Docker 一键部署

```shell
mvn clean package -DskipTests
docker compose up -d          # 自动构建 scad-app 镜像并随健康检查后的 scad-nacos 启动
```

镜像：https://hub.docker.com/r/javawiki/spring-cloud-alibaba-demo （`:vsca.2025.1.0.0` 起 基于 [javawiki/supervisord:v4.1.0-alijdk21](https://hub.docker.com/r/javawiki/supervisord) 🐉 Dragonwell 21）

<details>
<summary>手动 docker run 方式</summary>

```shell
docker build -t javawiki/spring-cloud-alibaba-demo:vsca.2025.1.0.0 .
docker run --rm -it -p 8700:8700 -p 8701:8701 -p 8702:8702 -p 8709:8709 \
  -e TZ=Asia/Shanghai \
  -e SCAD_OS_NACOS_ADDR=nacos:8848 -e SCAD_OS_NACOS_NS=public \
  -e SCAD_GW_NACOS_ADDR=nacos:8848 -e SCAD_GW_NACOS_NS=public \
  -e SCAD_USI_NACOS_ADDR=nacos:8848 -e SCAD_USI_NACOS_NS=public \
  -e SCAD_USP_NACOS_ADDR=nacos:8848 -e SCAD_USP_NACOS_NS=public \
  --link nacos --name scad -h scad javawiki/spring-cloud-alibaba-demo:vsca.2025.1.0.0
```
</details>

---

## ⚙️ 关键变更说明（相对 Boot 2.x 时代）

- 🔄 **javax → jakarta**：Servlet/注解校验等 API 全面切换 Jakarta 命名空间
- 📦 **网关坐标更名**：`spring-cloud-starter-gateway` → `spring-cloud-starter-gateway-server-webflux`，配置前缀同步迁移至 `spring.cloud.gateway.server.webflux.*`
- 🧹 **告别 bootstrap**：SCA 2025 已移除 bootstrap 配置加载机制，order-service 改用 `spring.config.import: optional:nacos:` 方式加载配置
- 🚫 根 pom 不再统一注入 `spring-boot-starter-web`，由各 Servlet 模块自行声明，网关保持纯 Reactive 无需 hack
- 📝 日志统一输出到项目运行目录下 `.logs/<应用名>/<应用名>.log`（可通过 `logging.file.path` 覆盖）

## 📚 Reference

* https://github.com/alibaba/spring-cloud-alibaba/wiki
* https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E
* https://docs.spring.io/spring-boot/index.html
* https://docs.spring.io/spring-cloud-openfeign/reference/
* https://docs.spring.io/spring-cloud-gateway/reference/
* https://nacos.io/docs/latest/manual/user/java-sdk/
* https://nacos.io/zh-cn/docs/system-configurations.html
* https://github.com/alibaba/Sentinel/wiki
