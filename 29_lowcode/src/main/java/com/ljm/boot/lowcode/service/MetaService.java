package com.ljm.boot.lowcode.service;

import com.ljm.boot.lowcode.model.MetaInfo;
import com.ljm.boot.lowcode.model.tool.JsonResult;

/**
 * @author Dominick Li
 * @description 元数据服务
 **/
public interface MetaService extends BaseService<MetaInfo,Long> {

    /**
     * 获取数据类型
     */
    JsonResult getDataTypeList();

}
