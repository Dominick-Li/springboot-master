package com.ljm.boot.simplifycode.model;


import java.util.Date;

public class Role  extends BaseModel{

    public Role(){}

    public Role(String roleName, Date createDate){
        this.roleName=roleName;
        this.setCreateTime(createDate);
        System.out.println(this);
    }
    private String roleName;
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public static void main(String[] args) {
        new Role("admin",new Date());
    }

}
