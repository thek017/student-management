package com.keshav.student_management.controller;

import com.keshav.student_management.dto.request.CourseRequest;
import com.keshav.student_management.dto.response.CourseResponse;
import com.keshav.student_management.dto.response.PageResponse;
import com.keshav.student_management.service.CourseService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Courses", description = "APIs for managing Courses")
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // 1. POST /api/courses -> Create Course (201 Created)
    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseRequest request) {
        CourseResponse createdCourse = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    // 2. GET /api/courses/{id} -> Get Course By ID (200 OK)
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    // 3. GET /api/courses -> Get All Courses with Pagination (200 OK)
    @GetMapping
    public ResponseEntity<PageResponse<CourseResponse>> getAllCourses(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(courseService.getAllCourses(pageNo, pageSize, sortBy, sortDir));
    }

    // 4. PUT /api/courses/{id} -> Update Course (200 OK)
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {

        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    // 5. DELETE /api/courses/{id} -> Delete Course (204 No Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    // 6. DELETE /api/courses/{courseId}/students/{studentId} -> Remove Student from
    // Course (204 No Content)
    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<Void> removeStudentFromCourse(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {

        courseService.removeCourseFromStudents(courseId, studentId);
        return ResponseEntity.noContent().build();
    }
}