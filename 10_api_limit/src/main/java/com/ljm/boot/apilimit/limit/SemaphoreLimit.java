package com.ljm.boot.apilimit.limit;

import java.lang.annotation.*;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/2 11:57
 * @description  Semaphore限流用的注解
 **/
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SemaphoreLimit {

    String limitKey() default ""; //限流的方法名

    int value()  default 3;  //发放的许可证数量

}
