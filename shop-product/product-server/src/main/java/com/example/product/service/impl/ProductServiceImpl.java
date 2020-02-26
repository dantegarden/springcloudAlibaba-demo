package com.example.product.service.impl;

import com.example.bean.Result;
import com.example.exception.BizException;
import com.example.product.dao.ProductDao;
import com.example.product.domain.Product;
import com.example.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @description:
 * @author: lij
 * @create: 2020-02-21 01:23
 */
@Transactional
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product findByPid(Long pid) {
        return productDao.findById(pid).get();
    }

    @Override
    public Result reduceStock(Long pid, Integer number) {
        Optional<Product> productOptional = productDao.findById(pid);
        if(!productOptional.isPresent()){
           throw new BizException("商品不存在");
        }

        Product product = productOptional.get();
        if(product.getStock() < number){
            throw new BizException("商品库存不足");
        }

        product.setStock(product.getStock() - number);
        productDao.save(product);
        return Result.ok();
    }
}
