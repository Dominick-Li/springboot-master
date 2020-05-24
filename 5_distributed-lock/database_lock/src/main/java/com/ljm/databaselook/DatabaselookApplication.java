package com.ljm.databaselook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DatabaselookApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaselookApplication.class, args);
    }

}
