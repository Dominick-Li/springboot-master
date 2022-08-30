package com.ljm.boot.lowcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
public class LowcodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LowcodeApplication.class, args);
    }

}
