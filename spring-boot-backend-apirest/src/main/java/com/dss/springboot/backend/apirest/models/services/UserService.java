package com.dss.springboot.backend.apirest.models.services;

import com.dss.springboot.backend.apirest.models.entity.User;

public interface UserService {
    User findByUsername(String username);
}
