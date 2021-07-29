package com.ljm.boot.springdatajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringDatajpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDatajpaApplication.class, args);
    }

}
