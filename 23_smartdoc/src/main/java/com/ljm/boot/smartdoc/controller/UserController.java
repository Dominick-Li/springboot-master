package com.ljm.boot.smartdoc.controller;

import com.ljm.boot.smartdoc.model.User;
import com.ljm.boot.smartdoc.model.JsonResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 用户管理
 * @author Dominick Li
 * @CreateTime 2021/10/9 11:46
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 获取所有用户信息
     */
    @GetMapping("/")
    public JsonResult<List<User>> findAll() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        return JsonResult.successResult(Arrays.asList(user));
    }

    /**
     * 根据用户id获取用户信息
     * @param id 用户Id
     */
    @GetMapping("/{id}")
    public JsonResult<User> findById(@PathVariable String id) {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        return JsonResult.successResult(user);
    }


    /**
     * 登录 入参方式: @RequestParam
     * @param username 用户名
     * @param password 密码
     */
    @PostMapping("/login")
    public JsonResult login(@RequestParam("username") String username, @RequestParam("password") String password) {
        return JsonResult.successResult();
    }

    /**
     * 添加用户 入参方式: @RequestBody
     */
    @PostMapping("/")
    public JsonResult save(@RequestBody User user) {
        return JsonResult.successResult();
    }

    /**
     * 删除用户
     * @param id 用户Id
     */
    @DeleteMapping("/{id}")
    public JsonResult deleteById(@PathVariable String id) {
        return JsonResult.successResult();
    }
}
