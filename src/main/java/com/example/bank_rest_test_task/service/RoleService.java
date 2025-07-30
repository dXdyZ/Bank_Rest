package com.example.bank_rest_test_task.service;

import com.example.bank_rest_test_task.entity.Role;
import com.example.bank_rest_test_task.entity.RoleName;
import com.example.bank_rest_test_task.exception.RoleNotFoundException;
import com.example.bank_rest_test_task.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findRoleByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName).orElseThrow(
                () -> new RoleNotFoundException("Role by name: %s not found".formatted(roleName.name()))
        );
    }
}
