package com.ljm.boot.swagger.controller;


import com.ljm.boot.swagger.model.HttpResult;
import com.ljm.boot.swagger.model.PageParam;
import com.ljm.boot.swagger.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@Api(tags = "用户管理相关接口")
@RequestMapping("/user")
public class UserController {

    @GetMapping("/")
    @ApiOperation("分页查询所有用户")
    public HttpResult<ArrayList<User>> getUserById(@RequestBody PageParam pageParam) {
        return HttpResult.successResult(new ArrayList<User>());
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询用户的接口")
    @ApiImplicitParam(name = "id", value = "用户id", required = true)
    public HttpResult<User> getUserById(@PathVariable Integer id) {
       return HttpResult.successResult(new User());
    }


    @PostMapping("/")
    @ApiOperation("添加用户的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", defaultValue = "李四"),
            @ApiImplicitParam(name = "address", value = "用户地址", defaultValue = "北京", required = true)
    })
    public HttpResult addUser(String username, @RequestParam(required = true) String address) {
        return HttpResult.successResult();
    }

    @PutMapping("/{id}")
    @ApiOperation("根据id更新用户的接口")
    public HttpResult updateUserById(@RequestBody User user) {
        return HttpResult.successResult();
    }

}
