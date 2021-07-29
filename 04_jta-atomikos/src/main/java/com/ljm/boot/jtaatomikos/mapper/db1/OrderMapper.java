package com.ljm.boot.jtaatomikos.mapper.db1;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders(id, name) VALUES(#{id}, #{name})")
    int insert(@Param("id") String id, @Param("name") String name);
}
