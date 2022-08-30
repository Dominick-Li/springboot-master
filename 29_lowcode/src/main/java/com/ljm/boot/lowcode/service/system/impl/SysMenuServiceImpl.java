package com.ljm.boot.lowcode.service.system.impl;


import com.ljm.boot.lowcode.config.JwtTokenConfig;
import com.ljm.boot.lowcode.model.system.SysMenu;
import com.ljm.boot.lowcode.model.system.SysRole;
import com.ljm.boot.lowcode.model.system.SysRoleMenu;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.repository.system.SysRoleMenuRepository;
import com.ljm.boot.lowcode.repository.system.SysMenuRepository;
import com.ljm.boot.lowcode.repository.system.SysRoleRepository;
import com.ljm.boot.lowcode.service.system.SysMenuService;
import com.ljm.boot.lowcode.service.system.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MCQ
 * @Description
 * @date 2021-04-15 14:53
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Autowired
    private SysRoleMenuRepository sysRoleMenuRepository;

    @Autowired
    private JwtTokenConfig jwtTokenConfig;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public JsonResult findAll() {
        java.util.List<SysMenu> sysMenus = sysMenuRepository.findAllByParentIdOrderBySortIndexAsc(0);
        return JsonResult.successResult(sysMenus);
    }

    @Override
    public JsonResult reloadMenu(java.util.List<SysMenu> sysMenuList, String token) {
        SysMenu curSysMenu, subSysMenu;
        List<SysMenu> saveSysMenuList = new ArrayList<>();
        for (int i = 1; i <= sysMenuList.size(); i++) {
            curSysMenu = sysMenuList.get(i - 1);
            curSysMenu.setParentId(0);
            curSysMenu.setSortIndex(i);
            saveSysMenuList.add(curSysMenu);
            for (int j = 1; j <= curSysMenu.getChildren().size(); j++) {
                subSysMenu = curSysMenu.getChildren().get(j - 1);
                subSysMenu.setSortIndex(j);
                subSysMenu.setParentId(curSysMenu.getId());
                saveSysMenuList.add(subSysMenu);
            }
        }
        sysMenuRepository.saveAll(saveSysMenuList);
        String roleCode = jwtTokenConfig.getRoleCodeFromToken(token);
        SysRole sysRole = sysRoleRepository.findTopByRoleCode(roleCode);
        return JsonResult.successResult(sysRoleService.getMenuListByRole(sysRole));
    }

    @Override
    public JsonResult save(SysMenu sysMenu) {
        if (sysMenu.getId() != null) {
            SysMenu one = sysMenuRepository.getOne(sysMenu.getId());
            one.setIcon(sysMenu.getIcon());
            one.setPath(sysMenu.getPath());
            one.setTitle(sysMenu.getTitle());
            one.setImportStr(sysMenu.getImportStr());
            sysMenu = one;
        }
        sysMenuRepository.saveAndFlush(sysMenu);
        return JsonResult.successResult();
    }

    @Override
    public JsonResult deleteByIdList(List<Integer> idList) {
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuRepository.findAllByMenuIdIn(idList);
        StringBuilder stringBuilder = new StringBuilder();
        if (sysRoleMenus.size() > 0) {
            List<Integer> collect = sysRoleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
            List<SysMenu> allById = sysMenuRepository.findAllById(collect);
            allById.forEach(menu -> stringBuilder.append(menu.getTitle()).append(","));
            idList.removeAll(collect);
        }
        if (idList.size() > 0) {
            List<SysMenu> sysMenus = sysMenuRepository.findAllById(idList);
            sysMenuRepository.deleteInBatch(sysMenus);
        }
        if (stringBuilder.length() != 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("已被角色使用,不能被删除");
            return JsonResult.failureResult(stringBuilder.toString());
        }
        return JsonResult.successResult();
    }
}
