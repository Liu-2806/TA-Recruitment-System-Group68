package edu.qmul.ta.model;

public class MatchResult {
    private final JobPosting jobPosting;
    private final int score;
    private final String explanation;
    private final String matchingMethod;

    public MatchResult(JobPosting jobPosting, int score, String explanation, String matchingMethod) {
        this.jobPosting = jobPosting;
        this.score = score;
        this.explanation = explanation;
        this.matchingMethod = matchingMethod;
    }

    public JobPosting getJobPosting() {
        return jobPosting;
    }

    public int getScore() {
        return score;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getMatchingMethod() {
        return matchingMethod;
    }
}
