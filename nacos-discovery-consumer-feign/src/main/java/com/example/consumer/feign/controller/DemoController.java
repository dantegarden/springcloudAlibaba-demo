package com.example.consumer.feign.controller;

import com.example.consumer.feign.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/test")
    public String test(String name){
        return demoService.sayHello(name);
    }
}
