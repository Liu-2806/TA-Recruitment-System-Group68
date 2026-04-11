package com.bupt.ta.match;

import com.bupt.ta.repository.file.ApplicationDataRepository;
import com.bupt.ta.repository.file.PostingDataRepository;
import com.bupt.ta.repository.file.TADataRepository;
import com.bupt.ta.service.RecommendationService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecommendationServiceImpl implements RecommendationService {
    private final TADataRepository taDataRepository;
    private final PostingDataRepository postingDataRepository;
    private final ApplicationDataRepository applicationDataRepository;
    private final LocalRuleMatcher localRuleMatcher = new LocalRuleMatcher();

    public RecommendationServiceImpl(
        TADataRepository taDataRepository,
        PostingDataRepository postingDataRepository,
        ApplicationDataRepository applicationDataRepository
    ) {
        this.taDataRepository = taDataRepository;
        this.postingDataRepository = postingDataRepository;
        this.applicationDataRepository = applicationDataRepository;
    }

    @Override
    public Map<String, Object> buildJobMatchForTA(String taUserId, String jobId) {
        Map<String, Object> ta = requireTa(taUserId);
        Map<String, Object> job = requireJob(jobId);
        return toMap(runMatcher(extractedResume(ta), job));
    }

    @Override
    public Map<String, Object> buildApplicationMatchForMO(String applicationId, String moUserId) {
        Map<String, Object> application = requireApplication(applicationId);
        Map<String, Object> job = requireJob(String.valueOf(application.get("postingId")));
        if (!moUserId.equals(String.valueOf(job.get("moId")))) {
            throw new IllegalStateException("You do not have permission to review this application.");
        }
        Map<String, Object> ta = requireTa(String.valueOf(application.get("taId")));
        return toMap(runMatcher(extractedResume(ta), job));
    }

    public Map<String, Object> enrichApplicationWithMatch(Map<String, Object> application) {
        Map<String, Object> job = requireJob(String.valueOf(application.get("postingId")));
        Map<String, Object> ta = requireTa(String.valueOf(application.get("taId")));
        MatchResult result = runMatcher(extractedResume(ta), job);
        application.put("skillMatchScore", result.getScore());
        application.put("skillMatchExplanation", result.getExplanation());
        application.put("matchedSkills", result.getMatchedSkills());
        application.put("missingSkills", result.getMissingSkills());
        application.put("matchMethod", result.getMethod());
        application.put("method", result.getMethod());
        application.put("score", result.getScore());
        application.put("explanation", result.getExplanation());
        enrichUserFacingFields(application);
        return application;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractedResume(Map<String, Object> ta) {
        Object extracted = ta.get("extractedResume");
        if (!(extracted instanceof Map<?, ?> extractedMap)) {
            throw new IllegalStateException("No structured resume data is available for the TA profile.");
        }
        return (Map<String, Object>) extractedMap;
    }

    private MatchResult runMatcher(Map<String, Object> resumeProfile, Map<String, Object> job) {
        Map<String, String> env = EnvConfigLoader.load();
        if (EnvConfigLoader.isComplete(env)) {
            try {
                return new OpenAiCompatibleMatcher(
                    env.get("LLM_API_URL"),
                    env.get("LLM_API_KEY"),
                    env.get("LLM_MODEL")
                ).analyze(resumeProfile, job);
            } catch (Exception ex) {
                MatchResult fallback = localRuleMatcher.analyze(resumeProfile, job);
                return new MatchResult(
                    fallback.getScore(),
                    fallback.getExplanation() + " The AI endpoint could not return a complete result, so the system fell back to local rule matching. Technical reason: " + ex.getMessage(),
                    "local-rule-fallback",
                    fallback.getMatchedSkills(),
                    fallback.getMissingSkills()
                );
            }
        }
        return localRuleMatcher.analyze(resumeProfile, job);
    }

    private Map<String, Object> requireTa(String taUserId) {
        Map<String, Object> ta = taDataRepository.findByTaId(taUserId);
        if (ta == null) {
            throw new IllegalStateException("TA profile not found: " + taUserId);
        }
        return ta;
    }

    private Map<String, Object> requireJob(String jobId) {
        Map<String, Object> job = postingDataRepository.findByPostingId(jobId);
        if (job == null) {
            throw new IllegalStateException("Job posting not found: " + jobId);
        }
        return job;
    }

    private Map<String, Object> requireApplication(String applicationId) {
        Map<String, Object> application = applicationDataRepository.findByApplicationId(applicationId);
        if (application == null) {
            throw new IllegalStateException("Application not found: " + applicationId);
        }
        return application;
    }

    private Map<String, Object> toMap(MatchResult result) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("score", result.getScore());
        map.put("explanation", result.getExplanation());
        map.put("method", result.getMethod());
        map.put("matchedSkills", List.copyOf(result.getMatchedSkills()));
        map.put("missingSkills", List.copyOf(result.getMissingSkills()));
        enrichUserFacingFields(map);
        return map;
    }

    private void enrichUserFacingFields(Map<String, Object> map) {
        int score = parseScore(map.get("score"));
        List<String> matchedSkills = toStringList(map.get("matchedSkills"));
        List<String> missingSkills = toStringList(map.get("missingSkills"));
        String method = String.valueOf(map.getOrDefault("method", ""));
        String explanation = String.valueOf(map.getOrDefault("explanation", ""));

        map.put("scoreBand", scoreBand(score));
        map.put("strengthSummary", strengthSummary(score, matchedSkills));
        map.put("riskSummary", riskSummary(score, missingSkills));
        map.put("nextStepSuggestion", nextStepSuggestion(score, missingSkills));
        map.put("confidenceHint", confidenceHint(method, matchedSkills, missingSkills, explanation));
        map.put("methodLabel", methodLabel(method));
        map.put("methodHint", methodHint(method));
    }

    private int parseScore(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value).trim());
        } catch (Exception ex) {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> toStringList(Object value) {
        if (value instanceof List<?> list) {
            return list.stream().map(String::valueOf).toList();
        }
        return List.of();
    }

    private String scoreBand(int score) {
        if (score >= 80) {
            return "Strong Match";
        }
        if (score >= 60) {
            return "Moderate Match";
        }
        return "Weak Match";
    }

    private String strengthSummary(int score, List<String> matchedSkills) {
        if (!matchedSkills.isEmpty()) {
            return "Strongest evidence aligns with " + joinLimited(matchedSkills, 3) + ".";
        }
        if (score >= 60) {
            return "The current profile suggests some relevant background, but the overlap is not strongly evidenced in skill tags.";
        }
        return "The current profile does not yet show strong direct evidence for this role.";
    }

    private String riskSummary(int score, List<String> missingSkills) {
        if (!missingSkills.isEmpty()) {
            return "The main risk is missing or unclear evidence for " + joinLimited(missingSkills, 3) + ".";
        }
        if (score >= 80) {
            return "No major risk stands out from the structured data, but a human reviewer should still confirm the original resume.";
        }
        return "The result may be conservative because the structured profile does not provide much evidence to work with.";
    }

    private String nextStepSuggestion(int score, List<String> missingSkills) {
        if (score >= 80) {
            return "This role looks worth applying for now. Keep your statement focused on the matched evidence.";
        }
        if (score >= 60) {
            return missingSkills.isEmpty()
                ? "This role may still be worth applying for, but explain your most relevant evidence clearly in the statement."
                : "Consider applying only if you can address the missing evidence in your statement, especially around " + joinLimited(missingSkills, 2) + ".";
        }
        return missingSkills.isEmpty()
            ? "Apply with caution. The current structured profile may be too thin to support a strong case."
            : "Consider strengthening your profile before applying, especially by adding clearer evidence for " + joinLimited(missingSkills, 2) + ".";
    }

    private String confidenceHint(String method, List<String> matchedSkills, List<String> missingSkills, String explanation) {
        boolean thinEvidence = matchedSkills.size() + missingSkills.size() <= 1 || explanation.isBlank();
        String normalizedMethod = method == null ? "" : method.trim().toLowerCase(Locale.ROOT);
        if ("local-rule-fallback".equals(normalizedMethod)) {
            return "This result uses the local fallback matcher because the AI endpoint did not return a complete response. Please review the original resume manually.";
        }
        if ("local-rule".equals(normalizedMethod)) {
            return thinEvidence
                ? "This result is based on local matching rules and limited structured evidence, so treat it as a conservative estimate."
                : "This result is based on local matching rules. Use it as a screening aid rather than a final decision.";
        }
        if (thinEvidence) {
            return "The AI result may be conservative because the structured resume evidence is limited.";
        }
        return "This AI result is advisory only and should be confirmed against the original resume and job requirements.";
    }

    private String methodLabel(String method) {
        String normalizedMethod = method == null ? "" : method.trim().toLowerCase(Locale.ROOT);
        return switch (normalizedMethod) {
            case "api-llm" -> "AI-assisted review";
            case "local-rule-fallback" -> "Fallback rule review";
            case "local-rule" -> "Rule-based review";
            default -> "Advisory review";
        };
    }

    private String methodHint(String method) {
        String normalizedMethod = method == null ? "" : method.trim().toLowerCase(Locale.ROOT);
        return switch (normalizedMethod) {
            case "api-llm" -> "Generated from the structured resume and job posting using the configured AI endpoint.";
            case "local-rule-fallback" -> "The AI endpoint was unavailable or incomplete, so the system fell back to local matching rules.";
            case "local-rule" -> "Generated entirely from local matching rules using extracted resume data and required skills.";
            default -> "Generated from the available structured data.";
        };
    }

    private String joinLimited(List<String> values, int maxItems) {
        if (values == null || values.isEmpty()) {
            return "the current role requirements";
        }
        int end = Math.min(values.size(), Math.max(1, maxItems));
        List<String> shown = values.subList(0, end);
        if (values.size() > end) {
            return String.join(", ", shown) + " and more";
        }
        return String.join(", ", shown);
    }
}
