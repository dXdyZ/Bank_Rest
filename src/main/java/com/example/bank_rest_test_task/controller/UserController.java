package com.example.bank_rest_test_task.controller;

import com.example.bank_rest_test_task.dto.UserDto;
import com.example.bank_rest_test_task.dto.UserRegisterDto;
import com.example.bank_rest_test_task.dto.UserRoleUpdateDto;
import com.example.bank_rest_test_task.dto.UsernameUpdateDto;
import com.example.bank_rest_test_task.service.UserService;
import com.example.bank_rest_test_task.util.factory.UserDtoFactory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserDtoFactory userDtoFactory;

    public UserController(UserService userService, UserDtoFactory userDtoFactory) {
        this.userService = userService;
        this.userDtoFactory = userDtoFactory;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        userService.registrationUser(userRegisterDto);
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@Size(min = 3, max = 50,
            message = "Username must be between 3 and 100 characters long") @PathVariable String username) {
        return ResponseEntity.ok(userDtoFactory.createUserDtoAndCardDtoForAdmin(userService.findUserByUsername(username)));
    }

    @DeleteMapping("/by-username/{username}")
    public ResponseEntity<?> deleteUserByUsername(@Size(min = 3, max = 50,
            message = "Username must be between 3 and 100 characters long") @PathVariable String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@Positive(message = "Id must not be less than zero") @PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/username")
    public ResponseEntity<UserDto> updateUsername(@Positive(message = "Id must not be less than zero") @PathVariable Long userId,
                                            @Valid @RequestBody UsernameUpdateDto usernameUpdateDto) {
        return ResponseEntity.ok(
                userDtoFactory.createUserDto(
                        userService.updateUsername(usernameUpdateDto.newUsername(), userId)
                )
        );
    }

    @PatchMapping("/{userId}/role")
    public ResponseEntity<UserDto> updateRol(@Positive(message = "Id must not be less than zero") @PathVariable Long userId,
                                             @Valid @RequestBody UserRoleUpdateDto userRoleUpdateDto) {
        return ResponseEntity.ok(
                userDtoFactory.createUserDto(
                        userService.updateRole(userRoleUpdateDto.role(), userId)
                )
        );
    }
}
