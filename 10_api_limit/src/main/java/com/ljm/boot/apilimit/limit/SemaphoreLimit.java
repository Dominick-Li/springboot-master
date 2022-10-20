package com.ljm.boot.apilimit.limit;

import java.lang.annotation.*;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/2 11:57
 * @description  Semaphore�����õ�ע��
 **/
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SemaphoreLimit {

    String limitKey() default ""; //�����ķ�����

    int value()  default 3;  //���ŵ����֤����

}
