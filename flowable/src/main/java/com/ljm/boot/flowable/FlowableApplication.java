package com.ljm.boot.flowable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlowableApplication {

    /**
     * springboot整合  Flowable Ui
     * 访问 http://localhost:8033/flow-ui/
     */
    public static void main(String[] args) {
        SpringApplication.run(FlowableApplication.class, args);
    }

}
