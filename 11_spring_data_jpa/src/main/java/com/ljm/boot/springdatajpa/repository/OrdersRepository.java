package com.ljm.boot.springdatajpa.repository;


import com.ljm.boot.springdatajpa.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,String> {
}
