package com.example.user.controller;

import com.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: lij
 * @create: 2020-02-21 01:24
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;
}
