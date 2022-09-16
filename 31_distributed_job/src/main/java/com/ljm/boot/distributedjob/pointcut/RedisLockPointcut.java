package com.ljm.boot.distributedjob.pointcut;

import com.ljm.boot.distributedjob.annotation.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author Dominick Li
 * @description 获取分布式锁切入点
 **/
@Aspect
@Slf4j
@Configuration
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "redis")
public class RedisLockPointcut {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Pointcut("@annotation(com.ljm.boot.distributedjob.annotation.RedisLock)")
    public void redisLockPointCut() {
    }

    @Around("redisLockPointCut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Method method = currentMethod(proceedingJoinPoint);
        //获取到方法的注解对象
        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        //获取锁的名称
        String methodName = redisLock.name();
        if (!StringUtils.hasLength(methodName)) {
            //如果注解里没有设置锁的名称,默认使用方法的名称
            methodName = method.getName();
        }
        //获取到锁的标识
        boolean flag = true;
        int retryCount = redisLock.retry();
        do {
            if (!flag && retryCount > 0) {
                Thread.sleep(1000L);
                retryCount--;
            }
            flag = stringRedisTemplate.opsForValue().setIfAbsent(methodName, "1", redisLock.expired(), TimeUnit.SECONDS);
            if (flag) {
                //获取到锁结束循环
                break;
            }
            //根据配置的重试次数,执行n次获取锁的方法,默认不重试
        } while (retryCount > 0);

        //result为连接点的返回结果
        Object result = null;
        if (flag) {
            try {
                result = proceedingJoinPoint.proceed();
            } catch (Throwable e) {
                /*异常通知方法*/
                log.error("异常通知方法>目标方法名{},异常为：{}", method.getName(), e);
            } finally {
                stringRedisTemplate.delete(methodName);
            }
            return result;
        }
        log.error("执行:{} 未获取锁,重试次数:{}", method.getName(), redisLock.retry());
        return null;
    }

    /**
     * 根据切入点获取执行的方法
     */
    private Method currentMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        //获取目标类的所有方法，找到当前要执行的方法
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        Method resultMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                resultMethod = method;
                break;
            }
        }
        return resultMethod;
    }
}
