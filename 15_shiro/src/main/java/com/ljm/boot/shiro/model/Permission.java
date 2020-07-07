package com.ljm.boot.shiro.model;

public class Permission {

    private Integer  id;

    private String  permissionName;

    public Permission(String permissionName){
        this.permissionName=permissionName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
