package com.ljm.boot.dobbo.consumer.controller;

import com.ljm.boot.dobbo.consumer.service.OrderService;
import com.ljm.boot.dobbo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class WebController {

    @Autowired
    OrderService orderService;

    @GetMapping("/initOrder/{userId}")
    public User initOrder(@PathVariable Integer userId){
        return orderService.InitOrder(userId);
    }
}
