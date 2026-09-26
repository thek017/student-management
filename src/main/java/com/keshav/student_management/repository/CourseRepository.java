package com.keshav.student_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.keshav.student_management.entity.Course;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // 1. Derived Query Method: Check duplicate course code
    boolean existsByCourseCode(String courseCode);

    // find by course code
    Optional<Course> findByCourseCode(String courseCode);

    // 2. Derived Query Method: Check duplicate course title
    boolean existsByTitle(String title);

}
