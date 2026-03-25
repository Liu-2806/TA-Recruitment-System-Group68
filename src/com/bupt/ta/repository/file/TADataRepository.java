package com.bupt.ta.repository.file;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TADataRepository extends JsonFileRepositorySupport {
    private static final String FILE_PATH = "users/ta.json";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Map<String, Object> findByTaId(String taId) {
        for (Map<String, Object> record : findAll()) {
            if (taId.equals(String.valueOf(record.get("taId")))) {
                return new LinkedHashMap<>(record);
            }
        }
        return null;
    }

    public void save(Map<String, Object> taRecord) {
        List<Map<String, Object>> all = findAll();
        boolean replaced = false;
        for (int i = 0; i < all.size(); i++) {
            if (String.valueOf(all.get(i).get("taId")).equals(String.valueOf(taRecord.get("taId")))) {
                all.set(i, new LinkedHashMap<>(taRecord));
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            all.add(new LinkedHashMap<>(taRecord));
        }
        writeList(FILE_PATH, all);
    }

    public List<Map<String, Object>> findAll() {
        return new ArrayList<>(readList(FILE_PATH, defaultRecords()));
    }

    private List<Map<String, Object>> defaultRecords() {
        List<Map<String, Object>> defaults = new ArrayList<>();
        Map<String, Object> ta = new LinkedHashMap<>();
        ta.put("taId", "TA001");
        ta.put("fullName", "Alex Chen");
        ta.put("studentId", "2021001234");
        ta.put("majorProgram", "Software Engineering");
        ta.put("academicYear", "Year 3");
        ta.put("email", "alex.chen@bupt.edu.cn");
        ta.put("phone", "+44 7123 456789");
        ta.put("intro", "Interested in teaching support and software engineering modules.");
        ta.put("skills", List.of("Java", "Python", "Git", "Testing", "Communication"));
        ta.put("resumeFileName", "sample_resume.pdf");
        ta.put("resumeUploadedAt", LocalDateTime.now().format(FORMATTER));
        ta.put("extractedResume", defaultExtractedResume());
        defaults.add(ta);
        return defaults;
    }

    private Map<String, Object> defaultExtractedResume() {
        Map<String, Object> extracted = new LinkedHashMap<>();
        extracted.put("name", "Alex Chen");
        extracted.put("email", "alex.chen@example.com");
        extracted.put("phone", "+44 7123 456789");
        extracted.put("education", "MSc Computer Science, Queen Mary University of London");
        extracted.put("skills", List.of("Java", "Python", "Git", "Testing", "Communication", "Teaching", "Data Analysis"));
        extracted.put("experienceHighlights", List.of(
            "Teaching Assistant for introductory programming labs.",
            "Research project on machine learning-based skill classification.",
            "Internship project building internal dashboards in Python."
        ));
        extracted.put("rawText", "Alex Chen resume sample");
        return extracted;
    }
}
