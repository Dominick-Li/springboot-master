package com.ljm.databaselook.repository;


import com.ljm.databaselook.model.MethodLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


public interface MethodLockRepository extends JpaRepository<MethodLock, Integer> {

    MethodLock findByMethodNameAndMethodDesc(String methodName,String methodDesc);

    @Transactional
    @Modifying
    @Query(value = "insert into method_lock(methodName,methodDesc,updateTime) values(?1,?2,?3)",nativeQuery = true)
    int insert(String methodName, String methodDesc, Date updateTime);

}
