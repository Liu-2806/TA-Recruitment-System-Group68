import com.bupt.ta.exception.BusinessException;
import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;
import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.repository.file.JsonUserRepository;
import com.bupt.ta.service.UserService;
import com.bupt.ta.service.impl.UserServiceImpl;
import com.bupt.ta.util.DataPaths;
import com.bupt.ta.util.JsonUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Admin MO Detail 后端命令行验证入口。
 */
public class AdminMODetailConsoleRunner {
    private static final DateTimeFormatter SUFFIX_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static void main(String[] args) throws Exception {
        UserRepository userRepository = new JsonUserRepository();
        UserService userService = new UserServiceImpl(userRepository);

        String suffix = SUFFIX_FORMATTER.format(LocalDateTime.now());
        User targetMo = userService.createMO(buildMoParams(
                "mo.detail.target." + suffix,
                "MO Detail Target " + suffix,
                "MO-DETAIL-TARGET-" + suffix,
                "mo.detail.target." + suffix + "@example.com",
                "Software Engineering",
                "+44 7000 111111",
                "Original MO description"
        ));
        User duplicateEmailMo = userService.createMO(buildMoParams(
                "mo.detail.duplicate." + suffix,
                "MO Detail Duplicate " + suffix,
                "MO-DETAIL-DUP-" + suffix,
                "mo.detail.duplicate." + suffix + "@example.com",
                "Computer Science",
                "+44 7000 222222",
                "Duplicate email holder"
        ));

        seedPostings(targetMo.getMoId());

        User detailBeforeUpdate = userService.getMOById(targetMo.getMoId());
        assertEquals(targetMo.getMoId(), detailBeforeUpdate.getMoId(), "GET detail should expose moId.");
        assertEquals("MO Detail Target " + suffix, detailBeforeUpdate.getFullName(), "GET detail should expose fullName.");
        assertEquals("MO-DETAIL-TARGET-" + suffix, detailBeforeUpdate.getStaffId(), "GET detail should expose staffId.");
        assertEquals(("mo.detail.target." + suffix + "@example.com").toLowerCase(Locale.ENGLISH), detailBeforeUpdate.getEmail(), "GET detail should expose email.");
        assertEquals("Software Engineering", detailBeforeUpdate.getDepartment(), "GET detail should expose department.");
        assertEquals("+44 7000 111111", detailBeforeUpdate.getPhone(), "GET detail should expose phone.");
        assertEquals("Original MO description", detailBeforeUpdate.getDescription(), "GET detail should expose description.");
        assertEquals("ACTIVE", detailBeforeUpdate.getStatus(), "Newly created MO should default to ACTIVE.");
        assertTrue(notBlank(detailBeforeUpdate.getCreatedAt()), "GET detail should expose createdAt.");
        assertEquals(2, detailBeforeUpdate.getPostingCount(), "GET detail should aggregate postingCount from postings.json.");

        Map<String, Object> updateParams = new LinkedHashMap<String, Object>();
        updateParams.put("moUserId", targetMo.getMoId());
        updateParams.put("name", "Updated MO Detail " + suffix);
        updateParams.put("email", "mo.detail.updated." + suffix + "@example.com");
        updateParams.put("phone", "+44 7000 999999");
        updateParams.put("description", "Updated MO description");
        updateParams.put("status", "INACTIVE");
        userService.updateMOByAdmin(updateParams);

        User detailAfterUpdate = userService.getMOById(targetMo.getMoId());
        assertEquals(targetMo.getMoId(), detailAfterUpdate.getMoId(), "Updated detail should keep the same moId.");
        assertEquals("Updated MO Detail " + suffix, detailAfterUpdate.getFullName(), "POST update should accept name and map it to fullName.");
        assertEquals("mo.detail.updated." + suffix + "@example.com", detailAfterUpdate.getEmail(), "POST update should persist the new email.");
        assertEquals("+44 7000 999999", detailAfterUpdate.getPhone(), "POST update should persist the new phone.");
        assertEquals("Updated MO description", detailAfterUpdate.getDescription(), "POST update should persist the new description.");
        assertEquals("INACTIVE", detailAfterUpdate.getStatus(), "POST update should persist the new status.");
        assertEquals(detailBeforeUpdate.getCreatedAt(), detailAfterUpdate.getCreatedAt(), "createdAt should remain stable after update.");
        assertEquals(2, detailAfterUpdate.getPostingCount(), "postingCount should remain stable after update.");

        Map<String, Object> persistedRecord = readMoRecord(targetMo.getMoId());
        assertEquals("Updated MO Detail " + suffix, String.valueOf(persistedRecord.get("fullName")), "Persisted fullName should match updated value.");
        assertEquals("mo.detail.updated." + suffix + "@example.com", String.valueOf(persistedRecord.get("email")), "Persisted email should match updated value.");
        assertEquals("+44 7000 999999", String.valueOf(persistedRecord.get("phone")), "Persisted phone should match updated value.");
        assertEquals("Updated MO description", String.valueOf(persistedRecord.get("description")), "Persisted description should match updated value.");
        assertEquals(Boolean.FALSE, persistedRecord.get("active"), "Persisted active flag should become false after INACTIVE update.");

        expectUpdateFailure(buildUpdateParams(targetMo.getMoId(), "Duplicate Email", String.valueOf(duplicateEmailMo.getEmail()), "+44 7000 333333", "Desc", "ACTIVE"), "邮箱已存在", userService);
        expectUpdateFailure(buildUpdateParams(targetMo.getMoId(), "Invalid Status", "mo.detail.invalid." + suffix + "@example.com", "+44 7000 444444", "Desc", "UNKNOWN"), "MO 状态无效", userService);
        expectUpdateFailure(buildUpdateParams("MO999", "Missing User", "missing.user." + suffix + "@example.com", "+44 7000 555555", "Desc", "ACTIVE"), "MO 账号不存在", userService);

        System.out.println("DATA_FILE=" + DataPaths.resolveUsersFile(Role.MO));
        System.out.println("POSTINGS_FILE=" + DataPaths.resolvePostingsFile());
        System.out.println("DETAIL_BEFORE_UPDATE=" + JsonUtils.toJson(describe(detailBeforeUpdate)));
        System.out.println("DETAIL_AFTER_UPDATE=" + JsonUtils.toJson(describe(detailAfterUpdate)));
        System.out.println("PERSISTED_JSON=" + JsonUtils.toJson(persistedRecord));
        System.out.println("NEGATIVE_CASES=PASS");
        System.out.println("ADMIN_MO_DETAIL_TEST=PASS");
    }

