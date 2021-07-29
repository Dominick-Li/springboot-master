package com.ljm.boot.jtaatomikos.datasource;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.ljm.boot.jtaatomikos.config.DB1Config;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.ljm.boot.jtaatomikos.mapper.db1"}, sqlSessionFactoryRef = "sqlSessionFactoryDb1")
public class DbSessionFactory1 {


    @Bean(name = "db1")
    public DataSource businessDbDataSource(@Qualifier("DB1Config") DB1Config db1Conf) {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(db1Conf.getJdbcUrl());
        mysqlXaDataSource.setPassword(db1Conf.getPassword());
        mysqlXaDataSource.setUser(db1Conf.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("db1");
        return xaDataSource;
    }

    @Bean(name="sqlSessionFactoryDb1")
    public SqlSessionFactory sqlSessionFactoryDb1(@Qualifier("db1")DataSource DataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(DataSource);
        return factoryBean.getObject();
    }

    @Bean(name="sqlSessionTemplateDb1")
    public SqlSessionTemplate sqlSessionTemplateDb1(@Qualifier("sqlSessionFactoryDb1") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
