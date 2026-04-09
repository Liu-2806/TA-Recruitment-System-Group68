package com.bupt.ta.util;

import com.bupt.ta.model.Role;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * 项目数据目录解析工具。
 */
public final class DataPaths {
    private static final String DATA_DIR_PROPERTY = "ta.data.dir";
    private static final String DATA_DIR_ENV = "TA_DATA_DIR";
    private static final Logger LOGGER = Logger.getLogger(DataPaths.class.getName());
    private static final AtomicBoolean ROOT_LOGGED = new AtomicBoolean(false);

    private DataPaths() {
    }

    public static Path resolveDataRoot() {
        Path root;
        String source;
        String configuredPath = System.getProperty(DATA_DIR_PROPERTY);
        if (isBlank(configuredPath)) {
            configuredPath = System.getenv(DATA_DIR_ENV);
        }
        if (!isBlank(configuredPath)) {
            root = Paths.get(configuredPath).toAbsolutePath().normalize();
            source = DATA_DIR_PROPERTY + "/" + DATA_DIR_ENV;
        } else {
            // Stable default for all runtime contexts (IDE/Tomcat/CLI).
            root = Paths.get(
                System.getProperty("user.home"),
                ".ta-recruitment-system",
                "data"
            ).toAbsolutePath().normalize();
            source = "default-user-home";
        }
        if (ROOT_LOGGED.compareAndSet(false, true)) {
            LOGGER.info("TA data root: " + root + " (source=" + source + ")");
        }
        return root;
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
