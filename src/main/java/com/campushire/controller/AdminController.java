package com.campushire.controller;

import com.campushire.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.campushire.entity.Admin;
import com.campushire.service.AdminService;
import com.campushire.service.StudentService;
import com.campushire.service.CompanyService;
import com.campushire.service.ApplicationService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.campushire.dto.StudentsXmlWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;
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
    @PostMapping(
            value = "/students/upload-xml",
            consumes = {"multipart/form-data"}
    )
    public ResponseEntity<?> uploadStudentsXml(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(StudentsXmlWrapper.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StudentsXmlWrapper wrapper =
                    (StudentsXmlWrapper) unmarshaller.unmarshal(file.getInputStream());

            if (wrapper == null || wrapper.getStudents() == null || wrapper.getStudents().isEmpty()) {
                return ResponseEntity.badRequest().body("No student data found in XML");
            }

            List<Student> saved = studentService.saveAllStudents(wrapper.getStudents());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process XML: " + e.getMessage());
        }
    }

    @PostMapping(
            value = "/students/upload-csv",
            consumes = {"multipart/form-data"}
    )
    public ResponseEntity<?> uploadStudentsCsv(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isHeader = true;
            List<Student> students = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // skip header row
                    continue;
                }
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 10) continue;

                Student s = new Student();
                s.setFirstName(parts[0].trim());                      // firstName
                s.setLastName(parts[1].trim());                       // lastName
                s.setEmail(parts[2].trim());                          // email
                s.setPasswordHash(parts[3].trim());                   // passwordHash
                s.setDepartment(parts[4].trim());                     // department
                s.setCgpa(parts[5].isEmpty() ? null
                        : Double.valueOf(parts[5].trim()));           // cgpa
                s.setPhone(parts[6].trim());                          // phone
                s.setYearOfStudy(parts[7].isEmpty() ? null
                        : Integer.valueOf(parts[7].trim()));          // yearOfStu
                s.setSkills(parts[8].trim());                         // skills
                s.setResumeUrl(parts[9].trim());                      // resumeUrl

                students.add(s);
            }

            if (students.isEmpty()) {
                return ResponseEntity.badRequest().body("No valid student data found in CSV");
            }

            List<Student> saved = studentService.saveAllStudents(students);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process CSV: " + e.getMessage());
        }
    }


    // Bulk notifications endpoint and analytics would require Notification & Dashboard modules
}
