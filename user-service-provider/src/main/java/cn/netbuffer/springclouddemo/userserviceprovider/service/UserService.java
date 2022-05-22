package cn.netbuffer.springclouddemo.userserviceprovider.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;

@Slf4j
@Service
public class UserService {

    public Map add(Map user) {
        int id = (int) (Math.random() * 100000);
        log.debug("add user={} id={}", user, id);
        user.put("id", id);
        return user;
    }

}