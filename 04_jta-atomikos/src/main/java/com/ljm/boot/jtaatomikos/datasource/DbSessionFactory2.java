package com.ljm.boot.jtaatomikos.datasource;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.ljm.boot.jtaatomikos.config.DB2Config;
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
@MapperScan(basePackages = {"com.ljm.boot.jtaatomikos.mapper.db2"}, sqlSessionFactoryRef = "sqlSessionFactoryDb2")
public class DbSessionFactory2 {


    @Bean(name = "db2")
    public DataSource newhomeDbDataSource(@Qualifier("DB2Config") DB2Config db2Conf) {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(db2Conf.getJdbcUrl());
        mysqlXaDataSource.setPassword(db2Conf.getPassword());
        mysqlXaDataSource.setUser(db2Conf.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("db2");
        return xaDataSource;
    }


    @Bean(name="sqlSessionFactoryDb2")
    public SqlSessionFactory sqlSessionFactoryDb2(@Qualifier("db2")DataSource DataSource ) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(DataSource);
        return factoryBean.getObject();
    }

    @Bean(name="sqlSessionTemplateDb2")
    public SqlSessionTemplate sqlSessionTemplateDb2(@Qualifier("sqlSessionFactoryDb2") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
