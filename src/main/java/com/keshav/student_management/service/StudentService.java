package com.keshav.student_management.service;

import com.keshav.student_management.dto.request.StudentRequest;
import com.keshav.student_management.dto.response.CourseResponse;
import com.keshav.student_management.dto.response.PageResponse;
import com.keshav.student_management.dto.response.StudentResponse;
import com.keshav.student_management.entity.Course;
import com.keshav.student_management.entity.Student;
import com.keshav.student_management.exception.DuplicateResourceException;
import com.keshav.student_management.exception.ResourceNotFoundException;
import com.keshav.student_management.repository.CourseRepository;
import com.keshav.student_management.repository.StudentRepository;
import com.keshav.student_management.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    // 1. Create Student
    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Student with email already exists: " + request.getEmail());
        }
        if (studentRepository.existsByRollNumber(request.getRollNumber())) {
            throw new DuplicateResourceException("Student with roll number already exists: " + request.getRollNumber());
        }

        Student student = Student.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .department(request.getDepartment())
                .rollNumber(request.getRollNumber())
                .courses(new HashSet<>())
                .build();

        if (request.getCourseIds() != null && !request.getCourseIds().isEmpty()) {
            List<Course> courses = courseRepository.findAllById(request.getCourseIds());
            student.getCourses().addAll(courses);
        }

        Student savedStudent = studentRepository.save(student);
        return mapToStudentResponse(savedStudent);
    }

    // 2. Get Student by ID
    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findByIdWithCourses(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
        return mapToStudentResponse(student);
    }

    // 3. Get All Students (Paginated & Sorted using PageUtils)
    @Transactional(readOnly = true)
    public PageResponse<StudentResponse> getAllStudents(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = PageUtils.createPageable(pageNo, pageSize, sortBy, sortDir);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return PageUtils.toPageResponse(studentPage, this::mapToStudentResponse);
    }

    // 4. Update Student
    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));

        if (!student.getEmail().equalsIgnoreCase(request.getEmail()) &&
                studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
        }

        if (!student.getRollNumber().equalsIgnoreCase(request.getRollNumber()) &&
                studentRepository.existsByRollNumber(request.getRollNumber())) {
            throw new DuplicateResourceException("Roll number already in use: " + request.getRollNumber());
        }

        student.setFullName(request.getFullName());
        student.setEmail(request.getEmail());
        student.setDepartment(request.getDepartment());
        student.setRollNumber(request.getRollNumber());

        Student updatedStudent = studentRepository.save(student);
        return mapToStudentResponse(updatedStudent);
    }

    // 5. Delete Student
    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student", id);
        }
        studentRepository.deleteById(id);
    }

    // 6. Enroll Student in Course
    @Transactional
    public StudentResponse enrollStudentInCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findByIdWithCourses(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));

        student.getCourses().add(course);
        Student updatedStudent = studentRepository.save(student);
        return mapToStudentResponse(updatedStudent);
    }

    // 7. Unenroll Student from Course
    @Transactional
    public StudentResponse unenrollStudentFromCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findByIdWithCourses(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));

        student.getCourses().remove(course);
        Student updatedStudent = studentRepository.save(student);
        return mapToStudentResponse(updatedStudent);
    }

    // Helper: Map Student Entity to StudentResponse DTO
    private StudentResponse mapToStudentResponse(Student student) {
        Set<CourseResponse> courseResponses = student.getCourses() == null ? new HashSet<>() :
                student.getCourses().stream()
                        .map(course -> CourseResponse.builder()
                                .id(course.getId())
                                .courseCode(course.getCourseCode())
                                .title(course.getTitle())
                                .description(course.getDescription())
                                .credits(course.getCredits())
                                .build())
                        .collect(Collectors.toSet());

        return StudentResponse.builder()
                .id(student.getId())
                .fullName(student.getFullName())
                .email(student.getEmail())
                .department(student.getDepartment())
                .rollNumber(student.getRollNumber())
                .courses(courseResponses)
                .build();
    }
}
