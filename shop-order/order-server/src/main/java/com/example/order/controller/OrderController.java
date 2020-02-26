package com.example.order.controller;

import com.alibaba.fastjson.JSON;
import com.example.annotation.DistributedTransactional;
import com.example.bean.Result;
import com.example.exception.BizException;
import com.example.order.domain.Order;
import com.example.order.service.OrderService;
import com.example.product.client.ProductService;
import com.example.product.domain.Product;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description:
 * @author: lij
 * @create: 2020-02-21 01:57
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @GlobalTransactional
//    @DistributedTransactional //自定义注解，作用同@GlobalTransactional，手动开始/关闭分布式事务
    @PostMapping("/prod")
    public Result order(@RequestParam("pid") Long pid, @RequestParam Integer number){
        log.info("收到商品【{}】的下单请求", pid);
        //1.调用商品服务，查询商品信息
        Result<Product> result = productService.findByPid(pid);
        if(!result.getStatus()){
            return result;
        }
        Product product = result.getData();

        //2.创建订单
        Order order = orderService.createOrder(product, number);
        log.info("创建订单成功，订单信息为{}", JSON.toJSONString(order));

        //3.扣库存
        productService.reduceStock(pid, number);
        log.info("商品{}减库存成功", pid);

        //4.向MQ发布消息，topic和消息体
        rocketMQTemplate.convertAndSend("order-topic", order);

        return Result.ok(order);
    }

}
