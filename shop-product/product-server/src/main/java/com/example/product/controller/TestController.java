package com.example.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 测试动态刷新配置
 * @author: lij
 * @create: 2020-02-24 16:29
 */
@RestController
@RequestMapping("/test")
@RefreshScope
public class TestController {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Value("${test-config.appName}")
    private String appName;

    @Value("${test-config.env}")
    private String env;

    //测试动态刷新配置 硬编码方式
    @GetMapping("/config1")
    public String testConfig(){
        return applicationContext.getEnvironment().getProperty("test-config.appName");
    }

    //测试动态刷新配置 注解方式
    @GetMapping("/config2")
    public String testConfig2(){
        return appName;
    }

    //便于测试跨微服务配置共享是否可以动态刷新
    @GetMapping("/config3")
    public String testConfig(@RequestParam String configKey){
        return applicationContext.getEnvironment().getProperty(configKey);
    }

    //测试配置共享，同个微服务不同环境的配置共享
    @GetMapping("/env")
    public String testEnv(){
        return env;
    }
}
