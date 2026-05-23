import com.bupt.ta.util.JsonUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DataIntegrityTest implements TestSupport.AcceptanceTest {
    @Override
    public void run(TestSupport ctx) throws Exception {
        Path root = TestSupport.dataRoot();
        TestSupport.assertTrue(Files.isDirectory(root), "Acceptance test data root should exist.");

        List<Map<String, Object>> tas = readJsonArray(root.resolve("users").resolve("ta.json"));
        List<Map<String, Object>> mos = readJsonArray(root.resolve("users").resolve("mo.json"));
        List<Map<String, Object>> admins = readJsonArray(root.resolve("users").resolve("admin.json"));
        List<Map<String, Object>> postings = readJsonArray(root.resolve("postings").resolve("postings.json"));
        List<Map<String, Object>> applications = readJsonArray(root.resolve("applications").resolve("applications.json"));

        TestSupport.assertTrue(!tas.isEmpty(), "TA JSON data should contain at least one TA.");
        TestSupport.assertTrue(!mos.isEmpty(), "MO JSON data should contain at least one MO.");
        TestSupport.assertTrue(!admins.isEmpty(), "Admin JSON data should contain at least one admin.");
        TestSupport.assertTrue(!postings.isEmpty(), "Posting JSON data should contain at least one posting.");
        TestSupport.assertTrue(!applications.isEmpty(), "Application JSON data should contain at least one application.");

        Set<String> taIds = collectIds(tas, "taId", "id");
        Set<String> moIds = collectIds(mos, "moId", "id");
        Set<String> postingIds = collectIds(postings, "postingId");
        TestSupport.assertTrue(taIds.contains(TestSupport.TA_ID), "Demo TA ID should exist in isolated data.");
        TestSupport.assertTrue(moIds.contains(TestSupport.MO_ID), "Demo MO ID should exist in isolated data.");

        Map<String, Object> demoTa = findById(tas, "taId", TestSupport.TA_ID);
        TestSupport.assertNonBlank(demoTa.get("resumeFileName"), "Demo TA should keep resume metadata.");
        Path resumeFile = root.resolve("resumes").resolve(TestSupport.stringValue(demoTa.get("resumeFileName")));
        TestSupport.assertTrue(Files.exists(resumeFile), "Stored resume file should exist: " + resumeFile);

        boolean hasReviewState = false;
        boolean hasWithdrawn = false;
        boolean hasRejected = false;
        boolean hasRevocationRequested = false;
        for (Map<String, Object> application : applications) {
            String applicationId = TestSupport.stringValue(application.get("applicationId"));
            String taId = TestSupport.stringValue(application.get("taId"));
            String postingId = TestSupport.stringValue(application.get("postingId"));
            String status = TestSupport.stringValue(application.get("status"));

            TestSupport.assertNonBlank(applicationId, "Every application should have applicationId.");
            TestSupport.assertTrue(taIds.contains(taId), "Application should reference an existing TA: " + applicationId);
            TestSupport.assertTrue(postingIds.contains(postingId), "Application should reference an existing posting: " + applicationId);
            TestSupport.assertNonBlank(status, "Every application should have a status: " + applicationId);

            hasReviewState = hasReviewState || "SUBMITTED".equals(status) || "UNDER_REVIEW".equals(status);
            hasWithdrawn = hasWithdrawn || "WITHDRAWN".equals(status);
            hasRejected = hasRejected || "REJECTED".equals(status);
            hasRevocationRequested = hasRevocationRequested || "REVOCATION_REQUESTED".equals(status);
        }

        TestSupport.assertTrue(hasReviewState, "Workflow tests should keep at least one in-review or submitted application.");
        TestSupport.assertTrue(hasWithdrawn, "Workflow tests should persist a withdrawn application.");
        TestSupport.assertTrue(hasRejected, "Workflow tests should persist a rejected application.");
        TestSupport.assertTrue(hasRevocationRequested, "Workflow tests should persist a revocation-requested application.");

        TestSupport.assertTrue(ctx.applicationDataRepository.findAll().size() == applications.size(),
            "Application repository should re-read the same number of JSON records after writes.");
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> readJsonArray(Path path) throws Exception {
        TestSupport.assertTrue(Files.exists(path), "Expected JSON data file to exist: " + path);
        String json = Files.readString(path, StandardCharsets.UTF_8).trim();
        Object parsed = JsonUtils.parse(json.isEmpty() ? "[]" : json);
        TestSupport.assertTrue(parsed instanceof List<?>, "JSON data should be an array: " + path);
        return (List<Map<String, Object>>) parsed;
    }

    private Set<String> collectIds(List<Map<String, Object>> records, String... fields) {
        Set<String> ids = new HashSet<>();
        for (Map<String, Object> record : records) {
            for (String field : fields) {
                String value = TestSupport.stringValue(record.get(field));
                if (!value.isEmpty()) {
                    ids.add(value);
                }
            }
        }
        return ids;
    }

    private Map<String, Object> findById(List<Map<String, Object>> records, String field, String value) {
        for (Map<String, Object> record : records) {
            if (value.equals(TestSupport.stringValue(record.get(field)))) {
                return record;
            }
        }
        throw new AssertionError("Record not found: " + field + "=" + value);
    }
}
