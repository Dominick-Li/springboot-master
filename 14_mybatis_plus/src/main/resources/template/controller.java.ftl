package ${package.Controller};

import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
* @description
* @author ${author}
* @createTime ${date}
**/
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    @Autowired
    private ${table.serviceName} ${(table.serviceName)?uncap_first};

    /**
    * 根据id查询
    */
    @GetMapping("/{id}")
    public ${table.entityName} getById(@PathVariable Integer id) {
        return ${(table.serviceName)?uncap_first}.getById(id);
    }

    /**
    * 查询所有
    */
    @GetMapping
    public List<${table.entityName}> list() {
        return ${(table.serviceName)?uncap_first}.list();
    }

    /**
    * 添加
    */
    @PostMapping
    public boolean add(${table.entityName} ${(table.entityName)?uncap_first}) {
        return ${(table.serviceName)?uncap_first}.save(${(table.entityName)?uncap_first});
    }

    /**
    * 修改
    */
    @PutMapping
    public boolean modify(${table.entityName} ${(table.entityName)?uncap_first}) {
        return ${(table.serviceName)?uncap_first}.updateById(${(table.entityName)?uncap_first});
    }

    /**
    * 根据Id删除
    */
    @DeleteMapping("/{id}")
    public boolean deleteId(@PathVariable Integer id) {
       return  ${(table.serviceName)?uncap_first}.removeById(id);
    }

}
</#if>
