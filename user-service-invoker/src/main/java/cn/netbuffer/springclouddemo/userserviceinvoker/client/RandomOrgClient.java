package cn.netbuffer.springclouddemo.userserviceinvoker.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "randomOrgApiClient", url = "https://www.random.org")
public interface RandomOrgClient {

    //https://www.random.org/integers/?num=10&min=1&max=6&col=1&base=10&format=plain&rnd=new
    @GetMapping("integers")
    String integers(@RequestParam(value = "num", defaultValue = "1") Integer num, @RequestParam(value = "min", defaultValue = "1") Integer min, @RequestParam(value = "max", defaultValue = "10000") Integer max,
                    @RequestParam(value = "col", defaultValue = "1") Integer col, @RequestParam(value = "base", defaultValue = "10") Integer base, @RequestParam(value = "format", defaultValue = "plain") String format,
                    @RequestParam(value = "rnd", defaultValue = "new") String rnd);
}