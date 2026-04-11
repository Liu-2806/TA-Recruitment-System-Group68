package com.bupt.ta.repository.file;

import com.bupt.ta.exception.BusinessException;
import com.bupt.ta.model.Role;
import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.util.DataPaths;
import com.bupt.ta.util.JsonUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 基于 JSON 文件的用户仓储实现。
 */
public class JsonUserRepository implements UserRepository {
    public JsonUserRepository() {
        ensureStorage();
        ensureDefaultAdmin();
        migrateExistingRecords(Role.TA);
        migrateExistingRecords(Role.MO);
        migrateExistingRecords(Role.ADMIN);
        migrateLegacyAdminPassword();
    }

    @Override
    public synchronized List<Map<String, Object>> findAllByRole(Role role) {
        return readUsers(role);
    }

    @Override
    public synchronized Map<String, Object> findById(Role role, String userId) {
        if (isBlank(userId)) {
            return null;
        }
        List<Map<String, Object>> users = readUsers(role);
        for (Map<String, Object> user : users) {
            if (matchesStoredId(role, user, userId)) {
                return cloneRecord(user);
            }
        }
        return null;
    }

    @Override
    public synchronized Map<String, Object> findByLoginIdentifier(Role role, String identifier) {
        if (isBlank(identifier)) {
            return null;
        }
        String normalized = identifier.trim().toLowerCase(Locale.ENGLISH);
        List<Map<String, Object>> users = readUsers(role);
        for (Map<String, Object> user : users) {
            if (matchesIdentifier(role, user, normalized)) {
                return cloneRecord(user);
            }
        }
        return null;
    }

