package com.example.consumer.controller;

import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
public class DemoController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/test")
    public String test(String name){
           //1.手动从注册中心获取服务，再调用的方式
//        ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-discovery-provider");
//        String url = serviceInstance.getUri() + "/sayHello?name=" + name;
//        //获取元数据
//        RibbonLoadBalancerClient.RibbonServer ribbonServer = (RibbonLoadBalancerClient.RibbonServer) serviceInstance;
//        NacosServer nacosServer = (NacosServer) ribbonServer.getServer();
//        System.out.println("-->" + nacosServer.getMetadata());
//        return restTemplate.getForObject(url, String.class);

        //2.通过服务名直接请求
        return restTemplate.getForObject("http://nacos-discovery-provider/sayHello?name=" + name, String.class);
    }
}
