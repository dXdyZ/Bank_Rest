package com.example.bank_rest_test_task.service;

import com.example.bank_rest_test_task.entity.User;
import com.example.bank_rest_test_task.exception.UserNotFoundException;
import com.example.bank_rest_test_task.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
