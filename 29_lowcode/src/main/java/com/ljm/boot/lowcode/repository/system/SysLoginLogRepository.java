package com.ljm.boot.lowcode.repository.system;

import com.ljm.boot.lowcode.model.system.SysLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface SysLoginLogRepository extends JpaRepository<SysLoginLog, Integer>, JpaSpecificationExecutor {

    @Transactional
    @Modifying
    void deleteAllByIdIn(java.util.List<Integer> ids);

    @Transactional
    @Modifying
    @Query(value = "delete from SysLoginLog where createTime< ?1 " )
    void deleteLogGreaterThanDay(Date endDate);
}
