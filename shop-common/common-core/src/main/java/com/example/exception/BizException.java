package com.example.exception;

/**
 * @description: 业务
 * @author: lij
 * @create: 2020-02-25 20:09
 */
public class BizException extends RuntimeException {
    public BizException(String message) {
        super(message);
    }
}
