package com.keshav.student_management.controller;

import com.keshav.student_management.dto.request.StudentRequest;
import com.keshav.student_management.dto.response.PageResponse;
import com.keshav.student_management.dto.response.StudentResponse;
import com.keshav.student_management.service.StudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Students", description = "APIs for managing Students")
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // POST /api/students -> Create
    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(request));
    }

    // GET /api/students/{id}
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // GET /api/students (paginated)
    @GetMapping
    public ResponseEntity<PageResponse<StudentResponse>> getAllStudents(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(studentService.getAllStudents(pageNo, pageSize, sortBy, sortDir));
    }

    // PUT /api/students/{id}
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    // DELETE /api/students/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // POST /api/students/{studentId}/enrollments/{courseId} -> Enroll
    @PostMapping("/{studentId}/enrollments/{courseId}")
    public ResponseEntity<StudentResponse> enrollStudentInCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.enrollStudentInCourse(studentId, courseId));
    }

    // DELETE /api/students/{studentId}/enrollments/{courseId} -> Unenroll
    @DeleteMapping("/{studentId}/enrollments/{courseId}")
    public ResponseEntity<StudentResponse> unenrollStudentFromCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.unenrollStudentFromCourse(studentId, courseId));
    }
}
