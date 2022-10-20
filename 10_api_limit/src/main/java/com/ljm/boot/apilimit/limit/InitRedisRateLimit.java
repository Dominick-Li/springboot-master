package com.ljm.boot.apilimit.limit;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/2 12:35
 * @description 初始化需要SemaphoreLimit限流的许可证数量
 **/
@Component
public class InitRedisRateLimit implements ApplicationContextAware {

    @Autowired
    private RedissonClient client;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(RestController.class);
        beanMap.forEach((k, v) -> {
            Class<?> controllerClass = v.getClass();
            System.out.println(controllerClass.toString());
            System.out.println(controllerClass.getSuperclass().toString());
            //获取所有声明的方法
            Method[] allMethods = controllerClass.getSuperclass().getDeclaredMethods();
            RedisRateLimit redisRateLimit;
            RRateLimiter rRateLimiter;
            for (Method method : allMethods) {
                //判断方法是否使用了限流注解
                if (method.isAnnotationPresent(RedisRateLimit.class)) {
                    //获取配置的限流量,实际值可以动态获取,配置key,根据key从配置文件获取
                    redisRateLimit = method.getAnnotation(RedisRateLimit.class);
                    String key = redisRateLimit.limitKey();
                    if (key.equals("")) {
                        key = method.getName();
                    }
                    System.out.println("RedisRatelimitKey:" + key + ",许可证数是" + redisRateLimit.value());
                    //key作为key.value为具体限流量,传递到切面的map中
                    rRateLimiter = client.getRateLimiter(key);
                    //访问模式    访问数 访问速率  访问时间
                    //访问模式 RateType.PER_CLIENT=单实例共享     RateType.OVERALL=所有实例共享
                    rRateLimiter.trySetRate(RateType.OVERALL, redisRateLimit.value(), redisRateLimit.time(), RateIntervalUnit.SECONDS);
                    RedisRateLimitAspect.rateLimitMap.put(key, rRateLimiter);
                }
            }
        });
    }
}
