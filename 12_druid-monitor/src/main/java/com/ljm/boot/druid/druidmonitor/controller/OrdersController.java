package com.ljm.boot.druid.druidmonitor.controller;

import com.ljm.boot.druid.druidmonitor.model.Orders;
import com.ljm.boot.druid.druidmonitor.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
public class OrdersController {

    @Autowired
    OrdersRepository ordersRepository;

    @RequestMapping("/saveAll")
    public String saveAll(){
        Orders orders=null;
        List<Orders> ordersList=new ArrayList<>(100);
        for(int i=0;i<300;i++){
            orders=new Orders();
            //订单表的主键应该使用雪花算法之类生成,雪花算法生成的id是有序的，这样索引查询起来会快很多,uuid是无序的,查询效率极低。。。。
            orders.setId(UUID.randomUUID().toString());
            orders.setOrderName("订单_"+1);
            ordersList.add(orders);
        }
        ordersRepository.saveAll(ordersList);
        return "success";
    }

}
