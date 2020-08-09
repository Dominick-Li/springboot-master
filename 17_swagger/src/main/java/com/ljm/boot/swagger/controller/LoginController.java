package com.ljm.boot.swagger.controller;


import com.ljm.boot.swagger.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = "登录接口")
public class LoginController {


    @GetMapping("/login")
    @ApiOperation("登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名"),
            @ApiImplicitParam(name = "password", value = "密码", required = true)
    })
    public HttpResult<User> getUserById(String username, String password)
    {
     return HttpResult.successResult(new User());
    }

}
