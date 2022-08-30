package com.ljm.boot.lowcode.service.impl;

import com.ljm.boot.lowcode.enums.DataTypeEnum;
import com.ljm.boot.lowcode.enums.RoleTypeEnum;
import com.ljm.boot.lowcode.model.MetaColumn;
import com.ljm.boot.lowcode.model.MetaInfo;
import com.ljm.boot.lowcode.model.system.SysMenu;
import com.ljm.boot.lowcode.model.system.SysRoleMenu;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.model.tool.PageParam;
import com.ljm.boot.lowcode.model.vo.PageVO;
import com.ljm.boot.lowcode.model.vo.SelectVO;
import com.ljm.boot.lowcode.repository.MetaColumnRepository;
import com.ljm.boot.lowcode.repository.MetaInfoRepository;
import com.ljm.boot.lowcode.repository.system.BaseCustomRepository;
import com.ljm.boot.lowcode.repository.system.SysRoleMenuRepository;
import com.ljm.boot.lowcode.repository.system.SysMenuRepository;
import com.ljm.boot.lowcode.service.MetaService;
import com.ljm.boot.lowcode.util.NativeQueryUtil;
import com.ljm.boot.lowcode.util.IdWorkerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Dominick Li
 * @CreateTime 2022/5/24 22:27
 * @Description
 **/
@Service
public class MetaServiceImpl implements MetaService, BaseCustomRepository<MetaInfo> {

    @Autowired
    private MetaInfoRepository metaInfoRepository;

    @Autowired
    private MetaColumnRepository metaColumnRepository;

    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Autowired
    private SysRoleMenuRepository sysRoleMenuRepository;

    @Autowired
    private NativeQueryUtil nativeQueryUtil;

    private IdWorkerUtil idWorkerUtil = new IdWorkerUtil();

    @Override
    public JsonResult queryByCondition(PageParam<MetaInfo> pageParam) {
        return JsonResult.successResult(new PageVO<>(this.findAllByCondition(pageParam)));
    }

    @Override
    public JsonResult save(MetaInfo metaInfo) {
        MetaInfo exists = metaInfoRepository.findByMetaNameOrTableCode(metaInfo.getMetaName(), metaInfo.getTableCode());
        if (exists != null && !exists.getId().equals(metaInfo.getId())) {
            return JsonResult.failureResult("模型名称和数据库表名称不能重复!");
        }
        if (metaInfo.getId() == null) {
            //id为null表示新增
            metaInfo.setId(idWorkerUtil.nextId());
            Integer sortIndex = 0;
            for (MetaColumn metaColumn : metaInfo.getMetaColumnList()) {
                metaColumn.setSortIndex(sortIndex);
                metaColumn.setMetaId(metaInfo.getId());
                sortIndex++;
            }
            boolean flag = nativeQueryUtil.createTable(metaInfo);
            if (!flag) {
                return JsonResult.failureResult("创建表异常!");
            }

            //添加菜单
            SysMenu sysMenu = new SysMenu();
            sysMenu.setIcon("iconfont icon-rizhiguanli icon-menu");
            Integer maxSortIndex = sysMenuRepository.findMaxSortIndex();
            sysMenu.setSortIndex(maxSortIndex++);
            sysMenu.setPath("/index/universal/" + metaInfo.getId());
            sysMenu.setTitle(metaInfo.getMetaName());
            sysMenu.setImportStr("universal/Index.vue");
            sysMenuRepository.save(sysMenu);
            metaInfo.setMenuId(sysMenu.getId());
            //添加角色菜单关联表
            SysRoleMenu sysRoleMenu = new SysRoleMenu(RoleTypeEnum.ADMIN.getCode(), sysMenu.getId());
            sysRoleMenuRepository.save(sysRoleMenu);

        } else {
            //如果元数据模型名称变更需同步修改菜单名称
            if (!exists.getMetaName().equals(metaInfo.getMetaName())) {
                SysMenu sysMenu = sysMenuRepository.getOne(metaInfo.getMenuId());
                sysMenu.setTitle(metaInfo.getMetaName());
                sysMenuRepository.save(sysMenu);
            }

            //需要删除字段或者添加字段
            Integer sortIndex = 0;
            //获取数据库中已存在的字段id和字段名称映射
            Map<Integer, MetaColumn> idColumnMapping = exists.getMetaColumnList().stream().collect(Collectors.toMap(MetaColumn::getId, java.util.function.Function.identity()));

            for (MetaColumn metaColumn : metaInfo.getMetaColumnList()) {
                metaColumn.setSortIndex(sortIndex);
                sortIndex++;
                if (metaColumn.getMetaId() == null) {
                    //新增字段
                    metaColumn.setMetaId(metaInfo.getId());
                    //执行sql新增字段
                    nativeQueryUtil.addColumn(metaInfo.getTableCode(), metaColumn);
                } else {
                    MetaColumn existsColumn = idColumnMapping.remove(metaColumn.getId());
                    if (!metaColumn.getColumnCode().equals(existsColumn.getColumnCode())) {
                        //如果字段名称修改过,则需要执行sql修改数据库中的字段名称
                        nativeQueryUtil.changeColumn(metaInfo.getTableCode(), existsColumn.getColumnCode(), metaColumn);
                    }
                }
            }
            //删除字段
            if (idColumnMapping.size() > 0) {
                metaColumnRepository.deleteAll(idColumnMapping.values());
                for (MetaColumn metaColumn : idColumnMapping.values()) {
                    //执行sql删除字段
                    nativeQueryUtil.dropColumn(metaInfo.getTableCode(), metaColumn.getColumnCode());
                }
            }
        }
        metaInfoRepository.save(metaInfo);
        metaColumnRepository.saveAll(metaInfo.getMetaColumnList());
        return JsonResult.successResult();
    }

    @Override
    public JsonResult deleteByIdList(List<Long> idList) {
        List<MetaInfo> metaInfoList = metaInfoRepository.findAllById(idList);
        for (MetaInfo metaInfo : metaInfoList) {
            nativeQueryUtil.dropTable(metaInfo.getTableCode());
        }
        //查询元数据绑定的菜单
        List<Integer> menuIdList = metaInfoRepository.findMenuIdByMetaIdIn(idList);
        //删除角色菜单关联表
        sysRoleMenuRepository.deleteAllByMenuIdIn(menuIdList);
        //删除菜单表
        sysMenuRepository.deleteAllByIdIn(menuIdList);
        metaColumnRepository.deleteAllByMetaIdIn(idList);
        metaInfoRepository.deleteAllByIdIn(idList);
        return JsonResult.successResult();
    }

    @Override
    public Page<MetaInfo> findAllByCondition(PageParam<MetaInfo> pageParam) {
        Specification<MetaInfo> querySpecifi = new Specification<MetaInfo>() {
            @Override
            public Predicate toPredicate(Root<MetaInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                pageParam.setPredicate(predicates, cb, root);
                // and到一起的话所有条件就是且关系，or就是或关系82
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        pageParam.initPageable();
        return this.metaInfoRepository.findAll(querySpecifi, pageParam.getPageable());
    }

    @Override
    public JsonResult getDataTypeList() {
        List<SelectVO> selectVOList = new ArrayList<>();
        for (DataTypeEnum dataTypeEnum : DataTypeEnum.values()) {
            selectVOList.add(new SelectVO(dataTypeEnum.getValue(), dataTypeEnum.getName()));
        }
        return JsonResult.successResult(selectVOList);
    }

}
