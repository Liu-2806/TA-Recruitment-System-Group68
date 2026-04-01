package com.bupt.ta.repository.file;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PostingDataRepository extends JsonFileRepositorySupport {
    private static final String FILE_PATH = "postings/postings.json";

    public List<Map<String, Object>> findAll() {
        return new ArrayList<>(readList(FILE_PATH, defaultRecords()));
    }

    public Map<String, Object> findByPostingId(String postingId) {
        for (Map<String, Object> record : findAll()) {
            if (postingId.equals(String.valueOf(record.get("postingId")))) {
                return new LinkedHashMap<>(record);
            }
        }
        return null;
    }

    public void save(Map<String, Object> postingRecord) {
        List<Map<String, Object>> all = findAll();
        boolean replaced = false;
        for (int i = 0; i < all.size(); i++) {
            if (String.valueOf(all.get(i).get("postingId")).equals(String.valueOf(postingRecord.get("postingId")))) {
                all.set(i, new LinkedHashMap<>(postingRecord));
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            all.add(new LinkedHashMap<>(postingRecord));
        }
        writeList(FILE_PATH, all);
    }

    public String nextPostingId() {
        int max = 0;
        for (Map<String, Object> posting : findAll()) {
            String postingId = String.valueOf(posting.get("postingId"));
            if (postingId.startsWith("POST")) {
                try {
                    int current = Integer.parseInt(postingId.substring(4));
                    if (current > max) {
                        max = current;
                    }
                } catch (NumberFormatException ignored) {
                    // Ignore malformed IDs and continue scanning.
                }
            }
        }
        return String.format("POST%03d", max + 1);
    }

    private List<Map<String, Object>> defaultRecords() {
        List<Map<String, Object>> defaults = new ArrayList<>();
        defaults.add(createPosting(
            "POST001", "SE3001", "Software Engineering TA", "MO001", "Prof. Wang",
            3, 1, "2026-03-30",
            "Assist in lab session teaching, support assignment marking, and answer weekly software engineering questions.",
            List.of("Java", "Testing", "Communication", "Object-Oriented Programming"),
            6, "OPEN"
        ));
        defaults.add(createPosting(
            "POST002", "CS2202", "Database Systems TA", "MO002", "Prof. Li",
            2, 0, "2026-03-31",
            "Support SQL labs, help students debug database queries, and assist with coursework Q&A.",
            List.of("SQL", "Data Analysis", "Communication"),
            5, "OPEN"
        ));
        defaults.add(createPosting(
            "POST003", "AI1101", "AI Foundations TA", "MO003", "Prof. Zhang",
            2, 0, "2026-04-02",
            "Support foundational AI classes, review student tasks, and help with model evaluation activities.",
            List.of("Python", "Machine Learning", "Data Analysis", "Communication"),
            6, "OPEN"
        ));
        return defaults;
    }

    private Map<String, Object> createPosting(
        String postingId,
        String courseCode,
        String courseName,
        String moId,
        String moName,
        int vacancies,
        int applicationCount,
        String deadline,
        String description,
        List<String> requiredSkills,
        int estimatedWorkloadHours,
        String status
    ) {
        Map<String, Object> posting = new LinkedHashMap<>();
        posting.put("postingId", postingId);
        posting.put("courseCode", courseCode);
        posting.put("courseName", courseName);
        posting.put("moId", moId);
        posting.put("moName", moName);
        posting.put("vacancies", vacancies);
        posting.put("applicationCount", applicationCount);
        posting.put("deadline", deadline);
        posting.put("description", description);
        posting.put("requiredSkills", requiredSkills);
        posting.put("estimatedWorkloadHours", estimatedWorkloadHours);
        posting.put("status", status);
        return posting;
    }
}
