package com.ljm.boot.jwttoken.controller;

import com.ljm.boot.jwttoken.annotation.IgnoreWebSecurity;
import com.ljm.boot.jwttoken.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author Dominick Li
 * @CreateTime 2020/3/29 12:13
 * @description 测试token
 **/
@RestController
public class TokenController {


    @Autowired
    private UserService userService;


    /**
     * 登录接口
     */
    @IgnoreWebSecurity
    @PostMapping("/sso/login")
    public Map login(@RequestParam("username") String username,
                     @RequestParam("password") String password) {
        // 省略数据源校验
        return userService.login(username, password);
    }

    /**
     * 需要角色user才能访问的地址
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/index")
    public String info() {
        return "index";
    }

    /**
     * 需要角色admin才能访问的地址
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "admin";
    }


    /**
     * 需要权限delete 才能删除用户信息
     */
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasAuthority('delete')")
    public String deleteUser(@PathVariable Integer id) {
        System.out.println("删除用户:id=" + id);
        return "user";
    }


    /**
     * 需要权限select 才能查看用户信息
     */
    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('select')")
    public String user(@PathVariable Integer id) {
        System.out.println("查询用户:id=" + id);
        return "user";
    }

    /**
     * 需要权限put 才能修改用户信息
     */
    @PutMapping("/user")
    @PreAuthorize("hasAuthority('put')")
    public String putUser() {
        System.out.println("修改用户:id");
        return "user";
    }
}
