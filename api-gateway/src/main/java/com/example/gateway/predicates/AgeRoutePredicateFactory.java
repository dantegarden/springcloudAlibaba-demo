package com.example.gateway.predicates;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义断言工厂，要求：
 * 1.类名必须是配置+RoutePredicateFactory，如 - Age的类名就是AgeRoutePredicateFactory
 * 2.必须继承AbstractRoutePredicateFactory<类名.配置类> 其中配置类用于接收配置中的参数
 */
@Component
public class AgeRoutePredicateFactory extends AbstractRoutePredicateFactory<AgeRoutePredicateFactory.Config> {
    //构造函数
    public AgeRoutePredicateFactory() {
        super(AgeRoutePredicateFactory.Config.class);
    }

    //读取配置文件中的参数，并赋值到配置类的对应属性上
    @Override
    public List<String> shortcutFieldOrder() {
        //注意，这个位置的顺序必须和配置文件的值的顺序对应
        return Arrays.asList("minAge", "maxAge");
    }

    //断言逻辑
    @Override
    public Predicate<ServerWebExchange> apply(AgeRoutePredicateFactory.Config config) {
        return new Predicate<ServerWebExchange>() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                //1接收前台传入的age参数 （实际可以从header、session或redis取） webflux的取参数这么写
                String ageStr = serverWebExchange.getRequest().getQueryParams().getFirst("age");
                //2判断是否为空
                if(StrUtil.isNotBlank(ageStr)){
                    //3如果不为空，再进行路由逻辑判断
                    int age = Integer.parseInt(ageStr);
                    if(age <= config.getMaxAge() && age >= config.getMinAge()){
                        return true;
                    }
                }
                return false;
            }
        };
    }
    //配置类，用于接收配置文件中的参数
    @Data
    @NoArgsConstructor
    public static class Config {
        private int minAge;
        private int maxAge;
    }
}
