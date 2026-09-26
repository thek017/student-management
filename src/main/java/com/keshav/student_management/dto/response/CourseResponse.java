package com.keshav.student_management.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {

    private Long id;
    private String courseCode;
    private String title;
    private String description;
    private Integer credits;
}
