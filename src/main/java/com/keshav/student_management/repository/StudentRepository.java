package com.keshav.student_management.repository;

import com.keshav.student_management.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // 1. Derived Query Method: Check duplicate email
    boolean existsByEmail(String email);

    // 2. Derived Query Method: Check duplicate roll number
    boolean existsByRollNumber(String rollNumber);

    // 3. Derived Query Method: Fetch by roll number
    Optional<Student> findByRollNumber(String rollNumber);

    // 4. Custom JPQL with JOIN FETCH to fix N+1 Problem
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.id = :id")
    Optional<Student> findByIdWithCourses(@Param("id") Long id);
}
