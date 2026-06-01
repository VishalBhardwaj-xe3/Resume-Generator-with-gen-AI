package com.intervi.resume.services;

import org.springframework.http.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.intervi.resume.models.InterviewReport;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;



@Service
public class AIservices {
    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTamplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public InterviewReport generateReport(String jobDescription, String selfDescription) {
        String prompt = buildPrompt(jobDescription, selfDescription);

        String jsonResponse = callGemini(prompt);

        return parseResponse(jsonResponse);

    }

    private String buildPrompt(String jobDescription, String selfDescription) {
        return """
                You are an expert career coach and interview preparation assistant.

                Analyze the following and generate a detailed interview preparation report.

                Job Description:
                """ + jobDescription + """

                Candidate Background:
                """ + selfDescription + """

                Return ONLY a valid JSON object with exactly this structure, no extra text:
                {
                    "matchScore": 75.0,
                    "technicalQuestions": [
                        {
                            "question": "question here",
                            "intention": "why interviewer asks this",
                            "answer": "ideal answer here"
                        }
                    ],
                    "behavioralQuestions": [
                        {
                            "question": "question here",
                            "intention": "why interviewer asks this",
                            "answer": "ideal answer here"
                        }
                    ],
                    "skillGaps": [
                        {
                            "skill": "skill name",
                            "severity": "low/medium/high"
                        }
                    ],
                    "preparationPlan": [
                        {
                            "day": 1,
                            "focus": "focus area",
                            "tasks": ["task 1", "task 2"]
                        }
                    ]
                }
                """;
    }
    
    private String callGemini(String prompt) {
        Map<String, Object> parts = new HashMap<>();
        parts.put("text", prompt);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(parts));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        String urlWithKey = apiUrl + "?key=" + apiKey;
        System.out.println("Gemini URL: " + urlWithKey);
        ResponseEntity<String> response = restTamplate.postForEntity(urlWithKey, request, String.class);

        return response.getBody();

    }
    
    private InterviewReport parseResponse(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            String text = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asString();

            text = text.replaceAll("```json", "").replaceAll("```", "").trim();

            return objectMapper.readValue(text, InterviewReport.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response: " + e.getMessage());
        }
    }


}
