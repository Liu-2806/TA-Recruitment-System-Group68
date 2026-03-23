package edu.qmul.ta.service;

import edu.qmul.ta.match.JobMatcher;
import edu.qmul.ta.model.JobPosting;
import edu.qmul.ta.model.MatchResult;
import edu.qmul.ta.model.ResumeProfile;
import edu.qmul.ta.parser.PdfResumeParser;
import edu.qmul.ta.parser.ResumeStructurer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResumeMatchingService {
    private final PdfResumeParser pdfResumeParser;
    private final ResumeStructurer resumeStructurer;
    private final JobMatcher jobMatcher;

    public ResumeMatchingService(PdfResumeParser pdfResumeParser, ResumeStructurer resumeStructurer, JobMatcher jobMatcher) {
        this.pdfResumeParser = pdfResumeParser;
        this.resumeStructurer = resumeStructurer;
        this.jobMatcher = jobMatcher;
    }

    public ResumeProfile extractProfile(Path pdfPath) throws Exception {
        String rawText = pdfResumeParser.extractText(pdfPath);
        return resumeStructurer.structure(rawText);
    }

    public List<MatchResult> match(ResumeProfile profile, List<JobPosting> jobs) throws Exception {
        List<MatchResult> results = new ArrayList<>();
        for (JobPosting job : jobs) {
            results.add(jobMatcher.match(profile, job));
        }
        results.sort(Comparator.comparingInt(MatchResult::getScore).reversed());
        return results;
    }
}
