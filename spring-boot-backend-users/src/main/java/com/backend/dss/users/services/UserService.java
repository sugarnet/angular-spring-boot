package com.backend.dss.users.services;

import com.backend.dss.users.entities.User;
import com.backend.dss.users.models.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(Long id);

    User save(User user);

    Optional<User> update(UserUpdateRequest user, Long id);

    void deleteById(Long id);
}
