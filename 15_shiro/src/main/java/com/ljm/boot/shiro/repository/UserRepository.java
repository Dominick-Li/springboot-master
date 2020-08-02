package com.ljm.boot.shiro.repository;

import com.ljm.boot.shiro.model.Permission;
import com.ljm.boot.shiro.model.Role;
import com.ljm.boot.shiro.model.User;
import com.ljm.boot.shiro.util.PasswordHelper;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author Dominick Li
 * @description 初始化用户数据, 模拟数据库
 **/
@Component
public class UserRepository {

    HashMap<String, User> users = new HashMap();

    {
        //初始化权限
        Set<Permission> permissions = new HashSet<>();
        permissions.add(new Permission("save"));
        permissions.add(new Permission("delete"));
        permissions.add(new Permission("select"));
        Set<Role> roles = new HashSet<>();
        //初始化角色
        roles.add(new Role("admin", permissions));
        //初始化管理员
        User user = new User();
        user.setUsername("admin");
        String userPassword = "123";
        //盐前面必须拼接用户名,不然密码认证的时候会失败 (盐需要存在数据库)
        String salt = user.getUsername() + PasswordHelper.randomSalt();
        user.setPassword(PasswordHelper.encryptPassword(user.getUsername(), userPassword, salt));
        user.setSalt(salt);
        user.setRoles(roles);

        //初始化普通用户信息
        Set<Permission> permissions2 = new HashSet<>();
        permissions.add(new Permission("select"));
        Set<Role> roles2 = new HashSet<>();
        roles2.add(new Role("user", permissions2));
        User user2 = new User();
        user2.setUsername("test");
        String user2Password = "123";
        String salt2 = user2.getUsername() + PasswordHelper.randomSalt();
        user2.setPassword(PasswordHelper.encryptPassword(user2.getUsername(), user2Password, salt2));
        user2.setSalt(salt2);
        user2.setRoles(roles2);

        users.put("admin", user);
        users.put("test", user2);
    }

    /**
     * 模拟重数据库获取用户信息
     */
    public User findByUserName(String username) {
        return users.get(username);
    }

    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        for (User user : users.values()) {
            userList.add(user);
        }
        return userList;
    }

    public void deleteByUserName(String username) {
        users.remove(username);
    }

}
