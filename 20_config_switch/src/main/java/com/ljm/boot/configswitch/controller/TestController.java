package com.ljm.boot.configswitch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        System.out.println("test接口被调用");
        return "test";
    }
}
