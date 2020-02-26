package com.example.order.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.example.bean.Result;
import com.example.order.service.impl.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @description: 用于测试sentinel的容错规则
 * @author: lij
 * @create: 2020-02-22 00:52
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestServiceImpl testService;

    //以下两个接口用于测试sentinel容错功能是否生效
    //msg1和msg2测试流控规则 流控方式：直接、关联、链路
    @RequestMapping("/msg1")
    public Result message1(){
        testService.test("msg1");
        return Result.ok("msg1");
    }

    @RequestMapping("/msg2")
    public Result message2(){
        testService.test("msg2");
        return Result.ok("msg2");
    }

    //测试热点规则
    @RequestMapping("/msg3")
    @SentinelResource("msg3") //标识资源
    public Result message3(String name, Integer age){
        return Result.ok(StrUtil.format("msg3 name:{} age:{}", name, age));
    }

    //测试降级规则  异常数 异常占比
    @RequestMapping("/msg4")
    public Result message4(){
        if(new Random().nextInt(10)%2==0){
            throw new RuntimeException("抛异常啦");
        }
        return Result.ok();
    }

    //测试授权规则  自定义授权规则com.com.example.order.config.RequestOriginParserDefinition
    @RequestMapping("/msg5")
    public Result message5(String serviceName){
        return Result.ok("msg5:" + serviceName);
    }

    //测试@SentinelResource
    @RequestMapping("/msg6")
    public Result message6(){
        String msg6 = testService.test2("msg6");
        return Result.ok(msg6);
    }

    @RequestMapping("/ssss")
    public Result test(){
        testService.testException();
        return Result.ok();
    }

}
