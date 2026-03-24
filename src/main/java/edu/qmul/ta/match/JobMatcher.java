package edu.qmul.ta.match;

import edu.qmul.ta.model.JobPosting;
import edu.qmul.ta.model.MatchResult;
import edu.qmul.ta.model.ResumeProfile;

public interface JobMatcher {
    MatchResult match(ResumeProfile profile, JobPosting jobPosting) throws Exception;
}
