package com.bupt.ta.match;

import java.util.ArrayList;
import java.util.List;

final class MatchResult {
    private final int score;
    private final String explanation;
    private final String method;
    private final List<String> matchedSkills;
    private final List<String> missingSkills;

    MatchResult(int score, String explanation, String method) {
        this(score, explanation, method, new ArrayList<>(), new ArrayList<>());
    }

    MatchResult(int score, String explanation, String method, List<String> matchedSkills, List<String> missingSkills) {
        this.score = score;
        this.explanation = explanation;
        this.method = method;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
    }

    int getScore() {
        return score;
    }

    String getExplanation() {
        return explanation;
    }

    String getMethod() {
        return method;
    }

    List<String> getMatchedSkills() {
        return matchedSkills;
    }

    List<String> getMissingSkills() {
        return missingSkills;
    }
}
