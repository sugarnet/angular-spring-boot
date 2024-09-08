package com.backend.dss.users.services.impl;

import com.backend.dss.users.entities.Role;
import com.backend.dss.users.entities.User;
import com.backend.dss.users.models.UserAdmin;
import com.backend.dss.users.models.UserUpdateRequest;
import com.backend.dss.users.repositories.RoleRepository;
import com.backend.dss.users.repositories.UserRepository;
import com.backend.dss.users.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public User save(User user) {

        user.setRoles(getRoles(user));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Optional<User> update(UserUpdateRequest user, Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User updatedUser = userOptional.get();
            updatedUser.setName(user.getName());
            updatedUser.setLastname(user.getLastname());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setUsername(user.getUsername());

            List<Role> roles = getRoles(user);

            updatedUser.setRoles(roles);

            return Optional.of(userRepository.save(updatedUser));
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private List<Role> getRoles(UserAdmin user) {
        Optional<Role> role = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        role.ifPresent(roles::add);

        if (user.isAdmin()) {
            Optional<Role> roleAdmin = roleRepository.findByName("ROLE_ADMIN");
            roleAdmin.ifPresent(roles::add);
        }
        return roles;
    }
}
