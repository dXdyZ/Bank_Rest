package com.example.bank_rest_test_task.dto;

import com.example.bank_rest_test_task.entity.StatusCard;

public record UpdateStatusCardDto (
        StatusCard newStatus
){}
