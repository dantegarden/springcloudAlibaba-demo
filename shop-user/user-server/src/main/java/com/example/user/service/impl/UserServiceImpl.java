package com.example.user.service.impl;

import com.example.user.dao.UserDao;
import com.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: lij
 * @create: 2020-02-21 01:23
 */
@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
}
