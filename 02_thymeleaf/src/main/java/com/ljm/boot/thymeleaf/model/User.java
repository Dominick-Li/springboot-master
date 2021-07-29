package com.ljm.boot.thymeleaf.model;

import java.util.Date;

public class User {

    public User(){}

    public User(Integer userId,String name,Integer gender){
        this.userId=userId;
        this.name=name;
        this.gender=gender;
        this.createTime=new Date();
    }

    //用户ID
    private Integer userId;
    //用户姓名
    private String name;
    //性别
    private Integer gender;
    //创建时间
    private Date createTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGendenName(){
        return this.gender==0?"男":"女";
    }
}
