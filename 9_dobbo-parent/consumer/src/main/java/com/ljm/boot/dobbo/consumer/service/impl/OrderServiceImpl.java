package com.ljm.boot.dobbo.consumer.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.ljm.boot.dobbo.consumer.service.OrderService;
import com.ljm.boot.dobbo.pojo.User;
import com.ljm.boot.dobbo.provider.UserService;
import org.springframework.stereotype.Component;

/**
 * @Description RPC调用获取用户信息
 **/
@Service
@Component
public class OrderServiceImpl implements OrderService {

    @Reference
    UserService userService;

    @Override
    public User InitOrder(Integer userId) {
        return userService.getUserInfo(userId);
    }
}
