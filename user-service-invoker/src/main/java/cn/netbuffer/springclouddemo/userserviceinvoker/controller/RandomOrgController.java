package cn.netbuffer.springclouddemo.userserviceinvoker.controller;

import cn.netbuffer.springclouddemo.userserviceinvoker.client.RandomOrgClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/randomorg")
public class RandomOrgController {

    @Resource
    private RandomOrgClient randomOrgClient;

    @GetMapping("integers")
    public String integers(@RequestParam(value = "num", defaultValue = "1") Integer num, @RequestParam(value = "min", defaultValue = "1") Integer min, @RequestParam(value = "max", defaultValue = "10000") Integer max,
                           @RequestParam(value = "col", defaultValue = "1") Integer col, @RequestParam(value = "base", defaultValue = "10") Integer base, @RequestParam(value = "format", defaultValue = "plain") String format,
                           @RequestParam(value = "rnd", defaultValue = "new") String rnd) {
        return randomOrgClient.integers(num, min, max, col, base, format, rnd);
    }

}
