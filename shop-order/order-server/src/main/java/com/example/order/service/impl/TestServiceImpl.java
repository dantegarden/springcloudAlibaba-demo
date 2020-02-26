package com.example.order.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.order.service.TestService;
import com.example.order.service.block.TestServiceBlockHandler;
import com.example.order.service.block.TestServiceFallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @description: 用于测试sentinel限流（链路模式）的service
 * @author: lij
 * @create: 2020-02-21 22:54
 */
@Service
@Slf4j
public class TestServiceImpl implements TestService {

    @SentinelResource("test") //指定调用服务的名字
    @Override
    public String test(String source){
        log.info("{}调用TestServiceImpl#test", source);
        return source;
    }

    /**定义一个资源
     * 定义当资源内部发生异常时的处理逻辑
     * blockHandler 定义当资源内部发生了BlockException时应该进入的方法（捕获的是sentinel定义的异常）
     * fallback 定义当资源内部发生了Throwable时应该进入的方法
     **/
    @SentinelResource( value = "test2",
            blockHandlerClass = TestServiceBlockHandler.class,
            blockHandler = "blockHandler",
            fallbackClass = TestServiceFallBack.class,
            fallback = "fallback")
    @Override
    public String test2(String source){
        log.info("{}调用TestServiceImpl#test2", source);
        if(new Random().nextInt(10)%2 == 0){
//            int i = 1/0; //模拟业务逻辑错误
        }
        return source;
    }

    @Override
    public String testException(){
//        try {
            int i = 1/0;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return "";
    }

}
