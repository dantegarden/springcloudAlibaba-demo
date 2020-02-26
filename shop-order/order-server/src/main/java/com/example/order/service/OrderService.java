package com.example.order.service;

import com.example.order.domain.Order;
import com.example.product.domain.Product;

public interface OrderService {
    /**创建订单**/
    Order createOrder(Product product, Integer number);
}
