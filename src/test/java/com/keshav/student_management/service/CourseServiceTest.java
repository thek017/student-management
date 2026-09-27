package com.keshav.student_management.service;

import com.keshav.student_management.dto.request.CourseRequest;
import com.keshav.student_management.dto.response.CourseResponse;
import com.keshav.student_management.entity.Course;
import com.keshav.student_management.exception.DuplicateResourceException;
import com.keshav.student_management.exception.ResourceNotFoundException;
import com.keshav.student_management.repository.CourseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    @DisplayName("Should create course successfully")
    void createCourse_Success() {
        // ARRANGE
        CourseRequest request = CourseRequest.builder()
                .courseCode("CS101")
                .title("Data Structures & Algorithms")
                .description("Learn DSA")
                .credits(4)
                .build();

        Course savedCourse = Course.builder()
                .id(1L)
                .courseCode("CS101")
                .title("Data Structures & Algorithms")
                .description("Learn DSA")
                .credits(4)
                .build();

        when(courseRepository.existsByCourseCode("CS101")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        // ACT
        CourseResponse response = courseService.createCourse(request);

        // ASSERT
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("CS101", response.getCourseCode());
        assertEquals("Data Structures & Algorithms", response.getTitle());

        verify(courseRepository, times(1)).existsByCourseCode("CS101");
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when course code already exists")
    void createCourse_DuplicateCode_ThrowsException() {
        // ARRANGE
        CourseRequest request = CourseRequest.builder()
                .courseCode("CS101")
                .title("Data Structures")
                .credits(4)
                .build();

        when(courseRepository.existsByCourseCode("CS101")).thenReturn(true);

        // ACT & ASSERT
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> courseService.createCourse(request)
        );

        assertTrue(exception.getMessage().contains("courseCode already exists") || exception.getMessage().contains("Course with code already exists"));
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    @DisplayName("Should return CourseResponse when course exists by ID")
    void getCourseById_Success() {
        // ARRANGE
        Course mockCourse = Course.builder()
                .id(1L)
                .courseCode("CS101")
                .title("Data Structures")
                .credits(4)
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));

        // ACT
        CourseResponse response = courseService.getCourseById(1L);

        // ASSERT
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("CS101", response.getCourseCode());

        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when course ID not found")
    void getCourseById_NotFound_ThrowsException() {
        // ARRANGE
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.getCourseById(99L)
        );

        assertEquals("Course not found with id: 99", exception.getMessage());
        verify(courseRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Should delete course when ID exists")
    void deleteCourse_Success() {
        // ARRANGE
        when(courseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(1L);

        // ACT
        courseService.deleteCourse(1L);

        // ASSERT
        verify(courseRepository, times(1)).existsById(1L);
        verify(courseRepository, times(1)).deleteById(1L);
    }
}
