package com.bupt.ta.match;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class EnvConfigLoader {
    private EnvConfigLoader() {
    }

    static Map<String, String> load() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("LLM_API_URL", trim(System.getenv("LLM_API_URL")));
        values.put("LLM_API_KEY", trim(System.getenv("LLM_API_KEY")));
        values.put("LLM_MODEL", trim(System.getenv("LLM_MODEL")));
        if (isComplete(values)) {
            return values;
        }

        Path envFile = Paths.get(".env.local");
        if (!Files.exists(envFile)) {
            return values;
        }
        try {
            List<String> lines = Files.readAllLines(envFile, StandardCharsets.UTF_8);
            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty() || line.startsWith("#") || !line.contains("=")) {
                    continue;
                }
                String[] parts = line.split("=", 2);
                String key = parts[0].trim();
                String value = trim(parts[1].trim());
                if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                    value = value.substring(1, value.length() - 1);
                }
                if (values.containsKey(key) && (values.get(key) == null || values.get(key).isBlank())) {
                    values.put(key, value);
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read .env.local for LLM configuration.", ex);
        }
        return values;
    }

    static boolean isComplete(Map<String, String> values) {
        return notBlank(values.get("LLM_API_URL"))
            && notBlank(values.get("LLM_API_KEY"))
            && notBlank(values.get("LLM_MODEL"));
    }

    private static String trim(String value) {
        return value == null ? null : value.trim();
    }

    private static boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }
}
