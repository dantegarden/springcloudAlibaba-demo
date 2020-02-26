package com.example.annotation;

import java.lang.annotation.*;

/**
 * @description: 自定义分布式事务注解
 * @author: lij
 * @create: 2020-02-26 16:17
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedTransactional {
    String value() default "";
}
