package com.ljm.boot.redisson.test;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class TestRepeatSubmit {

    public static void main(String[] args) throws Exception {
        ///设置线程池最大执行20个线程并发执行任务
        int threadSize = 20;
        //AtomicInteger通过CAS操作能保证统计数量的原子性
        AtomicInteger successCount = new AtomicInteger(0);
        CountDownLatch downLatch = new CountDownLatch(20);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadSize);
        for (int i = 0; i < threadSize; i++) {
            int finalI = i;
            fixedThreadPool.submit(() -> {
                RestTemplate restTemplate = new RestTemplate();
                String str = restTemplate.getForObject("http://localhost:8032/test/"+ finalI, String.class);
                //String str = restTemplate.getForObject("http://localhost:8032/test/1", String.class);
                if ("success".equals(str)) {
                    successCount.incrementAndGet();
                }
                System.out.println(str);
                downLatch.countDown();
            });
            //模拟网络传输时间
            Thread.sleep(100);
        }
        //等待所有线程都执行完任务
        downLatch.await();
        fixedThreadPool.shutdown();
        System.out.println("总共有" + successCount.get() + "个线程请求成功!");

    }

}
