package com.ljm.boot.mybatisplus.controller;

import com.ljm.boot.mybatisplus.entity.SysUsers;
import com.ljm.boot.mybatisplus.service.SysUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
* @description
* @author Dominick Li
* @createTime 2023-02-07
**/
@RestController
@RequestMapping("/sysUsers")
public class SysUsersController {

    @Autowired
    private SysUsersService sysUsersService;

    /**
    * 根据id查询
    */
    @GetMapping("/{id}")
    public SysUsers getById(@PathVariable Integer id) {
        return sysUsersService.getById(id);
    }

    /**
    * 查询所有
    */
    @GetMapping
    public List<SysUsers> list() {
        return sysUsersService.list();
    }

    /**
    * 添加
    */
    @PostMapping
    public boolean add(SysUsers sysUsers) {
        return sysUsersService.save(sysUsers);
    }

    /**
    * 修改
    */
    @PutMapping
    public boolean modify(SysUsers sysUsers) {
        return sysUsersService.updateById(sysUsers);
    }

    /**
    * 根据Id删除
    */
    @DeleteMapping("/{id}")
    public boolean deleteId(@PathVariable Integer id) {
       return  sysUsersService.removeById(id);
    }

}
