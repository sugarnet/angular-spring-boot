package com.backend.dss.users.repositories;

import com.backend.dss.users.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
