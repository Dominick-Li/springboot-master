package com.ljm.boot.lowcode.model.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;

/**
 * @author Dominick Li
 * @description 系统菜单表
 **/
@Data
@Entity
@Table(name = "sys_menu")
public class SysMenu extends com.ljm.boot.lowcode.model.base.BaseModelCID {

    /**
     * 访问路径
     */
    private String path;
    /**
     * 菜单名称
     */
    private String title;
    /**
     * 菜单图标样式
     */
    private String icon;
    /**
     * 父菜单编号
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer parentId=0;
    /**
     * 排序下标
     */
    private Integer sortIndex = 1;
    /**
     * 组件路径
     */
    private String importStr;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", referencedColumnName = "id", foreignKey = @ForeignKey(name = "null", value = ConstraintMode.NO_CONSTRAINT))
    @OrderBy("sortIndex")
    private java.util.List<SysMenu> children;

}
