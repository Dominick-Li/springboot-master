package com.ljm.boot.lowcode.model.system;

import com.ljm.boot.lowcode.model.base.BaseModelCIDNoModifyTime;
import lombok.Data;

import javax.persistence.Entity;

/**
 * @author Dominick Li
 * @description 登录日志表
 **/
@Data
@Entity
@javax.persistence.Table( name ="sys_loginlog" )
public class SysLoginLog extends BaseModelCIDNoModifyTime{


    /**
     * 用户名
     */
    private String username;

    /**
     * ip地址
     */
    private String ipAddr;

    public SysLoginLog(){}

    public SysLoginLog(String username, String ipAddr){
        this.ipAddr=ipAddr;
        this.username=username;
    }
}
