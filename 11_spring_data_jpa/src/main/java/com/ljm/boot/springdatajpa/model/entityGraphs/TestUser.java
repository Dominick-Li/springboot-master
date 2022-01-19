package com.ljm.boot.springdatajpa.model.entityGraphs;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author Dominick Li
 * @CreateTime 2022/1/19 6:49
 * @Description
 **/
@Entity
@Table(name = "test_user")
@Data
@NamedEntityGraphs({
        @NamedEntityGraph(name = "User.Graph", attributeNodes = {
                @NamedAttributeNode(value = "testRole", subgraph = "testMenuList")
        },
                subgraphs = {
                        @NamedSubgraph(name = "testMenuList", attributeNodes = {
                                @NamedAttributeNode(value = "testMenuList")
                        }),
                })
})
public class TestUser {

    @Id
    private Integer id;

    private String name;

    private Integer roleId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", referencedColumnName = "id", insertable = false, updatable = false)
    private TestRole testRole;
}
