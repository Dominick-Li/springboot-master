package com.ljm.boot.lowcode.model;

import com.ljm.boot.lowcode.model.base.BaseModel;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Dominick Li
 * @description 映射到数据中的表描述信息
 **/
@Data
@Entity
@Table(name = "meta_info")
public class MetaInfo extends BaseModel {

    public MetaInfo() {
    }

    public MetaInfo(Long id, String metaName, String tableCode, boolean increment, String tableDescription, java.util.List<MetaColumn> metaColumnList) {
        this.setId(id);
        this.metaName = metaName;
        this.tableCode = tableCode;
        this.metaColumnList = metaColumnList;
        this.metaDescription = tableDescription;
        this.metaColumnList = metaColumnList;
        this.increment = increment;
    }

    /**
     * 表名称->映射到页面显示的菜单名称
     */
    @NotBlank
    private String metaName;

    /**
     * 数据库表名称
     */
    @NotBlank
    private String tableCode;

    /**
     * 表的字段
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "metaId", insertable = false, updatable = false)
    @OrderBy("sortIndex")
    private List<MetaColumn> metaColumnList;

    /**
     * 描述信息
     */
    private String metaDescription;

    /**
     * 表的主键是否为自增类型 非自增使用雪花算法生成Id
     */
    private boolean increment;

    /**
     * 菜单Id
     */
    private Integer menuId;
}
