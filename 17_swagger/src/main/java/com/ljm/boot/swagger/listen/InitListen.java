package com.ljm.boot.swagger.listen;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitListen implements CommandLineRunner {

    @Value("${server.port}")
    private Integer port;

    @Override
    public void run(String... args){
        //启动一个浏览器访问页面
        try {
            Runtime.getRuntime().exec("cmd   /c   start   http://localhost:"+port+"/swagger-ui.html");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
