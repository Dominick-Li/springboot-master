package com.ljm.boot.lowcode.model.system;

import com.ljm.boot.lowcode.model.base.BaseModelCIDNoModifyTime;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Dominick Li
 * @description 错误日志表
 **/
@Data
@Entity
@javax.persistence.Table(name = "sys_errorlog")
public class SysErrorLog extends BaseModelCIDNoModifyTime {

    public SysErrorLog(){

    }

    public SysErrorLog(String errorType, String position, String errorMsg, String serverIp){
        this.errorType=errorType;
        this.position=position;
        this.errorMsg=errorMsg;
        this.ipAddr=serverIp;
    }

    /**
     * 错误信息
     */
    @Column(columnDefinition = "text")
    private String errorMsg;

    /**
     * ip地址
     */
    private String ipAddr;

    /**
     * 错误类型
     */
    private String errorType;

    /**
     * 异常发生的位置
     */
    @Column(columnDefinition = "text")
    private String position;


}
