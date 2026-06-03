package com.intervi.resume.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.intervi.resume.models.InterviewReport;
import com.intervi.resume.repository.InterviewreportRepository;
import com.intervi.resume.services.AIservices;
import com.intervi.resume.services.fileStrorageService;

@RestController
@RequestMapping("/api/interview")
@CrossOrigin(origins = "http://localhost:5173")
public class InterviewReportController {
    
    @Autowired
    private AIservices aiServices;

    @Autowired
    private InterviewreportRepository interviewreportRepository;

    @Autowired
    private fileStrorageService fileStrorageService;

    @PostMapping(value = "/generate", consumes = "multipart/form-data")
    public ResponseEntity<InterviewReport> generate(
            @RequestParam("jobDescription") String jobDescription,
            @RequestParam("selfDescription") String selfDescription,
            @RequestParam(value = "resume", required = false) MultipartFile resume,
            @RequestAttribute("email") String email
    ) {

        String resumePath = null;
        if (resume != null && !resume.isEmpty()) {
        resumePath = fileStrorageService.saveFile(resume);
    }

        InterviewReport report = aiServices.generateReport(jobDescription, selfDescription, resumePath);

        report.setUserId(email);
        report.setJobDescription(jobDescription);
        report.setSelfDescription(selfDescription);
        report.setResume(resumePath);
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
