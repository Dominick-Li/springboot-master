package com.ljm.boot.apilimit.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/21 23:17
 * @description通过Semaphore控制并发数
 **/
public class SemaphoreTest {

    private final static Semaphore permit = new Semaphore(5);

    public static void main(String[] args) {
        //设置线程池最大执行20个线程并发执行任务
        int threadSize = 20;
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadSize);
        for (int i = 0; i < threadSize; i++) {
            fixedThreadPool.submit(() -> {
                try {
                    //获取令牌
                    permit.acquire();
                    Thread.sleep(1L);
                    //业务逻辑处理
                    System.out.println("处理任务的线程是" + Thread.currentThread().getId() + ",当前时间是" + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //释放令牌
                    permit.release();
                }
            });
        }

    }
}
