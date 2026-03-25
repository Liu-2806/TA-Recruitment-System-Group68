package com.bupt.ta.util;

import com.bupt.ta.model.Role;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 项目数据目录解析工具。
 */
public final class DataPaths {
    private static final String DATA_DIR_PROPERTY = "ta.data.dir";
    private static final String DATA_DIR_ENV = "TA_DATA_DIR";

    private DataPaths() {
    }

    public static Path resolveDataRoot() {
        String configuredPath = System.getProperty(DATA_DIR_PROPERTY);
        if (isBlank(configuredPath)) {
            configuredPath = System.getenv(DATA_DIR_ENV);
        }
        if (!isBlank(configuredPath)) {
            return Paths.get(configuredPath).toAbsolutePath().normalize();
        }

        Path current = Paths.get("").toAbsolutePath().normalize();
        Path cursor = current;
        while (cursor != null) {
            Path candidate = cursor.resolve("data");
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
            cursor = cursor.getParent();
        }

        return current.resolve("data");
    }

    public static Path resolveUsersFile(Role role) {
        if (role == Role.TA) {
            return resolveDataRoot().resolve(Paths.get("users", "ta.json"));
        }
        if (role == Role.MO) {
            return resolveDataRoot().resolve(Paths.get("users", "mo.json"));
        }
        return resolveDataRoot().resolve(Paths.get("users", "admin.json"));
    }

    public static Path resolvePostingsFile() {
        return resolveDataRoot().resolve(Paths.get("postings", "postings.json"));
    }

    public static Path resolveApplicationsFile() {
        return resolveDataRoot().resolve(Paths.get("applications", "applications.json"));
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
