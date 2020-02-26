package com.example.gateway.filters;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义的全局过滤器，统一权限控制
 * 要求必须实现GlobalFilter, Ordered接口
 */
@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    //过滤器逻辑
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //从url参数里获取，也可以从cookie,header里获取
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if(StrUtil.isBlank(token)){
            log.info("认证未通过");
            //没有token，不转发，返回401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange); //放行
    }

    //标识当前过滤器的优先级， 返回值越小优先级越高
    @Override
    public int getOrder() {
        return 0;
    }

}
