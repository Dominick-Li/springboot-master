package com.ljm.boot.mybatisplus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ljm.boot.mybatisplus.entity.SysUser;
import com.ljm.boot.mybatisplus.entity.page.PageParam;
import com.ljm.boot.mybatisplus.entity.page.PageResult;
import com.ljm.boot.mybatisplus.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author LeeJunMing
 * @since 2020-06-29
 */
@Controller
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    ISysUserService userService;

    /**
     * 页码跳转 分页查询
     */
    @GetMapping("/")
    public String getUserBypage(PageParam<SysUser> pageParam, Model model) {
        pageParam.initialization();
        IPage<SysUser> iPage = userService.page(pageParam.getPage(), pageParam.getQueryWrapper());
        System.out.printf("总页数:{%d},总记录数:{%d}", iPage.getPages(), iPage.getTotal());
        model.addAttribute("page", new PageResult<>(iPage));
        return "user/index";
    }

    /**
     * ajax 分页查询
     */
    @PostMapping(value = "/json")
    @ResponseBody
    public PageResult getUserJsonDataBypage(@RequestBody PageParam<SysUser> pageParam, Model model) {
        pageParam.initialization();
        IPage<SysUser> iPage = userService.page(pageParam.getPage(), pageParam.getQueryWrapper());
        model.addAttribute("page", new PageResult<>(iPage));
        return new PageResult<>(iPage);
    }


    /**
     * 根据id获取user,并且跳转到数据修改页面
     */
    @GetMapping("/{id}")
    public String getUserById(@PathVariable Integer id, Model model) {
        //id=-1的时候代表着是重添加用户按钮进来的请求
        if (id != -1) {
            SysUser user = userService.getById(id);
            model.addAttribute("user", user);
        }
        return "user/save";
    }


    /**
     * 添加用户
     */
    @PostMapping("/")
    @ResponseBody
    public boolean addUser(SysUser user) {
        return userService.save(user);
    }

    /**
     * 修改用户
     */
    @PutMapping("/")
    @ResponseBody
    public boolean modifyUser(SysUser user) {
        return userService.saveOrUpdate(user);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    @ResponseBody
    public boolean deleteUserById(@PathVariable String ids) {
        String idarr[] = ids.split(",");
        if (idarr.length == 1) {
            return  userService.removeById(idarr[0]);
        } else {
            return userService.removeByIds(Arrays.asList(idarr));
        }
    }

}
