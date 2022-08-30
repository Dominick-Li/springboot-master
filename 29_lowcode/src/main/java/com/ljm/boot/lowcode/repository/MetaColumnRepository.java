package com.ljm.boot.lowcode.repository;

import com.ljm.boot.lowcode.model.MetaColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 /**
 * @author Dominick Li
 * @description 元数据模型字段持久化仓库
 **/
public interface MetaColumnRepository extends JpaRepository<MetaColumn,Integer> {

    @Transactional
    @Modifying
    void deleteAllByMetaIdIn(java.util.List<Long> metaIdList);

}
