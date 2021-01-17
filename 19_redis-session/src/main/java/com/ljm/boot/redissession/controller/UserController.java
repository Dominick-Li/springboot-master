package com.ljm.boot.redissession.controller;

import com.ljm.boot.redissession.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/login/{username}")
    public User login(HttpSession session, @PathVariable String username){
        User user=new User();
        user.setUsername(username);
        user.setLoginSessionId(session.getId());
        session.removeAttribute("user");
        session.setAttribute("user",user);
        redisTemplate.opsForValue().set("loginUser:"+username, session.getId());
        return user;
    }

    @GetMapping("/index")
    public User index(HttpSession session){
        User user=(User) session.getAttribute("user");
        return user;
    }
}
