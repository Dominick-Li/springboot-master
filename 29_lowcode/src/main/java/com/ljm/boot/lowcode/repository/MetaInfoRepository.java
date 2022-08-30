package com.ljm.boot.lowcode.repository;

import com.ljm.boot.lowcode.model.MetaInfo;
import com.ljm.boot.lowcode.model.vo.SelectVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Dominick Li
 * @description 元数据模型信息持久化仓库
 **/
public interface MetaInfoRepository extends JpaRepository<MetaInfo,Long>, JpaSpecificationExecutor {

    MetaInfo findByMetaNameOrTableCode(String metaName, String tableCode);

    @Modifying
    @Transactional
    void deleteAllByIdIn(java.util.List<Long> idList);

    @Query(value = "select menuId  from MetaInfo where id in(?1) ")
    List<Integer> findMenuIdByMetaIdIn(List<Long> idList);
}
