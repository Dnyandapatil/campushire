package com.campushire.repository;

import com.campushire.entity.InterviewExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewExperienceRepository extends JpaRepository<InterviewExperience, Long> {

    List<InterviewExperience> findByCompanyNameIgnoreCaseOrderByCreatedAtDesc(String companyName);

    List<InterviewExperience> findByPassingBatchOrderByCreatedAtDesc(String passingBatch);

    List<InterviewExperience> findByDifficultyLevelIgnoreCaseOrderByCreatedAtDesc(String difficultyLevel);
}
