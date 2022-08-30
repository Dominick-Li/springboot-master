package com.ljm.boot.lowcode.model.system;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Dominick Li
 * @description 系统角色表
 **/
@Data
@Entity
@Table(name = "sys_role")
public class SysRole extends com.ljm.boot.lowcode.model.base.BaseModelCID {

    public SysRole() { }

    public SysRole(String roleName, String roleCode) {
        this.roleName = roleName;
        this.roleCode = roleCode;
    }
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色编码
     */
    private String roleCode;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", updatable = false, insertable = false)
    private java.util.List<SysUser> sysUsers;

    @Transient
    private String userStr;

    @Transient
    private java.util.List<Integer> menuIds;

    /**
     * 用户拥有的角色信息
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sys_role_menu", joinColumns = {@JoinColumn(name = "roleId",foreignKey =  @ForeignKey(name = "null", value = ConstraintMode.NO_CONSTRAINT))},
            inverseJoinColumns = {@JoinColumn(name = "menuId",foreignKey =  @ForeignKey(name = "null2", value = ConstraintMode.NO_CONSTRAINT))})
    @OrderBy("sortIndex")
    private java.util.List<SysMenu> sysMenus;

}
