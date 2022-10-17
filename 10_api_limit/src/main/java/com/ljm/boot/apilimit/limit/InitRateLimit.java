package com.ljm.boot.apilimit.limit;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.BeansException;
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
public class InitRateLimit implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(RestController.class);
        beanMap.forEach((k, v) -> {
            Class<?> controllerClass = v.getClass();
            System.out.println(controllerClass.toString());
            System.out.println(controllerClass.getSuperclass().toString());
            //获取所有声明的方法
            Method[] allMethods = controllerClass.getSuperclass().getDeclaredMethods();
            for (Method method : allMethods) {
                //判断方法是否使用了限流注解
                if (method.isAnnotationPresent(RateLimit.class)) {
                    //获取配置的限流量,实际值可以动态获取,配置key,根据key从配置文件获取
                    double value = method.getAnnotation(RateLimit.class).value();
                    String key = method.getAnnotation(RateLimit.class).limitKey();
                    if(key.equals("")){
                        key=method.getName();
                    }
                    System.out.println("RatelimitKey:" +key+",许可证数是"+value);
                    //key作为key.value为具体限流量,传递到切面的map中
                    RateLimitAspect.rateLimitMap.put(key, RateLimiter.create(value));
                }
            }
        });
    }
}
