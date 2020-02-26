package com.example.config;

import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @description:
 * @author: lij
 * @create: 2020-02-21 13:34
 */
@Configuration
@ComponentScans({
    @ComponentScan(value = "com.example.sentinel"),
    @ComponentScan(value = "com.example.handler"),
    @ComponentScan(value = "com.example.processor")
})
@Import(DataSourceProxyConfig.class)
public class CommonConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
