package com.example.order.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: lij
 * @create: 2020-02-21 17:05
 */
@Configuration
@ComponentScan(basePackages = {"com.example.product.client"},  includeFilters = {
        @ComponentScan.Filter(type= FilterType.ANNOTATION, classes = {Component.class})
}, useDefaultFilters = false)
@EnableFeignClients(basePackages = {"com.example.product.client"})
public class ClientConfig {
}
