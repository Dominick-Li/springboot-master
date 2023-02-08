package com.ljm.boot.mybatisplus;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ljm.boot.mybatisplus.entity.SysUsers;
import com.ljm.boot.mybatisplus.mapper.SysUsersMapper;
import com.ljm.boot.mybatisplus.service.SysUsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
class MybatisPlusApplicationTests {

    @Autowired
    private SysUsersService sysUsersService;

    @Resource
    private SysUsersMapper sysUsersMapper;

    @Test
    void contextLoads() {
        //查询
        {
            //指定查询列和查询条件
            LambdaQueryWrapper<SysUsers> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper
                    //只查询id,姓名，年纪三个字段的数据
                    .select(SysUsers::getId, SysUsers::getUsername, SysUsers::getGrade)
                    //根据年级等值查询
                    .eq(SysUsers::getGrade, 7)
                    //模糊查询
                    .like(SysUsers::getUsername, "%test%")
                    //根据Id范围查询
                    .in(SysUsers::getId, Arrays.asList(1));

            //根据Id查询
            SysUsers sysUsers = sysUsersService.getById(1);
            //根据查询条件获取一条数据
            sysUsers = sysUsersService.getOne(lambdaQueryWrapper);
            System.out.println(sysUsers == null);

            //根据条件查询数据列表数据
            List<SysUsers> userList = sysUsersService.list(lambdaQueryWrapper);
            System.out.println(userList.size());

            //页码，每页显示的记录数
            Integer currentPage = 1, pageSize = 10;
            Page page = new Page<>(currentPage, pageSize);
            //分页查询
            IPage<SysUsers> iPage = sysUsersService.page(page);
            System.out.printf("总页数:{%d},总记录数:{%d}", iPage.getPages(), iPage.getTotal());
            System.out.println();
            //分页指定查询条件
            //IPage<SysUser> iPage = sysUserService.page(page, lambdaQueryWrapper);

            //封装查询条件用于xml的自定义查询
            SysUsers condition = new SysUsers();
            condition.setUsername("test");
            condition.setPassword("123456");

            //自定义xml分页查询 会写sql就行
            IPage<SysUsers> iPage2 = sysUsersMapper.page(page, condition);
            System.out.printf("自定义xml分页查询 总页数:{%d},总记录数:{%d}", iPage2.getPages(), iPage2.getTotal());
        }
        //增删改
        {
            SysUsers user = new SysUsers();
            user.setUsername("test99");

            //增加
            boolean flag = sysUsersService.save(user);
            System.out.println("save flag=" + flag);
            //批量增加
            //flag = sysUsersService.saveBatch(Arrays.asList(user));
            System.out.println("save flag=" + flag);

            //修改
            SysUsers updateUser = new SysUsers();
            updateUser.setPassword("1234567");
            updateUser.setId(1);

            //根据id修改
            flag = sysUsersService.updateById(updateUser);
            System.out.println("updateById flag=" + flag);

            UpdateWrapper<SysUsers> sysUsersUpdateWrapper = Wrappers.update();
            sysUsersUpdateWrapper.lambda()
                    .set(SysUsers::getUsername, "admin")
                    .eq(SysUsers::getId, 1);
            flag = sysUsersService.update(sysUsersUpdateWrapper);
            System.out.println("update flag=" + flag);
            //删除
            //根据Id删除
            flag = sysUsersService.removeById(2);
            System.out.println("removeById flag=" + flag);
            //根据Id批量删除
            flag = sysUsersService.removeBatchByIds(Arrays.asList(3, 4, 5));
            System.out.println("removeBatchByIds flag=" + flag);
            //根据自定义条件删除
            LambdaQueryWrapper<SysUsers> condition = new LambdaQueryWrapper<>();
            condition.eq(SysUsers::getGrade, 9);
            flag = sysUsersService.remove(condition);
            System.out.println("removeBatchByIds flag=" + flag);

        }
    }

}
