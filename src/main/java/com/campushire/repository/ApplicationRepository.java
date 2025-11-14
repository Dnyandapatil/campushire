package com.campushire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.campushire.entity.Application;
import com.campushire.entity.Student;
import com.campushire.entity.Company;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStudent(Student student);
    List<Application> findByCompany(Company company);
}
