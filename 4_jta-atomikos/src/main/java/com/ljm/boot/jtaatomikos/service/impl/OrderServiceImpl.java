package com.ljm.boot.jtaatomikos.service.impl;

import com.ljm.boot.jtaatomikos.mapper.db1.OrderMapper;
import com.ljm.boot.jtaatomikos.mapper.db2.OrderDetailMapper;
import com.ljm.boot.jtaatomikos.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional
    public String insertDbaAndDbB(boolean flag) {
        //实际生产环境订单id应该通关雪花算法生成(生产的id是数字类型,b+树索引查询效率会很高),uuid没有规律,会严重影响索引查找速度
        String orderId = UUID.randomUUID().toString();
        //插入订单
        orderMapper.insert(orderId, "重入门到放弃");
        //模拟报错,事务会回滚
        if(flag){
            //flag=true的时候,触发2异常,事务回滚,为false的话,两个数据库都能正确插入数据
            System.out.println(1 / 0);
        }
        //插入订单明细
        orderDetailMapper.insert(1, orderId);
        return "success";
    }
}
