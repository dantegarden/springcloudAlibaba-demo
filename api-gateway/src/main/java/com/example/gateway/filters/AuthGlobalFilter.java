package com.example.gateway.filters;

import com.alibaba.fastjson.JSON;
import com.example.user.dto.UserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //过滤器逻辑
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //跳过拦截
        String className = getClass().getName();
        if (className.equals(exchange.getAttribute(IgnoreGlobalFilterFactor.IGNORE_GLOBAL_FILTER_NAME))) {
            return chain.filter(exchange);
        }

        //从url参数里获取，也可以从cookie,header里获取
        ServerHttpRequest request = exchange.getRequest();
        HttpCookie cookie = request.getCookies().getFirst("X-Token");
        if(cookie == null || StringUtils.isEmpty(cookie.getValue())){
            log.info("认证未通过");
            return reject(exchange, HttpStatus.UNAUTHORIZED);
        }else{
            String userInfoJson = stringRedisTemplate.opsForValue().get(cookie.getValue());
            if(StringUtils.isBlank(userInfoJson)){
                return reject(exchange, HttpStatus.FORBIDDEN);
            }else{
                UserInfoDTO userInfoDTO = JSON.parseObject(userInfoJson, UserInfoDTO.class);
                if(!userInfoDTO.getRole().equals(1)){
                    return reject(exchange, HttpStatus.UNAUTHORIZED);
                }
                return chain.filter(exchange); //放行
            }
        }
    }


    private Mono<Void> reject(ServerWebExchange exchange, HttpStatus status){
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    //标识当前过滤器的优先级， 返回值越小优先级越高
    @Override
    public int getOrder() {
        return 100;
    }


}
