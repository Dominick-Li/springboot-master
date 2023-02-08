package com.ljm.boot.mybatisplus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Dominick Li
 * @Description 数据填充
 * @CreateTime 2022/10/26 10:24
 **/
@Component
public class MybatisMetaObjectHandlerConfig implements MetaObjectHandler {

    /**
     * 插入数据添加默认值
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 该属性为空，可以进行填充
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    /**
     * 修改数据添加默认值
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

}
