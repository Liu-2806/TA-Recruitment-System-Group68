import com.bupt.ta.dto.PageResult;
import com.bupt.ta.exception.BusinessException;
import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;
import com.bupt.ta.service.AuthService;
import com.bupt.ta.service.UserService;
import com.bupt.ta.util.DataPaths;
import com.bupt.ta.util.JsonUtils;
import com.bupt.ta.util.ServiceRegistry;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin Create MO 后端命令行验证入口。
 */
public class AdminCreateMOConsoleRunner {
    private static final DateTimeFormatter SUFFIX_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static void main(String[] args) throws Exception {
        UserService userService = ServiceRegistry.userService();
        AuthService authService = ServiceRegistry.authService();
        String suffix = SUFFIX_FORMATTER.format(LocalDateTime.now());
        String username = "create.mo.runner." + suffix;
        String fullName = "Create MO Runner " + suffix;
        String staffId = "MO-RUNNER-" + suffix;
        String email = "create.mo.runner." + suffix + "@example.com";
        String password = "Temp123!";

        long beforeTotal = userService.searchMOs(new LinkedHashMap<String, Object>()).getTotal();

        Map<String, Object> successParams = new LinkedHashMap<String, Object>();
        successParams.put("username", username);
        successParams.put("fullName", fullName);
        successParams.put("staffId", staffId);
        successParams.put("email", email);
        successParams.put("department", "Software Engineering");
        successParams.put("phone", "+44 7000 123456");
        successParams.put("tempPassword", password);
        successParams.put("confirmPassword", password);

        User createdMo = userService.createMO(successParams);
        User moLogin = authService.authenticate(username, password, Role.MO);

        PageResult<User> mosPage = userService.searchMOs(new LinkedHashMap<String, Object>());
        assertTrue(createdMo.getId() != null && createdMo.getId().startsWith("MO"), "Created MO ID should start with MO.");
        assertEquals(Role.MO, moLogin.getRole(), "Created MO should be able to log in.");
        assertEquals(beforeTotal + 1L, mosPage.getTotal(), "MO search total should increase by 1 after one successful creation.");

        Map<String, Object> persistedRecord = readMoRecordByStaffId(staffId);
        assertEquals(createdMo.getId(), String.valueOf(persistedRecord.get("moId")), "Persisted moId should match created ID.");
        assertEquals(username, String.valueOf(persistedRecord.get("username")), "Persisted username should match.");
        assertEquals(fullName, String.valueOf(persistedRecord.get("fullName")), "Persisted fullName should match.");
        assertEquals(staffId, String.valueOf(persistedRecord.get("staffId")), "Persisted staffId should match.");
        assertEquals(email, String.valueOf(persistedRecord.get("email")), "Persisted email should match.");
        assertEquals("Software Engineering", String.valueOf(persistedRecord.get("department")), "Persisted department should match.");
        assertEquals("+44 7000 123456", String.valueOf(persistedRecord.get("phone")), "Persisted phone should match.");
        assertEquals(Role.MO.name(), String.valueOf(persistedRecord.get("role")), "Persisted role should be MO.");

        expectFailure(buildMoParams(username, "MO-NEG-STAFF-" + suffix, "other.username." + suffix + "@example.com", password, password), "用户名已存在");
        expectFailure(buildMoParams("create.mo.runner.staff." + suffix, staffId, "other.staff." + suffix + "@example.com", password, password), "工号已存在");
        expectFailure(buildMoParams("create.mo.runner.email." + suffix, "MO-NEG-EMAIL-" + suffix, email, password, password), "邮箱已存在");
        expectFailure(buildMoParams("create.mo.runner.mismatch." + suffix, "MO-NEG-MISMATCH-" + suffix, "mismatch." + suffix + "@example.com", password, "Mismatch123!"), "两次输入的密码不一致");

        System.out.println("CREATED_MO_ID=" + createdMo.getId());
        System.out.println("CREATED_USERNAME=" + username);
        System.out.println("CREATED_STAFF_ID=" + staffId);
        System.out.println("DATA_FILE=" + DataPaths.resolveUsersFile(Role.MO));
        System.out.println("MO_LOGIN_ROLE=" + moLogin.getRole());
        System.out.println("SEARCH_TOTAL=" + mosPage.getTotal());
        System.out.println("PERSISTED_JSON=" + JsonUtils.toJson(persistedRecord));
        System.out.println("NEGATIVE_CASES=PASS");
        System.out.println("ADMIN_CREATE_MO_TEST=PASS");
    }

    private static Map<String, Object> buildMoParams(String username, String staffId, String email, String tempPassword, String confirmPassword) {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("username", username);
        params.put("fullName", "Create MO Runner");
        params.put("staffId", staffId);
        params.put("email", email);
        params.put("department", "Software Engineering");
        params.put("phone", "+44 7000 123456");
        params.put("tempPassword", tempPassword);
        params.put("confirmPassword", confirmPassword);
        return params;
    }

    private static Map<String, Object> readMoRecordByStaffId(String staffId) throws Exception {
        Path moFile = DataPaths.resolveUsersFile(Role.MO);
        String rawJson = new String(Files.readAllBytes(moFile), StandardCharsets.UTF_8);
        Object parsed = JsonUtils.parse(rawJson);
        if (!(parsed instanceof List)) {
            throw new IllegalStateException("MO JSON is not an array: " + moFile);
        }
        List<?> records = (List<?>) parsed;
        List<Map<String, Object>> normalizedRecords = new ArrayList<Map<String, Object>>();
        for (Object item : records) {
            if (item instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> record = (Map<String, Object>) item;
                normalizedRecords.add(record);
            }
        }
        for (Map<String, Object> record : normalizedRecords) {
            if (staffId.equals(String.valueOf(record.get("staffId")))) {
                return record;
            }
        }
        throw new IllegalStateException("MO JSON does not contain the created staffId: " + staffId + " in " + moFile);
    }

    private static void expectFailure(Map<String, Object> params, String expectedMessage) {
        try {
            ServiceRegistry.userService().createMO(params);
            throw new IllegalStateException("Expected failure but request succeeded: " + expectedMessage);
        } catch (BusinessException ex) {
            if (!expectedMessage.equals(ex.getMessage())) {
                throw new IllegalStateException("Expected message '" + expectedMessage + "' but was '" + ex.getMessage() + "'.", ex);
            }
        }
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

