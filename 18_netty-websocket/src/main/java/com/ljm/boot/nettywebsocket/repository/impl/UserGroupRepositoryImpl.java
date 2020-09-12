package com.ljm.boot.nettywebsocket.repository.impl;

import com.ljm.boot.nettywebsocket.repository.UserGroupRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/8 21:54
 * @description
 **/
@Component
public class UserGroupRepositoryImpl implements UserGroupRepository {
    /**
     * 组装假数据,真实环境应该重数据库获取
     */
    HashMap<Integer, List<Integer>> userGroup = new HashMap<>(4);

    {
        List<Integer> list = Arrays.asList(1, 2);
        userGroup.put(1, list);
        userGroup.put(2, list);
        userGroup.put(3, list);
        userGroup.put(4, list);
    }

    @Override
    public List<Integer> findGroupIdByUserId(Integer userId) {
        return this.userGroup.get(userId);
    }
}
