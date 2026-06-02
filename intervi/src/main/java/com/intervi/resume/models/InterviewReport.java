package com.intervi.resume.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Document(collection = "interview_reports")
@JsonIgnoreProperties(ignoreUnknown = true) 
public class InterviewReport {

    @Id
    private String id;

    // which user owns this report
    private String userId;

    @NotBlank(message = "Job descrioption is required")
    private String jobDescription;

    private String Resume;

    private String selfDescription;

    @Min(value = 0)
    @Max(value = 100)
    private double matchScore;


    private List<QusetionAnswer> technicalQuestions;

    private List<Behavioral> behavioralQuestions;

    private List<SkillGap> skillGaps;

    private List<Preparation> preparationPlan;



    @Data
    public static class QusetionAnswer {
        @NotBlank(message = "Technical question is required")
        private String question;

        @NotBlank(message = "Intention is required")
        private String intention;

        @NotBlank(message = "Answer is required")
        private String answer;

    }

    @Data
    static class Behavioral {
        @NotBlank(message = "Technical question is required")
        private String question;

        @NotBlank(message = "Intention is required")
        private String intention;

        @NotBlank(message = "Answer is required")
        private String answer;
    }

    @Data
    public static class SkillGap {
        @NotBlank(message = "Skill is required")
        private String skill;

        @NotBlank(message = "Severity is required")
        private String severity; // low, medium ,hard

    }
    @Data
    public static class Preparation {
        @NotNull(message = "Day is required")
        private Integer day;

        @NotBlank(message = "focus is required")
        private String focus;

        @NotBlank(message = "Tasks is required")
        private List<String> tasks;
        
    }

}
