package com.example.bank_rest_test_task.service;

import com.example.bank_rest_test_task.dto.UserRegisterDto;
import com.example.bank_rest_test_task.entity.User;
import com.example.bank_rest_test_task.entity.UserRole;
import com.example.bank_rest_test_task.exception.DuplicateUserException;
import com.example.bank_rest_test_task.exception.UserNotFoundException;
import com.example.bank_rest_test_task.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Boolean existUserByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User by name: %s not found".formatted(username))
        );
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User by name: %s not found".formatted(username)));
        userRepository.delete(user);
    }

    @Transactional
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User by id: %s not found".formatted(userId)));

        userRepository.delete(user);
    }

    @Transactional
    public User updateUsername(String newUsername, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User by id: %s not found".formatted(userId)));
        if (userRepository.existsByUsername(newUsername)) {
            throw new DuplicateUserException("User by name: %s already exists".formatted(newUsername));
        }
        user.setUsername(newUsername);
        return userRepository.save(user);
    }

    @Transactional
    public User updateRole(String roleName, Long userId) {
        String modRole = "ROLE_" + roleName.toUpperCase();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User by id: %s not found".formatted(userId)));
        user.setRole(UserRole.valueOf(modRole));
        return userRepository.save(user);
    }

    @Transactional
    public void registrationUser(UserRegisterDto userRegisterDto) {
        if (userRepository.existsByUsername(userRegisterDto.username())) {
            throw new DuplicateUserException("User by name: %s already exists".formatted(userRegisterDto.username()));
        }
        userRepository.save(User.builder()
                .username(userRegisterDto.username())
                .password(passwordEncoder.encode(userRegisterDto.password()))
                .role(UserRole.ROLE_USER)
                .build());
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id: %s not found".formatted(id)));
    }
}
