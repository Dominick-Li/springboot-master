package com.ljm.boot.lowcode.model.base;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Dominick Li
 * @description 自增Id基类无修改时间
 **/
@Data
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)//选择继承策略
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public  class BaseModelCIDNoModifyTime implements java.io.Serializable {

    private static final long serialVersionUID = 2863256929817929825L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    private Date createTime;


}
