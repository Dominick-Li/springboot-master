package com.ljm.boot.jtaatomikos.mapper.db2;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDetailMapper {
    @Insert("INSERT INTO orderdetails(id, orderId) VALUES(#{id}, #{orderId})")
    int insert(@Param("id") int id, @Param("orderId") String orderId);

}
