package com.ljm.boot.shardingsphere;

import com.ljm.boot.shardingsphere.model.User;
import com.ljm.boot.shardingsphere.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class ShardingsphereApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @Test
    void contextLoads() {
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setAge(i);
            user.setName("张三" + i);
            userRepository.save(user);
        }
        List<User> userList=userRepository.findAll();
        System.out.println("用户的数量="+userList.size());
    }
}
