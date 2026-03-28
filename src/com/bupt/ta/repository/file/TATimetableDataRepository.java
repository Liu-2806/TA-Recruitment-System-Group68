package com.bupt.ta.repository.file;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TATimetableDataRepository extends JsonFileRepositorySupport {
    private static final String FILE_PATH = "system/ta-timetable.json";
    private static final DateTimeFormatter LABEL_FORMATTER = DateTimeFormatter.ofPattern("dd MMM");

    public Map<String, Object> findTimetableByTaIdAndWeek(String taId, LocalDate weekStart) {
        LocalDate normalizedWeekStart = normalizeWeekStart(weekStart);
        for (Map<String, Object> row : findAll()) {
            if (taId.equals(String.valueOf(row.get("taId")))
                && normalizedWeekStart.toString().equals(String.valueOf(row.get("weekStart")))) {
                return new LinkedHashMap<>(row);
            }
        }
        return defaultTimetable(taId, normalizedWeekStart);
    }

    public List<Map<String, Object>> findPostingSchedule(String postingId) {
        for (Map<String, Object> row : findAll()) {
            if (postingId.equals(String.valueOf(row.get("postingId")))) {
                Object blocks = row.get("scheduleBlocks");
                if (blocks instanceof List<?> list) {
                    List<Map<String, Object>> results = new ArrayList<>();
                    for (Object item : list) {
                        if (item instanceof Map<?, ?> mapItem) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> block = new LinkedHashMap<>((Map<String, Object>) mapItem);
                            results.add(block);
                        }
                    }
                    return results;
                }
            }
        }
        return new ArrayList<>();
    }

    public void releaseAssignment(String taId, String applicationId) {
        if (isBlank(taId) || isBlank(applicationId)) {
            return;
        }
        List<Map<String, Object>> all = findAll();
        boolean changed = false;
        for (Map<String, Object> row : all) {
            if (!taId.equals(String.valueOf(row.get("taId")))) {
                continue;
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> assignment = row.get("courseAssignment") instanceof Map<?, ?>
                ? new LinkedHashMap<>((Map<String, Object>) row.get("courseAssignment"))
                : null;
            if (assignment != null && applicationId.equals(String.valueOf(assignment.get("relatedApplicationId")))) {
                row.put("courseAssignment", createCourseAssignment("", "", "", "Course TA", "", "", "", "", "No fixed course assignment recorded for this week.", ""));
                changed = true;
            }

            Object eventsObject = row.get("activityEvents");
            if (eventsObject instanceof List<?> events) {
                List<Map<String, Object>> retained = new ArrayList<>();
                int originalSize = events.size();
                for (Object eventObject : events) {
                    if (!(eventObject instanceof Map<?, ?> mapItem)) {
                        continue;
                    }
                    @SuppressWarnings("unchecked")
                    Map<String, Object> event = new LinkedHashMap<>((Map<String, Object>) mapItem);
                    if (!applicationId.equals(String.valueOf(event.get("applicationId")))) {
                        retained.add(event);
                    }
                }
                if (retained.size() != originalSize) {
                    row.put("activityEvents", retained);
                    changed = true;
                }
            }
        }
        if (changed) {
            writeList(FILE_PATH, all);
        }
    }

    private List<Map<String, Object>> findAll() {
        return new ArrayList<>(readList(FILE_PATH, defaultRecords()));
    }

    private List<Map<String, Object>> defaultRecords() {
        LocalDate weekStart = normalizeWeekStart(LocalDate.now());
        List<Map<String, Object>> defaults = new ArrayList<>();

        Map<String, Object> taSchedule = new LinkedHashMap<>();
        taSchedule.put("taId", "TA001");
        taSchedule.put("weekStart", weekStart.toString());
        taSchedule.put("currentWeekLabel", "Week of " + LABEL_FORMATTER.format(weekStart) + " - " + LABEL_FORMATTER.format(weekStart.plusDays(6)));
        taSchedule.put("courseAssignment", createCourseAssignment("POST001", "SE3001", "Software Engineering TA", "Course TA", "TUE", "14:00", "16:00", "QB-302", "Weekly support session", "APP001"));
        taSchedule.put("activityEvents", List.of(
            createActivityEvent("EVT001", "POST001", "APP001", "SE3001 Lab Support", "lab", weekStart.plusDays(1).toString(), "10:00", "12:00", "QB-302", "Guide students through the weekly lab and answer implementation questions."),
            createActivityEvent("EVT002", "POST001", "APP001", "SE3001 Check-off", "checkoff", weekStart.plusDays(3).toString(), "16:00", "17:00", "QB-302", "Review sprint progress and check implementation milestones.")
        ));
        defaults.add(taSchedule);

        defaults.add(createPostingSchedule("POST001", "SE3001", "Software Engineering TA", List.of(
            createScheduleBlock("TUE", "14:00", "16:00", "Course TA Support"),
            createScheduleBlock("WED", "10:00", "12:00", "Weekly Lab Support"),
            createScheduleBlock("FRI", "16:00", "17:00", "Project Check-off")
        )));
        defaults.add(createPostingSchedule("POST002", "CS2202", "Database Systems TA", List.of(
            createScheduleBlock("TUE", "15:00", "17:00", "SQL Lab Support"),
            createScheduleBlock("THU", "10:00", "12:00", "Database Q&A Session")
        )));
        defaults.add(createPostingSchedule("POST003", "AI1101", "AI Foundations TA", List.of(
            createScheduleBlock("MON", "10:00", "12:00", "Model Evaluation Workshop"),
            createScheduleBlock("WED", "10:30", "12:30", "AI Lab Assistance")
        )));
        return defaults;
    }

    private Map<String, Object> defaultTimetable(String taId, LocalDate weekStart) {
        Map<String, Object> timetable = new LinkedHashMap<>();
        timetable.put("taId", taId);
        timetable.put("weekStart", weekStart.toString());
        timetable.put("currentWeekLabel", "Week of " + LABEL_FORMATTER.format(weekStart) + " - " + LABEL_FORMATTER.format(weekStart.plusDays(6)));
        timetable.put("courseAssignment", createCourseAssignment("", "", "", "Course TA", "", "", "", "", "No fixed course assignment recorded for this week.", ""));
        timetable.put("activityEvents", new ArrayList<>());
        return timetable;
    }

    private LocalDate normalizeWeekStart(LocalDate date) {
        LocalDate safeDate = date == null ? LocalDate.now() : date;
        return safeDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    private Map<String, Object> createCourseAssignment(
        String postingId,
        String courseCode,
        String courseName,
        String label,
        String dayOfWeek,
        String startTime,
        String endTime,
        String location,
        String description,
        String relatedApplicationId
    ) {
        Map<String, Object> assignment = new LinkedHashMap<>();
        assignment.put("postingId", postingId);
        assignment.put("courseCode", courseCode);
        assignment.put("courseName", courseName);
        assignment.put("label", label);
        assignment.put("dayOfWeek", dayOfWeek);
        assignment.put("startTime", startTime);
        assignment.put("endTime", endTime);
        assignment.put("location", location);
        assignment.put("description", description);
        assignment.put("relatedApplicationId", relatedApplicationId);
        return assignment;
    }

    private Map<String, Object> createActivityEvent(
        String eventId,
        String postingId,
        String applicationId,
        String title,
        String type,
        String date,
        String startTime,
        String endTime,
        String location,
        String description
    ) {
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("eventId", eventId);
        event.put("postingId", postingId);
        event.put("applicationId", applicationId);
        event.put("title", title);
        event.put("type", type);
        event.put("date", date);
        event.put("startTime", startTime);
        event.put("endTime", endTime);
        event.put("location", location);
        event.put("description", description);
        return event;
    }

    private Map<String, Object> createPostingSchedule(String postingId, String courseCode, String courseName, List<Map<String, Object>> scheduleBlocks) {
        Map<String, Object> postingSchedule = new LinkedHashMap<>();
        postingSchedule.put("postingId", postingId);
        postingSchedule.put("courseCode", courseCode);
        postingSchedule.put("courseName", courseName);
        postingSchedule.put("scheduleBlocks", scheduleBlocks);
        return postingSchedule;
    }

    private Map<String, Object> createScheduleBlock(String dayOfWeek, String startTime, String endTime, String label) {
        Map<String, Object> block = new LinkedHashMap<>();
        block.put("dayOfWeek", dayOfWeek);
        block.put("startTime", startTime);
        block.put("endTime", endTime);
        block.put("label", label);
        return block;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
