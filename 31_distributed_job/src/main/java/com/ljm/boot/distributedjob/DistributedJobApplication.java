package com.ljm.boot.distributedjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DistributedJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedJobApplication.class, args);
    }

}
