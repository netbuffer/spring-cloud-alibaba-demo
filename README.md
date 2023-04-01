# spring-cloud-alibaba-demo
* https://github.com/netbuffer/spring-cloud-alibaba-demo
* https://gitee.com/netbuffer/spring-cloud-alibaba-demo
* https://github.com/netbuffer/spring-cloud-demo
* https://gitee.com/netbuffer/spring-cloud-demo

## Help
* docker-compose up -d  (use docker-compose run nacos)
* curl http://localhost:8701/invoke/user/1?s=1
* curl http://localhost:8701/invoke/feign/user/1?s=6
* curl http://localhost:8702/order/detail/1
* curl -s "http://localhost:8702/order/dictionaries?key=order.sex&className=java.util.List" (not work)
* curl -s "http://localhost:8702/order/dictionaries?key=a&className=java.lang.String"
* curl -s "http://localhost:8702/order/dictionaries?key=corp.name&className=java.lang.String"
* curl -s "http://localhost:8702/order/dictionaries?key=home.address&className=java.lang.String"
* curl -s "http://localhost:8702/order/dictionaries?key=info.app.version&className=java.lang.String"
* curl http://localhost:8701/randomorg/integers
* curl http://localhost:8709/scados/order/detail/1
* curl http://localhost:8709/scadusi/randomorg/integers
* curl http://localhost:8709/scadusi/invoke/feign/user/1
* curl http://localhost:8709/scadusp/user/1
* curl http://localhost:8709/actuator/gateway/globalfilters
* curl http://localhost:8709/actuator/gateway/routefilters
* curl -H "X-Request-bd-id:1234" -v http://localhost:8709
* curl -v http://localhost:8709?x-user-type=test

### nacos
* docker run --name nacos -e MODE=standalone -p 8848:8848 -d nacos/nacos-server:v2.1.0  (use docker run nacos)
* docker-compose -f nacos-standalone.yml up -d
* http://localhost:8848/nacos nacos/nacos (create namespace with id 3f21b39c-8476-4520-ac89-3ea4c0e1f47b)

## Docker Image
* https://hub.docker.com/r/javawiki/spring-cloud-alibaba-demo
* docker build -t javawiki/spring-cloud-alibaba-demo:v1.0.0 .
* docker-compose up
* use docker run
```shell
docker run --name nacos -e MODE=standalone -p 8848:8848 -d nacos/nacos-server:v2.1.0
docker run --rm -it -p 8702:8702 -p 8709:8709 -p 8701:8701 -p 8700:8700 -e TZ=Asia/Shanghai -e SCAD_OS_NACOS_ADDR=nacos:8848 -e SCAD_OS_NACOS_NS=public -e SCAD_GW_NACOS_ADDR=nacos:8848 -e SCAD_GW_NACOS_NS=public -e SCAD_USI_NACOS_ADDR=nacos:8848 -e SCAD_USI_NACOS_NS=public -e SCAD_USP_NACOS_ADDR=nacos:8848 -e SCAD_USP_NACOS_NS=public --link nacos --name scad -h scad javawiki/spring-cloud-alibaba-demo:v1.0.0 bash
```

## Reference
* https://github.com/alibaba/spring-cloud-alibaba/wiki
* https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/
* https://nacos.io/zh-cn/docs/system-configurations.html
* https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/
* https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/
* https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#writing-custom-route-predicate-factories
* https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#default-filters
* https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#global-timeouts
* https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel#spring-cloud-gateway-%E6%94%AF%E6%8C%81
