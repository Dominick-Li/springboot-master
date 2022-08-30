package com.ljm.boot.lowcode.model.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Dominick Li
 * @description 非自增Id基类
 **/
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) //选择继承策略
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseModel implements java.io.Serializable {

    private static final long serialVersionUID = 2863256929817929825L;

    /**
     * id主键，需要手动set Id, 推荐使用雪花算法生成的id， uuid作为主键查询效率低
     */
    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 创建时间
     */
    @CreatedDate
    private Date createTime;

    /**
     * 修改时间
     */
    @LastModifiedDate
    private Date lastmodifiedTime;

}
