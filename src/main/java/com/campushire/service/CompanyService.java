package com.campushire.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.campushire.entity.Company;
import com.campushire.repository.CompanyRepository;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Company addCompany(Company company) {
        company.setStatus("Active");
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long companyId) {
        Optional<Company> company = companyRepository.findById(companyId);
        return company.orElse(null);
    }

    public Company updateCompany(Long companyId, Company companyDetails) {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isPresent()) {
            Company existingCompany = company.get();
            if (companyDetails.getName() != null)
                existingCompany.setName(companyDetails.getName());
            if (companyDetails.getEmail() != null)
                existingCompany.setEmail(companyDetails.getEmail());
            if (companyDetails.getContactNumber() != null)
                existingCompany.setContactNumber(companyDetails.getContactNumber());
            if (companyDetails.getRoleOffered() != null)
                existingCompany.setRoleOffered(companyDetails.getRoleOffered());
            if (companyDetails.getPackageOffered() != null)
                existingCompany.setPackageOffered(companyDetails.getPackageOffered());
            if (companyDetails.getMinCgpa() != null)
                existingCompany.setMinCgpa(companyDetails.getMinCgpa());
            if (companyDetails.getEligibleBranches() != null)
                existingCompany.setEligibleBranches(companyDetails.getEligibleBranches());
            if (companyDetails.getApplicationDeadline() != null)
                existingCompany.setApplicationDeadline(companyDetails.getApplicationDeadline());
            if (companyDetails.getStatus() != null)
                existingCompany.setStatus(companyDetails.getStatus());

            return companyRepository.save(existingCompany);
        }
        return null;
    }

    public boolean deleteCompany(Long companyId) {
        if (companyRepository.existsById(companyId)) {
            companyRepository.deleteById(companyId);
            return true;
        }
        return false;
    }

    public List<Company> getActiveCompanies() {
        return companyRepository.findByStatus("Active");
    }
}
