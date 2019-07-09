package com.example.config.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//允许配置动态更新
@RefreshScope
@RestController
public class DemoController {

    @Value("${author:nobody}")
    public String author;

    @RequestMapping("/test/config")
    public String testConfig(){
        return author;
    }
}
