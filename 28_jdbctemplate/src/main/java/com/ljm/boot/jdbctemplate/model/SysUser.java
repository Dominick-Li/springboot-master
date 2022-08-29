package com.ljm.boot.jdbctemplate.model;

import java.io.Serializable;

/**
 * @Description
 * @Author Dominick Li
 * @CreateTime 2022/8/29 17:31
 **/
public class SysUser implements Serializable {

    public SysUser(){}

    public SysUser(Integer id, String username, String password, Integer age){
        this.id=id;
        this.username=username;
        this.password=password;
        this.age=age;
    }

    private Integer id;

    private String username;

    private String password;

    private Integer age;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "SysUsers{" +
                "id=" + id  +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
