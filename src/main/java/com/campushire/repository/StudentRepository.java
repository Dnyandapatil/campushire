package com.campushire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.campushire.entity.Student;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    List<Student> findByDepartment(String department);
    List<Student> findByCgpaGreaterThanEqual(Double cgpa);
    List<Student> findByFirstNameIgnoreCaseOrLastNameIgnoreCase(String firstName, String lastName);

    @Query("SELECT s FROM Student s WHERE s.cgpa >= :minCgpa AND s.department IN :departments")
    List<Student> findEligibleStudents(@Param("minCgpa") Double minCgpa, @Param("departments") List<String> departments);

    @Query(value = "SELECT * FROM students ORDER BY cgpa DESC LIMIT :limit", nativeQuery = true)
    List<Student> findTopStudents(@Param("limit") int limit);
}
