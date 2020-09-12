package com.ljm.boot.nettywebsocket.repository;


import java.util.List;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/8 21:51
 * @description
 **/

public interface UserGroupRepository {
    List<Integer> findGroupIdByUserId(Integer userId);
}
