package com.ljm.boot.lowcode.enums;

public enum RoleTypeEnum {

    USER(1, "user"),
    MANAGER(2, "manager"),
    ADMIN(3, "admin");

    private Integer code;
    private String name;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    RoleTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static RoleTypeEnum nameOf(String name) {
        for (RoleTypeEnum roleTypeEnum : values()) {
            if (roleTypeEnum.name.equals(name)) {
                return roleTypeEnum;
            }
        }
        return null;
    }


    public static RoleTypeEnum valueOf(Integer val) {
        for (RoleTypeEnum roleTypeEnum : values()) {
            if (roleTypeEnum.code == val) {
                return roleTypeEnum;
            }
        }
        return null;
    }

}
