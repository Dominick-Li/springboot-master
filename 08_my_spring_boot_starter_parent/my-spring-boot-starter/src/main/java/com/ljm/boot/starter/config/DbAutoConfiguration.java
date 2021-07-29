package com.ljm.boot.starter.config;

import com.ljm.boot.starter.db.DbServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DbConfig.class)
public class DbAutoConfiguration {
    Logger logger= LoggerFactory.getLogger(this.getClass());
    @Bean
    public DbServer getBean(DbConfig dbConfig) {
        //创建组件实例
        logger.info("开始注册我的Bean对象");
        DbServer dbServer = new DbServer();
        dbServer.setUsername(dbConfig.getUsername());
        dbServer.setPassword(dbConfig.getPassword());
        return dbServer;
    }
}


