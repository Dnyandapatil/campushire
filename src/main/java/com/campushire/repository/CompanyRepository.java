package com.campushire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.campushire.entity.Company;
import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByStatus(String status);
    List<Company> findByEligibleBranchesContaining(String branch);
    List<Company> findByMinCgpaLessThanEqual(Double cgpa);
}
