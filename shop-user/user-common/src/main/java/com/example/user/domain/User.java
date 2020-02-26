package com.example.user.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @description: 用户
 * @author: lij
 * @create: 2020-02-21 00:24
 */
@Entity(name = "shop_user")
@Data
@Accessors(chain = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键自增
    private Long id; //主键
    private String username;
    private String password;
    private String telephone;
}
