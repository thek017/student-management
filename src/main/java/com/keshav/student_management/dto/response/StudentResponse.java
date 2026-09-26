package com.keshav.student_management.dto.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {

    private Long id;
    private String fullName;
    private String email;
    private String department;
    private String rollNumber;

    // Relationship representation: Set of CourseResponse DTOs (No Circular Loop!)
    private Set<CourseResponse> courses;
}