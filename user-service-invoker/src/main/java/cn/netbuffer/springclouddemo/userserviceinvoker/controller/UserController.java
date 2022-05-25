package cn.netbuffer.springclouddemo.userserviceinvoker.controller;

import cn.netbuffer.springclouddemo.userserviceinvoker.client.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/invoke")
public class UserController {

    @Resource
    private DiscoveryClient discoveryClient;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private UserClient userClient;

    @GetMapping("instances/{serviceId}")
    public List<ServiceInstance> getInstances(@PathVariable("serviceId") String serviceId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        for (ServiceInstance instance : instances) {
            //这里如果未设置prefer-ip-address: true，那么host将是主机名
            log.info("host={} port={} uri={}", instance.getHost(), instance.getPort(), instance.getUri());
        }
        return instances;
    }

    @GetMapping("getOrder")
    public int getOrder() {
        return discoveryClient.getOrder();
    }

    @GetMapping("description")
    public String description() {
        return discoveryClient.description();
    }

    @GetMapping("getServices")
    public List<String> getServices() {
        return discoveryClient.getServices();
    }

    @GetMapping("discoveryclient")
    public DiscoveryClient getDiscoveryClient() {
        return discoveryClient;
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable("id") Long id, Integer s) {
        log.info("invoke get user {},sleep {} s", id, s);
        if (s == null) {
            return restTemplate.getForObject("http://user-service-provider/user/" + id, String.class);
        } else {
            try {
                TimeUnit.SECONDS.sleep(s);
            } catch (InterruptedException e) {
                log.error("sleep InterruptedException", e);
            }
            String result = "user:" + id;
            log.info("return {}", result);
            return result;
        }
    }

    @GetMapping("headers")
    public Map<String, String> headers(HttpServletRequest httpServletRequest) {
        //经过zuul后会携带x-forwarded-prefix
        Enumeration<String> names = httpServletRequest.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            headers.put(key, httpServletRequest.getHeader(key));
        }
        return headers;
    }

    @GetMapping("from")
    public void from(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getHeader("x-forwarded-prefix") != null) {
            log.debug("from zuul");
        }
    }

    @GetMapping("/feign/user/{id}")
    public String fgetUser(@PathVariable("id") Long id, @RequestParam(required = false) Integer s) {
        return userClient.getUser(id, s);
    }
}
