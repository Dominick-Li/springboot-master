package com.ljm.boot.apilimit.controller;

import com.ljm.boot.apilimit.limit.RateLimit;
import com.ljm.boot.apilimit.limit.RedisRateLimit;
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
    @RateLimit(limitKey = "ratelimitKey",value =3)
    @RequestMapping("/ratelimit")
    public String ratelimit() throws Exception{
        //假设业务处理了1秒
        TimeUnit.SECONDS.sleep(1);
        return "success";
    }

    /**
     * 设置limitKey=semaphoreKey,并且最多允许3个线程同时执行
     */
    @SemaphoreLimit(limitKey ="semaphoreKey", value =3)
    @RequestMapping("/semaphoreLimit")
    public String semaphoreLimit() throws Exception{
        //假设业务处理了1秒
        TimeUnit.SECONDS.sleep(1);
        return "success";
    }

    /**
     * 设置limitKey=redisRatelimit,并且每2秒许可证只有7个
     */
    @RedisRateLimit(limitKey = "redisRatelimit",value =7,time = 2)
    @RequestMapping("/redisRatelimit")
    public String redisRatelimit(){
        return "success";
    }
}
