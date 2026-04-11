package com.bupt.ta.match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
                if (skillsMatch(skill, required)) {
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
        String strongestEvidence = matched.isEmpty()
            ? "Strongest evidence: limited direct overlap with the listed required skills."
            : "Strongest evidence: " + String.join(", ", matched) + ".";
        String biggestGap = missing.isEmpty()
            ? "Biggest gap: no critical missing skills were highlighted from the supplied requirements."
            : "Biggest gap: " + String.join(", ", missing) + ".";
        String practicalRecommendation = score >= 80
            ? "Practical recommendation: this candidate looks ready for shortlisting, subject to human review."
            : (score >= 60
                ? "Practical recommendation: this candidate may be worth review, but the organiser should check the weaker evidence carefully."
                : "Practical recommendation: review cautiously and confirm whether the missing requirements can be covered before progressing.");
        return "The applicant shows a " + score + "% advisory match for " + job.get("courseName") + ". "
            + strongestEvidence + " "
            + biggestGap + " "
            + practicalRecommendation;
    }

    private boolean skillsMatch(String left, String right) {
        if (left == null || right == null) {
            return false;
        }
        String normalizedLeft = normalizeSkill(left);
        String normalizedRight = normalizeSkill(right);
        if (normalizedLeft.equals(normalizedRight)) {
            return true;
        }
        Set<String> aliasesLeft = skillAliases(normalizedLeft);
        Set<String> aliasesRight = skillAliases(normalizedRight);
        for (String alias : aliasesLeft) {
            if (aliasesRight.contains(alias)) {
                return true;
            }
        }
        return normalizedLeft.contains(normalizedRight) || normalizedRight.contains(normalizedLeft);
    }

    private String normalizeSkill(String skill) {
        return skill.toLowerCase(Locale.ROOT)
            .replace("&", "and")
            .replace("-", "")
            .replace("_", "")
            .replace("/", "")
            .replace(" ", "")
            .trim();
    }

    private Set<String> skillAliases(String skill) {
        Set<String> aliases = new LinkedHashSet<>();
        aliases.add(skill);

        Map<String, List<String>> groups = new HashMap<>();
        groups.put("qa", Arrays.asList("qa", "testing", "qualityassurance"));
        groups.put("testing", Arrays.asList("qa", "testing", "qualityassurance"));
        groups.put("qualityassurance", Arrays.asList("qa", "testing", "qualityassurance"));
        groups.put("labsupport", Arrays.asList("lab", "labsupport", "laboratorysupport"));
        groups.put("lab", Arrays.asList("lab", "labsupport", "laboratorysupport"));
        groups.put("laboratorysupport", Arrays.asList("lab", "labsupport", "laboratorysupport"));
        groups.put("tutoring", Arrays.asList("tutoring", "tutor", "teachingsupport"));
        groups.put("tutor", Arrays.asList("tutoring", "tutor", "teachingsupport"));
        groups.put("teachingsupport", Arrays.asList("tutoring", "tutor", "teachingsupport"));
        groups.put("marking", Arrays.asList("marking", "grading", "assessment"));
        groups.put("grading", Arrays.asList("marking", "grading", "assessment"));
        groups.put("assessment", Arrays.asList("marking", "grading", "assessment"));

        if (groups.containsKey(skill)) {
            aliases.addAll(groups.get(skill));
        }
        return aliases;
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
