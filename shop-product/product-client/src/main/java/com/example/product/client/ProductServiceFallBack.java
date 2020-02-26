package com.example.product.client;

import com.example.bean.Result;
import com.example.product.domain.Product;
import org.springframework.stereotype.Service;

/**
 * @description: ProductService的容错类
 * @author: lij
 * @create: 2020-02-22 16:07
 */
@Service
public class ProductServiceFallBack implements ProductService {
    /**需要实现feign接口里的所有方法，
     * 一旦feign的接口方法出现问题，就会进入当前类的同名方法，执行容错逻辑
     * **/
    @Override
    public Result findByPid(Long pid) {
        return Result.fail("商品不存在");
    }

    @Override
    public Result reduceStock(Long pid, Integer number) {
        return Result.fail("减库存失败");
    }
}
