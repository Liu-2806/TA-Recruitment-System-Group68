package edu.qmul.ta.match;

import edu.qmul.ta.model.JobPosting;
import edu.qmul.ta.model.MatchResult;
import edu.qmul.ta.model.ResumeProfile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenAiCompatibleJobMatcher implements JobMatcher {
    private static final Pattern SCORE_PATTERN = Pattern.compile("\"score\"\\s*:\\s*(\\d{1,3})");
    private static final Pattern EXPLANATION_PATTERN = Pattern.compile("\"explanation\"\\s*:\\s*\"(.*?)\"", Pattern.DOTALL);
    private static final Pattern CONTENT_PATTERN = Pattern.compile("\"content\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"", Pattern.DOTALL);
    private static final Pattern OUTPUT_TEXT_PATTERN = Pattern.compile("\"output_text\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"", Pattern.DOTALL);

    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(20))
        .build();

    private final String endpoint;
    private final String apiKey;
    private final String model;

    public OpenAiCompatibleJobMatcher(String endpoint, String apiKey, String model) {
        this.endpoint = normalizeEndpoint(endpoint);
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public MatchResult match(ResumeProfile profile, JobPosting jobPosting) throws IOException, InterruptedException {
        String requestBody = buildRequestBody(profile, jobPosting);
        HttpRequest request = HttpRequest.newBuilder(URI.create(endpoint))
            .timeout(Duration.ofSeconds(60))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("API request failed with status " + response.statusCode() + ": " + response.body());
        }

        String content = extractRelevantPayload(response.body());
        int score = extractScore(content);
        String explanation = extractExplanation(content);
        return new MatchResult(jobPosting, score, explanation, "api-llm");
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
        return content != null
            && SCORE_PATTERN.matcher(content).find()
            && EXPLANATION_PATTERN.matcher(content).find();
    }

    private String extractEmbeddedContent(String responseBody, Pattern pattern) {
        Matcher matcher = pattern.matcher(responseBody);
        if (!matcher.find()) {
            return "";
        }
        return unescapeJsonString(matcher.group(1));
    }

    private String buildRequestBody(ResumeProfile profile, JobPosting jobPosting) {
        String prompt = "You are matching a TA applicant resume to one job posting. "
            + "Return JSON only with keys score and explanation. "
            + "score must be an integer from 0 to 100. "
            + "explanation must be one concise paragraph explaining the strongest signals and any clear gaps.\n\n"
            + "Applicant profile:\n"
            + "Name: " + profile.getName() + "\n"
            + "Education: " + profile.getEducation() + "\n"
            + "Skills: " + String.join(", ", profile.getSkills()) + "\n"
            + "Experience: " + String.join(" | ", profile.getExperienceHighlights()) + "\n\n"
            + "Job posting:\n"
            + "Title: " + jobPosting.getTitle() + "\n"
            + "Module: " + jobPosting.getModule() + "\n"
            + "Description: " + jobPosting.getDescription() + "\n"
            + "Required skills: " + String.join(", ", jobPosting.getRequiredSkills()) + "\n"
            + "Workload: " + jobPosting.getWorkload();

        return "{"
            + "\"model\":\"" + escapeJson(model) + "\","
            + "\"messages\":["
            + "{\"role\":\"system\",\"content\":\"You are a careful recruitment matching assistant.\"},"
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
        if (normalized.endsWith("/v1/")) {
            return normalized + "chat/completions";
        }
        if (normalized.endsWith("/")) {
            return normalized + "chat/completions";
        }
        if (normalized.matches("^https?://[^/]+$")) {
            return normalized + "/chat/completions";
        }
        return normalized;
    }

    private int extractScore(String content) throws IOException {
        Matcher matcher = SCORE_PATTERN.matcher(content);
        if (!matcher.find()) {
            throw new IOException("Could not parse score from API response. First 500 chars: " + abbreviate(content));
        }
        int score = Integer.parseInt(matcher.group(1));
        return Math.max(0, Math.min(100, score));
    }

    private String extractExplanation(String content) throws IOException {
        Matcher matcher = EXPLANATION_PATTERN.matcher(content);
        if (!matcher.find()) {
            throw new IOException("Could not parse explanation from API response. First 500 chars: " + abbreviate(content));
        }
        return matcher.group(1).replace("\\n", " ").replace("\\\"", "\"");
    }

    private String abbreviate(String content) {
        if (content == null) {
            return "";
        }
        String flattened = content.replace("\r", " ").replace("\n", " ");
        return flattened.length() <= 500 ? flattened : flattened.substring(0, 500);
    }

    private String unescapeJsonString(String input) {
        return input
            .replace("\\\"", "\"")
            .replace("\\n", "\n")
            .replace("\\r", "")
            .replace("\\t", "\t")
            .replace("\\\\", "\\");
    }

    private String escapeJson(String input) {
        return input
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\r", "")
            .replace("\n", "\\n");
    }
}
