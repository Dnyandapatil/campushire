package com.campushire.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_experiences")
public class InterviewExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experienceId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;        // optional link to your Student entity

    private String authorName;      // senior name
    private String passingBatch;    // e.g. "2024"
    private String companyName;
    private String roleApplied;
    private String location;
    private String difficultyLevel; // EASY / MEDIUM / HARD

    @Column(columnDefinition = "TEXT")
    private String experienceText;

    @Column(columnDefinition = "TEXT")
    private String tips;

    private LocalDate interviewDate;
    private LocalDateTime createdAt;

    public InterviewExperience() {}

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getExperienceId() { return experienceId; }
    public void setExperienceId(Long experienceId) { this.experienceId = experienceId; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getPassingBatch() { return passingBatch; }
    public void setPassingBatch(String passingBatch) { this.passingBatch = passingBatch; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getRoleApplied() { return roleApplied; }
    public void setRoleApplied(String roleApplied) { this.roleApplied = roleApplied; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public String getExperienceText() { return experienceText; }
    public void setExperienceText(String experienceText) { this.experienceText = experienceText; }

    public String getTips() { return tips; }
    public void setTips(String tips) { this.tips = tips; }

    public LocalDate getInterviewDate() { return interviewDate; }
    public void setInterviewDate(LocalDate interviewDate) { this.interviewDate = interviewDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
