package com.example.bank_rest_test_task.service;

import com.example.bank_rest_test_task.dto.UserRegisterDto;
import com.example.bank_rest_test_task.entity.Role;
import com.example.bank_rest_test_task.entity.RoleName;
import com.example.bank_rest_test_task.entity.User;
import com.example.bank_rest_test_task.exception.DuplicateUserException;
import com.example.bank_rest_test_task.exception.RoleNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RegistrationService {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrationUser(UserRegisterDto userRegisterDto) throws RoleNotFoundException {
        if (userService.existUserByUsername(userRegisterDto.username())) {
            throw new DuplicateUserException("User by name: %s already exists".formatted(userRegisterDto.username()));
        }

        Role userDefaultRole = roleService.findRoleByRoleName(RoleName.ROLE_USER);

        userService.saveUser(User.builder()
                        .username(userRegisterDto.username())
                        .password(passwordEncoder.encode(userRegisterDto.password()))
                        .roles(Set.of(userDefaultRole))
                .build());
    }
}
