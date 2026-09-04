# 🛡️ sentinel-dashboard（Docker 版）

> 基于 [bladex/sentinel-dashboard](https://hub.docker.com/r/bladex/sentinel-dashboard) 镜像，版本与项目 SCA 托管的 Sentinel `1.8.9` 保持一致，无需手动下载 jar。

## 🚀 启动

```shell
docker run -d --name sentinel-dashboard \
  -p 8800:8858 \
  bladex/sentinel-dashboard:1.8.9
```

或直接使用项目 compose：

```shell
docker compose up -d scad-sentinel-dashboard
```

## 🔑 控制台

* 地址：<http://localhost:8800>
* 账号 / 密码：`sentinel` / `sentinel`

## 🔌 与本项目的对接

各服务已预置好对接配置（以 order-service 为例，见 `application.yml`）：

```yaml
spring:
  cloud:
    sentinel:
      eager: true                     # 启动即注册到 dashboard
      transport:
        dashboard: 127.0.0.1:8800     # dashboard 地址（容器已映射到宿主机 8800）
        port: 8801                    # 本服务与 dashboard 通讯端口
```

order-service 的流控规则持久化在 Nacos（dataId `order-service-sentinel.json`、group `order`），dashboard 上可实时查看，规则变更请改 Nacos 配置。

## 📚 参考

* https://github.com/alibaba/Sentinel/tree/1.8.9/sentinel-dashboard
* https://sentinelguard.io/docs/latest/dashboard.html
