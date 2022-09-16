package com.ljm.boot.distributedjob.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author Dominick Li
 **/
@Slf4j
@Configuration
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "xxlJob")
public class XxlJobConfig {

    @Value("${xxlJob.accessToken}")
    private String accessToken;

    @Value("${xxlJob.adminAddresses}")
    private String adminAddresses;

    @Value("${xxlJob.executor.appname}")
    private String appName;

    @Value("${xxlJob.executor.ip}")
    private String ip;

    @Value("${xxlJob.executor.port}")
    private int port;

    @Value("${xxlJob.executor.logpath}")
    private String logPath;

    @Value("${xxlJob.executor.logretentiondays}")
    private int logRetentionDays;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info("*****************xxlJobExecutor bean init*****************");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appName);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        return xxlJobSpringExecutor;
    }
}
