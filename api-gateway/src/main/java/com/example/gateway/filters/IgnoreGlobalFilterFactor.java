package com.example.gateway.filters;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: lij
 * @create: 2020-02-26 22:29
 */
@Order(0)
@Component
public class IgnoreGlobalFilterFactor extends AbstractGatewayFilterFactory<IgnoreGlobalFilterFactor.Config> {

    protected static final String IGNORE_GLOBAL_FILTER_NAME = "ATTRIBUTE_IGNORE_GLOBAL_FILTER";

    public IgnoreGlobalFilterFactor() {
        super(IgnoreGlobalFilterFactor.Config.class);
    }

    @Override
    public String name() {
        return "IgnoreGlobalFilter";
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                exchange.getAttributes().put(IGNORE_GLOBAL_FILTER_NAME, config.getIgnoreGlobalFilterName());
                return chain.filter(exchange); //放行
            }
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("ignoreGlobalFilterName");
    }

    @Data
    @NoArgsConstructor
    public static class Config {
        private String ignoreGlobalFilterName;
    }
}
