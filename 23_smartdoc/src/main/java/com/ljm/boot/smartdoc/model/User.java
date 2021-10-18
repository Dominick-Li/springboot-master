package com.ljm.boot.smartdoc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Dominick Li
 * @CreateTime 2021/10/9 14:36
 * @description
 **/
public class User implements Serializable {

    /**
     *  用户id
     *  @ignore
     */
    @JsonIgnore
    private Integer id;

    /**
     * 用户名称
     * @required
     */
    private String username;

    /**
     * 密码
     * @required
     */
    private String password;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 创建时间
     * @ignore
     */
    private Date createDate;

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
}
