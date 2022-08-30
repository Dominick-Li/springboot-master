package com.ljm.boot.lowcode.model.system;

import com.ljm.boot.lowcode.model.base.BaseModelCID;
import lombok.Data;

import javax.persistence.*;

/**
 * @author Dominick Li
 * @description 系统机构表
 **/
@Data
@Entity
@Table(name = "sys_channel")
public class SysChannel extends BaseModelCID {

    /**
     * 机构名称
     */
    private String channelName;
    /**
     * 机构编码
     */
    private String channelCode;
    /**
     * 排序下标
     */
    private Integer orderStr;
    /**
     * 父机构编号
     */
    private Integer parentId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", foreignKey = @ForeignKey(name = "null", value = ConstraintMode.NO_CONSTRAINT))
    @OrderBy("orderStr")
    private java.util.List<SysChannel> children;

}
