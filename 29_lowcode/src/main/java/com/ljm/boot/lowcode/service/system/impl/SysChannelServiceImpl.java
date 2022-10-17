package com.ljm.boot.lowcode.service.system.impl;


import com.ljm.boot.lowcode.model.system.SysChannel;
import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.model.tool.PageParam;
import com.ljm.boot.lowcode.model.vo.PageVO;
import com.ljm.boot.lowcode.repository.system.SysChannelRepository;
import com.ljm.boot.lowcode.service.system.SysChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.List;


/**
 * @author Dominick Li
 * @CreateTime 2020/10/12 17:58
 * @description
 **/
@Service
public class SysChannelServiceImpl implements SysChannelService {

    @Autowired
    private SysChannelRepository sysChannelRepository;

    @Override
    public JsonResult queryByCondition(PageParam<SysChannel> pageParam) {
        org.springframework.data.domain.Page<SysChannel> page = sysChannelRepository.findAll(pageParam.getPageable());
        PageVO<SysChannel> pageVo = new PageVO(page.getTotalElements(), page.getTotalPages(), page.getContent());
        return JsonResult.successResult(pageVo);
    }

    @Override
    public JsonResult save(SysChannel sysChannel) {
        if (sysChannel.getId() != null) {
            SysChannel one = sysChannelRepository.getOne(sysChannel.getId());
            one.setChannelCode(sysChannel.getChannelCode());
            one.setChannelName(sysChannel.getChannelName());
            sysChannel = one;
        } else {
            SysChannel exists = sysChannelRepository.findTopByChannelName(sysChannel.getChannelName());
            if (exists != null) {
                return JsonResult.failureResult("机构名称不能重复!");
            }
            exists = sysChannelRepository.findTopByChannelCode(sysChannel.getChannelCode());
            if (exists != null) {
                return JsonResult.failureResult("机构编码不能重复!");
            }
        }
        sysChannelRepository.saveAndFlush(sysChannel);
        return JsonResult.successResult();
    }

    @Override
    public JsonResult findAll() {
        java.util.List<SysChannel> sysChannelList = sysChannelRepository.findAllByParentIdOrderByOrderStr(0);
        return JsonResult.successResult(sysChannelList);
    }

    @Override
    public JsonResult reload(List<SysChannel> sysChannelList) {
        SysChannel curSysChannel;
        List<SysChannel> saveSysChannelList = new ArrayList<>();
        for (int i = 1; i <= sysChannelList.size(); i++) {
            curSysChannel = sysChannelList.get(i - 1);
            curSysChannel.setOrderStr(i);
            recursionChannel(saveSysChannelList, curSysChannel);
            saveSysChannelList.add(curSysChannel);
        }
        sysChannelRepository.saveAll(saveSysChannelList);
        return JsonResult.successResult();
    }

    @Override
    public JsonResult getChildChannel(Integer id) {
        SysChannel sysChannel = sysChannelRepository.getOne(id);
        return JsonResult.successResult(Arrays.asList(sysChannel));
    }

    private void recursionChannel(List<SysChannel> sysChannelList, SysChannel curSysChannel) {
        if (curSysChannel.getChildren().size() == 0) {
            return;
        }
        SysChannel subSysChannel;
        for (int j = 1; j <= curSysChannel.getChildren().size(); j++) {
            subSysChannel = curSysChannel.getChildren().get(j - 1);
            subSysChannel.setOrderStr(j);
            subSysChannel.setParentId(curSysChannel.getId());
            recursionChannel(sysChannelList, subSysChannel);
            sysChannelList.add(subSysChannel);
        }
    }

    @Override
    @Transactional
    public JsonResult deleteChannelById(Integer id) {
        SysChannel sysChannel = sysChannelRepository.getOne(id);
        recursionDelete(sysChannel);
        sysChannel.setChildren(null);
        sysChannelRepository.delete(sysChannel);
        return JsonResult.successResult();
    }

    private void recursionDelete(SysChannel sysChannel) {
        if (sysChannel.getChildren().size() == 0) {
            return;
        }
        for (SysChannel subSysChannel : sysChannel.getChildren()) {
            recursionDelete(subSysChannel);
            sysChannelRepository.delete(subSysChannel);
        }
    }

}
