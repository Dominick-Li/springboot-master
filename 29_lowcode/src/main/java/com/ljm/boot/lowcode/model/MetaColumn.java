package com.ljm.boot.lowcode.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;

/**
 * @author Dominick Li
 * @description 元数据模型中的列数据
 **/
@Data
@Entity
@Table(name = "meta_column")
public class MetaColumn {

    public MetaColumn(){}

    public MetaColumn(String columnName, String columnCode, String dataType, Long metaId) {
        this.columnName = columnName;
        this.columnCode = columnCode;
        this.dataType = dataType;
        this.metaId = metaId;
    }

    /**
     * 唯一标识 主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 字段名名称 ->映射到页面显示的table列名称
     */
    private String columnName;

    /**
     * 数据库中字段名称
     */
    private String columnCode;

    /**
     * 字段类型
     */
    private String dataType;

    /**
     * 所属表的主键Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long metaId;

    /**
     * 页面上字段显示的顺序
     */
    private Integer sortIndex;

    /**
     * 页面上是否展示当前列
     */
    private boolean viewShow=true;

    /**
     * 是否作为查询条件
     */
    private boolean search=false;

}
