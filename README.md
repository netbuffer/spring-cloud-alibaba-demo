# spring-cloud-alibaba-demo
* https://github.com/netbuffer/spring-cloud-alibaba-demo
* https://gitee.com/netbuffer/spring-cloud-alibaba-demo
* https://github.com/netbuffer/spring-cloud-demo
* https://gitee.com/netbuffer/spring-cloud-demo

## Help
* docker run --name nacos -e MODE=standalone -p 8848:8848 -d nacos/nacos-server:v2.1.0  (use docker run nacos)
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
* curl http://localhost:8709/scadusp/user/1
* curl http://localhost:8709/actuator/gateway/globalfilters
* curl http://localhost:8709/actuator/gateway/routefilters
* curl -H "X-Request-bd-id:1234" -v http://localhost:8709

## Reference
* https://github.com/alibaba/spring-cloud-alibaba/wiki
* https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/
* https://nacos.io/zh-cn/docs/system-configurations.html
* https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/
* https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/
* https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#writing-custom-route-predicate-factories
* https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#default-filters
* https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#global-timeouts
