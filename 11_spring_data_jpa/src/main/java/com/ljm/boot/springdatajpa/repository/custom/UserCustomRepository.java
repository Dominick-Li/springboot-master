package com.ljm.boot.springdatajpa.repository.custom;


import com.ljm.boot.springdatajpa.model.User;

import java.util.List;

public interface UserCustomRepository {
    List<Object[]> findBynativeQuery(User user);
}
