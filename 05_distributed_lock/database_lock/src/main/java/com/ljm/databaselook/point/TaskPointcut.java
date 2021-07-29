package com.ljm.databaselook.point;

import com.ljm.databaselook.model.MethodLock;
import com.ljm.databaselook.repository.MethodLockRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Dominick Li
 * @date 2019/7/20 8:03 AM
 */
@Aspect
@Configuration
public class TaskPointcut {

    @Autowired
    private MethodLockRepository methodLockRepository;
    private final Logger logger = LoggerFactory.getLogger(TaskPointcut.class);

    @Value("${server.port}")
    private Integer port;

    /**
     * 需要加分布式锁的切入点
     * 这里可以指定OrderController下面的所有方法
     */
    @Pointcut("execution(public * com.ljm.databaselook.controller.OrderController.*(..)))")
    //@Pointcut("execution(public * com.ljm.databaselook.task.ScheduledTask.task1())")
    public void methodLock() {
    }


    /**
     * 事前处理
     * 获取锁 尝试获取锁
     *
     * @return 成功获取锁, 继续执行操作, 获取锁失败则返回错误信息
     */
    @Around("methodLock()")
    public Object around(ProceedingJoinPoint pj) {
        logger.info("Try to acquire the lock");
        try {
            MethodSignature signature = (MethodSignature) pj.getSignature();
            String methodName = signature.getMethod().getName();
            String methodDesc=Thread.currentThread().getId() + "-"+port;
            //插入数据成功则代表获取锁成功
            methodLockRepository.insert(methodName,methodDesc,new Date());
            logger.info("around getLook success taskName={},methodDesc={}", methodName,methodDesc);
            return pj.proceed();
        } catch (Throwable e) {
           logger.info("getLook fail error={}",e);
            return "getLook fail";
        }
    }

    /**
     * 事后处理
     * 释放锁信息
     */
    @After("methodLock()")
    public void doAfterAdvice(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        //只能释放当前线程拿到的锁信息
        MethodLock methodLock = methodLockRepository.findByMethodNameAndMethodDesc(methodName, Thread.currentThread().getId() +"-"+port);
        if (methodLock != null) {
            logger.info("freed lock method:{}", methodName);
            methodLockRepository.delete(methodLock);
            logger.info("doAfterAdvice unLook methodName={}", methodName);
        }
    }


    /**
     * 异常处理 释放锁
     */
    @AfterThrowing("methodLock()")
    public void afterThrowing(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        MethodLock methodLock = methodLockRepository.findByMethodNameAndMethodDesc(methodName, Thread.currentThread().getId() +"-"+port);
        if (methodLock != null) {
            logger.error("freed lock method:{}", methodName);
            methodLockRepository.delete(methodLock);
            logger.info("afterThrowing unLook methodName={}", methodName);
        }
    }

}
