package com.ljm.boot.apilimit.limit;

import java.lang.annotation.*;

/**
 * @Description RateLimit限流注解
 * @Author lijunming
 * @Date 2019/11/1 19:00
 **/
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisRateLimit {

    String limitKey() default ""; //限流的方法名

    int time() default  1; //默认设置为1秒

    int value()  default 3;  //发放的许可证数量
}
