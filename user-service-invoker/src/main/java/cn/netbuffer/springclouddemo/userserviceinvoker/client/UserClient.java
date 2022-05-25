package cn.netbuffer.springclouddemo.userserviceinvoker.client;

import com.google.common.collect.ImmutableMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@FeignClient(value = "user-service-provider", fallback = UserClient.UserClientFallback.class)
public interface UserClient {

    Map nullUser = ImmutableMap.of("id", -1);

    @GetMapping("/user/{id}")
    String getUser(@PathVariable("id") Long id, @RequestParam Integer s);

    @PostMapping("/user")
    Map add(@RequestBody Map user);

    @Component
    class UserClientFallback implements UserClient {
        @Override
        public String getUser(Long id, Integer s) {
            return id + ":" + s + ":error at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        @Override
        public Map add(Map user) {
            return nullUser;
        }
    }

}
