package com.ljm.boot.lowcode.service.system;


import com.ljm.boot.lowcode.model.system.SysChannel;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.service.BaseService;

/**
 * @author Dominick Li
 * @CreateTime 2020/10/13 16:30
 * @description
 **/
public interface SysChannelService extends BaseService<SysChannel,Integer> {

    JsonResult findAll();

    JsonResult reload(java.util.List<SysChannel> sysChannelList);

    JsonResult getChildChannel(Integer id);

    JsonResult deleteChannelById(Integer id);
}
