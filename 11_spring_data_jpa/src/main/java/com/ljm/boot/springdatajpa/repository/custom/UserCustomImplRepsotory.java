package com.ljm.boot.springdatajpa.repository.custom;


import com.ljm.boot.springdatajpa.model.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserCustomImplRepsotory implements UserCustomRepository {

    private EntityManagerFactory emf;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Object[]> findBynativeQuery(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = null;
            StringBuilder sql = new StringBuilder("select * from sys_user  where 1=1 ");
            List<Object> condition = new ArrayList<>();
            int index = 0;
            if (!StringUtils.isEmpty(user.getUsername())) {
                index++;
                condition.add(user.getUsername());
                sql.append(" and username=?" + index);
            }
            if (!StringUtils.isEmpty(user.getChannelId())) {
                index++;
                condition.add(user.getChannelId());
                sql.append(" and channelId=?" + index);
            }
            //创建query对象
            query = em.createNativeQuery(sql.toString());
            //注入参数
            if (index != 0) {
                for (int i = 1; i <= index; i++) {
                    //query的parameter 下标起始位置重1开始的
                    query.setParameter(i, condition.get(i - 1));
                }
            }
            List<Object[]> success = query.getResultList();
            em.close();
            return success;
        } finally {
            if (em != null) {
                em.close();
            }
        }

    }
}
