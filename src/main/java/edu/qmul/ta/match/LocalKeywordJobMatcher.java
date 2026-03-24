package edu.qmul.ta.match;

import edu.qmul.ta.model.JobPosting;
import edu.qmul.ta.model.MatchResult;
import edu.qmul.ta.model.ResumeProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocalKeywordJobMatcher implements JobMatcher {

    @Override
    public MatchResult match(ResumeProfile profile, JobPosting jobPosting) {
        List<String> matchedSkills = new ArrayList<>();
        for (String requiredSkill : jobPosting.getRequiredSkills()) {
            for (String candidateSkill : profile.getSkills()) {
                if (candidateSkill.equalsIgnoreCase(requiredSkill)) {
                    matchedSkills.add(requiredSkill);
                    break;
                }
            }
        }

        int score = 35;
        if (!jobPosting.getRequiredSkills().isEmpty()) {
            score += (int) Math.round(55.0 * matchedSkills.size() / jobPosting.getRequiredSkills().size());
        }

        String rawTextLower = profile.getRawText().toLowerCase(Locale.ROOT);
        if (!jobPosting.getModule().isEmpty() && rawTextLower.contains(jobPosting.getModule().toLowerCase(Locale.ROOT))) {
            score += 5;
        }
        if (rawTextLower.contains("teaching") || rawTextLower.contains("tutor") || rawTextLower.contains("assistant")) {
            score += 5;
        }

        int boundedScore = Math.max(0, Math.min(100, score));
        String explanation;
        if (matchedSkills.isEmpty()) {
            explanation = "Local fallback matcher found limited direct skill overlap. The score is based mainly on general TA-related context in the resume.";
        } else {
            explanation = "Local fallback matcher found overlap in "
                + String.join(", ", matchedSkills)
                + ". The score also considers TA-related experience and module keywords in the resume.";
        }
        return new MatchResult(jobPosting, boundedScore, explanation, "local-keyword");
    }
}
