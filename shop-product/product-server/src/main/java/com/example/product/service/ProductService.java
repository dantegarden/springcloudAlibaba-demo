package com.example.product.service;

import com.example.bean.Result;
import com.example.product.domain.Product;

public interface ProductService {
    /**根据pid查询商品信息*/
    Product findByPid(Long pid);
    /**扣库存*/
    Result reduceStock(Long pid, Integer number);
}
