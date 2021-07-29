package com.ljm.boot.jwttoken.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserDetailsFactory implements UserDetailsService {

    /**
     * 模拟数据库
     * 存放用户对应的角色
     */
    HashMap<String, String> userRole = new HashMap<>();

    {
        userRole.put("test", "USER");
        userRole.put("admin", "ADMIN");
    }

    /**
     * 模拟数据库
     * 存放角色对应的权限
     */
    HashMap<String, List<GrantedAuthority>> roleAuthorize = new HashMap<>();

    {
        List<GrantedAuthority> uList = new ArrayList<>();
        uList.add(new SimpleGrantedAuthority("select"));
        uList.add(new SimpleGrantedAuthority("put"));
        roleAuthorize.put("USER", uList);

        List<GrantedAuthority> aList =new ArrayList<>();
        aList.add(new SimpleGrantedAuthority("select"));
        aList.add(new SimpleGrantedAuthority("delete"));
        roleAuthorize.put("ADMIN", aList);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //加密过的密码  123456
        String password="$2a$10$JA61ycV.otx/d8cJSPAi8Ozbvij5QwDTFxp7jq9Qq3pm0xuZbW6ji";
        String roleName=userRole.get(username);
        List<GrantedAuthority> authorityList=roleAuthorize.get(roleName);
        //security资源注解是把角色和权限一起处理的,shiro是分开处理,需要手动在角色前面添加ROLE_
        roleName="ROLE_"+roleName;
        authorityList.add(new SimpleGrantedAuthority(roleName));
        return User.builder().username(username).password(password).authorities(authorityList).build();
    }
}
