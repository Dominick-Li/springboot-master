package com.ljm.boot.redisson.test;

import org.springframework.web.client.RestTemplate;


public class TestRepeatSubmit {

    public static void main(String[] args) throws Exception {
        ///设置线程池最大执行5个线程并发执行任务
        int threadSize = 5;
        Integer successSize = 0;
        for (int i = 0; i < threadSize; i++) {
            RestTemplate restTemplate = new RestTemplate();
            //String str = restTemplate.getForObject("http://localhost:8032/test/"+i, String.class);
            String str = restTemplate.getForObject("http://localhost:8032/test/1", String.class);
            if ("success".equals(str)) {
                successSize++;
            }
            Thread.sleep(1000L);
            System.out.println(str);
        }
        System.out.println("总共有" + successSize + "个线程请求成功!");
    }

}
