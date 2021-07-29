package com.ljm.boot.apilimit.limit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/2 11:57
 * @description 基于java concurrent包下的Semaphore实现限流  实现接口同时只能获取令牌的人调用,等接口调用完成才会释放令牌
 **/
@Component
@Scope
@Aspect
public class SemaphoreLimitAspect {

    /**
     * 存储限流量和方法,必须是static且线程安全,保证所有线程进入都唯一
     */
    public static Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    /**
     * 业务层切点
     */
    @Pointcut("@annotation(com.ljm.boot.apilimit.limit.SemaphoreLimit)")
    public void ServiceAspect() {

    }

    @Around("ServiceAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {
        //获取目标对象
        Class<?> clz = joinPoint.getTarget().getClass();
        //获取增强方法信息
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        String limitKey = getLimitKey(clz, name);
        Semaphore semaphore = semaphoreMap.get(limitKey);
        //立即获取许可证,非阻塞
        boolean flag = semaphore.tryAcquire();
        Object obj = null;
        try {
            //拿到许可证则执行任务
            if (flag) {
                obj = joinPoint.proceed();
            } else {
                //拒绝了请求（服务降级）
                obj = "limitKey:"+limitKey+", The system is busy, please visit after a while";
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (flag) semaphore.release(); //拿到许可证后释放通行证
        }
        return obj;
    }

    /**
     * 获取拦截方法配置的限流key,没有返回null
     */
    private String getLimitKey(Class<?> clz, String methodName) {
        for (Method method : clz.getDeclaredMethods()) {
            //找出目标方法
            if (method.getName().equals(methodName)) {
                //判断是否是限流方法
                if (method.isAnnotationPresent(SemaphoreLimit.class)) {
                    return method.getAnnotation(SemaphoreLimit.class).limitKey();
                }
            }
        }
        return null;
    }
}
