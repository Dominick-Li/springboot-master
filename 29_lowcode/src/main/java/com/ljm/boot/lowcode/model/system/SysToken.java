package com.ljm.boot.lowcode.model.system;

import com.ljm.boot.lowcode.model.base.BaseModelCIDNoModifyTime;
import lombok.Data;

import javax.persistence.Entity;

/**
 * @author Dominick Li
 * @description token凭证表
 **/
@Data
@Entity
@javax.persistence.Table(name = "sys_token")
public class SysToken extends BaseModelCIDNoModifyTime {

    public SysToken() {
    }

    public SysToken(Integer userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
    }

    /**
     * 用户编号
     */
    private Integer userId;
    /**
     * token令牌
     */
    private String accessToken;


}
