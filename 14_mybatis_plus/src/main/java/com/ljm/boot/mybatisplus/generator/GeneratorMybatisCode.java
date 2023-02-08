package com.ljm.boot.mybatisplus.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.ljm.boot.mybatisplus.entity.BaseEntity;

import java.util.Collections;

public class GeneratorMybatisCode {

    //生成类的作者描述
    private final static String AUTHOR = "Dominick Li";
    //数据库连接信息
    private final static String DATASOURCE_URL = "jdbc:mysql://localhost:3306/boot_master?useSSL=false&serverTimezone=GMT%2b8&characterEncoding=utf8&connectTimeout=10000&socketTimeout=3000&autoReconnect=true";
    //数据库用户名
    private final static String DATASOURCE_USERNAME = "root";
    //数据库密码
    private final static String DATASOURCE_PASSWORD = "123456";
    //项目包名全路径
    private final static String BASE_PACKAGE = "com.ljm.boot.mybatisplus";
    //通用父类的字段，该字段不会出现在生成的类中
    private final static String[] SUERP_COLUMN = new String[]{"id", "create_time", "update_time"};
    //需要指定生成模板的表名称
    private final static String[] GENERATOR_TABLES = new String[]{"sys_users"};

    public static void main(String[] args) {
        //项目根路径
        String rootPath = System.getProperty("user.dir");
        //聚合工程需要写工程的名字,普通工程写 "" 就行
        String parantPath = "/14_mybatis_plus";

        FastAutoGenerator.create(DATASOURCE_URL, DATASOURCE_USERNAME, DATASOURCE_PASSWORD)
                //全局配置
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者 baomidou 默认值:作者
                            //.enableSwagger() // 开启 swagger 模式 默认值:false
                            .fileOverride() // 覆盖已生成文件 默认值:false
                            .disableOpenDir()//禁止打开输出目录 默认值:true
                            .commentDate("yyyy-MM-dd")// 注释日期
                            .dateType(DateType.ONLY_DATE)//定义生成的实体类中日期类型 DateType.ONLY_DATE 默认值: DateType.TIME_PACK
                            .outputDir(rootPath + parantPath + "/src/main/java");
                })
                //包命名配置
                .packageConfig(builder -> {
                    builder.parent(BASE_PACKAGE)// 父包模块名 默认值:com.baomidou
//                            .controller("controller")//Controller 包名 默认值:controller
//                            .entity("entity")//Entity 包名 默认值:entity
//                            .service("service")//Service 包名 默认值:service
//                            .mapper("mapper")//Mapper 包名 默认值:mapper
                            .pathInfo(Collections.singletonMap(OutputFile.xml, rootPath + parantPath + "/src/main/resources/mapper")); // 设置mapper.xml存放路径
                })
                //生成策略配置
                .strategyConfig(builder -> {
                    builder.addInclude(GENERATOR_TABLES) // 设置需要生成的表名 可边长参数“user”, “user1”
                            //.addTablePrefix("sys_") // 设置生成的累名称过滤表前缀
                            .serviceBuilder()//service策略配置
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .entityBuilder()// 实体类策略配置
                            .idType(IdType.AUTO)//主键策略  根据表是否有自增自动分配
                            .superClass(BaseEntity.class)
                            .addSuperEntityColumns(SUERP_COLUMN) //设置父类的通用字段
                            .enableLombok() //开启lombok
                            //.logicDeleteColumnName("deleted")// 说明逻辑删除是哪个字段
                            .enableTableFieldAnnotation()// 属性加上注解说明
                            .controllerBuilder() //controller 策略配置
                            .formatFileName("%sController")
                            .enableRestStyle() // 开启RestController注解
                            .mapperBuilder()// mapper策略配置
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()//@mapper注解开启
                            .formatXmlFileName("%sMapper");
                })
                //生成的模板配置 如果用默认的模板则下面的不需要配置
                .templateConfig(
                        builder -> {
                            builder.controller("/template/controller.java");
                            builder.entity("/template/entity.java");
                            builder.mapper("/template/mapper.java");
                            builder.xml("/template/mapper.xml");
                            builder.service("/template/service.java");
                            builder.serviceImpl("/template/serviceImpl.java");
                        }
                )
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

}
