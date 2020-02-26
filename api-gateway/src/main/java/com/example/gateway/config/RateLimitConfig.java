package com.example.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.example.bean.Result;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @description: 利用sentinel做全局限流控制
 * @author: lij
 * @create: 2020-02-22 22:30
 */
//@Configuration //测试，先不让网关限流生效
public class RateLimitConfig {
    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public RateLimitConfig(ObjectProvider<List<ViewResolver>> viewResolversProvider, ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    //初始化一个限流的过滤器
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelGatewayFilter(){
        return new SentinelGatewayFilter();
    }

    //配置初始化的限流参数
    @PostConstruct
    public void initGatewayRules(){
        Set<GatewayFlowRule> rules = new HashSet<>();
        //routes维度
        rules.add(
                new GatewayFlowRule("svc-product") //资源名称，对应路由id
                    .setCount(1) //限流阈值
                    .setIntervalSec(1) //统计时间窗口，单位是秒，默认是1秒
        );
        //api组维度
        rules.add(new GatewayFlowRule("orderGroup") //api分组名称
                .setCount(1).setIntervalSec(1));
        GatewayRuleManager.loadRules(rules);
    }

    //配置限流的异常处理器
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler(){
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    //自定义限流异常页面
    @PostConstruct
    public void initBlockHandlers(){
        BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                return ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(BodyInserters.fromObject(Result.fail("接口被限流")));
            }
        };
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }

    //自定义API分组
    @PostConstruct
    private void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<> () ;
        //第一组
        ApiDefinition api1 = new ApiDefinition("productGroup")
                .setPredicateItems(new HashSet<ApiPredicateItem>(){{
            //匹配以/product-serv/product/开头的请求
            add(new ApiPathPredicateItem().setPattern("/product-serv/product/**")
                    .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
        }});
        //第二组
        ApiDefinition api2 = new ApiDefinition("orderGroup")
                .setPredicateItems(new HashSet<ApiPredicateItem>(){{
            ///order-serv/test/msg1 完整url路径匹配
            add(new ApiPathPredicateItem().setPattern("/order-serv/test/msg1"));
            add(new ApiPathPredicateItem().setPattern("/order-serv/test/msg2"));
        }}) ;
        definitions.add(api1);
        definitions.add(api2);
        GatewayApiDefinitionManager.loadApiDefinitions(definitions) ;
    }

}

