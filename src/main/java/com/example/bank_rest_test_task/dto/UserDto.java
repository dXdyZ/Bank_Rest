package com.example.bank_rest_test_task.dto;

import com.example.bank_rest_test_task.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private UserRole role;
    private List<CardDto> cards;
    private Boolean accountEnable;
    private Boolean accountLocked;
}
