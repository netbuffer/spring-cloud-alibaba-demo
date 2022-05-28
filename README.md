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

## Reference
* https://github.com/alibaba/spring-cloud-alibaba/wiki
* https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/
* https://nacos.io/zh-cn/docs/system-configurations.html