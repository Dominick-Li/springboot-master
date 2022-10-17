package com.ljm.boot.apilimit.controller;

import com.ljm.boot.apilimit.limit.RateLimit;
import com.ljm.boot.apilimit.limit.SemaphoreLimit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/2 10:41
 * @description
 **/
@RestController
public class LimitTestController {

    /**
     * 设置limitKey=ratelimitKey,并且每秒许可证只有3个
     */
    @RateLimit(value =3)
    @RequestMapping("/ratelimit")
    public String ratelimit() throws Exception{
        //假设业务处理了1秒
        TimeUnit.SECONDS.sleep(1);
        return "success";
    }

    /**
     * 设置limitKey=SemaphoreKey,并且最多允许3个线程同时执行
     */
    @SemaphoreLimit(limitKey ="SemaphoreKey", value =3)
    @RequestMapping("/SemaphoreLimit")
    public String SemaphoreLimit() throws Exception{
        //假设业务处理了1秒
        TimeUnit.SECONDS.sleep(1);
        return "success";
    }
}
