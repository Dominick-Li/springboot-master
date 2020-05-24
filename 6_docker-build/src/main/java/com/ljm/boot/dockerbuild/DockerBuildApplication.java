package com.ljm.boot.dockerbuild;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DockerBuildApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerBuildApplication.class, args);
    }

    @RestController
    public class  WebController{

        @Value("${username}")
        private String username;

        @Value("${password}")
        private String password;

        @RequestMapping("/print")
        public String printUserInfo(){
            return "User{username="+username+",password="+password+"}";
        }

    }
}
