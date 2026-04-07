import com.bupt.ta.exception.BusinessException;
import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;
import com.bupt.ta.service.AuthService;
import com.bupt.ta.service.UserService;
import com.bupt.ta.util.DataPaths;
import com.bupt.ta.util.ServiceRegistry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Admin Reset MO Password 后端命令行验证入口。
 */
public class AdminResetMOPasswordConsoleRunner {
    private static final DateTimeFormatter SUFFIX_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static void main(String[] args) {
        UserService userService = ServiceRegistry.userService();
        AuthService authService = ServiceRegistry.authService();

        String suffix = SUFFIX_FORMATTER.format(LocalDateTime.now());
        String username = "reset.mo.runner." + suffix;
        String fullName = "Reset MO Runner " + suffix;
        String staffId = "MO-RESET-" + suffix;
        String email = "reset.mo.runner." + suffix + "@example.com";
        String originalPassword = "Temp123!";
        String newPassword = "NewPass123!";

        Map<String, Object> createParams = new LinkedHashMap<String, Object>();
        createParams.put("username", username);
        createParams.put("fullName", fullName);
        createParams.put("staffId", staffId);
        createParams.put("email", email);
        createParams.put("department", "Software Engineering");
        createParams.put("phone", "+44 7000 888888");
        createParams.put("tempPassword", originalPassword);
        createParams.put("confirmPassword", originalPassword);

        User createdMo = userService.createMO(createParams);
        assertEquals(Role.MO, authService.authenticate(username, originalPassword, Role.MO).getRole(), "Original password should work before reset.");

        userService.resetPasswordByAdmin(createdMo.getId(), newPassword);

        expectAuthFailure(authService, username, originalPassword, Role.MO, "用户名、密码或角色不正确");
        assertEquals(Role.MO, authService.authenticate(username, newPassword, Role.MO).getRole(), "New password should work after reset.");

        expectResetFailure(userService, "ADMIN001", "Admin123!", "MO 账号不存在");
        expectResetFailure(userService, "MO-UNKNOWN", "Unknown123!", "MO 账号不存在");

        System.out.println("RESET_TARGET_MO_ID=" + createdMo.getId());
        System.out.println("RESET_TARGET_USERNAME=" + username);
        System.out.println("DATA_FILE=" + DataPaths.resolveUsersFile(Role.MO));
        System.out.println("NEGATIVE_CASES=PASS");
        System.out.println("ADMIN_RESET_MO_PASSWORD_TEST=PASS");
    }

    private static void expectAuthFailure(AuthService authService, String username, String password, Role role, String expectedMessage) {
        try {
            authService.authenticate(username, password, role);
            throw new IllegalStateException("Expected auth failure but succeeded.");
        } catch (BusinessException ex) {
            assertEquals(expectedMessage, ex.getMessage(), "Unexpected auth failure message.");
        }
    }

    private static void expectResetFailure(UserService userService, String userId, String newPassword, String expectedMessage) {
        try {
            userService.resetPasswordByAdmin(userId, newPassword);
            throw new IllegalStateException("Expected reset failure but succeeded for userId=" + userId);
        } catch (BusinessException ex) {
            assertEquals(expectedMessage, ex.getMessage(), "Unexpected reset failure message.");
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new IllegalStateException(message + " Expected=" + expected + ", Actual=" + actual);
        }
    }
}

