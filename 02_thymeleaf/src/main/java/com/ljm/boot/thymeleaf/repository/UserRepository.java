package com.ljm.boot.thymeleaf.repository;

import com.ljm.boot.thymeleaf.model.User;
import com.ljm.boot.thymeleaf.util.RandomName;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepository {

    //模拟数据库存储
    private List<User> userList = new ArrayList<>();

    //初始化仓储数据,实际应该重数据库中获取
    {
        User user;
        for (int i = 1; i <= 20; i++) {
            user = new User(i, RandomName.randomName(true, 3), i % 2);
            userList.add(user);
        }
    }

    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(userList.get(id));
    }
    public List<User> findAll() {
        return userList;
    }

}
