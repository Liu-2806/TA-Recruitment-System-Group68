import com.bupt.ta.dto.PageResult;
import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.repository.file.JsonUserRepository;
import com.bupt.ta.service.AnalyticsService;
import com.bupt.ta.service.impl.AnalyticsServiceImpl;
import com.bupt.ta.util.DataPaths;
import com.bupt.ta.util.JsonUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin TA Workload 后端命令行验证入口。
 */
public class AdminTAWorkloadConsoleRunner {
    public static void main(String[] args) {
        UserRepository userRepository = new JsonUserRepository();
        AnalyticsService analyticsService = new AnalyticsServiceImpl(userRepository);

        Map<String, Object> listQuery = new LinkedHashMap<String, Object>();
        listQuery.put("keyword", "TA001");
        listQuery.put("major", "Software");
        listQuery.put("status", "NORMAL");
        listQuery.put("page", "1");
        listQuery.put("size", "10");
        listQuery.put("sortBy", "hoursDesc");

        PageResult<Map<String, Object>> reportPage = analyticsService.getTAWorkloadReport(listQuery);
        assertEquals(1, reportPage.getPage(), "page should echo query");
        assertEquals(10, reportPage.getSize(), "size should echo query");
        assertTrue(reportPage.getTotal() >= 1, "report should include TA001 with current fixture data");

        Map<String, Object> first = reportPage.getRecords().get(0);
        assertWorkloadRecordContract(first);

        Map<String, Object> distributionSummary = analyticsService.getTAWorkloadDistributionSummary(listQuery);
        assertDistributionSummaryContract(distributionSummary);

        Map<String, Object> detail = analyticsService.getTAWorkloadDetail("TA001");
        assertDetailContract(detail);

        Map<String, Object> reportPageOutput = new LinkedHashMap<String, Object>();
        reportPageOutput.put("records", reportPage.getRecords());
        reportPageOutput.put("page", reportPage.getPage());
        reportPageOutput.put("size", reportPage.getSize());
        reportPageOutput.put("total", reportPage.getTotal());

        System.out.println("DATA_USERS_FILE=" + DataPaths.resolveUsersFile(com.bupt.ta.model.Role.TA));
        System.out.println("DATA_APPLICATIONS_FILE=" + DataPaths.resolveApplicationsFile());
        System.out.println("REPORT_PAGE=" + JsonUtils.toJson(reportPageOutput));
        System.out.println("DISTRIBUTION_SUMMARY=" + JsonUtils.toJson(distributionSummary));
        System.out.println("DETAIL_SAMPLE=" + JsonUtils.toJson(detail));
        System.out.println("ADMIN_TA_WORKLOAD_TEST=PASS");
    }

    private static void assertWorkloadRecordContract(Map<String, Object> row) {
        requireField(row, "taId");
        requireField(row, "fullName");
        requireField(row, "studentId");
        requireField(row, "majorProgram");
        requireField(row, "activePositionCount");
        requireField(row, "totalWorkloadHours");
        requireField(row, "workloadStatus");

        String status = String.valueOf(row.get("workloadStatus"));
        if (!("NORMAL".equals(status) || "HIGH_ALERT".equals(status) || "NOT_APPLIED".equals(status))) {
            throw new IllegalStateException("Unexpected workloadStatus: " + status);
        }
    }

    private static void assertDistributionSummaryContract(Map<String, Object> summary) {
        requireField(summary, "hourBuckets");
        requireField(summary, "peakWorkloadGroup");
        requireField(summary, "criticalAlertCount");

        Object bucketObj = summary.get("hourBuckets");
        if (!(bucketObj instanceof List)) {
            throw new IllegalStateException("hourBuckets should be a list.");
        }
        List<?> buckets = (List<?>) bucketObj;
        if (buckets.size() != 4) {
            throw new IllegalStateException("hourBuckets should contain 4 items.");
        }
    }

    private static void assertDetailContract(Map<String, Object> detail) {
        requireField(detail, "taProfile");
        requireField(detail, "workingPositions");
        requireField(detail, "workloadAnalysis");
        requireField(detail, "adminSuggestions");

        @SuppressWarnings("unchecked")
        Map<String, Object> profile = (Map<String, Object>) detail.get("taProfile");
        requireField(profile, "taId");
        requireField(profile, "fullName");
        requireField(profile, "studentId");
        requireField(profile, "majorProgram");
        requireField(profile, "academicYear");
        requireField(profile, "email");
        requireField(profile, "phone");
    }

    private static void requireField(Map<String, Object> map, String field) {
        if (!map.containsKey(field)) {
            throw new IllegalStateException("Missing field: " + field);
        }
        Object value = map.get(field);
        if (value == null) {
            throw new IllegalStateException("Field is null: " + field);
        }
        if (value instanceof String && String.valueOf(value).trim().isEmpty()) {
            throw new IllegalStateException("Field is blank: " + field);
        }
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new IllegalStateException(message + " Expected=" + expected + ", Actual=" + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}


