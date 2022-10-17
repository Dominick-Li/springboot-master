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
public @interface RateLimit {

    String limitKey() default ""; //限流的方法名

    double value()  default 0d;  //发放的许可证数量
}
