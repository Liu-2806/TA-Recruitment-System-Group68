package com.bupt.ta.match;

import com.bupt.ta.repository.file.ApplicationDataRepository;
import com.bupt.ta.repository.file.PostingDataRepository;
import com.bupt.ta.repository.file.TADataRepository;
import com.bupt.ta.service.RecommendationService;

import java.util.LinkedHashMap;
import java.util.List;
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
                    fallback.getExplanation() + " LLM fallback reason: " + ex.getMessage(),
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
        return map;
    }
}
