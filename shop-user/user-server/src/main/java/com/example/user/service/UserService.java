package com.example.user.service;

import com.example.user.domain.User;

public interface UserService {

    User findByUsernameAndPassword(String username, String password);
}
