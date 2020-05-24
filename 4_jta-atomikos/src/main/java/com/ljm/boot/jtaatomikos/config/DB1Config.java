package com.ljm.boot.jtaatomikos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource.db1")
@Data
public class DB1Config {
    private  String username;
    private String password;
    private String jdbcUrl;

}
