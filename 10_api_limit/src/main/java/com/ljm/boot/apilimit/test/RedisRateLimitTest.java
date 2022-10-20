package com.ljm.boot.apilimit.test;

import org.redisson.Redisson;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description
 * @Author Dominick Li
 * @CreateTime 2022/10/20 12:14
 **/
public class RedisRateLimitTest {
    public static void main(String[] args) throws Exception {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient client = Redisson.create(config);
        RRateLimiter rateLimiter = client.getRateLimiter("rate_limiter1");
        Integer threadSize=20;
        //访问模式    访问数 访问速率  访问时间
        //访问模式 RateType.PER_CLIENT=单实例共享     RateType.OVERALL=所有实例共享
        rateLimiter.trySetRate(RateType.OVERALL, 10, 2, RateIntervalUnit.SECONDS);
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        CountDownLatch downLatch = new CountDownLatch(threadSize);
        AtomicInteger successCount = new AtomicInteger(0);
        for (int i = 0; i < threadSize; i++) {
            executorService.submit(() -> {
                try {
                    if(rateLimiter.tryAcquire()){
                        successCount.incrementAndGet();
                        System.out.println("处理任务的线程是" + Thread.currentThread().getId() + ",当前时间是" + System.currentTimeMillis());
                    }
                    downLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        downLatch.await();
        System.out.println(successCount.get());
    }
}
