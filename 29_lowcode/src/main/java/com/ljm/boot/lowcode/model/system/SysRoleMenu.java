package com.ljm.boot.lowcode.model.system;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Dominick Li
 * @description 角色菜单关联表
 **/
@Data
@Entity
@Table(name = "sys_role_menu")
@EntityListeners(AuditingEntityListener.class)
public class SysRoleMenu {

    @EmbeddedId
    private PK id;

    public SysRoleMenu(){}

    public SysRoleMenu(Integer roleId, Integer menuId) {
        this.setId(new PK(roleId, menuId));
    }

    @CreatedDate
    private Date createTime;

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }


    public  Integer getMenuId(){
        return this.id.menuId;
    }

    /**
     * 联合主键
     */
    @Embeddable
    public static class PK implements java.io.Serializable {
        public PK() {

        }

        public PK(Integer roleId) {
            this.roleId = roleId;
        }

        public PK(Integer roleId, Integer menuId) {
            this.roleId = roleId;
            this.menuId = menuId;
        }

        /**
         * 角色编号
         */
        private Integer roleId;

        /**
         * 菜单编号
         */
        private Integer menuId;

        public Integer getRoleId() {
            return roleId;
        }

        public void setRoleId(Integer roleId) {
            this.roleId = roleId;
        }

        public Integer getMenuId() {
            return menuId;
        }

        public void setMenuId(Integer menuId) {
            this.menuId = menuId;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
            result = prime * result + ((menuId == null) ? 0 : menuId.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PK other = (PK) obj;
            if (roleId == null) {
                if (other.roleId != null)
                    return false;
            } else if (!roleId.equals(other.roleId))
                return false;
            if (menuId == null) {
                if (other.menuId != null)
                    return false;
            } else if (!menuId.equals(other.menuId))
                return false;
            return true;
        }
    }

}
