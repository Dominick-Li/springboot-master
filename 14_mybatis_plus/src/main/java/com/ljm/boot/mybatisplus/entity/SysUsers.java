package com.ljm.boot.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
* @description
* @author Dominick Li
* @createTime 2023-02-07
**/
@Getter
@Setter
@TableName("sys_users")
public class SysUsers extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 年纪
     */
    @TableField("grade")
    private Integer grade;


}
