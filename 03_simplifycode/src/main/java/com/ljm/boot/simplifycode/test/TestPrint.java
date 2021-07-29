package com.ljm.boot.simplifycode.test;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/16 3:00
 * @description 测试
 **/
public class TestPrint {
    public static void main(String[] args) {
        RestTemplate restTemplate=new RestTemplate();
        HashMap param=new HashMap();
        param.put("username","admin");
        param.put("password","123");
        String result=restTemplate.postForObject("http://localhost:8003/login",param,String.class);
        System.out.println(result);
    }
}
