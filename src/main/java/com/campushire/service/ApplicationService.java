package com.campushire.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.campushire.entity.Application;
import com.campushire.entity.Student;
import com.campushire.entity.Company;
import com.campushire.repository.ApplicationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;

    public Application createApplication(Student student, Company company) {
        Application app = new Application(student, company, "Applied");
        app.setAppliedDate(LocalDateTime.now());
        return applicationRepository.save(app);
    }

    public Application getApplicationById(Long applicationId) {
        Optional<Application> app = applicationRepository.findById(applicationId);
        return app.orElse(null);
    }

    public List<Application> getApplicationsByStudent(Student student) {
        return applicationRepository.findByStudent(student);
    }

    public List<Application> getApplicationsByCompany(Company company) {
        return applicationRepository.findByCompany(company);
    }

    public Application updateApplicationStatus(Long applicationId, String status) {
        Optional<Application> app = applicationRepository.findById(applicationId);
        if (app.isPresent()) {
            Application existing = app.get();
            existing.setStatus(status);
            return applicationRepository.save(existing);
        }
        return null;
    }

    public boolean deleteApplication(Long applicationId) {
        if (applicationRepository.existsById(applicationId)) {
            applicationRepository.deleteById(applicationId);
            return true;
        }
        return false;
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }
}
