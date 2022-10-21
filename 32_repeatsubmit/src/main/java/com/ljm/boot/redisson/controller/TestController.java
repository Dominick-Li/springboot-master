package com.ljm.boot.redisson.controller;

import com.ljm.boot.redisson.annotation.RepeatSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author Dominick Li
 * @CreateTime 2022/10/20 10:55
 **/
@RestController
@RequestMapping
public class TestController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/test/{id}")
    @RepeatSubmit(interval = 2,timeUnit = TimeUnit.SECONDS)
    public String test(@PathVariable Integer id){
        return "success";
    }
}
