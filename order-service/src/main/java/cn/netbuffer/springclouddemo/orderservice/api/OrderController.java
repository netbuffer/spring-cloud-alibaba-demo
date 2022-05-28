package cn.netbuffer.springclouddemo.orderservice.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/order")
public class OrderController {

    @Value("${orderType:'common'}")
    private String orderType;

    @Resource
    private Environment environment;

    @GetMapping(value = "detail/{id}")
    public String getUser(@PathVariable("id") Long id) {
        log.debug("get order detail id={}", id);
        return orderType + ":" + id;
    }

    @GetMapping(value = "dictionaries")
    public Object dictionaries(String key, String className) throws ClassNotFoundException {
        log.debug("get dictionaries key={} className={}", key, className);
        Object value = environment.getProperty(key, Class.forName(className));
        log.debug("get dictionaries key={} value={}", key, value);
        return value;
    }

}