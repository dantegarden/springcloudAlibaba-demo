package com.example.product.dao;

import com.example.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: lij
 * @create: 2020-02-21 01:22
 */
@Repository
public interface ProductDao extends JpaRepository<Product, Long> {
}
