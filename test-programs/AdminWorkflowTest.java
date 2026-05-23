import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;

import java.util.LinkedHashMap;
import java.util.Map;

public final class AdminWorkflowTest implements TestSupport.AcceptanceTest {
    @Override
    public void run(TestSupport ctx) {
        Map<String, Object> overview = ctx.analyticsService.getSystemOverview();
        TestSupport.assertTrue(TestSupport.intValue(overview.get("totalTAs")) >= 1, "Admin overview should count TA users.");
        TestSupport.assertTrue(TestSupport.intValue(overview.get("totalMOs")) >= 1, "Admin overview should count MO users.");
        TestSupport.assertTrue(TestSupport.intValue(overview.get("totalPostings")) >= 1, "Admin overview should count postings.");
        TestSupport.assertTrue(overview.containsKey("recentActivities"), "Admin overview should expose recent activities.");

        String suffix = String.valueOf(System.currentTimeMillis() % 100000);
        Map<String, Object> createMO = new LinkedHashMap<>();
        createMO.put("username", "acceptance_mo_" + suffix);
        createMO.put("fullName", "Acceptance Test MO " + suffix);
        createMO.put("staffId", "ACCEPT-MO-" + suffix);
        createMO.put("email", "acceptance.mo." + suffix + "@example.com");
        createMO.put("department", "Acceptance Testing");
        createMO.put("phone", "+44 7000 000000");
        createMO.put("description", "Created by AdminWorkflowTest.");
        createMO.put("tempPassword", "Temp123!");
        createMO.put("confirmPassword", "Temp123!");
        User created = ctx.userService.createMO(createMO);
        TestSupport.assertEquals(Role.MO, created.getRole(), "Admin should be able to create an MO account.");

        Map<String, Object> searchQuery = new LinkedHashMap<>();
        searchQuery.put("keyword", "Acceptance Test MO " + suffix);
        searchQuery.put("page", "1");
        searchQuery.put("size", "5");
        PageResult<User> moPage = ctx.userService.searchMOs(searchQuery);
        TestSupport.assertTrue(moPage.getTotal() >= 1, "Admin MO search should find the newly created MO.");

        User detail = ctx.userService.getMOById(created.getMoId());
        TestSupport.assertEquals(created.getMoId(), detail.getMoId(), "Admin should be able to inspect MO detail.");

        JobQuery allJobsQuery = new JobQuery();
        allJobsQuery.setSize(50);
        PageResult<Map<String, Object>> allJobs = ctx.jobService.searchAllJobsForAdmin(allJobsQuery);
        TestSupport.assertTrue(allJobs.getTotal() >= 1, "Admin should be able to inspect all jobs.");

        Map<String, Object> workloadQuery = new LinkedHashMap<>();
        workloadQuery.put("page", "1");
        workloadQuery.put("size", "10");
        PageResult<Map<String, Object>> workload = ctx.analyticsService.getTAWorkloadReport(workloadQuery);
        TestSupport.assertTrue(workload.getTotal() >= 1, "Admin should be able to view TA workload report.");

        Map<String, Object> workloadDetail = ctx.analyticsService.getTAWorkloadDetail(TestSupport.TA_ID);
        TestSupport.assertTrue(workloadDetail.get("taProfile") instanceof Map, "TA workload detail should include TA profile.");
        TestSupport.assertTrue(workloadDetail.get("workloadAnalysis") instanceof Map,
            "TA workload detail should include workload analysis.");

        Map<String, Object> distribution = ctx.analyticsService.getTAWorkloadDistributionSummary(workloadQuery);
        TestSupport.assertTrue(distribution.containsKey("hourBuckets"), "Workload distribution should include hour buckets.");
    }
}