    private static Map<String, Object> buildMoParams(String username, String fullName, String staffId, String email, String department, String phone, String description) {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("username", username);
        params.put("fullName", fullName);
        params.put("staffId", staffId);
        params.put("email", email.toLowerCase(Locale.ENGLISH));
        params.put("department", department);
        params.put("phone", phone);
        params.put("description", description);
        params.put("tempPassword", "Temp123!");
        params.put("confirmPassword", "Temp123!");
        return params;
    }

    private static Map<String, Object> buildUpdateParams(String moUserId, String name, String email, String phone, String description, String status) {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("moUserId", moUserId);
        params.put("name", name);
        params.put("email", email);
        params.put("phone", phone);
        params.put("description", description);
        params.put("status", status);
        return params;
    }

    private static void seedPostings(String targetMoId) throws Exception {
        List<Map<String, Object>> postings = new ArrayList<Map<String, Object>>();
        postings.add(buildPosting("POST-DETAIL-001", targetMoId, "MO Detail Course A"));
        postings.add(buildPosting("POST-DETAIL-002", targetMoId, "MO Detail Course B"));
        postings.add(buildPosting("POST-DETAIL-003", "MO-OTHER", "Other MO Course"));

        Path postingsFile = DataPaths.resolvePostingsFile();
        Files.createDirectories(postingsFile.getParent());
        Files.write(postingsFile, JsonUtils.toJson(postings).getBytes(StandardCharsets.UTF_8));
    }

    private static Map<String, Object> buildPosting(String postingId, String moId, String courseName) {
        Map<String, Object> posting = new LinkedHashMap<String, Object>();
        posting.put("postingId", postingId);
        posting.put("courseCode", postingId.replace("POST-", "CS"));
        posting.put("courseName", courseName);
        posting.put("moId", moId);
        posting.put("moName", "Test MO Owner");
        posting.put("vacancies", 1);
        posting.put("applicationCount", 0);
        posting.put("deadline", "2026-04-30");
        posting.put("description", courseName + " description");
        posting.put("requiredSkills", new ArrayList<String>());
        posting.put("roleResponsibilities", new ArrayList<String>());
        posting.put("department", "Test Department");
        posting.put("moduleType", "Lab Module");
        posting.put("estimatedWorkloadHours", 4);
        posting.put("status", "OPEN");
        return posting;
    }

    private static Map<String, Object> readMoRecord(String moId) throws Exception {
        Path moFile = DataPaths.resolveUsersFile(Role.MO);
        String rawJson = new String(Files.readAllBytes(moFile), StandardCharsets.UTF_8);
        Object parsed = JsonUtils.parse(rawJson);
        if (!(parsed instanceof List)) {
            throw new IllegalStateException("MO JSON is not an array: " + moFile);
        }
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> records = (List<Map<String, Object>>) parsed;
        for (Map<String, Object> record : records) {
            String recordId = record.get("id") == null ? null : String.valueOf(record.get("id"));
            String recordMoId = record.get("moId") == null ? null : String.valueOf(record.get("moId"));
            if (moId.equals(recordId) || moId.equals(recordMoId)) {
                return record;
            }
        }
        throw new IllegalStateException("MO JSON does not contain created ID: " + moId + " in " + moFile);
    }

    private static Map<String, Object> describe(User user) {
        Map<String, Object> detail = new LinkedHashMap<String, Object>();
        detail.put("moId", user.getMoId());
        detail.put("fullName", user.getFullName());
        detail.put("staffId", user.getStaffId());
        detail.put("email", user.getEmail());
        detail.put("phone", user.getPhone());
        detail.put("department", user.getDepartment());
        detail.put("description", user.getDescription());
        detail.put("status", user.getStatus());
        detail.put("createdAt", user.getCreatedAt());
        detail.put("postingCount", user.getPostingCount());
        return detail;
    }

    private static void expectUpdateFailure(Map<String, Object> params, String expectedMessage, UserService userService) {
        try {
            userService.updateMOByAdmin(params);
            throw new IllegalStateException("Expected failure but update succeeded: " + expectedMessage);
        } catch (BusinessException ex) {
            if (!expectedMessage.equals(ex.getMessage())) {
                throw new IllegalStateException("Expected message '" + expectedMessage + "' but was '" + ex.getMessage() + "'.", ex);
            }
        }
    }

    private static boolean notBlank(String value) {
        return value != null && !value.trim().isEmpty();
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

