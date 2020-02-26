package com.example.product.controller;

import com.alibaba.fastjson.JSON;
import com.example.bean.Result;
import com.example.product.domain.Product;
import com.example.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: lij
 * @create: 2020-02-21 01:24
 */
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/{pid}")
    public Result<Product> product(@PathVariable Long pid){
        log.info("查询商品信息：{}", pid);
        Product product = productService.findByPid(pid);
        log.info("查询结果：{}", JSON.toJSONString(product));
        return Result.ok(product);
    }

    @RequestMapping("/reduceStock")
    public Result product(@RequestParam Long pid, @RequestParam Integer number){
        return productService.reduceStock(pid, number);
    }

}
