package com.ljm.boot.springdatajpa;

import com.ljm.boot.springdatajpa.model.*;
import com.ljm.boot.springdatajpa.repository.*;
import com.ljm.boot.springdatajpa.repository.custom.UserCustomImplRepsotory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class SpringDatajpaApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    ChannelRepository channelRepository;

    @Autowired
    UserCustomImplRepsotory userCustomImplRepsotory;

    @Autowired
    MenuRepository menuRepository;

    @Test
    void contextLoads() {
        //selectUserByPageable();
        //saveAll();
        //addUser();
        //addUserRole();;
        //findUserById(1);
        //findChannelById(1);
        //SpecificationQuery();
        // nativeQuery();
        testEntityGraphs();
    }

    private void addUser() {
        User user = new User();
        user.setEmail("dominick_li@163.com");
        user.setUsername("dominick_li");
        user.setPassword("123");
        user.setMobile("17600251493");
        user.setChannelId(1);
        userRepository.save(user);
    }


    private void addUserRole() {
        //初始化2个角色信息
        Role role = new Role();
        role.setRoleCode("admin");
        role.setRoleName("管理员");
        roleRepository.save(role);
        role = new Role();
        role.setRoleCode("user");
        role.setRoleName("普通用户");
        roleRepository.save(role);
        //初始化用户角色表
        UserRole userRole = new UserRole(1, 1);
        userRoleRepository.save(userRole);
        userRole = new UserRole(1, 2);
        userRoleRepository.save(userRole);
    }

    /**
     * 分页查询
     */
    private void selectUserByPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        Page<User> userPage = userRepository.findAllByUsernameLike("user%", pageable);
        System.out.println("总记录数" + userPage.getTotalElements() + ",总页数" + userPage.getTotalPages());
        for (User user : userPage.getContent()) {
            System.out.println(user.getUsername());
        }
    }

    /**
     * 批量插入
     */
    private void saveAll() {
        Orders orders = null;
        List<Orders> ordersList = new ArrayList<>(100);
        for (int i = 0; i < 300; i++) {
            orders = new Orders();
            //订单表的主键应该使用雪花算法之类生成,雪花算法生成的id是有序的，这样索引查询起来会快很多,uuid是无序的,查询效率极低。。。。
            orders.setId(UUID.randomUUID().toString());
            orders.setOrderName("订单_" + 1);
            ordersList.add(orders);
        }
        ordersRepository.saveAll(ordersList);
    }

    private void findUserById(Integer id) {
        User user = userRepository.getOne(id);
        System.out.println("渠道名称是:" + user.getChannel().getChannelName());
        System.out.println("角色名称是:" + user.getRole().getRoleName());
        System.out.println("用户拥有:" + user.getRoles().size() + "个角色");
    }

    private void findChannelById(Integer id) {
        Channel channel = channelRepository.getOne(id);
        System.out.println(channel.getChannelName() + "有" + channel.getUsers().size() + "个用户");
    }

    private void SpecificationQuery() {
        String startDate = "2020-05-22";
        String endDate = "2020-05-25";
        String username = "dominick";
        Specification<User> querySpecifi = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringUtils.isEmpty(startDate)) {
                    //大于或等于传入时间
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate").as(String.class), startDate));
                }
                if (!StringUtils.isEmpty(endDate)) {
                    //小于或等于传入时间
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdDate").as(String.class), endDate));
                }
                if (!StringUtils.isEmpty(username)) {
                    //模糊查询,需要自己手动拼接%%字符
                    predicates.add(cb.like(root.get("username").as(String.class), "%" + username + "%"));
                }
                // and到一起的话所有条件就是且关系，or就是或关系
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<User> userList = userRepository.findAll(querySpecifi);
        for (User user : userList) {
            System.out.println(user.getUsername());
        }
    }

    /**
     * 测试使用源生的sql查询
     */
    private void nativeQuery() {
        User user = new User();
        user.setUsername("dominick_li");
        user.setChannelId(1);
        List<Object[]> list = userCustomImplRepsotory.findBynativeQuery(user);
        for (Object[] objs : list) {
            System.out.println(objs[0] + "," + objs[1]);
        }
    }

    private void testEntityGraphs() {
        //初始化插入基础数据
        {
//            Menu menu = new Menu();
//            menu.setId(1);
//            menu.setMenuName("系统管理");
//            Menu menu2 = new Menu();
//            menu2.setId(2);
//            menu2.setMenuName("用户管理");
//            menu2.setParentId(1);
//            Menu menu3 = new Menu();
//            menu3.setId(3);
//            menu3.setMenuName("角色管理");
//            menu3.setParentId(1);
//            Menu menu4 = new Menu();
//            menu4.setId(4);
//            menu4.setMenuName("报表统计");
//            Menu menu5 = new Menu();
//            menu5.setId(5);
//            menu5.setMenuName("按月统计");
//            menu5.setParentId(4);
//            menuRepository.save(menu);
//            menuRepository.save(menu2);
//            menuRepository.save(menu3);
//            menuRepository.save(menu4);
//            menuRepository.save(menu5);
        }

        List<Menu> menuList = menuRepository.findAllByParentIdIsNull();
        System.out.println("一级菜单数量=" + menuList.size());
        for (Menu menu : menuList) {
            System.out.println("菜单名称=" + menu.getMenuName() + "的子菜单数量=" + menu.getChildList().size());
        }
    }

}
