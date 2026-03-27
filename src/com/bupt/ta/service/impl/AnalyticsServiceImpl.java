package com.bupt.ta.service.impl;

import com.bupt.ta.dto.PageResult;
import com.bupt.ta.model.Role;
import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.service.AnalyticsService;
import com.bupt.ta.util.DataPaths;
import com.bupt.ta.util.JsonUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员统计服务实现。
 */
public class AnalyticsServiceImpl implements AnalyticsService {
    private static final DateTimeFormatter DATE_TIME_SECONDS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_MINUTES_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int RECENT_ACTIVITY_LIMIT = 8;

    private final UserRepository userRepository;

    public AnalyticsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, Object> getSystemOverview() {
        List<Map<String, Object>> postings = readJsonArray(DataPaths.resolvePostingsFile());
        List<Map<String, Object>> applications = readJsonArray(DataPaths.resolveApplicationsFile());

        Map<String, Object> overview = new LinkedHashMap<String, Object>();
        overview.put("totalTAs", userRepository.countByRole(Role.TA));
        overview.put("totalMOs", userRepository.countByRole(Role.MO));
        overview.put("totalPostings", postings.size());
        overview.put("openPostings", countOpenPostings(postings));
        overview.put("pendingApplications", countPendingApplications(applications));
        overview.put("activeRecruitmentCount", countActiveRecruitments(postings));
        overview.put("recentActivities", buildRecentActivities(postings, applications));
        return overview;
    }

    @Override
    public PageResult<Map<String, Object>> getTAWorkloadReport(Map<String, Object> query) {
        List<Map<String, Object>> taRecords = userRepository.findAllByRole(Role.TA);
        List<Map<String, Object>> applications = readJsonArray(DataPaths.resolveApplicationsFile());
        List<Map<String, Object>> reportRows = new ArrayList<Map<String, Object>>();

        for (Map<String, Object> ta : taRecords) {
            String taId = stringValue(ta.get("id"));
            int applicationCount = 0;
            int acceptedCount = 0;
            for (Map<String, Object> application : applications) {
                String applicationTaId = firstNonBlank(application, "taId", "taUserId");
                if (taId != null && taId.equals(applicationTaId)) {
                    applicationCount++;
                    if ("ACCEPTED".equalsIgnoreCase(firstNonBlank(application, "status"))) {
                        acceptedCount++;
                    }
                }
            }

            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("taId", taId);
            row.put("fullName", firstNonBlank(ta, "fullName", "displayName"));
            row.put("studentId", firstNonBlank(ta, "studentId"));
            row.put("applicationCount", applicationCount);
            row.put("acceptedCount", acceptedCount);
            reportRows.add(row);
        }

        PageResult<Map<String, Object>> result = new PageResult<Map<String, Object>>();
        result.setRecords(reportRows);
        result.setPage(1);
        result.setSize(reportRows.size());
        result.setTotal(reportRows.size());
        return result;
    }

