package com.example.gateway.filters;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义局部过滤器，要求：
 * 1.类名必须是配置+GatewayFilterFactory，如 - Log的类名就是LogGatewayFilterFactory
 * 2.必须继承AbstractGatewayFilterFactory<类名.配置类> 其中配置类用于接收配置中的参数
 */
@Component
@Slf4j
public class LogGatewayFilterFactory extends AbstractGatewayFilterFactory<LogGatewayFilterFactory.Config> {
    //构造函数
    public LogGatewayFilterFactory(){
        super(LogGatewayFilterFactory.Config.class);
    }

    //读取配置文件中的参数 赋值到配置类中
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("consoleLog", "cacheLog");
    }

    //过滤器逻辑
    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                if(config.isCacheLog()){
                    log.info("cacheLog已经开启");
                }
                if(config.isConsoleLog()){
                    log.info("consoleLog已经开启");
                }
                return chain.filter(exchange); //放行
            }
        };
    }

    //配置类，接收配置文件的参数
    @Data
    @NoArgsConstructor
    public static class Config {
        private boolean consoleLog;
        private boolean cacheLog;
    }

}
