package com.keshav.student_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false, unique = true)
    private String title;
    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "credits", nullable = false)
    private int credits;
}