    private List<Map<String, Object>> readJsonArray(Path path) {
        ensureJsonArrayFile(path);
        try {
            String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8).trim();
            if (json.isEmpty()) {
                return Collections.emptyList();
            }
            Object parsed = JsonUtils.parse(json);
            if (!(parsed instanceof List)) {
                return Collections.emptyList();
            }

            List<?> rawItems = (List<?>) parsed;
            List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
            for (Object rawItem : rawItems) {
                if (rawItem instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) rawItem;
                    items.add(new LinkedHashMap<String, Object>(map));
                }
            }
            return items;
        } catch (IOException ex) {
            return Collections.emptyList();
        }
    }

    private long countOpenPostings(List<Map<String, Object>> postings) {
        long count = 0;
        for (Map<String, Object> posting : postings) {
            if (isOpenPosting(posting)) {
                count++;
            }
        }
        return count;
    }

    private long countPendingApplications(List<Map<String, Object>> applications) {
        long count = 0;
        for (Map<String, Object> application : applications) {
            if ("SUBMITTED".equalsIgnoreCase(firstNonBlank(application, "status"))) {
                count++;
            }
        }
        return count;
    }

    private long countActiveRecruitments(List<Map<String, Object>> postings) {
        long count = 0;
        LocalDate today = LocalDate.now();
        for (Map<String, Object> posting : postings) {
            if (!isOpenPosting(posting)) {
                continue;
            }
            LocalDate deadline = parseDate(firstNonBlank(posting, "deadline"));
            if (deadline == null || !deadline.isBefore(today)) {
                count++;
            }
        }
        return count;
    }

    private boolean isOpenPosting(Map<String, Object> posting) {
        return "OPEN".equalsIgnoreCase(firstNonBlank(posting, "status"));
    }

    private List<Map<String, Object>> buildRecentActivities(List<Map<String, Object>> postings,
                                                            List<Map<String, Object>> applications) {
        List<ActivityRecord> activities = new ArrayList<ActivityRecord>();
        appendUserActivities(activities, userRepository.findAllByRole(Role.TA), Role.TA);
        appendUserActivities(activities, userRepository.findAllByRole(Role.MO), Role.MO);
        appendPostingActivities(activities, postings);
        appendApplicationActivities(activities, postings, applications);

        Collections.sort(activities, new Comparator<ActivityRecord>() {
            @Override
            public int compare(ActivityRecord left, ActivityRecord right) {
                int timeCompare = right.sortTime.compareTo(left.sortTime);
                if (timeCompare != 0) {
                    return timeCompare;
                }
                return String.valueOf(left.payload.get("message")).compareTo(String.valueOf(right.payload.get("message")));
            }
        });

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        int limit = Math.min(RECENT_ACTIVITY_LIMIT, activities.size());
        for (int i = 0; i < limit; i++) {
            result.add(activities.get(i).payload);
        }
        return result;
    }

    private void appendUserActivities(List<ActivityRecord> activities,
                                      List<Map<String, Object>> users,
                                      Role role) {
        for (Map<String, Object> user : users) {
            String happenedAt = firstNonBlank(user, "createdAt", "updatedAt");
            if (isBlank(happenedAt)) {
                continue;
            }

            String name = firstNonBlank(user, "fullName", "displayName", "username", "email", "id");
            String relatedEntityId = firstNonBlank(user, "id", role == Role.TA ? "taId" : "moId");
            if (role == Role.TA) {
                activities.add(createActivity(
                        happenedAt,
                        "TA_REGISTERED",
                        name + " registered as TA",
                        "TA",
                        relatedEntityId
                ));
            } else if (role == Role.MO) {
                activities.add(createActivity(
                        happenedAt,
                        "MO_CREATED",
                        name + " was added as MO",
                        "MO",
                        relatedEntityId
                ));
            }
        }
    }

    private void appendPostingActivities(List<ActivityRecord> activities,
                                         List<Map<String, Object>> postings) {
        for (Map<String, Object> posting : postings) {
            String happenedAt = firstNonBlank(posting, "createdAt", "postedAt", "publishedAt", "updatedAt");
            if (isBlank(happenedAt)) {
                continue;
            }

            String moName = firstNonBlank(posting, "moName", "moId", "ownerName", "ownerId");
            String postingTitle = resolvePostingTitle(posting);
            activities.add(createActivity(
                    happenedAt,
                    "POST_CREATED",
                    moName + " posted " + postingTitle,
                    "POSTING",
                    firstNonBlank(posting, "postingId")
            ));
        }
    }

    private void appendApplicationActivities(List<ActivityRecord> activities,
                                             List<Map<String, Object>> postings,
                                             List<Map<String, Object>> applications) {
        Map<String, String> postingTitleIndex = new LinkedHashMap<String, String>();
        for (Map<String, Object> posting : postings) {
            String postingId = firstNonBlank(posting, "postingId");
            if (!isBlank(postingId)) {
                postingTitleIndex.put(postingId, resolvePostingTitle(posting));
            }
        }

        for (Map<String, Object> application : applications) {
            String happenedAt = firstNonBlank(application, "appliedAt", "updatedAt", "createdAt");
            if (isBlank(happenedAt)) {
                continue;
            }

            String postingId = firstNonBlank(application, "postingId");
            String postingTitle = firstNonBlank(application, "postingTitle", postingTitleIndex.get(postingId), postingId);
            String taName = firstNonBlank(application, "taName", "taId");
            activities.add(createActivity(
                    happenedAt,
                    "APPLICATION_SUBMITTED",
                    taName + " applied for " + postingTitle,
                    "APPLICATION",
                    firstNonBlank(application, "applicationId")
            ));
        }
    }

    private ActivityRecord createActivity(String happenedAt,
                                          String activityType,
                                          String message,
                                          String relatedEntityType,
                                          String relatedEntityId) {
        LocalDateTime parsedTime = parseDateTime(happenedAt);
        LocalDateTime sortTime = parsedTime == null ? LocalDateTime.MIN : parsedTime;

        Map<String, Object> activity = new LinkedHashMap<String, Object>();
        activity.put("activityType", activityType);
        activity.put("message", message);
        activity.put("timeLabel", formatTimeLabel(happenedAt, parsedTime));
        activity.put("relatedEntityType", relatedEntityType);
        activity.put("relatedEntityId", relatedEntityId);
        return new ActivityRecord(sortTime, activity);
    }

    private String resolvePostingTitle(Map<String, Object> posting) {
        return firstNonBlank(posting, "courseName", "postingTitle", "courseCode", "postingId");
    }

    private String formatTimeLabel(String rawValue, LocalDateTime parsedTime) {
        if (parsedTime != null) {
            return DATE_TIME_MINUTES_FORMATTER.format(parsedTime);
        }
        return rawValue == null ? "" : rawValue.trim();
    }

    private LocalDateTime parseDateTime(String value) {
        if (isBlank(value)) {
            return null;
        }

        String normalized = value.trim();
        try {
            return LocalDateTime.parse(normalized, DATE_TIME_SECONDS_FORMATTER);
        } catch (DateTimeParseException ignored) {
            // Try the next supported format.
        }
        try {
            return LocalDateTime.parse(normalized, DATE_TIME_MINUTES_FORMATTER);
        } catch (DateTimeParseException ignored) {
            // Try the next supported format.
        }
        try {
            return LocalDateTime.parse(normalized, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException ignored) {
            // Fall through to date-only parsing.
        }

        LocalDate date = parseDate(normalized);
        return date == null ? null : date.atStartOfDay();
    }

    private LocalDate parseDate(String value) {
        if (isBlank(value)) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    private void ensureJsonArrayFile(Path path) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(path)) {
                Files.write(path, "[]".getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException ignored) {
            // Keep analytics read-only for the caller even when bootstrap fails.
        }
    }

    private String firstNonBlank(Map<String, Object> map, String... keys) {
        for (String key : keys) {
            String value = stringValue(map.get(key));
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }
        return null;
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

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value).trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static final class ActivityRecord {
        private final LocalDateTime sortTime;
        private final Map<String, Object> payload;

        private ActivityRecord(LocalDateTime sortTime, Map<String, Object> payload) {
            this.sortTime = sortTime;
            this.payload = payload;
        }
    }
}
