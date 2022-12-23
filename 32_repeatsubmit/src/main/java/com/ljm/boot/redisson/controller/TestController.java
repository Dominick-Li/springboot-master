package com.ljm.boot.redisson.controller;

import com.ljm.boot.redisson.annotation.RepeatSubmit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author Dominick Li
 * @CreateTime 2022/10/20 10:55
 **/
@RestController
public class TestController {

    /**
     * 测试 间隔时间2秒
     */
    @GetMapping("/test/{id}")
    @RepeatSubmit(interval = 2)
    public String test(@PathVariable Integer id) throws Exception {
        //模拟业务处理休眠1秒
        Thread.sleep(1000L);
        return "success";
    }

    /**
     * 测试 间隔时间1500毫秒
     */
    @GetMapping("/test")
    @RepeatSubmit(interval = 1500, timeUnit = TimeUnit.MILLISECONDS)
    public String test2(@PathVariable Integer id) throws Exception {
        //模拟业务处理休眠1秒
        Thread.sleep(1000L);
        return "success";
    }
}
