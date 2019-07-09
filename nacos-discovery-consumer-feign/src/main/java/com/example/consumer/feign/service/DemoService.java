package com.example.consumer.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("nacos-discovery-provider")
public interface DemoService {
    @GetMapping("/sayHello")
    public String sayHello(@RequestParam  String name);
}
