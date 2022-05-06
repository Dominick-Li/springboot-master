package com.ljm.boot.springdatajpa.repository;

import com.ljm.boot.springdatajpa.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Dominick Li
 * @CreateTime 2020/5/23 13:10
 * @description
 **/
public interface UserRepository extends JpaRepository<User,Integer>, JpaSpecificationExecutor {

    @Transactional
    @Modifying
    @Query("delete from User  where id=?1")
    void deleteByUserId(Integer id);

    Page<User> findAllByUsernameLike(String username, Pageable pageable);

//    @Query("select u from User u where u.username = ?1 and u.password = ?2")
//    User getByUsernameAndPassword(String username, String password);

    @Query("select u from User u where u.username = :username and u.password = :password")
    User getByUsernameAndPassword(@Param("username")String username, @Param("password") String password);


    @Query(value = "select u.* from sys_user u where u.username = :username and u.password = :password",nativeQuery = true)
    User getByUsernameAndPasswordSQl(@Param("username")String username, @Param("password") String password);

    @Query("select u from User u where 1=1 " +
            " and(:username is null or u.username=:username)" +
            " and(:mobile is null or u.mobile=:mobile)")
    User findByUserNameOrMobile(@Param("username")String username, @Param("mobile") String mobile);


}
