package com.example.product.client;

import com.example.bean.Result;
import com.example.product.domain.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(value = "service-product", fallback = ProductServiceFallBack.class) //指定微服务名称和容错类
//@FeignClient(value = "service-product", fallbackFactory = ProductServiceFallBackFactory.class) //与seata回滚冲突
@FeignClient(value = "service-product")
public interface ProductService {
    /**指定调用提供者的哪个方法
     * @FeignClient+@GetMapping = 一个完整的请求路径 http://service-product/product/{pid}
     * */
    @GetMapping("/product/{pid}")
    Result<Product> findByPid(@PathVariable("pid") Long pid);

    @GetMapping("/product/reduceStock")
    Result reduceStock(@RequestParam("pid") Long pid, @RequestParam("number") Integer number);
}
