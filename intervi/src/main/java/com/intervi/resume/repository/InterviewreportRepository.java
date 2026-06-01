package com.intervi.resume.repository;

import com.intervi.resume.models.InterviewReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InterviewreportRepository extends MongoRepository<InterviewReport, String> {
    List<InterviewReport> findByUserId(String userId);
}