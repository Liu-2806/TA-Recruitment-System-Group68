import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.repository.file.JsonUserRepository;
import com.bupt.ta.repository.file.PostingDataRepository;
import com.bupt.ta.service.JobService;
import com.bupt.ta.service.impl.JobServiceImpl;
import com.bupt.ta.util.DataPaths;
import com.bupt.ta.util.JsonUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Admin All Jobs 后端命令行验证入口。
 */
public class AdminAllJobsConsoleRunner {
    public static void main(String[] args) {
        PostingDataRepository postingRepository = new PostingDataRepository();
        UserRepository userRepository = new JsonUserRepository();
        JobService jobService = new JobServiceImpl(postingRepository, userRepository);

        List<Map<String, Object>> rawPostings = postingRepository.findAll();
        if (rawPostings.isEmpty()) {
            throw new IllegalStateException("postings.json has no records.");
        }

        Map<String, Object> reference = rawPostings.get(0);
        String moId = stringValue(reference.get("moId"));
        String moName = stringValue(reference.get("moName"));
        String status = stringValue(reference.get("status"));
        String keyword = firstKeyword(reference);

        JobQuery baseQuery = new JobQuery();
        baseQuery.setPage(1);
        baseQuery.setSize(1);
        PageResult<Map<String, Object>> basePage = jobService.searchAllJobsForAdmin(baseQuery);
        assertEquals(1, basePage.getPage(), "Page should echo request value.");
        assertEquals(1, basePage.getSize(), "Size should echo request value.");
        assertTrue(basePage.getTotal() >= basePage.getRecords().size(), "Total should be >= current page record count.");

        Map<String, Object> firstRecord = basePage.getRecords().isEmpty() ? null : basePage.getRecords().get(0);
        assertTrue(firstRecord != null, "Base query should return at least one record.");
        assertJobContract(firstRecord);

        JobQuery moIdQuery = new JobQuery();
        moIdQuery.setMoId(moId);
        moIdQuery.setPage(1);
        moIdQuery.setSize(50);
        PageResult<Map<String, Object>> moIdPage = jobService.searchAllJobsForAdmin(moIdQuery);
        assertTrue(moIdPage.getTotal() >= 1, "moId filter should return at least one record.");
        for (Map<String, Object> record : moIdPage.getRecords()) {
            assertEquals(moId.toLowerCase(Locale.ROOT), stringValue(record.get("moId")).toLowerCase(Locale.ROOT), "moId filter should match returned records.");
        }

        JobQuery moFilterQuery = new JobQuery();
        moFilterQuery.setMoFilter(moName);
        moFilterQuery.setPage(1);
        moFilterQuery.setSize(50);
        PageResult<Map<String, Object>> moFilterPage = jobService.searchAllJobsForAdmin(moFilterQuery);
        assertTrue(moFilterPage.getTotal() >= 1, "moFilter should return at least one record.");

        JobQuery statusKeywordQuery = new JobQuery();
        statusKeywordQuery.setStatus(status);
        statusKeywordQuery.setKeyword(keyword);
        statusKeywordQuery.setSortBy("deadlineAsc");
        statusKeywordQuery.setPage(1);
        statusKeywordQuery.setSize(50);
        PageResult<Map<String, Object>> statusKeywordPage = jobService.searchAllJobsForAdmin(statusKeywordQuery);
        assertTrue(statusKeywordPage.getTotal() >= 1, "status + keyword filter should return at least one record.");

        System.out.println("DATA_FILE=" + DataPaths.resolvePostingsFile());
        System.out.println("BASE_PAGE_TOTAL=" + basePage.getTotal());
        System.out.println("MO_ID_FILTER_TOTAL=" + moIdPage.getTotal());
        System.out.println("MO_FILTER_TOTAL=" + moFilterPage.getTotal());
        System.out.println("STATUS_KEYWORD_TOTAL=" + statusKeywordPage.getTotal());
        System.out.println("SAMPLE_RECORD=" + JsonUtils.toJson(describeRecord(firstRecord)));
        System.out.println("ADMIN_ALL_JOBS_TEST=PASS");
    }

    private static Object describeRecord(Map<String, Object> record) {
        Map<String, Object> item = new LinkedHashMap<String, Object>();
        item.put("postingId", record.get("postingId"));
        item.put("courseCode", record.get("courseCode"));
        item.put("courseName", record.get("courseName"));
        item.put("moId", record.get("moId"));
        item.put("moName", record.get("moName"));
        item.put("vacancies", record.get("vacancies"));
        item.put("applicationCount", record.get("applicationCount"));
        item.put("deadline", record.get("deadline"));
        item.put("status", record.get("status"));
        item.put("estimatedWorkloadHours", record.get("estimatedWorkloadHours"));
        return item;
    }

    private static String firstKeyword(Map<String, Object> posting) {
        String courseName = stringValue(posting.get("courseName"));
        if (courseName == null || courseName.isEmpty()) {
            return "TA";
        }
        String[] parts = courseName.split("\\s+");
        return parts.length == 0 ? courseName : parts[0];
    }

    private static void assertJobContract(Map<String, Object> record) {
        List<String> fields = new ArrayList<String>();
        fields.add("postingId");
        fields.add("courseCode");
        fields.add("courseName");
        fields.add("moId");
        fields.add("moName");
        fields.add("vacancies");
        fields.add("applicationCount");
        fields.add("deadline");
        fields.add("status");
        fields.add("estimatedWorkloadHours");
        for (String field : fields) {
            Object value = record.get(field);
            if (value == null || String.valueOf(value).trim().isEmpty()) {
                throw new IllegalStateException("Missing required job field: " + field);
            }
        }
    }

    private static String stringValue(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new IllegalStateException(message + " Expected=" + expected + ", Actual=" + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}

