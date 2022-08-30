package com.ljm.boot.lowcode.repository.system;

import com.ljm.boot.lowcode.model.system.SysToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface SysTokenRepository extends JpaRepository<SysToken, Integer> {

    SysToken findTopByAccessToken(String token);

    @Transactional
    @Modifying
    void deleteAllByUserId(Integer userId);

    @Transactional
    @Modifying
    @Query(value = "delete from SysToken  where  createTime<?1")
    void deleteByTimeOut(Date endDate);
}
