package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};

/**
* @description
* @author ${author}
* @createTime ${date}
**/
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

}
</#if>
