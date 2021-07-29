package com.ljm.boot.simplifycode.controller;

import com.ljm.boot.simplifycode.model.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/16 3:08
 * @description
 **/
@RestController
public class TestController {
    @RequestMapping("/login")
    public String  login(@RequestBody User user){
        if(user.getUsername().equals("admin") && user.getPassword().equals("123")){
            return "登录成功";
        }
        return "登录失败";
    }
}
