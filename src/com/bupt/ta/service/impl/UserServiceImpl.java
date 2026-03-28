package com.bupt.ta.service.impl;

import com.bupt.ta.dto.PageResult;
import com.bupt.ta.exception.BusinessException;
import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;
import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 用户服务实现。
 */
public class UserServiceImpl implements UserService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerTA(Map<String, Object> params) {
        String fullName = firstNonBlank(params, "fullName", "name");
        String studentId = firstNonBlank(params, "studentId");
        String email = firstNonBlank(params, "email");
        String password = firstNonBlank(params, "password");
        String confirmPassword = firstNonBlank(params, "confirmPassword");
        String majorProgram = firstNonBlank(params, "majorProgram");
        String academicYear = firstNonBlank(params, "academicYear", "yearOfStudy");
        String username = firstNonBlank(params, "username");
        String agreed = firstNonBlank(params, "agreeTerms");

        requireNotBlank(fullName, "姓名不能为空");
        requireNotBlank(studentId, "学号不能为空");
        requireNotBlank(email, "邮箱不能为空");
        requireValidEmail(email);
        requireNotBlank(password, "密码不能为空");
        requireMinPasswordLength(password);

        if (!password.equals(confirmPassword)) {
            throw new BusinessException("两次输入的密码不一致");
        }

        if (agreed != null && !"true".equalsIgnoreCase(agreed) && !"on".equalsIgnoreCase(agreed)) {
            throw new BusinessException("请先同意用户协议和隐私政策");
        }

        String normalizedUsername = isBlank(username) ? deriveUsername(email) : username.trim();
        validateUserUniqueness(normalizedUsername, email, studentId);

        String nextTaId = userRepository.nextUserId(Role.TA);
        Map<String, Object> record = new LinkedHashMap<String, Object>();
        record.put("id", nextTaId);
        record.put("taId", nextTaId);
        record.put("username", normalizedUsername);
        record.put("email", email.trim().toLowerCase(Locale.ENGLISH));
        record.put("fullName", fullName.trim());
        record.put("displayName", fullName.trim());
        record.put("studentId", studentId.trim());
        record.put("phone", "");
        record.put("intro", "");
        record.put("skills", new ArrayList<String>());
        record.put("majorProgram", majorProgram);
        record.put("academicYear", academicYear);
        record.put("role", Role.TA.name());
        record.put("password", password);
        record.put("active", Boolean.TRUE);
        record.put("resumeFileName", null);
        record.put("resumeUploadedAt", null);
        record.put("extractedResume", null);
        record.put("createdAt", DATE_TIME_FORMATTER.format(LocalDateTime.now()));
        record.put("updatedAt", DATE_TIME_FORMATTER.format(LocalDateTime.now()));

