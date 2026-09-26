package com.keshav.student_management.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

// Client ko bhejne wala error format
// Consistent response structure ✅
@Getter
@Builder
public class ErrorResponse {
    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private Map<String, String> fieldErrors; // Validation errors ke liye
}
