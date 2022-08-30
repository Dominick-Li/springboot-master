package com.ljm.boot.lowcode.repository.system;

import com.ljm.boot.lowcode.model.tool.PageParam;
import org.springframework.data.domain.Page;

/**
 * @author Dominick Li
 * @description 基础自定义接口
 **/
public interface BaseCustomRepository<T> {

    /**
     * 自定义查询
     * @param pageParam
     * @return
     */
    Page<T> findAllByCondition(PageParam<T> pageParam);

}
