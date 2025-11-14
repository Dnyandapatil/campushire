package com.campushire.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.campushire.entity.Application;
import com.campushire.entity.Student;
import com.campushire.entity.Company;
import com.campushire.service.ApplicationService;
import com.campushire.service.StudentService;
import com.campushire.service.CompanyService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<?> createApplication(@RequestParam Long studentId, @RequestParam Long companyId) {
        Student student = studentService.getStudentById(studentId);
        Company company = companyService.getCompanyById(companyId);
        if (student == null || company == null) {
            return ResponseEntity.badRequest().body("Invalid student or company ID");
        }
        Application application = applicationService.createApplication(student, company);
        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        Application app = applicationService.getApplicationById(id);
        if (app != null) {
            return ResponseEntity.ok(app);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Application>> getApplicationsByStudent(@PathVariable Long studentId) {
        Student student = studentService.getStudentById(studentId);
        if (student == null) return ResponseEntity.notFound().build();
        List<Application> apps = applicationService.getApplicationsByStudent(student);
        return ResponseEntity.ok(apps);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Application>> getApplicationsByCompany(@PathVariable Long companyId) {
        Company company = companyService.getCompanyById(companyId);
        if (company == null) return ResponseEntity.notFound().build();
        List<Application> apps = applicationService.getApplicationsByCompany(company);
        return ResponseEntity.ok(apps);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateApplicationStatus(@PathVariable Long id, @RequestParam String status) {
        Application app = applicationService.updateApplicationStatus(id, status);
        if (app != null) {
            return ResponseEntity.ok(app);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {
        boolean deleted = applicationService.deleteApplication(id);
        if (deleted) {
            return ResponseEntity.ok("Application deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
