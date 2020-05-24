package com.ljm.boot.helloword.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @RestController 等于springmvc中 @Controller和@ResponseBody组合使用的注解
 */
@RestController
public class HelloWordController {

    /**
     * @GetMapping注解等值于servlet里的doGet方法,只处理get请求
     * @GetMapping 等值于 @RequestMapping(value = "/hello",method = RequestMethod.GET)
     */
    @GetMapping("/hello")
    public String helloword(){
        return "Hello springboot  get method";
    }

    /**
     * @PostMapping注解等值于servlet里的doPost方法,只处理post请求
     * @PostMapping 等值于 @RequestMapping(value = "/hello",method = RequestMethod.POST)
     */
    @PostMapping("/hello")
    public String helloword_post(){
        return "Hello springboot  post method";
    }
}
