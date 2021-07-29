package com.ljm.boot.springdatajpa.repository;

import com.ljm.boot.springdatajpa.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRoleRepository extends JpaRepository<UserRole,UserRole.PK> {


}
