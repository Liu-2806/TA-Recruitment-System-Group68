package com.bupt.ta.repository.file;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ApplicationDataRepository extends JsonFileRepositorySupport {
    private static final String FILE_PATH = "applications/applications.json";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<Map<String, Object>> findAll() {
        return new ArrayList<>(readList(FILE_PATH, defaultRecords()));
    }

    public List<Map<String, Object>> findByTaId(String taId) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (Map<String, Object> record : findAll()) {
            if (taId.equals(String.valueOf(record.get("taId")))) {
                results.add(new LinkedHashMap<>(record));
            }
        }
        return results;
    }

    public List<Map<String, Object>> findByPostingId(String postingId) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (Map<String, Object> record : findAll()) {
            if (postingId.equals(String.valueOf(record.get("postingId")))) {
                results.add(new LinkedHashMap<>(record));
            }
        }
        return results;
    }

    public Map<String, Object> findByApplicationId(String applicationId) {
        for (Map<String, Object> record : findAll()) {
            if (applicationId.equals(String.valueOf(record.get("applicationId")))) {
                return new LinkedHashMap<>(record);
            }
        }
        return null;
    }

    public void save(Map<String, Object> applicationRecord) {
        List<Map<String, Object>> all = findAll();
        boolean replaced = false;
        for (int i = 0; i < all.size(); i++) {
            if (String.valueOf(all.get(i).get("applicationId")).equals(String.valueOf(applicationRecord.get("applicationId")))) {
                all.set(i, new LinkedHashMap<>(applicationRecord));
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            all.add(new LinkedHashMap<>(applicationRecord));
        }
        writeList(FILE_PATH, all);
    }

    public String nextApplicationId() {
        return "APP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private List<Map<String, Object>> defaultRecords() {
        List<Map<String, Object>> defaults = new ArrayList<>();
        Map<String, Object> app = new LinkedHashMap<>();
        app.put("applicationId", "APP001");
        app.put("postingId", "POST001");
        app.put("postingTitle", "Software Engineering TA");
        app.put("taId", "TA001");
        app.put("taName", "Alex Chen");
        app.put("appliedAt", LocalDateTime.now().minusDays(1).format(FORMATTER));
        app.put("status", "SUBMITTED");
        app.put("statement", "I have prior lab support experience and strong Java skills.");
        app.put("feedback", "");
        app.put("skillMatchScore", 0);
        app.put("skillMatchExplanation", "");
        defaults.add(app);
        return defaults;
    }
}
