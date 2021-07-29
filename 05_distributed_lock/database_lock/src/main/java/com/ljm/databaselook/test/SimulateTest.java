package com.ljm.databaselook.test;

import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulateTest {

    //java原子包,利用了CAS算法保证了数据的原子性,
    static  AtomicInteger successCount=new AtomicInteger(0);
    //volatile修饰的变量能保证修改操作的原子性,但是在 ++操作中设计到了2个指令操作 count=count+1,所以在++操作中volatile原子性可能会失效
    //static volatile  Integer successCount=0;

    public static void main(String[] args) throws Exception{
        //总共的线程数
        int threadSize=100;
        //每秒并发数
        final  int count=20;
        //同步执行器,必须等所有线程都完成任务,才能执行后面的代码
        CountDownLatch downLatch=new CountDownLatch(threadSize);
        ExecutorService fixedThreadPool=Executors.newFixedThreadPool(threadSize);
        for(int i=0;i<threadSize;i++) {
            int finalI = i;
            fixedThreadPool.submit(() -> {
                //没秒执行20个请求,执行5秒
                if(finalI%count==0){
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                RestTemplate restTemplate = new RestTemplate();
                String result = restTemplate.getForObject("http://localhost/deduct_stock", String.class);
                if("success".equals(result)){
                    successCount.incrementAndGet();
                }
                downLatch.countDown();
            });
        }
        downLatch.await();
        System.out.println("购买商品成功的次数:"+successCount.get());
        fixedThreadPool.shutdown();
    }
}
