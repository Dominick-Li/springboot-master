package com.ljm.boot.apilimit.test;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/21 23:17
 * @description通过Semaphore控制并发数
 **/
public class SemaphoreWebTest {

    public static void main(String[] args) throws Exception {
        //设置线程池最大执行20个线程并发执行任务
        int threadSize=20;
        AtomicInteger successCount=new AtomicInteger(0);
        CountDownLatch downLatch=new CountDownLatch(20);
        ExecutorService fixedThreadPool= Executors.newFixedThreadPool(threadSize);
        for (int i = 0; i < threadSize; i++) {
            fixedThreadPool.submit(()->{
                RestTemplate restTemplate=new RestTemplate();
                String str= restTemplate.getForObject("http://localhost:8010/SemaphoreLimit",String.class);
                if("success".equals(str)){
                    successCount.incrementAndGet();
                }
                System.out.println(str);
                downLatch.countDown();
            });
        }
        downLatch.await();
        fixedThreadPool.shutdown();
        System.out.println("总共有"+successCount.get()+"个线程获得到了许可证!");
    }
}
