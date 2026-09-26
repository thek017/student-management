package com.keshav.student_management.service;

import com.keshav.student_management.dto.request.CourseRequest;
import com.keshav.student_management.dto.response.CourseResponse;
import com.keshav.student_management.dto.response.PageResponse;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    // 1. Create Course
    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new DuplicateResourceException("Course with code already exists: " + request.getCourseCode());
        }

        Course course = Course.builder()
                .courseCode(request.getCourseCode())
                .title(request.getTitle())
                .description(request.getDescription())
                .credits(request.getCredits())
                .build();

        Course savedCourse = courseRepository.save(course);
        return mapToCourseResponse(savedCourse);
    }

    // 2. Get Course By ID
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
        return mapToCourseResponse(course);
    }

    // 3. Get All Courses
    @Transactional(readOnly = true)
    public PageResponse<CourseResponse> getAllCourses(int pageNo, int pageSize, String sortBy, String sortDir) {
        Pageable pageable = PageUtils.createPageable(pageNo, pageSize, sortBy, sortDir);
        Page<Course> coursePage = courseRepository.findAll(pageable);
        return PageUtils.toPageResponse(coursePage, this::mapToCourseResponse);
    }

    // 4. Update Course
    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));

        if (!course.getCourseCode().equalsIgnoreCase(request.getCourseCode()) &&
                courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new DuplicateResourceException("Course code already exists: " + request.getCourseCode());
        }

        course.setCourseCode(request.getCourseCode());
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());

        Course updatedCourse = courseRepository.save(course);
        return mapToCourseResponse(updatedCourse);
    }

    // 5. Delete Course
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course", id);
        }
        courseRepository.deleteById(id);
    }

    // 6. Remove Course from Student
    @Transactional
    public void removeCourseFromStudents(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));

        student.getCourses().remove(course);
        studentRepository.save(student);
    }

    // Helper Mapper
    private CourseResponse mapToCourseResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .title(course.getTitle())
                .description(course.getDescription())
                .credits(course.getCredits())
                .build();
    }
}
