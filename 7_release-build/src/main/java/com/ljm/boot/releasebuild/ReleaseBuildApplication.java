package com.ljm.boot.releasebuild;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReleaseBuildApplication {

    private static final Logger logger=LoggerFactory.getLogger(ReleaseBuildApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(ReleaseBuildApplication.class, args);
        logger.info("test info level");
        logger.error("test error level");
    }

}
