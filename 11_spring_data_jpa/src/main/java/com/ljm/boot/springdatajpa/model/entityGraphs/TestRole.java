package com.ljm.boot.springdatajpa.model.entityGraphs;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/23 23:35
 * @description
 **/
@Entity
@Table(name = "test_role")
@Data
public class TestRole {

    @Id
    private Integer id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="roleId")
    @Where(clause = "parentId is null")
    private List<TestMenu> testMenuList;

}
