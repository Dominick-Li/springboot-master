package com.ljm.boot.springdatajpa.repository;


import com.ljm.boot.springdatajpa.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
}
