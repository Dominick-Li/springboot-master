package com.ljm.boot.jdbctemplate;

import com.ljm.boot.jdbctemplate.model.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.List;
import java.util.Map;

@SpringBootTest
class JdbctemplateApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() throws Exception {
        //createTable();
        //insert();
        //select();
        //update();
        //delete();
    }

    /**
     * 创建表
     */
    public void createTable() {
        String sql = "create table sys_user(id int AUTO_INCREMENT,username varchar(255),password varchar(255),age int,PRIMARY KEY (id))ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 DEFAULT COLLATE=utf8mb4_0900_ai_ci;";
        jdbcTemplate.execute(sql);
        System.out.println("创建表成功");
    }

    /**
     * 添加
     */
    public void insert() {
        //1.插入数据 占位符
        String sql = "insert into sys_user (username,password,age) values(?,?,?);";
        int ret = jdbcTemplate.update(sql,"王五","123456",18);
        if (ret == 0) {
            System.out.println("添加失败!");
        }
        //插入数据 拼接好数据
        sql = "insert into sys_user (username,password,age) values('张三','李四',18);";
        for (int i = 0; i < 20; i++) {
            ret = jdbcTemplate.update(sql);
            if (ret == 0) {
                System.out.println("添加失败!");
            }
        }

        //2.插入数据并返回主键
        KeyHolder keyHolder1 = new GeneratedKeyHolder();
        String finalSql = sql;
        ret = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                // 设置返回的主键字段名
                PreparedStatement ps = con.prepareStatement(finalSql, Statement.RETURN_GENERATED_KEYS);
                return ps;
            }
        }, keyHolder1);
        if (ret == 1) {
            // 获取到插入数据生成的自增ID
            int id = keyHolder1.getKey().intValue();
            System.out.println(id);
        }
    }


    /**
     * 查询
     */
    public void select() {
        String sql = "select * from sys_user";
        //1.使用map封装查询出来的字段名称和字段值
        System.out.println("返回map数据");
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> map : mapList) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println("key=" + entry.getKey() + ",value=" + entry.getValue());
            }
        }

        //2.通过RowMapper获取ResultSet手动赋值给bean
        System.out.println("手动注入");
        List<SysUser> sysUserList = jdbcTemplate.query(sql, new RowMapper<SysUser>() {
            @Override
            public SysUser mapRow(ResultSet resultSet, int i) throws SQLException {
                SysUser sysUser = new SysUser(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getInt("age"));
                return sysUser;
            }
        });
        for (SysUser sysUser : sysUserList) {
            System.out.println(sysUser.toString());
        }

        //3.通过BeanPropertyRowMapper 克隆字段
        System.out.println("BeanPropertyRowMapper 克隆");
        sysUserList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SysUser.class));
        for (SysUser sysUser : sysUserList) {
            System.out.println(sysUser.toString());
        }

        //4.分页查询
        Integer totalElements = jdbcTemplate.queryForObject("SELECT count(id) FROM sys_user", Integer.class);
        //当前页
        Integer currentPage = 1;
        //页码大小
        Integer pageSize = 5;
        sysUserList = jdbcTemplate.query("SELECT * FROM sys_user limit ?,?", new BeanPropertyRowMapper<>(SysUser.class), (currentPage - 1) * pageSize, pageSize);
        for (SysUser sysUser : sysUserList) {
            System.out.println(sysUser.toString());
        }
        System.out.println("总记录数=" + totalElements + ",总页数=" + (totalElements % pageSize == 0 ? totalElements / pageSize : totalElements / pageSize + 1));
    }


    /**
     * 修改
     */
    public void update() {
        String sql = "update  sys_user set username='李四' where id=1";
        int ret = jdbcTemplate.update(sql);
        if (ret == 1) {
            System.out.println("修改成功");
        }
    }

    /**
     * 删除
     */
    public void delete() {
        String sql = "delete from sys_user where id=?";
        int ret = jdbcTemplate.update(sql, 1);
        if (ret == 1) {
            System.out.println("删除成功");
        }
    }


}
