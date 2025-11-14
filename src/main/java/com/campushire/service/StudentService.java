package com.campushire.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.campushire.entity.Student;
import com.campushire.repository.StudentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Student registerStudent(Student student) {
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        return student.orElse(null);
    }

    public Student getStudentByEmail(String email) {
        Optional<Student> student = studentRepository.findByEmail(email);
        return student.orElse(null);
    }

    public Student updateStudent(Long studentId, Student studentDetails) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            Student existingStudent = student.get();
            if (studentDetails.getFirstName() != null)
                existingStudent.setFirstName(studentDetails.getFirstName());
            if (studentDetails.getLastName() != null)
                existingStudent.setLastName(studentDetails.getLastName());
            if (studentDetails.getDepartment() != null)
                existingStudent.setDepartment(studentDetails.getDepartment());
            if (studentDetails.getCgpa() != null)
                existingStudent.setCgpa(studentDetails.getCgpa());
            if (studentDetails.getSkills() != null)
                existingStudent.setSkills(studentDetails.getSkills());
            if (studentDetails.getPhone() != null)
                existingStudent.setPhone(studentDetails.getPhone());
            if (studentDetails.getYearOfStudy() != null)
                existingStudent.setYearOfStudy(studentDetails.getYearOfStudy());

            existingStudent.setUpdatedAt(LocalDateTime.now());
            return studentRepository.save(existingStudent);
        }
        return null;
    }

    public Student uploadResume(Long studentId, String resumeUrl) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            Student existingStudent = student.get();
            existingStudent.setResumeUrl(resumeUrl);
            existingStudent.setUpdatedAt(LocalDateTime.now());
            return studentRepository.save(existingStudent);
        }
        return null;
    }

    public boolean deleteStudent(Long studentId) {
        if (studentRepository.existsById(studentId)) {
            studentRepository.deleteById(studentId);
            return true;
        }
        return false;
    }

    public List<Student> getStudentsByDepartment(String department) {
        return studentRepository.findByDepartment(department);
    }

    public List<Student> getStudentsEligibleForCompany(Double minCgpa, List<String> departments) {
        return studentRepository.findEligibleStudents(minCgpa, departments);
    }

    public List<Student> getTopStudents(int limit) {
        return studentRepository.findTopStudents(limit);
    }

    public boolean isEmailExists(String email) {
        return studentRepository.findByEmail(email).isPresent();
    }

    // Student login logic (plain hash compare for example only, add proper hashing for production)
    public Student login(String email, String password) {
        Student student = getStudentByEmail(email);
        if (student != null && student.getPasswordHash().equals(password)) {
            return student;
        }
        return null;
    }
    public List<Student> getStudentsByName(String name) {
        return studentRepository.findByFirstNameIgnoreCaseOrLastNameIgnoreCase(name, name);
    }

}
