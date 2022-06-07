package cn.netbuffer.springclouddemo.springcloudgateway.filter.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.net.URI;

@Slf4j
@Component
public class LogPathGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        HttpMethod httpMethod = exchange.getRequest().getMethod();
        URI uri = exchange.getRequest().getURI();
        log.debug("{} {} {}", httpMethod.name(), uri.getPath(), headers);
        return chain.filter(exchange);
    }

}