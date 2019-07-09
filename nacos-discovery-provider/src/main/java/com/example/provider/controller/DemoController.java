package com.example.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Value("${server.port}")
    private Integer port;

    @GetMapping("/sayHello")
    public String sayHello(String name){
        return  port + ": hello, " + name;
    }
}
