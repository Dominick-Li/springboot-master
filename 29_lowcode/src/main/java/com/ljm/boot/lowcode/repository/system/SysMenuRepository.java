package com.ljm.boot.lowcode.repository.system;

import com.ljm.boot.lowcode.model.system.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface SysMenuRepository extends JpaRepository<SysMenu, Integer>, JpaSpecificationExecutor<SysMenu> {

    List<SysMenu> findAllByParentIdOrderBySortIndexAsc(Integer parentId);

    @Transactional
    @Modifying
    void deleteAllByIdIn(List<Integer> idList);

    @Query(value = "select max(sortIndex) from SysMenu  where parentId=0")
    Integer findMaxSortIndex();

}
