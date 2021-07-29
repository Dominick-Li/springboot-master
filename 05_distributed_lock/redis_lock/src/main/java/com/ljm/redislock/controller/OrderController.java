package com.ljm.redislock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author Dominick Li
 * @date 2020/2/20 7:03 AM
 */
@RestController
public class OrderController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 设置初始库存数量
     */
    @RequestMapping("/set_stock")
    public String set_stock(){
        stringRedisTemplate.opsForValue().set("stock", "100");
        return "ok";
    }

    @RequestMapping("/deduct_stock/{productId}")
    public String deductStock(@PathVariable String productId) {
        String lockKey = "product_" + productId;
        try {
            //利用redis单线程模型去写值,写入成功即获取锁,设置30秒后失效,避免程序出现宕机情况
            boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "lock", 30, TimeUnit.SECONDS);
            if (!result) {
                //尝试再去获取3次锁,如果不需要尝试获取锁可以注释了下面这段,直接返回失败
                result = deductStockCAS(lockKey, 3);
                if (!result) {
                    return "error";
                }
            }
            Integer stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                stock -= 1;
                stringRedisTemplate.opsForValue().set("stock", stock.toString());
                System.out.println("库存扣减成功,剩余库存:" + stock);
                return "success";
            }
            System.out.println("库存不足,扣减失败!");
            return "error";
        } finally {
            //释放锁
            stringRedisTemplate.delete(lockKey);
        }
    }

    /**
     * 设置要获取的key和尝试的次数
     * 没有获取到锁,通过CAS自旋
     */
    public boolean deductStockCAS(String lockKey, Integer count) {
        try {
            int i = 0;
            do {
                Thread.sleep(1000L);
                i++;
                if (i == count + 1) {//自旋结束
                    return false;
                }
            } while (!stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "lock", 30, TimeUnit.SECONDS));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
