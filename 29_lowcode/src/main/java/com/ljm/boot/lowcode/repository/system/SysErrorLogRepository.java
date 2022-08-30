package com.ljm.boot.lowcode.repository.system;

import com.ljm.boot.lowcode.model.system.SysErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface SysErrorLogRepository extends JpaRepository<SysErrorLog, Integer>, JpaSpecificationExecutor {

    @Transactional
    @Modifying
    void deleteAllByIdIn(java.util.List<Integer> idList);

    @Transactional
    @Modifying
    @Query(value = "delete from SysErrorLog  where createTime<?1")
    void deleteLogGreaterThanDay(Date endDate);
}
