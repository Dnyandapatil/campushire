package com.campushire.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    private EmailService emailService;

    @Autowired private JavaMailSender javaMailSender;
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

    public Application updateApplicationStatus(Long id, String status) {
        Application app = applicationRepository.findById(id).orElse(null);
        if (app != null) {
            String oldStatus = app.getStatus();
            app.setStatus(status);
            app = applicationRepository.save(app);

            System.out.println("🔥 STATUS CHANGED: " + id + " [" + oldStatus + " → " + status + "]");

            // Send email for ANY status change using YOUR EmailService
            sendStatusEmail(app, status);

            return app;
        }
        return null;
    }

    private void sendStatusEmail(Application app, String status) {
        try {
            Student student = app.getStudent();
            Company company = app.getCompany();

            if (student != null && company != null) {
                System.out.println("🚀 Sending " + status + " email to: " + student.getEmail());

                // USE YOUR EXISTING EmailService method
                emailService.sendStatusUpdateMail(
                        student.getEmail(),
                        student.getFirstName() + " " + student.getLastName(),
                        company.getName(),
                        status.toUpperCase()
                );

                System.out.println("✅ EMAIL SENT SUCCESSFULLY using EmailService!");
            }
        } catch (Exception e) {
            System.err.println("❌ EMAIL FAILED: " + e.getMessage());
        }
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
