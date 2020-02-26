package com.example.product.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @description: 商品
 * @author: lij
 * @create: 2020-02-21 00:27
 */
@Entity(name = "shop_product")
@Data
@Accessors(chain = true)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    /**商品价格*/
    private Double price;
    /**库存*/
    private Integer stock;
}
