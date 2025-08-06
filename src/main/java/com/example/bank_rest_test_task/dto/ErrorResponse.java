package com.example.bank_rest_test_task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private OffsetDateTime timestamp;
    private String message;
    private Integer code;
    private List<ValidationError> validationErrors;


    @Data
    @Builder
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
    }
}
