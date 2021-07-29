package com.ljm.boot.druid.druidmonitor.repository;


import com.ljm.boot.druid.druidmonitor.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,String> {
}
