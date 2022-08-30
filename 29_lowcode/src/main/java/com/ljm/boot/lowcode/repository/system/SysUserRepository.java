package com.ljm.boot.lowcode.repository.system;

import com.ljm.boot.lowcode.model.system.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SysUserRepository extends JpaRepository<SysUser, Integer>, JpaSpecificationExecutor {

    SysUser findByUsernameAndDeleted(String username, Integer deleted);

    @Transactional
    @Modifying
    @Query(value = "update SysUser set deleted=1  where id in (?1)")
    void updateUserDeleteStatu(java.util.List<Integer> idList);

}
