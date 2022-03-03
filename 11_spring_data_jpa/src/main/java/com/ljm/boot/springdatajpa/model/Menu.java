package com.ljm.boot.springdatajpa.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @Description
 * @Author Dominick Li
 * @CreateTime 2022/2/28 17:46
 **/
@Data
@Entity
@Table(name = "menu")
@NamedEntityGraph(name = "menu.findAll", attributeNodes = {
        @NamedAttributeNode(value = "childList")
})
public class Menu {
    @Id
    private Integer id;
    private String menuName;
    private Integer parentId;
    @OneToMany
    @JoinColumn(name="parentId")
    private List<Menu> childList;

}
