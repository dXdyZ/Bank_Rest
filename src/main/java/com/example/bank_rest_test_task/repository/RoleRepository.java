package com.example.bank_rest_test_task.repository;

import com.example.bank_rest_test_task.entity.Role;
import com.example.bank_rest_test_task.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}
