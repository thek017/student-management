package com.keshav.student_management.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Roll number is required")
    private String rollNumber;

    // Optional: Student create karte time Course IDs pass kar sakte hain
    private Set<Long> courseIds;
}