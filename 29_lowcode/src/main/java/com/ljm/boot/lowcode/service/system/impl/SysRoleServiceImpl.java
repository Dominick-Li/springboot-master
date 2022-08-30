package com.ljm.boot.lowcode.service.system.impl;


import com.ljm.boot.lowcode.config.JwtTokenConfig;
import com.ljm.boot.lowcode.enums.RoleTypeEnum;
import com.ljm.boot.lowcode.model.system.SysMenu;
import com.ljm.boot.lowcode.model.system.SysRole;
import com.ljm.boot.lowcode.model.system.SysRoleMenu;
import com.ljm.boot.lowcode.model.system.SysUser;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.model.tool.PageParam;
import com.ljm.boot.lowcode.model.vo.PageVO;
import com.ljm.boot.lowcode.model.vo.RoleVO;
import com.ljm.boot.lowcode.model.vo.SelectVO;
import com.ljm.boot.lowcode.repository.system.SysRoleMenuRepository;
import com.ljm.boot.lowcode.repository.system.SysRoleRepository;
import com.ljm.boot.lowcode.service.system.SysRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dominick Li
 * @CreateTime 2020/10/12 13:51
 * @description
 **/
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysRoleMenuRepository sysRoleMenuRepository;

    @Autowired
    private JwtTokenConfig jwtTokenConfig;

    @Override
    public JsonResult queryByCondition(PageParam<SysRole> pageParam) {
        org.springframework.data.domain.Page<SysRole> page = sysRoleRepository.findAll(pageParam.getPageable());
        java.util.List<SysRole> sysRoles = page.getContent();
        List<RoleVO> roleVOList = new ArrayList<>();
        RoleVO roleVO;
        List<SysRoleMenu> sysRoleMenus;
        for (SysRole sysRole : sysRoles) {
            roleVO = new RoleVO();
            sysRole.setUserStr(userListToStr(sysRole.getSysUsers()));
            sysRole.setSysUsers(null);
            BeanUtils.copyProperties(sysRole, roleVO);
            if (RoleTypeEnum.nameOf(sysRole.getRoleCode()) != null) {
                roleVO.setBuiltIn("1");
            } else {
                roleVO.setBuiltIn("0");
            }
            sysRoleMenus = sysRoleMenuRepository.findAllByRoleId(sysRole.getId());
            roleVO.setMenuIds(sysRoleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList()));
            roleVOList.add(roleVO);
        }
        PageVO<RoleVO> pageVo = new PageVO<>(page.getTotalElements(), page.getTotalPages(), roleVOList);
        return JsonResult.successResult(pageVo);
    }

    @Override
    public JsonResult save(SysRole sysRole) {
        boolean insertOrUpdate = false;
        if (sysRole.getId() == null) {
            //角色名称和编码不能重复
            SysRole exists = sysRoleRepository.findTopByRoleName(sysRole.getRoleName());
            if (exists != null) {
                return JsonResult.failureResult("角色名称不能重复!");
            }
            exists = sysRoleRepository.findTopByRoleCode(sysRole.getRoleCode());
            if (exists != null) {
                return JsonResult.failureResult("角色编码不能重复!");
            }
            sysRole = sysRoleRepository.save(sysRole);
        } else {
            Optional<SysRole> optionalRole = sysRoleRepository.findById(sysRole.getId());
            if (optionalRole.isPresent()) {
                insertOrUpdate = true;
                SysRole oldSysRole = optionalRole.get();
                boolean flag = false;
                if (StringUtils.hasText(sysRole.getRoleCode())) {
                    oldSysRole.setRoleCode(sysRole.getRoleCode());
                    flag = true;
                }
                if (StringUtils.hasText(sysRole.getRoleName())) {
                    oldSysRole.setRoleName(sysRole.getRoleName());
                    flag = true;
                }
                if (flag) {
                    sysRoleRepository.saveAndFlush(oldSysRole);
                }
            } else {
                return JsonResult.failureResult("角色不存在");
            }
        }
        List<Integer> menuIds = sysRole.getMenuIds();
        if (insertOrUpdate && (menuIds == null || menuIds.size() == 0)) {
            List<SysRoleMenu> sysRoleMenus = sysRoleMenuRepository.findAllByRoleId(sysRole.getId());
            if (sysRoleMenus.size() > 0) {
                sysRoleMenuRepository.deleteAll(sysRoleMenus);
            }
        }
        if (menuIds != null && menuIds.size() > 0) {
            if (insertOrUpdate) {
                sysRoleMenuRepository.deleteAllByRoleId(sysRole.getId());
            }
            ArrayList<SysRoleMenu> sysRoleMenus = new ArrayList<>();
            Integer roleId = sysRole.getId();
            menuIds.forEach(menuId -> {
                SysRoleMenu sysRoleMenu = new SysRoleMenu(roleId, menuId);
                sysRoleMenus.add(sysRoleMenu);
            });
            sysRoleMenuRepository.saveAll(sysRoleMenus);
        }
        return JsonResult.successResult();
    }

    @Override
    public JsonResult deleteByIdList(List<Integer> id) {
        sysRoleRepository.deleteAllByIdIn(id);
        return JsonResult.successResult();
    }

    private String userListToStr(List<SysUser> sysUsers) {
        StringBuilder sb = new StringBuilder("");
        sysUsers.forEach(user -> {
            sb.append(user.getUsername()).append(",");
        });
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public List<SysMenu> getMenuListByRole(SysRole sysRole) {
        List<SysMenu> sysMenuList = new ArrayList<>();
        sysMenuList.addAll(sysRole.getSysMenus().stream().filter(menu -> menu.getParentId() == 0).collect(Collectors.toList()));
        for (SysMenu sysMenu : sysMenuList) {
            sysMenu.setChildren(sysRole.getSysMenus().stream().filter(subMenu -> subMenu.getParentId().equals(sysMenu.getId())).collect(Collectors.toList()));
        }
        return sysMenuList;
    }

    @Override
    public JsonResult<List<SelectVO>> findSelectList() {
        return JsonResult.successResult(sysRoleRepository.findSelectList());
    }

    @Override
    public JsonResult getMenuListByToken(String token) {
        String roleCode=jwtTokenConfig.getRoleCodeFromToken(token);
        SysRole sysRole=sysRoleRepository.findTopByRoleCode(roleCode);
        return JsonResult.successResult(getMenuListByRole(sysRole));
    }
}
