package com.ljm.boot.lowcode.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RoleVO {

    private Integer id;

    private Date createTime;

    private Date lastmodifiedTime;

    private String roleName;

    private String roleCode;

    private String userStr;

    private String builtIn;

    private java.util.List<Integer> menuIds;

}
