package com.ljm.boot.lowcode.service.system;


import com.ljm.boot.lowcode.model.system.SysMenu;
import com.ljm.boot.lowcode.model.system.SysRole;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.model.vo.SelectVO;
import com.ljm.boot.lowcode.service.BaseService;

import java.util.List;

public interface SysRoleService extends BaseService<SysRole, Integer> {

    List<SysMenu> getMenuListByRole(SysRole sysRole);

    JsonResult<List<SelectVO>> findSelectList();

    JsonResult getMenuListByToken(String token);
}
