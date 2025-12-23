package com.campushire.controller;

import com.campushire.entity.InterviewExperience;
import com.campushire.service.InterviewExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/experiences")
@CrossOrigin(origins = "*")
public class InterviewExperienceController {

    @Autowired
    private InterviewExperienceService experienceService;

    // Create new experience (senior adds it)
    @PostMapping
    public ResponseEntity<InterviewExperience> createExperience(
            @RequestBody InterviewExperience experience,
            @RequestParam(required = false) Long studentId) { // optional link to existing student
        InterviewExperience created = experienceService.createExperience(experience, studentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Get all experiences (for browsing)
    @GetMapping
    public ResponseEntity<List<InterviewExperience>> getAll() {
        return ResponseEntity.ok(experienceService.getAll());
    }

    // Get single experience by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        InterviewExperience exp = experienceService.getById(id);
        if (exp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(exp);
    }

    // Filter by company name
    @GetMapping("/company/{companyName}")
    public ResponseEntity<List<InterviewExperience>> getByCompany(@PathVariable String companyName) {
        return ResponseEntity.ok(experienceService.getByCompany(companyName));
    }

    // Filter by passing batch
    @GetMapping("/batch/{batch}")
    public ResponseEntity<List<InterviewExperience>> getByBatch(@PathVariable String batch) {
        return ResponseEntity.ok(experienceService.getByBatch(batch));
    }

    // Filter by difficulty level
    @GetMapping("/difficulty/{level}")
    public ResponseEntity<List<InterviewExperience>> getByDifficulty(@PathVariable String level) {
        return ResponseEntity.ok(experienceService.getByDifficulty(level));
    }

    // Delete an experience (admin-only ideally)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean deleted = experienceService.deleteById(id);
        if (deleted) {
            return ResponseEntity.ok("Experience deleted");
        }
        return ResponseEntity.notFound().build();
    }
}
