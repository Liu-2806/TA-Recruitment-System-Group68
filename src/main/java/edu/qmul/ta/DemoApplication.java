package edu.qmul.ta;

import edu.qmul.ta.io.JobPostingRepository;
import edu.qmul.ta.io.OutputWriter;
import edu.qmul.ta.match.JobMatcher;
import edu.qmul.ta.match.LocalKeywordJobMatcher;
import edu.qmul.ta.match.OpenAiCompatibleJobMatcher;
import edu.qmul.ta.model.JobPosting;
import edu.qmul.ta.model.MatchResult;
import edu.qmul.ta.model.ResumeProfile;
import edu.qmul.ta.parser.PdfResumeParser;
import edu.qmul.ta.parser.ResumeStructurer;
import edu.qmul.ta.service.ResumeMatchingService;

import java.nio.file.Path;
import java.util.List;

public class DemoApplication {
    public static void main(String[] args) throws Exception {
        Path resumePath = args.length > 0
            ? Path.of(args[0])
            : Path.of("demo-data", "sample_resume.pdf");
        Path jobsPath = args.length > 1
            ? Path.of(args[1])
            : Path.of("demo-data", "job_postings.csv");

        JobMatcher matcher = buildMatcher();
        ResumeMatchingService service = new ResumeMatchingService(
            new PdfResumeParser(),
            new ResumeStructurer(),
            matcher
        );

        JobPostingRepository repository = new JobPostingRepository();
        List<JobPosting> jobs = repository.loadFromCsv(jobsPath);
        ResumeProfile profile = service.extractProfile(resumePath);
        List<MatchResult> matches = service.match(profile, jobs);

        OutputWriter outputWriter = new OutputWriter();
        outputWriter.writeProfile(Path.of("output", "candidate-profile.json"), profile);
        outputWriter.writeMatches(Path.of("output", "job-match-results.json"), matches);

        System.out.println("Resume processed: " + resumePath);
        System.out.println("Structured profile written to output/candidate-profile.json");
        System.out.println("Match results written to output/job-match-results.json");
        if (!matches.isEmpty()) {
            MatchResult top = matches.get(0);
            System.out.println("Top match: " + top.getJobPosting().getTitle() + " (" + top.getScore() + ")");
            System.out.println(top.getExplanation());
        }
    }

    private static JobMatcher buildMatcher() {
        String endpoint = System.getenv("LLM_API_URL");
        String apiKey = System.getenv("LLM_API_KEY");
        String model = System.getenv("LLM_MODEL");
        if (endpoint != null && !endpoint.isBlank()
            && apiKey != null && !apiKey.isBlank()
            && model != null && !model.isBlank()) {
            System.out.println("Matcher mode: api-llm");
            System.out.println("LLM endpoint: " + endpoint);
            System.out.println("LLM model: " + model);
            return new OpenAiCompatibleJobMatcher(endpoint, apiKey, model);
        }
        System.out.println("Matcher mode: local-keyword");
        System.out.println("API mode was not enabled because one or more required environment variables are missing.");
        return new LocalKeywordJobMatcher();
    }
}
