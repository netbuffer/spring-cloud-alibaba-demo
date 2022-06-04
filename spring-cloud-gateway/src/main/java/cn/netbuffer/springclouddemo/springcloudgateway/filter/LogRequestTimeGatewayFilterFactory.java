package cn.netbuffer.springclouddemo.springcloudgateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LogRequestTimeGatewayFilterFactory extends AbstractGatewayFilterFactory<LogRequestTimeGatewayFilterFactory.Config> {

    public LogRequestTimeGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().pathWithinApplication().value();
            StopWatch stopWatch = new StopWatch("process " + path);
            stopWatch.start();
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                ServerHttpResponse response = exchange.getResponse();
                //Manipulate the response in some way
                stopWatch.stop();
                log.debug("process {} cost {} ms\n{}", path, stopWatch.getTotalTimeMillis(), stopWatch.prettyPrint());
            }));
        };
    }

    public static class Config {
    }

}