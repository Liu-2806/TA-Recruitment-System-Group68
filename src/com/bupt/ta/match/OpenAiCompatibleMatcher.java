package com.bupt.ta.match;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class OpenAiCompatibleMatcher {
    private static final Pattern SCORE_PATTERN = Pattern.compile("\"score\"\\s*:\\s*(\\d{1,3})");
    private static final Pattern EXPLANATION_PATTERN = Pattern.compile("\"explanation\"\\s*:\\s*\"(.*?)\"", Pattern.DOTALL);
    private static final Pattern CONTENT_PATTERN = Pattern.compile("\"content\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"", Pattern.DOTALL);
    private static final Pattern OUTPUT_TEXT_PATTERN = Pattern.compile("\"output_text\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"", Pattern.DOTALL);

    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build();
    private final String endpoint;
    private final String apiKey;
    private final String model;

    OpenAiCompatibleMatcher(String endpoint, String apiKey, String model) {
        this.endpoint = normalizeEndpoint(endpoint);
        this.apiKey = apiKey;
        this.model = model;
    }

    MatchResult analyze(Map<String, Object> resumeProfile, Map<String, Object> job) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(endpoint))
            .timeout(Duration.ofSeconds(60))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(buildRequestBody(resumeProfile, job)))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("LLM request failed with status " + response.statusCode() + ": " + response.body());
        }

        String payload = extractRelevantPayload(response.body());
        return new MatchResult(extractScore(payload), extractExplanation(payload), "api-llm");
    }

    private String buildRequestBody(Map<String, Object> resumeProfile, Map<String, Object> job) {
        String prompt = "Evaluate the suitability of this TA candidate for the job posting below.\n\n"
            + "Assessment criteria:\n"
            + "1. Required skill overlap\n"
            + "2. Relevance of education background\n"
            + "3. Relevance of teaching, tutoring, lab, marking, or academic support experience\n"
            + "4. Practical suitability for the stated responsibilities\n"
            + "5. Important missing requirements or weak evidence\n\n"
            + "Scoring guidance:\n"
            + "- 80 to 100: strong match with clear evidence for most important requirements\n"
            + "- 60 to 79: moderate match with some notable gaps\n"
            + "- 40 to 59: weak-to-moderate match with limited supporting evidence\n"
            + "- 0 to 39: low match with limited evidence for core requirements\n\n"
            + "Candidate profile:\n"
            + "Name: " + value(resumeProfile, "name") + "\n"
            + "Education: " + value(resumeProfile, "education") + "\n"
            + "Skills: " + joinList(resumeProfile.get("skills")) + "\n"
            + "Experience: " + joinList(resumeProfile.get("experienceHighlights")) + "\n\n"
            + "Job posting:\n"
            + "Course: " + value(job, "courseName") + "\n"
            + "Course code: " + value(job, "courseCode") + "\n"
            + "Description: " + value(job, "description") + "\n"
            + "Required skills: " + joinList(job.get("requiredSkills")) + "\n"
            + "Estimated workload hours: " + value(job, "estimatedWorkloadHours") + "\n\n"
            + "Return JSON only with keys score and explanation.\n"
            + "score must be an integer from 0 to 100.\n"
            + "explanation must be one concise review paragraph for a module organiser.\n"
            + "The explanation must explicitly cover: matched evidence, important gaps or missing evidence, and practical suitability.\n"
            + "Base the analysis only on the supplied structured data.\n"
            + "Do not assume facts not present in the input.\n"
            + "Do not use external knowledge.\n"
            + "Treat this as an advisory recommendation rather than an automatic hiring decision.";

        return "{"
            + "\"model\":\"" + escapeJson(model) + "\","
            + "\"messages\":["
            + "{\"role\":\"system\",\"content\":\"You are an explainable academic recruitment assistant for a Teaching Assistant recruitment system. Your role is to support, not replace, human decision-making by providing a structured, evidence-based suitability analysis. You must evaluate the candidate only using the supplied structured profile and the supplied job posting. Do not assume missing facts. Do not invent qualifications, skills, or experience. Your analysis must be reviewable by a human module organiser and must explain both supporting evidence and important gaps. Return valid JSON only.\"},"
            + "{\"role\":\"user\",\"content\":\"" + escapeJson(prompt) + "\"}"
            + "],"
            + "\"response_format\":{\"type\":\"json_object\"},"
            + "\"temperature\":0.2"
            + "}";
    }

    private String normalizeEndpoint(String rawEndpoint) {
        String normalized = rawEndpoint == null ? "" : rawEndpoint.trim();
        if (normalized.endsWith("/chat/completions")) {
            return normalized;
        }
        if (normalized.endsWith("/v1")) {
            return normalized + "/chat/completions";
        }
        if (normalized.endsWith("/v1/") || normalized.endsWith("/")) {
            return normalized + "chat/completions";
        }
        if (normalized.matches("^https?://[^/]+$")) {
            return normalized + "/chat/completions";
        }
        return normalized;
    }

    private String extractRelevantPayload(String responseBody) {
        if (containsMatchFields(responseBody)) {
            return responseBody;
        }
        String contentField = extractEmbeddedContent(responseBody, CONTENT_PATTERN);
        if (containsMatchFields(contentField)) {
            return contentField;
        }
        String outputTextField = extractEmbeddedContent(responseBody, OUTPUT_TEXT_PATTERN);
        if (containsMatchFields(outputTextField)) {
            return outputTextField;
        }
        return responseBody;
    }

    private boolean containsMatchFields(String content) {
        return content != null && SCORE_PATTERN.matcher(content).find() && EXPLANATION_PATTERN.matcher(content).find();
    }

    private String extractEmbeddedContent(String responseBody, Pattern pattern) {
        Matcher matcher = pattern.matcher(responseBody);
        if (!matcher.find()) {
            return "";
        }
        return unescapeJsonString(matcher.group(1));
    }

    private int extractScore(String content) throws IOException {
        Matcher matcher = SCORE_PATTERN.matcher(content);
        if (!matcher.find()) {
            throw new IOException("Could not parse score from LLM response: " + abbreviate(content));
        }
        int score = Integer.parseInt(matcher.group(1));
        return Math.max(0, Math.min(100, score));
    }

    private String extractExplanation(String content) throws IOException {
        Matcher matcher = EXPLANATION_PATTERN.matcher(content);
        if (!matcher.find()) {
            throw new IOException("Could not parse explanation from LLM response: " + abbreviate(content));
        }
        return matcher.group(1).replace("\\n", " ").replace("\\\"", "\"");
    }

    private String escapeJson(String input) {
        return input
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\r", "")
            .replace("\n", "\\n");
    }

    private String unescapeJsonString(String input) {
        return input
            .replace("\\\"", "\"")
            .replace("\\n", "\n")
            .replace("\\r", "")
            .replace("\\t", "\t")
            .replace("\\\\", "\\");
    }

    private String abbreviate(String content) {
        String flattened = content == null ? "" : content.replace("\r", " ").replace("\n", " ");
        return flattened.length() <= 500 ? flattened : flattened.substring(0, 500);
    }

    private String value(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value == null ? "" : String.valueOf(value);
    }

    private String joinList(Object value) {
        if (value instanceof List<?>) {
            return String.join(", ", ((List<?>) value).stream().map(String::valueOf).toList());
        }
        return value == null ? "" : String.valueOf(value);
    }
}
