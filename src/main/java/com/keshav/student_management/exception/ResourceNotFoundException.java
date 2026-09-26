package com.keshav.student_management.exception;

import org.springframework.http.HttpStatus;

// Jab koi Student/Course find nahi hota → 404
public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " not found with id: " + id, HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
