package com.intervi.resume.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intervi.resume.models.InterviewReport;
import com.intervi.resume.repository.InterviewreportRepository;
import com.intervi.resume.services.AIservices;

@RestController
@RequestMapping("/api/interview")
@CrossOrigin(origins = "http://localhost:5173")
public class InterviewReportController {
    
    @Autowired
    private AIservices aiServices;

    @Autowired
    private InterviewreportRepository interviewreportRepository;

    @PostMapping("/generate")
    public ResponseEntity<InterviewReport> generate(
        @RequestBody Map<String, String> body, @RequestAttribute("email") String email
    ) {
        String jobDescription = body.get("jobDescription");
        String selfDescription = body.get("selfDescription");

        InterviewReport report = aiServices.generateReport(jobDescription, selfDescription);

        report.setUserId(email);
        report.setJobDescription(jobDescription);
        report.setSelfDescription(selfDescription);
        interviewreportRepository.save(report);

        return ResponseEntity.ok(report);

    }

    @GetMapping("/all")
    public ResponseEntity<List<InterviewReport>> getAll(@RequestAttribute("email") String email) {
        List<InterviewReport> reports = interviewreportRepository
                .findByUserId(email);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewReport> getById(@PathVariable String id) {
        InterviewReport report = interviewreportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        return ResponseEntity.ok(report);
    }

}
