package com.ljm.boot.lowcode.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;

/**
 * @author Dominick Li
 * @description 自定义认证用户详细信息
 **/
public class MyUserDetail implements org.springframework.security.core.userdetails.UserDetails {


    private String username;
    private java.util.Collection<SimpleGrantedAuthority> authorities;

    public MyUserDetail(Claims claims) {
        this.username = claims.getSubject();
        SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority("ROLE_"+claims.get("role", String.class));
        authorities=new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
    }


    /**
     * 获取权限
     */

    @Override
    public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 帐号是否未过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 帐号是否未锁定
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 凭证未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用
     */
    @Override
    public boolean isEnabled() {
        return true;
    }


}
