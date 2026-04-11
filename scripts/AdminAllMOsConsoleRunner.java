import com.bupt.ta.dto.PageResult;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Admin All MOs 后端命令行验证入口。
 */
public class AdminAllMOsConsoleRunner {
    private static final DateTimeFormatter SUFFIX_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static void main(String[] args) throws Exception {
        UserRepository userRepository = new JsonUserRepository();
        UserService userService = new UserServiceImpl(userRepository);

        String suffix = SUFFIX_FORMATTER.format(LocalDateTime.now());
        User alpha = userService.createMO(buildMoParams(
                "all.mos.alpha." + suffix,
                "All MOs Alpha " + suffix,
                "MO-ALLMOS-A-" + suffix,
                "all.mos.alpha." + suffix + "@example.com",
                "Software Engineering",
                "+44 7000 000001"
        ));
        User beta = userService.createMO(buildMoParams(
                "all.mos.beta." + suffix,
                "All MOs Beta " + suffix,
                "MO-ALLMOS-B-" + suffix,
                "all.mos.beta." + suffix + "@example.com",
                "Computer Science",
                "+44 7000 000002"
        ));
        User gamma = userService.createMO(buildMoParams(
                "all.mos.gamma." + suffix,
                "All MOs Gamma " + suffix,
                "MO-ALLMOS-C-" + suffix,
                "all.mos.gamma." + suffix + "@example.com",
                "Data Science",
                "+44 7000 000003"
        ));

        markInactive(userRepository, beta.getId());

        PageResult<User> keywordPage1 = userService.searchMOs(buildQuery(suffix, null, "fullNameAsc", 1, 2));
        assertEquals(3L, keywordPage1.getTotal(), "Keyword filter should find exactly the 3 newly created MOs.");
        assertEquals(1, keywordPage1.getPage(), "Page number should echo the request.");
        assertEquals(2, keywordPage1.getSize(), "Page size should echo the request.");
        assertEquals(2, keywordPage1.getRecords().size(), "First page should contain 2 records.");
        assertEquals(alpha.getMoId(), keywordPage1.getRecords().get(0).getMoId(), "Ascending sort should return Alpha first.");
        assertEquals(beta.getMoId(), keywordPage1.getRecords().get(1).getMoId(), "Ascending sort should return Beta second.");
        assertMoFields(keywordPage1.getRecords().get(0), "ACTIVE");
        assertMoFields(keywordPage1.getRecords().get(1), "INACTIVE");

        PageResult<User> keywordPage2 = userService.searchMOs(buildQuery(suffix, null, "fullNameAsc", 2, 2));
        assertEquals(3L, keywordPage2.getTotal(), "Second page total should remain unchanged.");
        assertEquals(1, keywordPage2.getRecords().size(), "Second page should contain the last record only.");
        assertEquals(gamma.getMoId(), keywordPage2.getRecords().get(0).getMoId(), "Second page should return Gamma.");
        assertMoFields(keywordPage2.getRecords().get(0), "ACTIVE");

        PageResult<User> activeOnly = userService.searchMOs(buildQuery(suffix, "ACTIVE", "fullNameAsc", 1, 10));
        assertEquals(2L, activeOnly.getTotal(), "ACTIVE status filter should return Alpha and Gamma only.");
        assertEquals(alpha.getMoId(), activeOnly.getRecords().get(0).getMoId(), "ACTIVE ascending sort should start from Alpha.");
        assertEquals(gamma.getMoId(), activeOnly.getRecords().get(1).getMoId(), "ACTIVE ascending sort should end with Gamma.");

        PageResult<User> inactiveOnly = userService.searchMOs(buildQuery(suffix, "INACTIVE", "fullNameAsc", 1, 10));
        assertEquals(1L, inactiveOnly.getTotal(), "INACTIVE status filter should return Beta only.");
        assertEquals(beta.getMoId(), inactiveOnly.getRecords().get(0).getMoId(), "INACTIVE filter should return Beta.");
        assertEquals("INACTIVE", inactiveOnly.getRecords().get(0).getStatus(), "Inactive record should expose INACTIVE status.");

        PageResult<User> descPage = userService.searchMOs(buildQuery(suffix, null, "fullNameDesc", 1, 10));
        assertEquals(gamma.getMoId(), descPage.getRecords().get(0).getMoId(), "Descending sort should return Gamma first.");

        PageResult<User> staffIdKeyword = userService.searchMOs(buildQuery("MO-ALLMOS-B-" + suffix, null, "createdAtDesc", 1, 10));
        assertEquals(1L, staffIdKeyword.getTotal(), "Staff ID keyword should match exactly one MO.");
        assertEquals(beta.getMoId(), staffIdKeyword.getRecords().get(0).getMoId(), "Staff ID keyword should find Beta.");

        Map<String, Object> persistedBeta = readMoRecord(userRepository, beta.getId());
        assertEquals(Boolean.FALSE, persistedBeta.get("active"), "Persisted Beta record should be inactive in mo.json.");

        System.out.println("DATA_FILE=" + DataPaths.resolveUsersFile(Role.MO));
        System.out.println("KEYWORD_PAGE_1=" + JsonUtils.toJson(describeUsers(keywordPage1.getRecords())));
        System.out.println("KEYWORD_PAGE_2=" + JsonUtils.toJson(describeUsers(keywordPage2.getRecords())));
        System.out.println("ACTIVE_ONLY_TOTAL=" + activeOnly.getTotal());
        System.out.println("INACTIVE_ONLY_TOTAL=" + inactiveOnly.getTotal());
        System.out.println("DESC_FIRST_MO_ID=" + descPage.getRecords().get(0).getMoId());
        System.out.println("STAFF_ID_QUERY_TOTAL=" + staffIdKeyword.getTotal());
        System.out.println("ADMIN_ALL_MOS_TEST=PASS");
    }

