package com.ljm.boot.shiro.controller;

import com.ljm.boot.shiro.model.vo.ResponseResult;
import com.ljm.boot.shiro.model.User;
import com.ljm.boot.shiro.repository.UserRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/15 11:39
 * @description release
 **/
@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @PostMapping("/login.do")
    @ResponseBody
    public ResponseResult login(User user, HttpSession session) {
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            UsernamePasswordToken upt = new UsernamePasswordToken(user.getUsername(), user.getPassword());
            SecurityUtils.getSubject().login(upt);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new ResponseResult(false, "账号或密码错误！");
        } catch (AuthorizationException e) {
            e.printStackTrace();
            return new ResponseResult(false, "没有权限!");
        }
        return new ResponseResult(true, "登录成功!");
    }

    //必须拥有admin角色
    // @RequiresRoles( value = "admin")

    /**
     * value可以是多个,多个写在{}里
     * Logical.OR admin或user角色都能访问
     * logical默认是 AND ,如果多项的话,必须都满足所有条件才能访问
     */
    @RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
    @GetMapping("/user/index")
    public String user(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("userList", users);
        return "user/index";
    }

    /**
     * 需要管理员角色
     */
    @RequiresRoles(value = "admin")
    @GetMapping("/admin/index")
    public String adminList() {
        return "admin/index";
    }

    /**
     * 拥有save权限能访问
     */
    @RequiresPermissions(value = "save")
    @GetMapping("/user/save")
    public String save() {
        return "user/save";
    }

    @RequiresPermissions(value = {"delete", "*"}, logical = Logical.OR)
    @GetMapping("/user/delete/{username}")
    public String delete(@PathVariable String username) {
        userRepository.deleteByUserName(username);
        return "forward:/user/index";
    }

}
