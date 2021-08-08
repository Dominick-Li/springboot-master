package com.ljm.boot.rabbitmq.service;

import com.ljm.boot.rabbitmq.model.Orders;
import com.ljm.boot.rabbitmq.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class OrdersService {

    @Autowired
    OrdersRepository ordersRepository;

    @Transactional
    public Orders save(Orders orders) {
        Orders save = ordersRepository.save(orders);
        if (true) {
            //测试抛出RuntimeException让mysql事务回滚
            System.out.println(1 / 0);
        }
        return save;
    }

    public Optional<Orders> findById(Long id) {
        return ordersRepository.findById(id);
    }

}