    private static Map<String, Object> buildMoParams(String username, String fullName, String staffId, String email, String department, String phone) {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("username", username);
        params.put("fullName", fullName);
        params.put("staffId", staffId);
        params.put("email", email.toLowerCase(Locale.ENGLISH));
        params.put("department", department);
        params.put("phone", phone);
        params.put("tempPassword", "Temp123!");
        params.put("confirmPassword", "Temp123!");
        return params;
    }

    private static Map<String, Object> buildQuery(String keyword, String status, String sortBy, int page, int size) {
        Map<String, Object> query = new LinkedHashMap<String, Object>();
        query.put("keyword", keyword);
        query.put("status", status);
        query.put("sortBy", sortBy);
        query.put("page", page);
        query.put("size", size);
        return query;
    }

    private static void markInactive(UserRepository userRepository, String moId) {
        Map<String, Object> betaRecord = userRepository.findById(Role.MO, moId);
        if (betaRecord == null) {
            throw new IllegalStateException("Cannot find created MO by ID: " + moId);
        }
        betaRecord.put("active", Boolean.FALSE);
        userRepository.update(Role.MO, betaRecord);
    }

    private static Map<String, Object> readMoRecord(UserRepository userRepository, String moId) throws Exception {
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

    private static void assertMoFields(User user, String expectedStatus) {
        assertTrue(user.getMoId() != null && user.getMoId().startsWith("MO"), "MO record should expose moId.");
        assertTrue(notBlank(user.getFullName()), "MO record should expose fullName.");
        assertTrue(notBlank(user.getStaffId()), "MO record should expose staffId.");
        assertTrue(notBlank(user.getEmail()), "MO record should expose email.");
        assertTrue(notBlank(user.getDepartment()), "MO record should expose department.");
        assertTrue(notBlank(user.getPhone()), "MO record should expose phone.");
        assertEquals(expectedStatus, user.getStatus(), "MO record should expose expected status.");
    }

    private static Object describeUsers(List<User> users) {
        java.util.List<Map<String, Object>> items = new java.util.ArrayList<Map<String, Object>>();
        for (User user : users) {
            Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("moId", user.getMoId());
            item.put("fullName", user.getFullName());
            item.put("staffId", user.getStaffId());
            item.put("email", user.getEmail());
            item.put("department", user.getDepartment());
            item.put("phone", user.getPhone());
            item.put("status", user.getStatus());
            items.add(item);
        }
        return items;
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


