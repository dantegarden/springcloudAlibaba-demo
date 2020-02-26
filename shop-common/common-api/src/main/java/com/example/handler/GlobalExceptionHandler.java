package com.example.handler;

import com.example.bean.Result;
import com.example.enums.HttpStatusCodeEnum;
import com.example.exception.BizException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 全局异常处理器
 * @author: lij
 * @create: 2020-02-25 20:10
 */
//@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BizException.class)
    Object handleBizException(BizException e, HttpServletRequest request){
        return Result.fail(e.getMessage(), HttpStatusCodeEnum.SUCCESS.getCode());
    }

    @ExceptionHandler(value = Exception.class)
    Object handleException(Exception e, HttpServletRequest request){
        return Result.fail(e.getMessage(), HttpStatusCodeEnum.SERVER_ERROR.getCode());
    }
}
