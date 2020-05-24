package com.ljm.boot.testmystarter.controller;

import com.ljm.boot.starter.db.DbServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @Autowired
    DbServer dbServer;

    @RequestMapping("/test")
    public String test(){
        return dbServer.toString();
    }
}
