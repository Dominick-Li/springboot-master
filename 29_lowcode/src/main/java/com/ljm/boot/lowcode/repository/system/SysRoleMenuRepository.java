package com.ljm.boot.lowcode.repository.system;

import com.ljm.boot.lowcode.model.system.SysRoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysRoleMenuRepository extends JpaRepository<SysRoleMenu, SysRoleMenu.PK> {

    @Query(value = "select rm from SysRoleMenu rm  where rm.id.menuId=?1 ")
    List<SysRoleMenu> findAllByMenuIdIn(List<Integer> menuIds);

    @Query(value = "select rm from SysRoleMenu rm  where rm.id.roleId=?1 ")
    List<SysRoleMenu> findAllByRoleId(Integer roleId);

    @Transactional
    @Modifying
    @Query(value = "delete  from SysRoleMenu rm  where rm.id.roleId=?1 ")
    void deleteAllByRoleId(Integer roleId);

    @Transactional
    @Modifying
    @Query(value = "delete from SysRoleMenu  rm where rm.id.menuId in(?1)")
    void deleteAllByMenuIdIn(List<Integer> menuIdList);

}
