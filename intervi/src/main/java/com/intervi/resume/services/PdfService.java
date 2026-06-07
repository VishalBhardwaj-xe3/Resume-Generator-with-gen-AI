package com.intervi.resume.services;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.intervi.resume.models.InterviewReport;
import com.intervi.resume.models.User;
import com.intervi.resume.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AIservices aiServices;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public byte[] generateResumePdf(InterviewReport report) {
        try {
            // get user
            User user = userRepository.findByEmail(report.getUserId()).orElse(null);
            String name = user != null ? user.getUsername() : "Candidate";
            String email = user != null ? user.getEmail() : "";

            // ask Gemini to extract resume data from selfDescription
            String geminiResponse = aiServices.generateResumeData(
                    report.getSelfDescription(),
                    report.getJobDescription(),
                    report.getResume()
            );

            // parse Gemini response
            JsonNode root = objectMapper.readTree(geminiResponse);
            String text = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();
            text = text.replaceAll("```json", "").replaceAll("```", "").trim();
            JsonNode resumeData = objectMapper.readTree(text);

            // build HTML
            String html = buildResumeHtml(name, email, resumeData, report);

            // convert to PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage());
        }
    }

    private String buildResumeHtml(String name, String email,
                                    JsonNode resumeData,
                                    InterviewReport report) {
        StringBuilder html = new StringBuilder();

        html.append("""
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
                    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
                <html xmlns="http://www.w3.org/1999/xhtml">
                <head>
                <meta charset="UTF-8"/>
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; }
                    body {
                        font-family: Arial, sans-serif;
                        font-size: 11px;
                        color: #222;
                        padding: 40px 50px;
                        line-height: 1.5;
                    }
                    .header {
                        text-align: center;
                        border-bottom: 2px solid #111;
                        padding-bottom: 10px;
                        margin-bottom: 16px;
                    }
                    .name {
                        font-size: 26px;
                        font-weight: bold;
                        text-transform: uppercase;
                        letter-spacing: 3px;
                    }
                    .contact {
                        font-size: 10px;
                        color: #555;
                        margin-top: 4px;
                    }
                    .section {
                        margin-bottom: 14px;
                    }
                    .section-title {
                        font-size: 12px;
                        font-weight: bold;
                        text-transform: uppercase;
                        letter-spacing: 1px;
                        border-bottom: 1px solid #bbb;
                        padding-bottom: 3px;
                        margin-bottom: 8px;
                    }
                    .summary { font-size: 11px; color: #333; line-height: 1.6; }
                    .skills-wrap { }
                    .skill {
                        display: inline-block;
                        background: #f0f0f0;
                        border: 1px solid #ddd;
                        padding: 2px 8px;
                        margin: 2px 3px 2px 0;
                        font-size: 10px;
                        border-radius: 2px;
                    }
                    .exp-item { margin-bottom: 10px; }
                    .exp-header {
                        display: block;
                        font-weight: bold;
                        font-size: 11px;
                    }
                    .exp-company {
                        font-size: 10px;
                        color: #555;
                        margin-bottom: 4px;
                    }
                    ul.points {
                        margin-left: 16px;
                        margin-top: 3px;
                    }
                    ul.points li {
                        font-size: 10px;
                        color: #444;
                        margin-bottom: 2px;
                    }
                    .edu-item { margin-bottom: 6px; }
                    .edu-degree { font-weight: bold; font-size: 11px; }
                    .edu-school { font-size: 10px; color: #555; }
                    .proj-item { margin-bottom: 8px; }
                    .proj-name { font-weight: bold; font-size: 11px; }
                    .proj-desc { font-size: 10px; color: #444; margin: 2px 0; }
                    .proj-tech { font-size: 10px; color: #666; font-style: italic; }
                    .footer {
                        margin-top: 20px;
                        text-align: center;
                        font-size: 9px;
                        color: #aaa;
                        border-top: 1px solid #eee;
                        padding-top: 6px;
                    }
                </style>
                </head>
                <body>
                """);

        // ── Header ──
        html.append("<div class='header'>")
            .append("<div class='name'>").append(sanitize(name)).append("</div>")
            .append("<div class='contact'>").append(sanitize(email)).append("</div>")
            .append("</div>");

        // ── Summary ──
        String summary = resumeData.path("summary").asText("");
        if (!summary.isEmpty()) {
            html.append("<div class='section'>")
                .append("<div class='section-title'>Professional Summary</div>")
                .append("<p class='summary'>").append(sanitize(summary)).append("</p>")
                .append("</div>");
        }

        // ── Skills ──
        JsonNode skills = resumeData.path("skills");
        if (skills.isArray() && skills.size() > 0) {
            html.append("<div class='section'>")
                .append("<div class='section-title'>Technical Skills</div>")
                .append("<div class='skills-wrap'>");
            for (JsonNode skill : skills) {
                html.append("<span class='skill'>")
                    .append(sanitize(skill.asText()))
                    .append("</span>");
            }
            html.append("</div></div>");
        }

        // ── Experience ──
        JsonNode experience = resumeData.path("experience");
        if (experience.isArray() && experience.size() > 0) {
            html.append("<div class='section'>")
                .append("<div class='section-title'>Work Experience</div>");
            for (JsonNode exp : experience) {
                html.append("<div class='exp-item'>")
                    .append("<span class='exp-header'>")
                    .append(sanitize(exp.path("title").asText()))
                    .append("</span>")
                    .append("<div class='exp-company'>")
                    .append(sanitize(exp.path("company").asText()))
                    .append(" | ")
                    .append(sanitize(exp.path("duration").asText()))
                    .append("</div>")
                    .append("<ul class='points'>");
                for (JsonNode point : exp.path("points")) {
                    html.append("<li>").append(sanitize(point.asText())).append("</li>");
                }
                html.append("</ul></div>");
            }
            html.append("</div>");
        }

        // ── Education ──
        JsonNode education = resumeData.path("education");
        if (education.isArray() && education.size() > 0) {
            html.append("<div class='section'>")
                .append("<div class='section-title'>Education</div>");
            for (JsonNode edu : education) {
                html.append("<div class='edu-item'>")
                    .append("<div class='edu-degree'>")
                    .append(sanitize(edu.path("degree").asText()))
                    .append("</div>")
                    .append("<div class='edu-school'>")
                    .append(sanitize(edu.path("institution").asText()))
                    .append(" — ")
                    .append(sanitize(edu.path("year").asText()))
                    .append("</div>")
                    .append("</div>");
            }
            html.append("</div>");
        }

        // ── Projects ──
        JsonNode projects = resumeData.path("projects");
        if (projects.isArray() && projects.size() > 0) {
            html.append("<div class='section'>")
                .append("<div class='section-title'>Projects</div>");
            for (JsonNode proj : projects) {
                html.append("<div class='proj-item'>")
                    .append("<div class='proj-name'>")
                    .append(sanitize(proj.path("name").asText()))
                    .append("</div>")
                    .append("<div class='proj-desc'>")
                    .append(sanitize(proj.path("description").asText()))
                    .append("</div>")
                    .append("<div class='proj-tech'>Technologies: ");
                for (JsonNode tech : proj.path("technologies")) {
                    html.append(sanitize(tech.asText())).append(", ");
                }
                html.append("</div></div>");
            }
            html.append("</div>");
        }

        // ── Footer ──
        html.append("<div class='footer'>Generated by Interview Preparation Assistant</div>");
        html.append("</body></html>");

        return html.toString();
    }

    private String sanitize(String text) {
        if (text == null) return "";
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
}