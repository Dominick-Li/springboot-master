package com.ljm.boot.dobbo.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.ljm.boot.dobbo.pojo.User;
import com.ljm.boot.dobbo.provider.UserService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @Description 服务提供者的功能实现逻辑
 **/
@Service//注解不是 springframework包的,是dubbo包下的
@Component
public class UserServiceImpl implements UserService {
    private  ArrayList<User> list=new ArrayList<>();

    /**
     * 制造假数据,实际环境应该重数据库获取
     */
    {
        User user=new User();
        user.setUserName("张三");
        user.setAge("18");
        user.setAddress("北京大兴");
        User user1=new User();
        user1.setUserName("李四");
        user1.setAge("19");
        user1.setAddress("北京大兴");
        User user2=new User();
        user2.setUserName("王五");
        user2.setAge("20");
        user2.setAddress("北京大兴");
        list.add(user);
        list.add(user1);
        list.add(user2);
    }
    @Override
    public User getUserInfo(Integer userId) {
        return list.get(userId);
    }
}
