package com.ljm.boot.shiro.model;


import java.io.Serializable;
import java.util.Set;

public class User implements Serializable {

    private Integer id;

    private String username;

    private String password;

    /**
     * 密码加密用的盐
     */
    private String salt;

    /**
     * 用户拥有的角色
     */
    private Set<Role> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
