package com.ljm.boot.shardingsphere.repository;

import com.ljm.boot.shardingsphere.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

}
