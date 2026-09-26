package com.keshav.student_management.exception;

import org.springframework.http.HttpStatus;

// Jab email already exist karta ho → 409 Conflict
public class DuplicateResourceException extends AppException {

    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
