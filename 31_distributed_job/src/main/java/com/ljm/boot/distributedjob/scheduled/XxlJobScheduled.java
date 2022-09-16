package com.ljm.boot.distributedjob.scheduled;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 基于 xxlJob实现分布式任务调度
 * @Author Dominick Li
 **/
@Slf4j
@Configuration
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "xxlJob")
public class XxlJobScheduled {
    @XxlJob("xxlJobTask")
    public ReturnT<String> xxlJobTask(String data) {
        XxlJobContext xxlJobContext = XxlJobContext.getXxlJobContext();
        String jobParam = xxlJobContext.getJobParam();
        try {
            //模拟业务处理线程休眠10秒
            Thread.sleep(10000L);
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("xxlJobTest定时任务执行成功,jobParam:{},data={}",jobParam,data);
        return ReturnT.SUCCESS;
    }
}
