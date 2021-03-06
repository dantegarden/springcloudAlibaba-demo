package com.example.order.service.block;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 捕获BlockException的降级方法
 * @author: lij
 * @create: 2020-02-22 14:19
 */
@Slf4j
public class TestServiceFallBack {

    /** 降级方法（连业务异常一起捕获）
     * 要求：
     * 1.当前方法的返回值和参数要和原方法一致
     * 2.但是允许在参数列表最后一位多出一个参数BlockException，用来接收原方法中发生的异常
     * */
    public static String fallback(String source, Throwable e){
        log.error("{}调用TestServiceFallBack#fallback, 异常{}", source, e);
        return e.getMessage();
    }
}
