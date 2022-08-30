package com.ljm.boot.lowcode.util;

import com.ljm.boot.lowcode.enums.DataTypeEnum;
import com.ljm.boot.lowcode.model.MetaColumn;
import com.ljm.boot.lowcode.model.MetaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

/**
 * @author Dominick Li
 * @description sql执行工具类
 **/
@Component
@Slf4j
public class NativeQueryUtil {

    private EntityManagerFactory emf;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * 执行 ddl语句
     *
     * @param sql 需要执行的sql
     * @return 执行是否成功
     */
    public boolean executeUpdate(String sql) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            Query query = em.createNativeQuery(sql);
            tx.begin();
            query.executeUpdate();
            tx.commit();
            log.info("executeUpdate sql={} success", sql);
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.info("executeUpdate sql={} error:{}", sql, e.getMessage());
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * 根据MetaInfo和MetaColumn类的字段描述信息生成创建表的sql
     */
    public boolean createTable(MetaInfo metaInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("create table ").append(metaInfo.getTableCode()).append(" (");
        sb.append("id ");
        if (metaInfo.isIncrement()) {
            //设置主键自增
            sb.append(DataTypeEnum.INT.getValue());
            sb.append(" AUTO_INCREMENT");
        } else {
            //非自增的使用长整形，使用雪花算法手动设置id
            sb.append(DataTypeEnum.LONG.getValue());
        }
        sb.append(",");
        for (MetaColumn metaColumn : metaInfo.getMetaColumnList()) {
            sb.append(metaColumn.getColumnCode()).append(" ").append(metaColumn.getDataType());
            sb.append(",");
        }
        //设置主键
        sb.append(" PRIMARY KEY (id)");
        //设置字符集
        sb.append(" )ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 DEFAULT COLLATE=utf8mb4_0900_ai_ci;");
        return executeUpdate(sb.toString());
    }

    /**
     * 删除表
     */
    public boolean dropTable(String tableName) {
        String sql = "drop table " + tableName;
        return executeUpdate(sql);
    }

    /**
     * 新增字段
     */
    public  boolean addColumn(String tableName, MetaColumn metaColumn){
        return executeUpdate(String.format("alter table %s add %s %s",tableName,metaColumn.getColumnCode(),metaColumn.getDataType()));
    }

    /**
     * 修改字段
     */
    public  boolean changeColumn(String tableName, String oldColumnCode, MetaColumn metaColumn){
        return executeUpdate(String.format("alter table %s change %s %s %s",tableName,oldColumnCode,metaColumn.getColumnCode(),metaColumn.getDataType()));
    }

    /**
     * 删除字段
     */
    public boolean dropColumn(String tableName,String columnCode){
        return executeUpdate(String.format("alter table %s drop %s",tableName,columnCode));
    }

    /**
     * 分页查询列表数据
     */
    public java.util.List query(String sql, java.util.List<Object> condition) {
        EntityManager em = emf.createEntityManager();
        try {
            log.info("exec query sql={}",sql);
            Query query = em.createNativeQuery(sql);
            //注入参数
            Integer size = condition.size();
            if (size > 0) {
                for (int i = 1; i <= size; i++) {
                    //query的parameter 下标起始位置重1开始的
                    query.setParameter(i, condition.get(i - 1));
                }
            }
            List result = query.getResultList();
            log.info("getResultList size={}", sql, result.size());
            return result;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}
