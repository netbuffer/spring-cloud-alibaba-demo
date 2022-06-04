package cn.netbuffer.springclouddemo.springcloudgateway.route.predicate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Component
public class TokenRoutePredicateFactory extends AbstractRoutePredicateFactory<TokenRoutePredicateFactory.Config> {

    public static final String PARAM_KEY = "param";
    public static final String REGEXP_KEY = "regexp";

    public TokenRoutePredicateFactory() {
        super(TokenRoutePredicateFactory.Config.class);
    }

    public List<String> shortcutFieldOrder() {
        return Arrays.asList(PARAM_KEY, REGEXP_KEY);
    }

    public Predicate<ServerWebExchange> apply(TokenRoutePredicateFactory.Config config) {
        log.debug("TokenRoutePredicateFactory.apply config={}", config);
        return new GatewayPredicate() {
            public boolean test(ServerWebExchange exchange) {
                if (!StringUtils.hasText(config.regexp)) {
                    boolean c = exchange.getRequest().getQueryParams().containsKey(config.param);
                    log.debug("getQueryParams().containsKey({})={}", config.param, c);
                    return c;
                } else {
                    List<String> values = (List) exchange.getRequest().getQueryParams().get(config.param);
                    if (values == null) {
                        return false;
                    } else {
                        Iterator iterator = values.iterator();

                        String value;
                        do {
                            if (!iterator.hasNext()) {
                                return false;
                            }

                            value = (String) iterator.next();
                        } while (value == null || !value.matches(config.regexp));
                        return true;
                    }
                }
            }

            public Object getConfig() {
                return config;
            }

            public String toString() {
                return String.format("Token Query: param=%s regexp=%s", config.getParam(), config.getRegexp());
            }
        };
    }

    @Validated
    public static class Config {
        @NotEmpty
        private String param;
        private String regexp;

        public Config() {
        }

        public String getParam() {
            return this.param;
        }

        public TokenRoutePredicateFactory.Config setParam(String param) {
            this.param = param;
            return this;
        }

        public String getRegexp() {
            return this.regexp;
        }

        public TokenRoutePredicateFactory.Config setRegexp(String regexp) {
            this.regexp = regexp;
            return this;
        }
    }

}
