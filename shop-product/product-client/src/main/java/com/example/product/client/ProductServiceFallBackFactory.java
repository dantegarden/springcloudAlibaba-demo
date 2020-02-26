package com.example.product.client;

import com.example.bean.Result;
import com.example.product.domain.Product;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: 产生ProductService的容错类的工厂
 * @author: lij
 * @create: 2020-02-22 16:31
 */
@Component
@Slf4j
public class ProductServiceFallBackFactory implements FallbackFactory<ProductService> {
    /**feign在调用过程中产生的throwable**/
    @Override
    public ProductService create(Throwable throwable) {
        log.error("调用service-product#findByPid时出错：{}", throwable);
        //这玩意就相当于ProductServiceFallBack的实例
        return new ProductService() {
            @Override
            public Result findByPid(Long pid) {
                return Result.fail("商品不存在");
            }

            @Override
            public Result reduceStock(Long pid, Integer number) {
                return Result.fail("减库存失败");
            }
        };
    }
}
