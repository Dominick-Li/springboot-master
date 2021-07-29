package com.ljm.zookeeper.controller;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class OrderController {


    @Autowired
    private CuratorFramework zkClient;
    String lookPath = "/look/test";

    AtomicInteger atomicInteger = new AtomicInteger(5);//设置库存

    /**
     * 只有等锁释放了,别的线程才能获取新的锁
     *
     * @return
     */
    @RequestMapping("/deduct_stock")
    public String deduct_stock() {
        try {
            InterProcessMutex lock = new InterProcessMutex(zkClient, lookPath);
            //acquire设置等待时间,下面设置的尝试获取锁的时间，不设置参数默认无限等待
            if (lock.acquire(10, TimeUnit.SECONDS)) {
                try {
                    if (atomicInteger.get() > 0) {
                        atomicInteger.set(atomicInteger.get() - 1);
                        System.out.println("购买成功,剩余库存为:" + atomicInteger.get());
                        return "success";
                    }
                    System.out.println("库存不足:" + atomicInteger.get());
                } finally {
                    //释放锁
                    lock.release();
                }
            }
            return "error";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
    }
}
