package cn.netbuffer.springclouddemo.orderservice.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/order")
public class OrderController {

    @Value("${orderType:'common'}")
    private String orderType;

    @GetMapping(value = "detail/{id}")
    public String getUser(@PathVariable("id") Long id) {
        log.debug("get order detail id={}", id);
        return orderType + ":" + id;
    }

}