package com.ljm.boot.springdatajpa.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Description 用户角色关联表
 * @Author lijunming
 * @Date 2019-07-26
 */
@Entity
@Table(name = "user_role")
@Data
public class UserRole {

    public UserRole(){ }

    public UserRole(Integer userId, Integer roleId){
    this.id=new PK(userId,roleId);
    }

    @EmbeddedId
    private PK id;

    /**
     * 联合主键
     */
    @Embeddable
    @Data
    public static class PK implements Serializable {

        public PK() { }

        public PK(int userId, Integer roleId) {
            this.userId = userId;
            this.roleId = roleId;
        }
        /**
         * 用户id
         */
        private Integer userId;

        /**
         * 角色id
         */
        private Integer roleId;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
            result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
            if (userId == null) {
                if (other.userId != null)
                    return false;
            } else if (!userId.equals(other.userId))
                return false;
            return true;
        }
    }
}
