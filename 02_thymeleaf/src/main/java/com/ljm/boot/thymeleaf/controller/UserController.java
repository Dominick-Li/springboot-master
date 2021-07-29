package com.ljm.boot.thymeleaf.controller;

import com.ljm.boot.thymeleaf.model.User;
import com.ljm.boot.thymeleaf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = {"/","/index"})
    public ModelAndView index(HttpSession session){
        User user=new User(0,"admin",0);
        //往session中把当前用户信息放进去
        session.setAttribute("user",user);
        ModelAndView modelAndView=new ModelAndView("index");
        List<User> userList=userRepository.findAll();
        modelAndView.addObject(userList);
        modelAndView.addObject("id",0);
        HashMap map=new HashMap();
        map.put("totalPage",5);
        map.put("totalRecord",50);
        modelAndView.addObject("page",map);
        return modelAndView;
    }

    @GetMapping("index2")
    public String index2(){
        return "index2";
    }
}
