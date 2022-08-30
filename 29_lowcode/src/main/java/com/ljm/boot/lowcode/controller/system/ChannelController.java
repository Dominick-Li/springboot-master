package com.ljm.boot.lowcode.controller.system;


import com.ljm.boot.lowcode.annotation.OperationLog;
import com.ljm.boot.lowcode.model.system.SysChannel;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.service.system.SysChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author Dominick Li
 * @description 渠道管理
 **/
@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private SysChannelService channelService;

    @GetMapping("/findAll")
    public JsonResult queryByCondition() {
        return channelService.findAll();
    }

    @OperationLog(module = "渠道管理",description = "删除")
    @GetMapping("/{id}")
    public JsonResult deleteById(@PathVariable Integer id) {
        return channelService.deleteChannelById(id);
    }

    @OperationLog(module = "渠道管理",description = "添加或修改")
    @PostMapping("/")
    public JsonResult save(@RequestBody SysChannel sysChannel) {
        return channelService.save(sysChannel);
    }

    @OperationLog(module = "渠道管理",description = "刷新渠道层级和顺序")
    @PostMapping("/reload")
    public JsonResult reload(@RequestBody java.util.List<SysChannel> sysChannelList){
        return channelService.reload(sysChannelList);
    }

    @GetMapping("/getChildChannel/{id}")
    public JsonResult getChildChannel(@PathVariable Integer id){
        return channelService.getChildChannel(id);
    }
}
