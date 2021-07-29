package com.ljm.boot.configswitch;

import com.ljm.boot.configswitch.enable.EnableLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableLog
public class ConfigSwitchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigSwitchApplication.class, args);
    }

}
