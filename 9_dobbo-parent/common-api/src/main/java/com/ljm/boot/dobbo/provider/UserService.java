package com.ljm.boot.dobbo.provider;

import com.ljm.boot.dobbo.pojo.User;


/**
 * @Description   服务提供者接口
 **/
public interface UserService {

    User getUserInfo(Integer userId);
}