    @Override
    public synchronized boolean existsByField(Role role, String field, String value) {
        if (isBlank(field) || isBlank(value)) {
            return false;
        }
        List<Map<String, Object>> users = readUsers(role);
        for (Map<String, Object> user : users) {
            if (value.equalsIgnoreCase(stringValue(user.get(field)))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean existsAcrossRoles(String field, String value) {
        if (isBlank(field) || isBlank(value)) {
            return false;
        }
        for (Role role : Role.values()) {
            if (existsByField(role, field, value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized String nextUserId(Role role) {
        List<Map<String, Object>> users = readUsers(role);
        int max = 0;
        String prefix = idPrefix(role);
        for (Map<String, Object> user : users) {
            String id = firstNonBlank(user.get("id"), user.get(idField(role)));
            if (id != null && id.startsWith(prefix)) {
                try {
                    int value = Integer.parseInt(id.substring(prefix.length()));
                    if (value > max) {
                        max = value;
                    }
                } catch (NumberFormatException ignored) {
                    // Ignore malformed IDs and continue scanning.
                }
            }
        }
        return prefix + String.format(Locale.ENGLISH, "%03d", max + 1);
    }

    @Override
    public synchronized Map<String, Object> save(Role role, Map<String, Object> userRecord) {
        List<Map<String, Object>> users = readUsers(role);
        Map<String, Object> sanitizedRecord = sanitizeRecord(normalizeRoleRecord(role, userRecord));
        users.add(sanitizedRecord);
        writeUsers(role, users);
        return cloneRecord(sanitizedRecord);
    }

    @Override
    public synchronized Map<String, Object> update(Role role, Map<String, Object> userRecord) {
        List<Map<String, Object>> users = readUsers(role);
        String userId = firstNonBlank(userRecord.get("id"), userRecord.get(idField(role)));
        Map<String, Object> sanitizedRecord = sanitizeRecord(normalizeRoleRecord(role, userRecord));
        boolean replaced = false;
        for (int i = 0; i < users.size(); i++) {
            if (userId != null && matchesStoredId(role, users.get(i), userId)) {
                users.set(i, sanitizedRecord);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            users.add(sanitizedRecord);
        }
        writeUsers(role, users);
        return cloneRecord(sanitizedRecord);
    }

    @Override
    public synchronized long countByRole(Role role) {
        return readUsers(role).size();
    }

    private boolean matchesStoredId(Role role, Map<String, Object> user, String userId) {
        return userId.equals(stringValue(user.get("id")))
                || userId.equals(stringValue(user.get(idField(role))));
    }

    private boolean matchesIdentifier(Role role, Map<String, Object> user, String normalizedIdentifier) {
        return normalizedIdentifier.equals(normalizeValue(user.get("username")))
                || normalizedIdentifier.equals(normalizeValue(user.get("email")))
                || normalizedIdentifier.equals(normalizeValue(user.get("studentId")))
                || normalizedIdentifier.equals(normalizeValue(user.get("staffId")))
                || normalizedIdentifier.equals(normalizeValue(user.get("id")))
                || normalizedIdentifier.equals(normalizeValue(user.get(idField(role))));
    }

    private void ensureStorage() {
        ensureJsonArrayFile(pathFor(Role.TA));
        ensureJsonArrayFile(pathFor(Role.MO));
        ensureJsonArrayFile(pathFor(Role.ADMIN));
    }

    private void migrateExistingRecords(Role role) {
        List<Map<String, Object>> records = readUsers(role);
        boolean changed = false;
        List<Map<String, Object>> migrated = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> record : records) {
            Map<String, Object> normalized = normalizeRoleRecord(role, record);
            migrated.add(normalized);
            if (!normalized.equals(record)) {
                changed = true;
            }
        }
        if (changed) {
            writeUsers(role, migrated);
        }
    }

    private static final DateTimeFormatter ADMIN_CREATED_AT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 当 admin 数据文件为空时，写入默认管理员（与原型种子数据一致），便于首次启动即可登录。
     */
    private void ensureDefaultAdmin() {
        List<Map<String, Object>> admins = readUsers(Role.ADMIN);
        if (!admins.isEmpty()) {
            return;
        }
        Map<String, Object> admin = new LinkedHashMap<String, Object>();
        admin.put("id", "ADMIN001");
        admin.put("username", "admin");
        admin.put("email", "admin@tarecruitment");
        admin.put("fullName", "System Admin");
        admin.put("displayName", "System Admin");
        admin.put("role", Role.ADMIN.name());
        admin.put("password", "Admin123!");
        admin.put("active", Boolean.TRUE);
        admin.put("createdAt", LocalDateTime.now().format(ADMIN_CREATED_AT_FORMAT));
        List<Map<String, Object>> seed = new ArrayList<Map<String, Object>>();
        seed.add(admin);
        writeUsers(Role.ADMIN, seed);
    }

    private void migrateLegacyAdminPassword() {
        List<Map<String, Object>> admins = readUsers(Role.ADMIN);
        boolean changed = false;
        for (Map<String, Object> admin : admins) {
            if (isDefaultAdmin(admin) && isBlank(stringValue(admin.get("password")))) {
                admin.put("password", "Admin123!");
                admin.remove("passwordSalt");
                admin.remove("passwordHash");
                changed = true;
            }
        }
        if (changed) {
            writeUsers(Role.ADMIN, admins);
        }
    }

    private List<Map<String, Object>> readUsers(Role role) {
        Path path = pathFor(role);
        String rawJson;
        try {
            rawJson = new String(Files.readAllBytes(path), StandardCharsets.UTF_8).trim();
        } catch (IOException ex) {
            throw new BusinessException("读取用户数据失败: " + path);
        }

        if (rawJson.isEmpty()) {
            rawJson = "[]";
        }

        Object parsed = JsonUtils.parse(rawJson);
        if (!(parsed instanceof List)) {
            throw new BusinessException("用户数据格式无效: " + path);
        }

        List<?> items = (List<?>) parsed;
        List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();
        for (Object item : items) {
            if (item instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> record = (Map<String, Object>) item;
                users.add(cloneRecord(record));
            }
        }
        return users;
    }

    private void writeUsers(Role role, List<Map<String, Object>> users) {
        Path path = pathFor(role);
        try {
            Files.write(
                    path,
                    JsonUtils.toJson(users).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
        } catch (IOException ex) {
            throw new BusinessException("写入用户数据失败: " + path);
        }
    }

    private void ensureJsonArrayFile(Path path) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(path)) {
                Files.write(
                        path,
                        "[]".getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE_NEW
                );
            }
        } catch (IOException ex) {
            throw new BusinessException("初始化数据文件失败: " + path);
        }
    }

    private Path pathFor(Role role) {
        return DataPaths.resolveUsersFile(role);
    }

    private Map<String, Object> cloneRecord(Map<String, Object> source) {
        return new LinkedHashMap<String, Object>(source);
    }

    private Map<String, Object> sanitizeRecord(Map<String, Object> source) {
        Map<String, Object> sanitized = cloneRecord(source);
        if (!isBlank(stringValue(sanitized.get("password")))) {
            sanitized.remove("passwordSalt");
            sanitized.remove("passwordHash");
        }
        return sanitized;
    }

    private Map<String, Object> normalizeRoleRecord(Role role, Map<String, Object> source) {
        Map<String, Object> normalized = cloneRecord(source);
        String resolvedId = firstNonBlank(normalized.get("id"), normalized.get(idField(role)));
        if (!isBlank(resolvedId)) {
            normalized.put("id", resolvedId);
            normalized.put(idField(role), resolvedId);
        }
        if (isBlank(stringValue(normalized.get("role")))) {
            normalized.put("role", role.name());
        }
        if (isBlank(stringValue(normalized.get("displayName"))) && !isBlank(stringValue(normalized.get("fullName")))) {
            normalized.put("displayName", stringValue(normalized.get("fullName")));
        }
        if (!normalized.containsKey("active")) {
            normalized.put("active", Boolean.TRUE);
        }
        return normalized;
    }

    private String idPrefix(Role role) {
        if (role == Role.TA) {
            return "TA";
        }
        if (role == Role.MO) {
            return "MO";
        }
        return "ADMIN";
    }

    private String idField(Role role) {
        if (role == Role.TA) {
            return "taId";
        }
        if (role == Role.MO) {
            return "moId";
        }
        return "id";
    }

    private boolean isDefaultAdmin(Map<String, Object> admin) {
        return "ADMIN001".equals(stringValue(admin.get("id")))
                && "admin".equalsIgnoreCase(stringValue(admin.get("username")));
    }

    private String firstNonBlank(Object... values) {
        if (values == null) {
            return null;
        }
        for (Object value : values) {
            String text = stringValue(value);
            if (!isBlank(text)) {
                return text;
            }
        }
        return null;
    }

    private String normalizeValue(Object value) {
        String text = stringValue(value);
        return text == null ? null : text.trim().toLowerCase(Locale.ENGLISH);
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value).trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
