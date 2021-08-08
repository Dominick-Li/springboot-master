package com.ljm.boot.rabbitmq.repository;


import com.ljm.boot.rabbitmq.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
}
