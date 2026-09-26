package com.keshav.student_management.exception;

import org.springframework.http.HttpStatus;

// RuntimeException extend kiya — unchecked
// Har jagah throws likhna nahi padega
// @Transactional bhi isko rollback karega ✅
public class AppException extends RuntimeException {

    private final HttpStatus status;

    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
