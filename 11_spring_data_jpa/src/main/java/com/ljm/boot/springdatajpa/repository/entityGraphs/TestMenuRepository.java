package com.ljm.boot.springdatajpa.repository.entityGraphs;

import com.ljm.boot.springdatajpa.model.entityGraphs.TestMenu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author Dominick Li
 * @CreateTime 2022/1/19 7:06
 * @Description
 **/
public interface TestMenuRepository extends JpaRepository<TestMenu,Integer> {

    @EntityGraph(value = "Menu.Graph", type = EntityGraph.EntityGraphType.FETCH)
    @Override
    List<TestMenu> findAll();
}
