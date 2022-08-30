package com.ljm.boot.lowcode.service;


import com.ljm.boot.lowcode.model.tool.JsonResult;
import com.ljm.boot.lowcode.model.tool.PageParam;

/**
 * @author dominick Li
 * @description 基础服务
 **/
public interface BaseService<T, PK> {

    /**
     * 分页条件查询
     * @param pageParam
     * @return
     */
    default JsonResult queryByCondition(PageParam<T> pageParam) {
        return null;
    }

    /**
     * 修改或新增
     * @param t
     * @return
     */
    default JsonResult save(T t) {
        return null;
    }


    /**
     * 根据id列表删除数据
     * @param idList
     * @return
     */
    default JsonResult deleteByIdList(java.util.List<PK> idList) {
        return null;
    }
}
