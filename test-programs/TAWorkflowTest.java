import com.bupt.ta.dto.ApplicationQuery;
import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.model.ApplicationStatus;

import java.util.Map;

public final class TAWorkflowTest implements TestSupport.AcceptanceTest {
    @Override
    public void run(TestSupport ctx) {
        Map<String, Object> job = ctx.createOpenTestJob("TA");
        String jobId = TestSupport.stringValue(job.get("postingId"));

        JobQuery browseQuery = new JobQuery();
        browseQuery.setKeyword("Acceptance Testing Lab TA");
        browseQuery.setSize(20);
        PageResult<Map<String, Object>> browsedJobs = ctx.jobService.searchOpenJobs(browseQuery);
        TestSupport.assertTrue(
            TestSupport.findRecord(browsedJobs, "postingId", jobId) != null,
            "TA should be able to browse the newly created open test position."
        );

        Map<String, Object> detail = ctx.jobService.getJobById(jobId);
        TestSupport.assertEquals(jobId, detail.get("postingId"), "TA should be able to open a job detail page.");

        Map<String, Object> match = ctx.recommendationService.buildJobMatchForTA(TestSupport.TA_ID, jobId);
        int score = TestSupport.intValue(match.get("score"));
        TestSupport.assertTrue(score >= 0 && score <= 100, "Suitability score should be between 0 and 100.");
        TestSupport.assertNonBlank(match.get("strengthSummary"), "Suitability analysis should include strengths.");
        TestSupport.assertNonBlank(match.get("riskSummary"), "Suitability analysis should include missing evidence or risk.");
        TestSupport.assertNonBlank(match.get("nextStepSuggestion"), "Suitability analysis should include application advice.");
        TestSupport.assertNonBlank(match.get("method"), "Suitability analysis should expose the matching method.");

        Map<String, Object> eligibility = ctx.applicationService.checkEligibility(TestSupport.TA_ID, jobId);
        TestSupport.assertEquals(Boolean.TRUE, eligibility.get("eligible"), "TA should be eligible for the new test position.");

        Map<String, Object> application = ctx.applicationService.createApplication(
            TestSupport.TA_ID,
            jobId,
            "I have Java lab support and testing experience for this acceptance testing role."
        );
        String applicationId = TestSupport.stringValue(application.get("applicationId"));
        TestSupport.assertEquals(ApplicationStatus.SUBMITTED.name(), application.get("status"), "New TA application should start as SUBMITTED.");
        TestSupport.assertNonBlank(application.get("skillMatchExplanation"), "Submitted application should store match explanation.");

        ApplicationQuery myQuery = new ApplicationQuery();
        myQuery.setSize(50);
        PageResult<Map<String, Object>> myApplications = ctx.applicationService.listApplicationsByTA(TestSupport.TA_ID, myQuery);
        TestSupport.assertTrue(
            TestSupport.findRecord(myApplications, "applicationId", applicationId) != null,
            "TA application list should include the new application."
        );

        Map<String, Object> withdrawn = ctx.applicationService.withdrawApplicationByTA(
            applicationId,
            TestSupport.TA_ID,
            "Testing normal application withdrawal before MO acceptance."
        );
        TestSupport.assertEquals(ApplicationStatus.WITHDRAWN.name(), withdrawn.get("status"),
            "A non-accepted application should become WITHDRAWN.");

        Map<String, Object> revocationJob = ctx.createOpenTestJob("TAREV");
        String revocationJobId = TestSupport.stringValue(revocationJob.get("postingId"));
        Map<String, Object> acceptedApplication = ctx.applicationService.createApplication(
            TestSupport.TA_ID,
            revocationJobId,
            "This application will be accepted first to test revocation request flow."
        );
        String acceptedApplicationId = TestSupport.stringValue(acceptedApplication.get("applicationId"));
        ctx.applicationService.updateStatusByMO(
            acceptedApplicationId,
            TestSupport.MO_ID,
            ApplicationStatus.ACCEPTED,
            "Accepted for revocation workflow testing."
        );
        Map<String, Object> revocationRequest = ctx.applicationService.withdrawApplicationByTA(
            acceptedApplicationId,
            TestSupport.TA_ID,
            "Testing revocation request after acceptance."
        );
        TestSupport.assertEquals(ApplicationStatus.REVOCATION_REQUESTED.name(), revocationRequest.get("status"),
            "An accepted application withdrawal should become REVOCATION_REQUESTED.");
    }
}
