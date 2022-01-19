package com.ljm.boot.springdatajpa.model.entityGraphs;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @Author Dominick Li
 * @CreateTime 2022/1/19 6:56
 * @Description
 **/
@Entity
@Table(name = "test_menu")
@Data
//@NamedEntityGraph(name = "Menu.Graph", attributeNodes = {@NamedAttributeNode("children")})
public class TestMenu {

    @Id
    private Integer id;

    private String name;

    private Integer roleId;

    private Integer parentId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", referencedColumnName = "id", foreignKey = @ForeignKey(name = "null", value = ConstraintMode.NO_CONSTRAINT))
    private List<TestMenu> children;
}
