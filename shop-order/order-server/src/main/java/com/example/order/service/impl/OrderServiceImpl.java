package com.example.order.service.impl;

import com.example.order.dao.OrderDao;
import com.example.order.domain.Order;
import com.example.order.service.OrderService;
import com.example.product.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: lij
 * @create: 2020-02-21 01:57
 */
@Transactional
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Override
    public Order createOrder(Product product, Integer number) {
        Order order = new Order();
        order.setUid(1L)
                .setUsername("me")
                .setPid(product.getId())
                .setProductName(product.getProductName())
                .setPrice(product.getPrice())
                .setNumber(number)
                .setTotalPrice(number * product.getPrice());
        orderDao.save(order);
        return order;
    }
}
