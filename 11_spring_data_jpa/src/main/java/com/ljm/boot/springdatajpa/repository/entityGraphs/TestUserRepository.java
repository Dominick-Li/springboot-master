package com.ljm.boot.springdatajpa.repository.entityGraphs;

import com.ljm.boot.springdatajpa.model.entityGraphs.TestMenu;
import com.ljm.boot.springdatajpa.model.entityGraphs.TestUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author Dominick Li
 * @CreateTime 2022/1/19 7:06
 * @Description
 **/
public interface TestUserRepository  extends JpaRepository<TestUser,Integer> {

    //还是N+1
    @Override
    @EntityGraph(value="User.Graph",type = EntityGraph.EntityGraphType.FETCH)
    TestUser getOne(Integer integer);

    @EntityGraph(value="User.Graph",type = EntityGraph.EntityGraphType.FETCH)
    TestUser findByName(String name);
}
