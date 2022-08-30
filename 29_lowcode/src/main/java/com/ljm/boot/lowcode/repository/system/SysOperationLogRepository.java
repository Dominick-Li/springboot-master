package com.ljm.boot.lowcode.repository.system;

import com.ljm.boot.lowcode.model.system.SysOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface SysOperationLogRepository  extends JpaRepository<SysOperationLog, Long>, JpaSpecificationExecutor {

    @Transactional
    @Modifying
    @Query(value = "delete from SysOperationLog where createTime<?1 ")
    void deleteLogGreaterThanDay(Date endDate);

}
