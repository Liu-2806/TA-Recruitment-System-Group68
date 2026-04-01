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
    private final UserRepository userRepository;

    public AnalyticsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, Object> getSystemOverview() {
        Map<String, Object> overview = new LinkedHashMap<String, Object>();
        overview.put("totalTas", userRepository.countByRole(Role.TA));
        overview.put("totalMos", userRepository.countByRole(Role.MO));
        overview.put("totalAdmins", userRepository.countByRole(Role.ADMIN));
        overview.put("totalPostings", readJsonArray(DataPaths.resolvePostingsFile()).size());
        overview.put("totalApplications", readJsonArray(DataPaths.resolveApplicationsFile()).size());
        overview.put("recentActivities", Collections.emptyList());
        return overview;
    }

    @Override
    public Map<String, Object> getMODashboardOverview(String moUserId) {
        Map<String, Object> mo = userRepository.findById(Role.MO, moUserId);
        if (mo == null) {
            throw new IllegalStateException("MO profile not found: " + moUserId);
        }

        List<Map<String, Object>> postings = readJsonArray(DataPaths.resolvePostingsFile());
        List<Map<String, Object>> applications = readJsonArray(DataPaths.resolveApplicationsFile());
        List<Map<String, Object>> activePostings = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> recentActivity = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> alerts = new ArrayList<Map<String, Object>>();
        int awaitingReviewCount = 0;

        for (Map<String, Object> posting : postings) {
            if (!moUserId.equals(firstNonBlank(posting, "moId"))) {
                continue;
            }

            Map<String, Object> postingCopy = new LinkedHashMap<String, Object>(posting);
            if ("OPEN".equalsIgnoreCase(firstNonBlank(posting, "status"))) {
                activePostings.add(postingCopy);
            }

            if (isClosingSoon(firstNonBlank(posting, "deadline"), firstNonBlank(posting, "status"))) {
                Map<String, Object> alert = new LinkedHashMap<String, Object>();
                alert.put("type", "DEADLINE");
                alert.put("postingId", firstNonBlank(posting, "postingId"));
                alert.put("courseName", firstNonBlank(posting, "courseName"));
                alert.put("deadline", firstNonBlank(posting, "deadline"));
                alerts.add(alert);
            }

            for (Map<String, Object> application : applications) {
                if (!firstNonBlank(posting, "postingId").equals(firstNonBlank(application, "postingId"))) {
                    continue;
                }
                if ("SUBMITTED".equalsIgnoreCase(firstNonBlank(application, "status"))) {
                    awaitingReviewCount++;
                }

                Map<String, Object> activity = new LinkedHashMap<String, Object>();
                activity.put("applicationId", firstNonBlank(application, "applicationId"));
                activity.put("postingId", firstNonBlank(application, "postingId"));
                activity.put("postingTitle", firstNonBlank(application, "postingTitle", "courseName"));
                activity.put("taId", firstNonBlank(application, "taId"));
                activity.put("taName", firstNonBlank(application, "taName"));
                activity.put("status", firstNonBlank(application, "status"));
                activity.put("appliedAt", firstNonBlank(application, "appliedAt"));
                activity.put("skillMatchScore", application.get("skillMatchScore"));
                recentActivity.add(activity);
            }
        }

        activePostings.sort(Comparator.comparing((Map<String, Object> posting) -> parseDate(posting.get("deadline"))));
        recentActivity.sort(
            Comparator.comparing((Map<String, Object> activity) -> firstNonBlank(activity, "appliedAt"), Comparator.nullsLast(String::compareTo)).reversed()
        );

        Map<String, Object> overview = new LinkedHashMap<String, Object>();
        overview.put("profileCard", sanitizeUserRecord(mo));
        overview.put("awaitingReviewCount", awaitingReviewCount);
        overview.put("alerts", alerts);
        overview.put("activePostings", activePostings);
        overview.put("recentActivity", recentActivity.size() > 5 ? recentActivity.subList(0, 5) : recentActivity);
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

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value).trim();
    }

    private Map<String, Object> sanitizeUserRecord(Map<String, Object> user) {
        Map<String, Object> sanitized = new LinkedHashMap<String, Object>(user);
        sanitized.remove("password");
        sanitized.remove("passwordSalt");
        sanitized.remove("passwordHash");
        return sanitized;
    }

    private boolean isClosingSoon(String deadline, String status) {
        if (deadline == null || deadline.isEmpty() || !"OPEN".equalsIgnoreCase(status)) {
            return false;
        }
        try {
            LocalDate deadlineDate = LocalDate.parse(deadline);
            LocalDate today = LocalDate.now();
            return !deadlineDate.isBefore(today) && !deadlineDate.isAfter(today.plusDays(3));
        } catch (Exception ex) {
            return false;
        }
    }

    private LocalDate parseDate(Object rawDate) {
        if (rawDate == null) {
            return LocalDate.MAX;
        }
        try {
            return LocalDate.parse(String.valueOf(rawDate).trim());
        } catch (Exception ex) {
            return LocalDate.MAX;
        }
    }
}
