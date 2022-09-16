package com.ljm.boot.distributedjob.scheduled;

import com.ljm.boot.distributedjob.annotation.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description 基于redis分布式锁
 * @Author Dominick Li
 **/
@Slf4j
@Configuration
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "redis")
@Component
public class RedisLockScheduled {

    /**
     * 每分钟执行一次任务,设置分布式锁的名称insertStatData,过期时间为30秒,重试次数为3次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    @RedisLock(name="insertStatData",expired = 30,retry = 3)
    public void insertStatData() {
        try {
            //模拟业务处理线程休眠10秒
            Thread.sleep(10000L);
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("-------------每分钟打印一次日志--------------");
    }

}
