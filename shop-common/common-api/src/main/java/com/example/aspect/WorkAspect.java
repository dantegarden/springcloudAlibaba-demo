package com.example.aspect;

import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description: 用于处理程序调用发生异常的时候由于异常被处理以后无法触发事务，而进行的处理，使之可以正常的触发事务。
 * @author: lij
 * @create: 2020-02-26 14:22
 */
@Aspect
@Component
@Order
@Slf4j
public class WorkAspect {

    @Pointcut("@annotation(com.example.annotation.DistributedTransactional)")
    public void pointCut(){
    }

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) throws TransactionException {
        log.info("@Before：{}分布式事务方法开始，入参：{}", joinPoint.getSignature().getName(), joinPoint.getArgs());
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        log.info("拦截到需要分布式事务的方法：{}", method.getName());
        // 开启分布式事务
        GlobalTransaction tx = GlobalTransactionContext.getCurrentOrCreate();
        tx.begin(300000, "global-tx");
//        RootContext.bind(tx.getXid());
        log.info("创建分布式事务完毕，XID:{}", tx.getXid());
    }

    @AfterThrowing(value = "pointCut()", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint,  Exception exception) throws TransactionException {
        /***
         * 写个切面切所有的降级方法，一旦before降级方法就手动回滚
         * 下游服务异常必须要原封不动的抛出来，可以自定义一个异常，当下游异常被全局异常处理器拦截的时候再抛出个新异常，或者全局异常处理器不拦截自定义的异常
         * 这样就解决fallback会破坏分布式事务的问题了
         * ***/
        log.info("@AfterThrowing：{}方法发生异常，异常是：{}", joinPoint.getSignature().getName(), exception.getMessage());
        if (StringUtils.isNotBlank(RootContext.getXID())) { //手动回滚
            log.info("XID:{} 事务回滚", RootContext.getXID());
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
        }
    }

    @AfterReturning(value = "pointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint,  Object result) throws TransactionException {
        log.info("@AfterReturning：{}方法执行结束，返回值：{}", joinPoint.getSignature().getName(), result);
        if (StringUtils.isNotBlank(RootContext.getXID())) {
            log.info("XID:{} 事务提交", RootContext.getXID());
            GlobalTransactionContext.reload(RootContext.getXID()).commit();
        }
    }
}
