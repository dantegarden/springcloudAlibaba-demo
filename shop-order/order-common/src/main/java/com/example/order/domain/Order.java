package com.example.order.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @description: 订单
 * @author: lij
 * @create: 2020-02-21 00:29
 */
@Entity(name = "shop_order")
@Data
@Accessors(chain = true)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**用户id*/
    private Long uid;
    private String username;
    /**商品id*/
    private Long pid;
    private String productName;
    private Double price;
    /**购买数量**/
    private Integer number;
    private Double totalPrice;
}
