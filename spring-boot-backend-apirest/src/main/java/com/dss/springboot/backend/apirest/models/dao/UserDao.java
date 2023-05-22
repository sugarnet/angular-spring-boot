package com.dss.springboot.backend.apirest.models.dao;

import com.dss.springboot.backend.apirest.models.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long> {
    User findByUsername(String username);

    @Query("select u from User u where u.username=?1")
    User getByUsername(String username);
}
