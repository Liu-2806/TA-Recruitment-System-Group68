package com.bupt.ta.match;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

final class LocalRuleMatcher {
    MatchResult analyze(Map<String, Object> resumeProfile, Map<String, Object> job) {
        List<String> resumeSkills = toStringList(resumeProfile.get("skills"));
        List<String> requiredSkills = toStringList(job.get("requiredSkills"));
        Set<String> matched = new LinkedHashSet<>();
        Set<String> missing = new LinkedHashSet<>();
        for (String required : requiredSkills) {
            boolean found = false;
            for (String skill : resumeSkills) {
                if (skill.equalsIgnoreCase(required)) {
                    matched.add(required);
                    found = true;
                    break;
                }
            }
            if (!found) {
                missing.add(required);
            }
        }

        int baseScore = 30;
        if (!requiredSkills.isEmpty()) {
            baseScore += (int) Math.round(55.0 * matched.size() / requiredSkills.size());
        }
        String combinedText = String.valueOf(resumeProfile.get("rawText")).toLowerCase(Locale.ROOT);
        if (combinedText.contains("assistant") || combinedText.contains("teaching") || combinedText.contains("tutor")) {
            baseScore += 10;
        }
        if (combinedText.contains(String.valueOf(job.get("courseCode")).toLowerCase(Locale.ROOT))
            || combinedText.contains(String.valueOf(job.get("courseName")).toLowerCase(Locale.ROOT))) {
            baseScore += 5;
        }
        int score = Math.max(0, Math.min(100, baseScore));
        String explanation = buildExplanation(job, matched, missing, score);
        return new MatchResult(score, explanation, "local-rule", new ArrayList<>(matched), new ArrayList<>(missing));
    }

    private String buildExplanation(Map<String, Object> job, Set<String> matched, Set<String> missing, int score) {
        StringBuilder sb = new StringBuilder();
        sb.append("The applicant shows a ").append(score).append("% match for ")
            .append(job.get("courseName")).append(". ");
        if (matched.isEmpty()) {
            sb.append("There is limited direct overlap with the listed required skills. ");
        } else {
            sb.append("Matched strengths include ").append(String.join(", ", matched)).append(". ");
        }
        if (!missing.isEmpty()) {
            sb.append("Potential gaps remain in ").append(String.join(", ", missing)).append(". ");
        }
        sb.append("This result is based on extracted resume content and should be used by the MO as advisory evidence rather than an automatic decision.");
        return sb.toString();
    }

    private List<String> toStringList(Object value) {
        if (value instanceof List<?>) {
            List<String> results = new ArrayList<>();
            for (Object item : (List<?>) value) {
                results.add(String.valueOf(item));
            }
            return results;
        }
        return new ArrayList<>();
    }
}
