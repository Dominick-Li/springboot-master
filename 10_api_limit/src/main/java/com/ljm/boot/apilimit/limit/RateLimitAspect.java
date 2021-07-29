package com.ljm.boot.apilimit.limit;

import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/2 11:57
 * @description  基于guava包下的RateLimimiter限流  实现每秒令牌数量控制
 **/
@Component
@Scope
@Aspect
public class RateLimitAspect {
    //每秒只发出3个令牌,内部采用令牌捅算法实现
    private static RateLimiter rateLimiter = RateLimiter.create(3.0);

    /**
     * 业务层切点
     */
    @Pointcut("@annotation(com.ljm.boot.apilimit.limit.RateLimit)")
    public void ServiceAspect() { }

    @Around("ServiceAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object obj = null;
        try {
            //tryAcquire()是非阻塞, rateLimiter.acquire()是阻塞的
            if (rateLimiter.tryAcquire()) {
                obj = joinPoint.proceed();
            } else {
                //拒绝了请求（服务降级）
                obj = "The system is busy, please visit after a while";
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;
    }
}
