package com.ljm.boot.springdatajpa.repository;

import com.ljm.boot.springdatajpa.model.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * @Description
 * @Author Dominick Li
 * @CreateTime 2022/2/28 17:52
 **/
public interface MenuRepository extends JpaRepository<Menu,Integer> {

    @EntityGraph(value = "menu.findAll", type = EntityGraph.EntityGraphType.FETCH)
    List<Menu> findAllByParentIdIsNull();
}
