import com.bupt.ta.dto.ApplicationQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.model.ApplicationStatus;

import java.util.Map;

public final class MOWorkflowTest implements TestSupport.AcceptanceTest {
    @Override
    public void run(TestSupport ctx) throws Exception {
        Map<String, Object> job = ctx.createOpenTestJob("MOA");
        String jobId = TestSupport.stringValue(job.get("postingId"));
        TestSupport.assertNonBlank(jobId, "MO should be able to create a posting.");

        Map<String, Object> updatedParams = ctx.newJobParams("MOAUPD");
        updatedParams.put("courseName", "Acceptance Testing Lab MO Updated");
        Map<String, Object> updatedJob = ctx.jobService.updateJob(TestSupport.MO_ID, jobId, updatedParams);
        TestSupport.assertEquals("Acceptance Testing Lab MO Updated", updatedJob.get("courseName"),
            "MO should be able to edit an owned posting.");

        Map<String, Object> application = ctx.applicationService.createApplication(
            TestSupport.TA_ID,
            jobId,
            "Application created so the MO can review a candidate."
        );
        String applicationId = TestSupport.stringValue(application.get("applicationId"));

        ApplicationQuery applicantQuery = new ApplicationQuery();
        applicantQuery.setSize(20);
        PageResult<Map<String, Object>> applicants = ctx.applicationService.listApplicationsByJob(jobId, applicantQuery);
        TestSupport.assertTrue(
            TestSupport.findRecord(applicants, "applicationId", applicationId) != null,
            "MO applicant list should include the submitted application."
        );

        Map<String, Object> detail = ctx.applicationService.getApplicationDetailForMO(applicationId, TestSupport.MO_ID);
        TestSupport.assertTrue(detail.get("taProfile") instanceof Map, "MO application detail should include TA profile.");
        TestSupport.assertNonBlank(detail.get("skillMatchExplanation"), "MO application detail should include suitability analysis.");
        TestSupport.assertNonBlank(detail.get("methodLabel"), "MO application detail should expose analysis method label.");

        Map<String, Object> downloadInfo = ctx.resumeService.openResumeStreamForMO(applicationId, TestSupport.MO_ID);
        TestSupport.assertReadableStream(downloadInfo);

        ctx.applicationService.updateStatusByMO(
            applicationId,
            TestSupport.MO_ID,
            ApplicationStatus.ACCEPTED,
            "Accepted during MO workflow acceptance testing."
        );
        Map<String, Object> acceptedDetail = ctx.applicationService.getApplicationDetailForMO(applicationId, TestSupport.MO_ID);
        TestSupport.assertEquals(ApplicationStatus.ACCEPTED.name(), acceptedDetail.get("status"),
            "MO should be able to accept an application.");

        Map<String, Object> revocationRequest = ctx.applicationService.withdrawApplicationByTA(
            applicationId,
            TestSupport.TA_ID,
            "TA requests to revoke the accepted duty during acceptance testing."
        );
        TestSupport.assertEquals(ApplicationStatus.REVOCATION_REQUESTED.name(), revocationRequest.get("status"),
            "Accepted application should become a pending revocation request.");

        Map<String, Object> revocationDecision = ctx.applicationService.respondToRevocationRequest(
            applicationId,
            TestSupport.MO_ID,
            true,
            "Approved in MO workflow acceptance testing."
        );
        TestSupport.assertEquals(ApplicationStatus.WITHDRAWN.name(), revocationDecision.get("status"),
            "MO should be able to approve a revocation request.");

        Map<String, Object> rejectedJob = ctx.createOpenTestJob("MOREJ");
        Map<String, Object> rejectedApplication = ctx.applicationService.createApplication(
            TestSupport.TA_ID,
            TestSupport.stringValue(rejectedJob.get("postingId")),
            "Application created so the MO can reject a candidate."
        );
        String rejectedApplicationId = TestSupport.stringValue(rejectedApplication.get("applicationId"));
        ctx.applicationService.updateStatusByMO(
            rejectedApplicationId,
            TestSupport.MO_ID,
            ApplicationStatus.REJECTED,
            "Rejected during MO workflow acceptance testing."
        );
        Map<String, Object> rejectedDetail = ctx.applicationService.getApplicationDetailForMO(rejectedApplicationId, TestSupport.MO_ID);
        TestSupport.assertEquals(ApplicationStatus.REJECTED.name(), rejectedDetail.get("status"),
            "MO should be able to reject an application.");
    }
}
