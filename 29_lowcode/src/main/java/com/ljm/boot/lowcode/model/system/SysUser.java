package com.ljm.boot.lowcode.model.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ljm.boot.lowcode.model.base.BaseModelCID;
import lombok.Data;

import javax.persistence.*;

/**
 * @author Dominick Li
 * @description 系统用户表
 **/
@Data
@Entity
@Table(name = "sys_user", uniqueConstraints = {@UniqueConstraint(columnNames="username")})
public class SysUser extends BaseModelCID {

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 加密用得盐
     */
    private String salt;
    /**
     * 用户昵称
     */
    private String name;
    /**
     * 用户状态 1=正常 0=禁用
     */
    private Integer enabled = 1;
    /**
     * 角色编号
     */
    private Integer roleId;
    /**
     * 机构编号
     */
    private Integer channelId;
    /**
     * 逻辑删除字段 1=删除 0=未删除
     */
    private Integer deleted = 0;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private SysRole sysRole;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channelId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private SysChannel sysChannel;

    @Transient
    private String roleName;

    @Transient
    private String channelName;

}

