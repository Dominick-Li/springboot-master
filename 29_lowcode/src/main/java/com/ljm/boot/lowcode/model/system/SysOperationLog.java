package com.ljm.boot.lowcode.model.system;

import com.ljm.boot.lowcode.model.base.BaseModelCIDNoModifyTime;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Dominick Li
 * @description 操作日志表
 **/
@Data
@Entity
@javax.persistence.Table(name = "sys_operationlog")
public class SysOperationLog extends BaseModelCIDNoModifyTime {

    /**
     * 操作员
     */
    private String username;
    /**
     * 模块
     */
    private String module;
    /**
     * 操作
     */
    private String description;
    /**
     * 详细信息
     */
    @Column(columnDefinition = "text")
    private String content;
    /**
     * ip地址
     */
    private String ipAddr;

}