        return toUser(userRepository.save(Role.TA, record));
    }

    @Override
    public User createMO(Map<String, Object> params) {
        String fullName = firstNonBlank(params, "fullName", "name");
        String staffId = firstNonBlank(params, "staffId");
        String email = firstNonBlank(params, "email");
        String department = firstNonBlank(params, "department");
        String phone = firstNonBlank(params, "phone");
        String description = firstNonBlank(params, "description");
        String initialPassword = firstNonBlank(params, "tempPassword", "initialPassword", "password");
        String confirmPassword = firstNonBlank(params, "confirmPassword");
        String username = firstNonBlank(params, "username");

        requireNotBlank(fullName, "MO 姓名不能为空");
        requireNotBlank(staffId, "工号不能为空");
        requireNotBlank(email, "邮箱不能为空");
        requireValidEmail(email);
        requireNotBlank(initialPassword, "初始密码不能为空");
        requireNotBlank(confirmPassword, "确认密码不能为空");
        requireMinPasswordLength(initialPassword);

        if (!initialPassword.equals(confirmPassword)) {
            throw new BusinessException("两次输入的密码不一致");
        }

        String normalizedUsername = isBlank(username) ? deriveUsername(email) : username.trim();
        if (userRepository.existsAcrossRoles("username", normalizedUsername)) {
            throw new BusinessException("用户名已存在");
        }
        if (userRepository.existsAcrossRoles("email", email)) {
            throw new BusinessException("邮箱已存在");
        }
        if (userRepository.existsByField(Role.MO, "staffId", staffId)) {
            throw new BusinessException("工号已存在");
        }

        String nextMoId = userRepository.nextUserId(Role.MO);
        Map<String, Object> record = new LinkedHashMap<String, Object>();
        record.put("id", nextMoId);
        record.put("moId", nextMoId);
        record.put("username", normalizedUsername);
        record.put("email", email.trim().toLowerCase(Locale.ENGLISH));
        record.put("fullName", fullName.trim());
        record.put("displayName", fullName.trim());
        record.put("staffId", staffId.trim());
        record.put("department", department == null ? null : department.trim());
        record.put("phone", phone == null ? null : phone.trim());
        record.put("description", description == null ? null : description.trim());
        record.put("role", Role.MO.name());
        record.put("password", initialPassword);
        record.put("active", Boolean.TRUE);
        record.put("createdAt", DATE_TIME_FORMATTER.format(LocalDateTime.now()));
        record.put("updatedAt", DATE_TIME_FORMATTER.format(LocalDateTime.now()));

        return toUser(userRepository.save(Role.MO, record));
    }

    @Override
    public PageResult<User> searchMOs(Map<String, Object> query) {
        String keyword = firstNonBlank(query, "keyword");
        String department = firstNonBlank(query, "department");
        List<Map<String, Object>> records = userRepository.findAllByRole(Role.MO);
        List<User> users = new ArrayList<User>();
        for (Map<String, Object> record : records) {
            if (!matchesKeyword(record, keyword) || !matchesDepartment(record, department)) {
                continue;
            }
            users.add(toUser(record));
        }

        PageResult<User> result = new PageResult<User>();
        result.setRecords(users);
        result.setPage(parsePositiveInt(query == null ? null : query.get("page"), 1));
        result.setSize(users.size());
        result.setTotal(users.size());
        return result;
    }

    @Override
    public User getMOById(String moUserId) {
        Map<String, Object> record = userRepository.findById(Role.MO, moUserId);
        if (record == null) {
            throw new BusinessException("MO 账号不存在");
        }
        return toUser(record);
    }

    @Override
    public void updateMOByAdmin(Map<String, Object> params) {
        String userId = firstNonBlank(params, "userId", "id", "moUserId");
        requireNotBlank(userId, "缺少 MO 用户 ID");

        Map<String, Object> existing = userRepository.findById(Role.MO, userId);
        if (existing == null) {
            throw new BusinessException("MO 账号不存在");
        }

        String fullName = firstNonBlank(params, "fullName", "name");
        String department = firstNonBlank(params, "department");
        String phone = firstNonBlank(params, "phone");
        String description = firstNonBlank(params, "description");

        if (!isBlank(fullName)) {
            existing.put("fullName", fullName.trim());
            existing.put("displayName", fullName.trim());
        }
        if (!isBlank(department)) {
            existing.put("department", department.trim());
        }
        if (!isBlank(phone)) {
            existing.put("phone", phone.trim());
        }
        if (!isBlank(description)) {
            existing.put("description", description.trim());
        }
        existing.put("updatedAt", DATE_TIME_FORMATTER.format(LocalDateTime.now()));
        userRepository.update(Role.MO, existing);
    }

    @Override
    public void resetPasswordByAdmin(String userId, String rawPassword) {
        requireNotBlank(userId, "缺少用户 ID");
        requireNotBlank(rawPassword, "新密码不能为空");
        requireMinPasswordLength(rawPassword);

        Map<String, Object> record = userRepository.findById(Role.MO, userId);
        if (record == null) {
            record = userRepository.findById(Role.ADMIN, userId);
        }
        if (record == null) {
            throw new BusinessException("目标账号不存在");
        }

        Role role = Role.valueOf(String.valueOf(record.get("role")));
        record.put("password", rawPassword);
        record.remove("passwordSalt");
        record.remove("passwordHash");
        record.put("updatedAt", DATE_TIME_FORMATTER.format(LocalDateTime.now()));
        userRepository.update(role, record);
    }

    private void validateUserUniqueness(String username, String email, String studentId) {
        if (userRepository.existsAcrossRoles("username", username)) {
            throw new BusinessException("用户名已存在");
        }
        if (userRepository.existsAcrossRoles("email", email)) {
            throw new BusinessException("邮箱已存在");
        }
        if (userRepository.existsByField(Role.TA, "studentId", studentId)) {
            throw new BusinessException("学号已存在");
        }
    }

    private User toUser(Map<String, Object> record) {
        User user = new User();
        user.setId(firstNonBlank(record, "id", "taId", "moId"));
        user.setUsername(firstNonBlank(record, "username"));
        user.setDisplayName(firstNonBlank(record, "displayName", "fullName"));
        user.setRole(Role.valueOf(firstNonBlank(record, "role")));
        return user;
    }

    private boolean matchesKeyword(Map<String, Object> record, String keyword) {
        if (isBlank(keyword)) {
            return true;
        }
        String lowered = keyword.trim().toLowerCase(Locale.ENGLISH);
        return contains(record.get("fullName"), lowered)
                || contains(record.get("email"), lowered)
                || contains(record.get("staffId"), lowered)
                || contains(record.get("department"), lowered);
    }

    private boolean matchesDepartment(Map<String, Object> record, String department) {
        if (isBlank(department)) {
            return true;
        }
        return department.trim().equalsIgnoreCase(firstNonBlank(record, "department"));
    }

    private boolean contains(Object fieldValue, String keyword) {
        if (fieldValue == null) {
            return false;
        }
        return String.valueOf(fieldValue).toLowerCase(Locale.ENGLISH).contains(keyword);
    }

    private String deriveUsername(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            return email.trim();
        }
        return email.substring(0, atIndex).trim();
    }

    private void requireNotBlank(String value, String message) {
        if (isBlank(value)) {
            throw new BusinessException(message);
        }
    }

    private void requireValidEmail(String email) {
        if (isBlank(email) || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new BusinessException("邮箱格式不正确");
        }
    }

    private void requireMinPasswordLength(String password) {
        if (password == null || password.length() < 6) {
            throw new BusinessException("密码长度不能少于 6 位");
        }
    }

    private String firstNonBlank(Map<String, Object> values, String... keys) {
        if (values == null || keys == null) {
            return null;
        }
        for (String key : keys) {
            Object value = values.get(key);
            if (value != null) {
                String text = String.valueOf(value).trim();
                if (!text.isEmpty()) {
                    return text;
                }
            }
        }
        return null;
    }

    private int parsePositiveInt(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(String.valueOf(value));
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
