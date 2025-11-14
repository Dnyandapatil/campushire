package com.campushire.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.campushire.entity.Admin;
import com.campushire.service.AdminService;
import com.campushire.service.StudentService;
import com.campushire.service.CompanyService;
import com.campushire.service.ApplicationService;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/register")
    public ResponseEntity<Admin> registerAdmin(@RequestBody Admin admin) {
        Admin created = adminService.registerAdmin(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        Admin admin = adminService.login(email, password);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid admin credentials");
        }
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/companies")
    public ResponseEntity<?> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/applications")
    public ResponseEntity<?> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @DeleteMapping("/student/{id}/delete")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        boolean deleted = studentService.deleteStudent(id);
        if (deleted) return ResponseEntity.ok("Student deleted successfully");
        return ResponseEntity.notFound().build();
    }

    // Bulk notifications endpoint and analytics would require Notification & Dashboard modules
}
