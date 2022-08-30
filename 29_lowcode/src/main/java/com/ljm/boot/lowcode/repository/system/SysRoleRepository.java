package com.ljm.boot.lowcode.repository.system;

import com.ljm.boot.lowcode.model.system.SysRole;
import com.ljm.boot.lowcode.model.vo.SelectVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface SysRoleRepository extends JpaRepository<SysRole, Integer>, JpaSpecificationExecutor {

    @Transactional
    @Modifying
    void deleteAllByIdIn(java.util.List<Integer> ids);

    SysRole findTopByRoleCode(String roleCode);

    SysRole findTopByRoleName(String roleName);

    @Query(value = "select new com.ljm.boot.lowcode.model.vo.SelectVO(id,roleName) from SysRole   ")
    java.util.List<SelectVO> findSelectList();
}
