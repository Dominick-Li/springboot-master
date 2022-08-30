package com.ljm.boot.lowcode.model.base;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Dominick Li
 * @description 自增Id基类
 **/
@Data
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)//选择继承策略
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public  class BaseModelCID implements java.io.Serializable {

    private static final long serialVersionUID = 2863256929817929825L;

    /**
     * 唯一标识  修改时候需要该参数
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    /**
     * 创建时间
     * @ignore
     */
    @CreatedDate
    private Date createTime;

    /**
     * 修改时间
     * @ignore
     */
    @LastModifiedDate
    private Date lastmodifiedTime;

}
