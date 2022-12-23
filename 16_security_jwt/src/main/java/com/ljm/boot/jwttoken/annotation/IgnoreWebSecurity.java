package com.ljm.boot.jwttoken.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreWebSecurity {

    /**
     * 忽略接口的名称,主要用于url动态参数的请求  例如: /user/{id}  ,像这种需要配置注解内容为 /user/*
     */
    String value() default "";

}
