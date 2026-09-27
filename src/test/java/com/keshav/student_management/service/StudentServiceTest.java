package com.keshav.student_management.service;

import com.keshav.student_management.dto.request.StudentRequest;
import com.keshav.student_management.dto.response.StudentResponse;
import com.keshav.student_management.entity.Student;
import com.keshav.student_management.exception.DuplicateResourceException;
import com.keshav.student_management.exception.ResourceNotFoundException;
import com.keshav.student_management.repository.CourseRepository;
import com.keshav.student_management.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    @DisplayName("Should create student successfully")
    void createStudent_Success() {
        // ARRANGE
        StudentRequest request = StudentRequest.builder()
                .fullName("Keshav Sharma")
                .email("keshav@gmail.com")
                .department("Computer Science")
                .rollNumber("CS-001")
                .build();

        Student savedStudent = Student.builder()
                .id(1L)
                .fullName("Keshav Sharma")
                .email("keshav@gmail.com")
                .department("Computer Science")
                .rollNumber("CS-001")
                .courses(new HashSet<>())
                .build();

        when(studentRepository.existsByEmail("keshav@gmail.com")).thenReturn(false);
        when(studentRepository.existsByRollNumber("CS-001")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        // ACT
        StudentResponse response = studentService.createStudent(request);

        // ASSERT
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Keshav Sharma", response.getFullName());
        assertEquals("keshav@gmail.com", response.getEmail());

        verify(studentRepository, times(1)).existsByEmail("keshav@gmail.com");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when email already exists")
    void createStudent_DuplicateEmail_ThrowsException() {
        // ARRANGE
        StudentRequest request = StudentRequest.builder()
                .fullName("Keshav Sharma")
                .email("duplicate@gmail.com")
                .department("Computer Science")
                .rollNumber("CS-001")
                .build();

        when(studentRepository.existsByEmail("duplicate@gmail.com")).thenReturn(true);

        // ACT & ASSERT
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> studentService.createStudent(request)
        );

        assertTrue(exception.getMessage().contains("email already exists"));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Should return StudentResponse when student exists by ID")
    void getStudentById_Success() {
        // ARRANGE
        Student mockStudent = Student.builder()
                .id(1L)
                .fullName("Keshav Sharma")
                .email("keshav@gmail.com")
                .department("Computer Science")
                .rollNumber("CS-001")
                .courses(new HashSet<>())
                .build();

        when(studentRepository.findByIdWithCourses(1L)).thenReturn(Optional.of(mockStudent));

        // ACT
        StudentResponse response = studentService.getStudentById(1L);

        // ASSERT
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Keshav Sharma", response.getFullName());

        verify(studentRepository, times(1)).findByIdWithCourses(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when student ID not found")
    void getStudentById_NotFound_ThrowsException() {
        // ARRANGE
        when(studentRepository.findByIdWithCourses(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> studentService.getStudentById(99L)
        );

        assertEquals("Student not found with id: 99", exception.getMessage());
        verify(studentRepository, times(1)).findByIdWithCourses(99L);
    }

    @Test
    @DisplayName("Should delete student when ID exists")
    void deleteStudent_Success() {
        // ARRANGE
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        // ACT
        studentService.deleteStudent(1L);

        // ASSERT
        verify(studentRepository, times(1)).existsById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }
}
