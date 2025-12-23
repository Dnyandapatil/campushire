package com.campushire.service;

import com.campushire.entity.InterviewExperience;
import com.campushire.entity.Student;
import com.campushire.repository.InterviewExperienceRepository;
import com.campushire.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewExperienceService {

    @Autowired
    private InterviewExperienceRepository interviewExperienceRepository;

    @Autowired
    private StudentRepository studentRepository;

    public InterviewExperience createExperience(InterviewExperience dto, Long studentId) {
        if (studentId != null) {
            Student student = studentRepository.findById(studentId).orElse(null);
            dto.setStudent(student);
        }
        return interviewExperienceRepository.save(dto);
    }

    public List<InterviewExperience> getAll() {
        return interviewExperienceRepository.findAll();
    }

    public InterviewExperience getById(Long id) {
        return interviewExperienceRepository.findById(id).orElse(null);
    }

    public List<InterviewExperience> getByCompany(String companyName) {
        return interviewExperienceRepository.findByCompanyNameIgnoreCaseOrderByCreatedAtDesc(companyName);
    }

    public List<InterviewExperience> getByBatch(String batch) {
        return interviewExperienceRepository.findByPassingBatchOrderByCreatedAtDesc(batch);
    }

    public List<InterviewExperience> getByDifficulty(String level) {
        return interviewExperienceRepository.findByDifficultyLevelIgnoreCaseOrderByCreatedAtDesc(level);
    }

    public boolean deleteById(Long id) {
        if (interviewExperienceRepository.existsById(id)) {
            interviewExperienceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
