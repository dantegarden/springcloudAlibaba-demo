package com.example.user.dao;

import com.example.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: lij
 * @create: 2020-02-21 01:22
 */
@Repository
public interface UserDao extends JpaRepository<User, Long> {
}
