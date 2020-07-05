package com.ljm.boot.mybatisplus.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.ljm.boot.mybatisplus.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class MysqlGenerator {

    //生成类的作者描述
    private final static String AUTHOR="LeeJunMing";
    private final static String DATASOURCE_URL="jdbc:mysql://localhost:3306/boot_master?useSSL=false&serverTimezone=GMT%2b8&characterEncoding=utf8&connectTimeout=10000&socketTimeout=3000&autoReconnect=true";
    private final static String DATASOURCE_DRIVER_NAME="com.mysql.cj.jdbc.Driver";
    private final static String DATASOURCE_USERNAME="root";
    private final static String DATASOURCE_PASSWORD="123456";
    //基础包名
    private final static String BASE_PACKAGE="com.ljm.boot";
    //生成的模块名称
    private final static String MODEL_NAME="mybatisplus";
    //基类的字段(不会出现在自动生成的表中)
    private final static String[] SUERP_COLUMN=new String[]{"id","create_time"};
    //要把哪些表生成模板代码
    private final static String [] GENERATOR_TABLES=new String[]{"sys_role"};

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        //聚合工程需要写工程的名字,普通工程写 '' 就行
        String parantPath="/15_mybatis-plus";
        gc.setOutputDir(projectPath + parantPath+"/src/main/java");
        gc.setAuthor(AUTHOR);
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(DATASOURCE_URL);
        dsc.setDriverName(DATASOURCE_DRIVER_NAME);
        dsc.setUsername(DATASOURCE_USERNAME);
        dsc.setPassword(DATASOURCE_PASSWORD);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(MODEL_NAME);
        pc.setParent(BASE_PACKAGE);
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return projectPath + parantPath+"/src/main/java/"+BASE_PACKAGE.replaceAll("\\.","/")+"/" + pc.getModuleName()
                        + "/mapper/xml/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);

        //设置控制器的超类
        //strategy.setSuperControllerClass("com.baomidou.mybatisplus.samples.generator.common.BaseController");

        //设置实体类的超类
        strategy.setSuperEntityClass(BaseEntity.class);
        //设置
        strategy.setInclude(GENERATOR_TABLES);
        strategy.setSuperEntityColumns(SUERP_COLUMN);
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);

        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}
