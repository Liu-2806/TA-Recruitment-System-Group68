package com.bupt.ta.service.impl;

import com.bupt.ta.dto.PageResult;
import com.bupt.ta.exception.BusinessException;
import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;
import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.service.UserService;
import com.bupt.ta.util.DataPaths;
import com.bupt.ta.util.JsonUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        String status = firstNonBlank(query, "status");
        String department = firstNonBlank(query, "department");
        String sortBy = firstNonBlank(query, "sortBy");
        int page = parsePositiveInt(query == null ? null : query.get("page"), 1);
        int size = parsePositiveInt(query == null ? null : query.get("size"), 10);
        List<Map<String, Object>> records = userRepository.findAllByRole(Role.MO);
        List<Map<String, Object>> matchedRecords = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> record : records) {
            if (!matchesKeyword(record, keyword)
                    || !matchesStatus(record, status)
                    || !matchesDepartment(record, department)) {
                continue;
            }
            matchedRecords.add(record);
        }

        sortMoRecords(matchedRecords, sortBy);

        long total = matchedRecords.size();
        int fromIndex = Math.min((page - 1) * size, matchedRecords.size());
        int toIndex = Math.min(fromIndex + size, matchedRecords.size());

        List<User> users = new ArrayList<User>();
        for (Map<String, Object> record : matchedRecords.subList(fromIndex, toIndex)) {
            users.add(toUser(record));
        }

        PageResult<User> result = new PageResult<User>();
        result.setRecords(users);
        result.setPage(page);
        result.setSize(size);
        result.setTotal(total);
        return result;
    }

    @Override
    public User getMOById(String moUserId) {
        Map<String, Object> record = userRepository.findById(Role.MO, moUserId);
        if (record == null) {
            throw new BusinessException("MO 账号不存在");
        }
        User user = toUser(record);
        user.setPostingCount(countPostingRecords(user.getMoId()));
        return user;
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
        String email = firstNonBlank(params, "email");
        String department = firstNonBlank(params, "department");
        String phone = normalizeOptionalText(params, "phone");
        String description = normalizeOptionalText(params, "description");
        String status = firstNonBlank(params, "status");

        requireNotBlank(fullName, "MO 姓名不能为空");
        requireNotBlank(email, "邮箱不能为空");
        requireValidEmail(email);

        String currentEmail = firstNonBlank(existing, "email");
        String normalizedEmail = email.trim().toLowerCase(Locale.ENGLISH);
        if (!normalizedEmail.equalsIgnoreCase(currentEmail) && userRepository.existsAcrossRoles("email", normalizedEmail)) {
            throw new BusinessException("邮箱已存在");
        }

        existing.put("fullName", fullName.trim());
        existing.put("displayName", fullName.trim());
        existing.put("email", normalizedEmail);

        if (params != null && params.containsKey("department")) {
            existing.put("department", department);
        }
        if (params != null && params.containsKey("phone")) {
            existing.put("phone", phone);
        }
        if (params != null && params.containsKey("description")) {
            existing.put("description", description);
        }
        if (!isBlank(status)) {
            existing.put("active", parseMoActiveStatus(status));
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
        user.setMoId(firstNonBlank(record, "moId", "id"));
        user.setUsername(firstNonBlank(record, "username"));
        user.setFullName(firstNonBlank(record, "fullName", "displayName"));
        user.setDisplayName(firstNonBlank(record, "displayName", "fullName"));
        user.setEmail(firstNonBlank(record, "email"));
        user.setStaffId(firstNonBlank(record, "staffId"));
        user.setDepartment(firstNonBlank(record, "department"));
        user.setPhone(firstNonBlank(record, "phone"));
        user.setDescription(firstNonBlank(record, "description"));
        user.setCreatedAt(firstNonBlank(record, "createdAt"));
        user.setStatus(resolveStatus(record));
        user.setRole(Role.valueOf(firstNonBlank(record, "role")));
        return user;
    }

    private int countPostingRecords(String moId) {
        if (isBlank(moId)) {
            return 0;
        }

        Path postingsFile = DataPaths.resolvePostingsFile();
        if (!Files.exists(postingsFile)) {
            return 0;
        }

        try {
            String rawJson = new String(Files.readAllBytes(postingsFile), StandardCharsets.UTF_8).trim();
            if (rawJson.isEmpty()) {
                return 0;
            }
            Object parsed = JsonUtils.parse(rawJson);
            if (!(parsed instanceof List)) {
                throw new BusinessException("岗位数据格式无效: " + postingsFile);
            }

            int count = 0;
            for (Object item : (List<?>) parsed) {
                if (!(item instanceof Map)) {
                    continue;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> posting = (Map<String, Object>) item;
                String ownerMoId = firstNonBlank(posting, "moId", "ownerId");
                if (moId.equals(ownerMoId)) {
                    count++;
                }
            }
            return count;
        } catch (IOException ex) {
            throw new BusinessException("读取岗位数据失败: " + postingsFile);
        }
    }

    private void sortMoRecords(List<Map<String, Object>> records, String sortBy) {
        if (records == null || records.size() <= 1) {
            return;
        }

        Comparator<Map<String, Object>> comparator = buildMoComparator(sortBy);
        Collections.sort(records, comparator);
    }

    private Comparator<Map<String, Object>> buildMoComparator(String sortBy) {
        String normalizedSortBy = isBlank(sortBy) ? "createdAtDesc" : sortBy.trim();
        if ("fullNameAsc".equalsIgnoreCase(normalizedSortBy) || "nameAsc".equalsIgnoreCase(normalizedSortBy)) {
            return compareByField("fullName", false);
        }
        if ("fullNameDesc".equalsIgnoreCase(normalizedSortBy) || "nameDesc".equalsIgnoreCase(normalizedSortBy)) {
            return compareByField("fullName", true);
        }
        if ("staffIdAsc".equalsIgnoreCase(normalizedSortBy)) {
            return compareByField("staffId", false);
        }
        if ("staffIdDesc".equalsIgnoreCase(normalizedSortBy)) {
            return compareByField("staffId", true);
        }
        if ("emailAsc".equalsIgnoreCase(normalizedSortBy)) {
            return compareByField("email", false);
        }
        if ("emailDesc".equalsIgnoreCase(normalizedSortBy)) {
            return compareByField("email", true);
        }
        if ("departmentAsc".equalsIgnoreCase(normalizedSortBy)) {
            return compareByField("department", false);
        }
        if ("departmentDesc".equalsIgnoreCase(normalizedSortBy)) {
            return compareByField("department", true);
        }
        if ("createdAtAsc".equalsIgnoreCase(normalizedSortBy)) {
            return compareByField("createdAt", false);
        }
        return compareByField("createdAt", true);
    }

    private Comparator<Map<String, Object>> compareByField(final String fieldName, final boolean descending) {
        return new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> left, Map<String, Object> right) {
                String leftValue = comparableFieldValue(left, fieldName);
                String rightValue = comparableFieldValue(right, fieldName);
                int comparison = leftValue.compareToIgnoreCase(rightValue);
                if (comparison == 0) {
                    comparison = comparableFieldValue(left, "id").compareToIgnoreCase(comparableFieldValue(right, "id"));
                }
                return descending ? -comparison : comparison;
            }
        };
    }

    private String comparableFieldValue(Map<String, Object> record, String fieldName) {
        if ("fullName".equals(fieldName)) {
            return safeComparable(firstNonBlank(record, "fullName", "displayName"));
        }
        if ("createdAt".equals(fieldName)) {
            return safeComparable(firstNonBlank(record, "createdAt", "updatedAt"));
        }
        if ("id".equals(fieldName)) {
            return safeComparable(firstNonBlank(record, "id", "moId"));
        }
        return safeComparable(firstNonBlank(record, fieldName));
    }

    private String safeComparable(String value) {
        return value == null ? "" : value;
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

    private boolean matchesStatus(Map<String, Object> record, String status) {
        if (isBlank(status)) {
            return true;
        }
        return normalizeStatus(status).equals(resolveStatus(record));
    }

    private String resolveStatus(Map<String, Object> record) {
        String explicitStatus = firstNonBlank(record, "status");
        if (!isBlank(explicitStatus)) {
            return normalizeStatus(explicitStatus);
        }
        Object active = record.get("active");
        if (active instanceof Boolean) {
            return ((Boolean) active).booleanValue() ? "ACTIVE" : "INACTIVE";
        }
        String activeText = active == null ? null : String.valueOf(active).trim();
        if ("false".equalsIgnoreCase(activeText) || "0".equals(activeText)) {
            return "INACTIVE";
        }
        return "ACTIVE";
    }

    private String normalizeStatus(String status) {
        if (isBlank(status)) {
            return "ACTIVE";
        }
        String normalized = status.trim().toUpperCase(Locale.ENGLISH);
        if ("ENABLED".equals(normalized)) {
            return "ACTIVE";
        }
        if ("DISABLED".equals(normalized)) {
            return "INACTIVE";
        }
        return normalized;
    }

    private Boolean parseMoActiveStatus(String status) {
        String normalized = normalizeStatus(status);
        if ("ACTIVE".equals(normalized)) {
            return Boolean.TRUE;
        }
        if ("INACTIVE".equals(normalized)) {
            return Boolean.FALSE;
        }
        throw new BusinessException("MO 状态无效");
    }

    private String normalizeOptionalText(Map<String, Object> values, String key) {
        if (values == null || key == null || !values.containsKey(key)) {
            return null;
        }
        Object raw = values.get(key);
        if (raw == null) {
            return null;
        }
        String text = String.valueOf(raw).trim();
        return text.isEmpty() ? null : text;
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
