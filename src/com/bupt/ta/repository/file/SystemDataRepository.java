package com.bupt.ta.repository.file;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SystemDataRepository extends JsonFileRepositorySupport {
    private static final String FILE_PATH = "system/skill-tags.json";

    public List<String> listSkillTags() {
        List<String> skills = new ArrayList<>();
        for (Map<String, Object> row : readList(FILE_PATH, defaultRecords())) {
            Object value = row.get("name");
            if (value != null) {
                skills.add(String.valueOf(value));
            }
        }
        return skills;
    }

    private List<Map<String, Object>> defaultRecords() {
        List<Map<String, Object>> defaults = new ArrayList<>();
        for (String skill : List.of("Java", "Python", "Git", "Testing", "Communication", "SQL", "Machine Learning", "Data Analysis")) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("name", skill);
            defaults.add(row);
        }
        return defaults;
    }
}
