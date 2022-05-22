package cn.netbuffer.springclouddemo.userserviceprovider.api;

import cn.netbuffer.springclouddemo.userserviceprovider.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "retry-test")
    public ResponseEntity retryTest(HttpServletRequest httpServletRequest, @RequestParam(value = "code", defaultValue = "200") Integer code) {
        log.debug("retryTest code={}", code);
        HttpStatus httpStatus;
        switch (code) {
            case 200:
                httpStatus = HttpStatus.OK;
                break;
            case 502:
                httpStatus = HttpStatus.BAD_GATEWAY;
                break;
            case 503:
                httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
                break;
            case 504:
                httpStatus = HttpStatus.GATEWAY_TIMEOUT;
                break;
            default:
                httpStatus = HttpStatus.OK;
        }
        log.debug("retryTest return httpStatus={}", httpStatus);
        return new ResponseEntity(httpServletRequest.getMethod() + " " + httpServletRequest.getRequestURI() + " queryString=" + httpServletRequest.getQueryString() + " return code=" + httpStatus.value(), httpStatus);
    }

    @GetMapping(value = "/{id}")
    public String getUser(@PathVariable("id") Long id, Integer s) {
        log.info("receive getUser {},sleep {} s", id, s);
        if (s != null) {
            //模拟请求耗时测试熔断
            try {
                TimeUnit.SECONDS.sleep(s);
            } catch (InterruptedException e) {
                log.error("sleep error:{}", e.getMessage());
            }
        }
        String result = "user:" + id;
        log.info("return {}", result);
        return result;
    }

    @PostMapping
    public Map addUser(@RequestBody Map user) {
        log.info("receive user map={}", user);
        return userService.add(user);
    }
}