package com.ljm.boot.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljm.boot.mybatisplus.entity.SysUsers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @description
* @author Dominick Li
* @createTime 2023-02-07
**/
@Mapper
public interface SysUsersMapper extends BaseMapper<SysUsers> {
    IPage<SysUsers> page(Page page,@Param("condition") SysUsers condition);
}
