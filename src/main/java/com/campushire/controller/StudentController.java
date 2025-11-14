package com.campushire.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.campushire.entity.Student;
import com.campushire.service.StudentService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody Student student) {
        if (studentService.isEmailExists(student.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        Student registeredStudent = studentService.registerStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredStudent);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        Student student = studentService.login(email, password);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        // For JWT: Issue token here, for now just return profile
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        if (student != null) {
            return ResponseEntity.ok(student);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getStudentByEmail(@PathVariable String email) {
        Student student = studentService.getStudentByEmail(email);
        if (student != null) {
            return ResponseEntity.ok(student);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        Student updatedStudent = studentService.updateStudent(id, studentDetails);
        if (updatedStudent != null) {
            return ResponseEntity.ok(updatedStudent);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<?> uploadResume(@PathVariable Long id, @RequestParam String resumeUrl) {
        Student student = studentService.uploadResume(id, resumeUrl);
        if (student != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Resume uploaded successfully");
            response.put("resumeUrl", resumeUrl);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        boolean deleted = studentService.deleteStudent(id);
        if (deleted) {
            return ResponseEntity.ok("Student deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<Student>> getStudentsByDepartment(@PathVariable String department) {
        List<Student> students = studentService.getStudentsByDepartment(department);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/eligible-for-company")
    public ResponseEntity<List<Student>> getEligibleStudents(
            @RequestParam Double minCgpa,
            @RequestParam List<String> departments) {
        List<Student> students = studentService.getStudentsEligibleForCompany(minCgpa, departments);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/top-students")
    public ResponseEntity<List<Student>> getTopStudents(@RequestParam(defaultValue = "10") int limit) {
        List<Student> students = studentService.getTopStudents(limit);
        return ResponseEntity.ok(students);
    }
}
