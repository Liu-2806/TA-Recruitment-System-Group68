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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 管理员统计服务实现。
 */
public class AnalyticsServiceImpl implements AnalyticsService {
    private static final DateTimeFormatter DATE_TIME_SECONDS_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_MINUTES_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter REVIEW_QUEUE_DAY_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter REVIEW_QUEUE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
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
                String postingId = firstNonBlank(posting, "postingId");
                postingCopy.put("applicationCount", countApplicationsForPosting(postingId, applications));
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

        activePostings.sort(Comparator.comparing((Map<String, Object> posting) -> parseDate(firstNonBlank(posting, "deadline")), Comparator.nullsLast(Comparator.naturalOrder())));
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
    public Map<String, Object> getMOReviewQueue(String moUserId) {
        List<Map<String, Object>> postings = readJsonArray(DataPaths.resolvePostingsFile());
        List<Map<String, Object>> applications = readJsonArray(DataPaths.resolveApplicationsFile());
        Set<String> moPostingIds = new HashSet<String>();
        for (Map<String, Object> posting : postings) {
            if (moUserId.equals(firstNonBlank(posting, "moId"))) {
                String pid = firstNonBlank(posting, "postingId");
                if (!isBlank(pid)) {
                    moPostingIds.add(pid);
                }
            }
        }

        List<Map<String, Object>> pendingRows = new ArrayList<Map<String, Object>>();
        Set<String> postingIdsWithPending = new HashSet<String>();
        for (Map<String, Object> application : applications) {
            String postingId = firstNonBlank(application, "postingId");
            if (isBlank(postingId) || !moPostingIds.contains(postingId)) {
                continue;
            }
            if (!"SUBMITTED".equalsIgnoreCase(firstNonBlank(application, "status"))) {
                continue;
            }
            postingIdsWithPending.add(postingId);
            pendingRows.add(buildMOReviewQueueRow(application));
        }

        pendingRows.sort(
            Comparator.comparing(
                (Map<String, Object> row) -> parseDateTime(String.valueOf(row.get("appliedAtRaw"))),
                Comparator.nullsLast(Comparator.naturalOrder())
            ).reversed()
        );

        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("pendingRows", pendingRows);
        result.put("pendingCount", pendingRows.size());
        result.put("postingsInQueueCount", postingIdsWithPending.size());
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> getTAWorkloadReport(Map<String, Object> query) {
        Map<String, Object> safeQuery = query == null ? new LinkedHashMap<String, Object>() : query;
        List<Map<String, Object>> reportRows = buildTAWorkloadRows();

        List<Map<String, Object>> filteredRows = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> row : reportRows) {
            if (!matchesWorkloadKeyword(row, firstNonBlank(safeQuery, "keyword"))) {
                continue;
            }
            if (!matchesWorkloadMajor(row, firstNonBlank(safeQuery, "major"))) {
                continue;
            }
            if (!matchesWorkloadStatus(row, firstNonBlank(safeQuery, "status"))) {
                continue;
            }
            filteredRows.add(row);
        }

        sortWorkloadRows(filteredRows, firstNonBlank(safeQuery, "sortBy"));

        int page = parsePositiveInt(firstNonBlank(safeQuery, "page"), 1);
        int size = parsePositiveInt(firstNonBlank(safeQuery, "size"), 10);

        PageResult<Map<String, Object>> result = new PageResult<Map<String, Object>>();
        result.setRecords(paginateRows(filteredRows, page, size));
        result.setPage(page);
        result.setSize(size);
        result.setTotal(filteredRows.size());
        return result;
    }

    @Override
    public Map<String, Object> getTAWorkloadDetail(String taId) {
        String normalizedTaId = taId == null ? null : taId.trim();
        if (isBlank(normalizedTaId)) {
            throw new IllegalStateException("taId is required");
        }

        Map<String, Object> taRecord = userRepository.findById(Role.TA, normalizedTaId);
        if (taRecord == null) {
            throw new IllegalStateException("TA profile not found: " + normalizedTaId);
        }

        List<Map<String, Object>> applications = readJsonArray(DataPaths.resolveApplicationsFile());
        List<Map<String, Object>> postings = readJsonArray(DataPaths.resolvePostingsFile());
        Map<String, Map<String, Object>> postingIndex = indexPostingsById(postings);

        List<Map<String, Object>> workingPositions = new ArrayList<Map<String, Object>>();
        int totalWorkloadHours = 0;
        String peakDay = "N/A";
        for (Map<String, Object> application : applications) {
            if (!normalizedTaId.equals(firstNonBlank(application, "taId", "taUserId"))) {
                continue;
            }
            if (!"ACCEPTED".equalsIgnoreCase(firstNonBlank(application, "status"))) {
                continue;
            }

            String postingId = firstNonBlank(application, "postingId");
            Map<String, Object> posting = postingIndex.get(postingId);
            if (posting == null) {
                continue;
            }

            int workloadHours = safeInt(posting.get("estimatedWorkloadHours"));
            totalWorkloadHours += workloadHours;

            String appliedAt = firstNonBlank(application, "appliedAt", "updatedAt");
            peakDay = resolvePeakDay(appliedAt, peakDay);

            Map<String, Object> position = new LinkedHashMap<String, Object>();
            position.put("postingId", firstNonBlank(posting, "postingId"));
            position.put("courseCode", firstNonBlank(posting, "courseCode"));
            position.put("courseName", firstNonBlank(posting, "courseName"));
            position.put("roleType", resolveRoleType(firstNonBlank(posting, "moduleType")));
            position.put("workloadHours", workloadHours);
            position.put("status", isOpenPosting(posting) ? "ACTIVE" : "INACTIVE");
            workingPositions.add(position);
        }

        int activePositionCount = 0;
        for (Map<String, Object> position : workingPositions) {
            if ("ACTIVE".equalsIgnoreCase(firstNonBlank(position, "status"))) {
                activePositionCount++;
            }
        }

        Map<String, Object> taProfile = new LinkedHashMap<String, Object>();
        taProfile.put("taId", firstNonBlank(taRecord, "taId", "id"));
        taProfile.put("fullName", firstNonBlank(taRecord, "fullName", "displayName"));
        taProfile.put("studentId", firstNonBlank(taRecord, "studentId"));
        taProfile.put("majorProgram", firstNonBlank(taRecord, "majorProgram"));
        taProfile.put("academicYear", firstNonBlank(taRecord, "academicYear"));
        taProfile.put("email", firstNonBlank(taRecord, "email"));
        taProfile.put("phone", firstNonBlank(taRecord, "phone"));

        String riskLevel = resolveRiskLevel(totalWorkloadHours);
        Map<String, Object> workloadAnalysis = new LinkedHashMap<String, Object>();
        workloadAnalysis.put("totalWorkloadHours", totalWorkloadHours);
        workloadAnalysis.put("activePositionCount", activePositionCount);
        workloadAnalysis.put("peakDay", peakDay);
        workloadAnalysis.put("riskLevel", riskLevel);
        workloadAnalysis.put("statusLabel", resolveStatusLabel(riskLevel, activePositionCount));

        Map<String, Object> detail = new LinkedHashMap<String, Object>();
        detail.put("taProfile", taProfile);
        detail.put("workingPositions", workingPositions);
        detail.put("workloadAnalysis", workloadAnalysis);
        detail.put("adminSuggestions", buildAdminSuggestions(riskLevel, activePositionCount));
        return detail;
    }

    @Override
    public Map<String, Object> getTAWorkloadDistributionSummary(Map<String, Object> query) {
        Map<String, Object> safeQuery = query == null ? new LinkedHashMap<String, Object>() : query;
        List<Map<String, Object>> filteredRows = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> row : buildTAWorkloadRows()) {
            if (!matchesWorkloadKeyword(row, firstNonBlank(safeQuery, "keyword"))) {
                continue;
            }
            if (!matchesWorkloadMajor(row, firstNonBlank(safeQuery, "major"))) {
                continue;
            }
            if (!matchesWorkloadStatus(row, firstNonBlank(safeQuery, "status"))) {
                continue;
            }
            filteredRows.add(row);
        }

        int bucket0To4 = 0;
        int bucket4To8 = 0;
        int bucket8To12 = 0;
        int bucket12Plus = 0;
        int criticalAlertCount = 0;
        Map<String, Integer> majorHours = new LinkedHashMap<String, Integer>();

        for (Map<String, Object> row : filteredRows) {
            int totalHours = safeInt(row.get("totalWorkloadHours"));
            if (totalHours < 4) {
                bucket0To4++;
            } else if (totalHours < 8) {
                bucket4To8++;
            } else if (totalHours < 12) {
                bucket8To12++;
            } else {
                bucket12Plus++;
            }

            if ("HIGH_ALERT".equalsIgnoreCase(firstNonBlank(row, "workloadStatus"))) {
                criticalAlertCount++;
            }

            String major = firstNonBlank(row, "majorProgram");
            if (isBlank(major)) {
                major = "Unknown";
            }
            int current = majorHours.containsKey(major) ? majorHours.get(major) : 0;
            majorHours.put(major, current + totalHours);
        }

        String peakWorkloadGroup = "N/A";
        int maxHours = Integer.MIN_VALUE;
        for (Map.Entry<String, Integer> entry : majorHours.entrySet()) {
            if (entry.getValue() > maxHours) {
                maxHours = entry.getValue();
                peakWorkloadGroup = entry.getKey();
            }
        }

        List<Map<String, Object>> hourBuckets = new ArrayList<Map<String, Object>>();
        hourBuckets.add(bucketItem("0-4h", bucket0To4));
        hourBuckets.add(bucketItem("4-8h", bucket4To8));
        hourBuckets.add(bucketItem("8-12h", bucket8To12));
        hourBuckets.add(bucketItem("12h+", bucket12Plus));

        Map<String, Object> summary = new LinkedHashMap<String, Object>();
        summary.put("hourBuckets", hourBuckets);
        summary.put("peakWorkloadGroup", peakWorkloadGroup);
        summary.put("criticalAlertCount", criticalAlertCount);
        return summary;
    }

    private List<Map<String, Object>> buildTAWorkloadRows() {
        List<Map<String, Object>> taRecords = userRepository.findAllByRole(Role.TA);
        List<Map<String, Object>> applications = readJsonArray(DataPaths.resolveApplicationsFile());
        List<Map<String, Object>> postings = readJsonArray(DataPaths.resolvePostingsFile());
        Map<String, Map<String, Object>> postingIndex = indexPostingsById(postings);
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

        for (Map<String, Object> ta : taRecords) {
            String taId = firstNonBlank(ta, "taId", "id");
            if (isBlank(taId)) {
                continue;
            }
            int activePositionCount = 0;
            int totalWorkloadHours = 0;

            for (Map<String, Object> application : applications) {
                if (!taId.equals(firstNonBlank(application, "taId", "taUserId"))) {
                    continue;
                }
                if (!"ACCEPTED".equalsIgnoreCase(firstNonBlank(application, "status"))) {
                    continue;
                }

                String postingId = firstNonBlank(application, "postingId");
                Map<String, Object> posting = postingIndex.get(postingId);
                if (posting == null) {
                    continue;
                }

                totalWorkloadHours += safeInt(posting.get("estimatedWorkloadHours"));
                if (isOpenPosting(posting)) {
                    activePositionCount++;
                }
            }

            Map<String, Object> row = new LinkedHashMap<String, Object>();
            row.put("taId", taId);
            row.put("fullName", firstNonBlank(ta, "fullName", "displayName"));
            row.put("studentId", firstNonBlank(ta, "studentId"));
            row.put("majorProgram", firstNonBlank(ta, "majorProgram"));
            row.put("activePositionCount", activePositionCount);
            row.put("totalWorkloadHours", totalWorkloadHours);
            row.put("workloadStatus", resolveWorkloadStatus(activePositionCount, totalWorkloadHours));
            rows.add(row);
        }
        return rows;
    }

    private Map<String, Map<String, Object>> indexPostingsById(List<Map<String, Object>> postings) {
        Map<String, Map<String, Object>> index = new LinkedHashMap<String, Map<String, Object>>();
        for (Map<String, Object> posting : postings) {
            String postingId = firstNonBlank(posting, "postingId");
            if (!isBlank(postingId)) {
                index.put(postingId, posting);
            }
        }
        return index;
    }

    private boolean matchesWorkloadKeyword(Map<String, Object> row, String keyword) {
        if (isBlank(keyword)) {
            return true;
        }
        String lowered = keyword.trim().toLowerCase(Locale.ROOT);
        String haystack = (firstNonBlank(row, "fullName", "taId", "studentId") + " "
            + firstNonBlank(row, "studentId") + " "
            + firstNonBlank(row, "taId")).toLowerCase(Locale.ROOT);
        return haystack.contains(lowered);
    }

    private boolean matchesWorkloadMajor(Map<String, Object> row, String major) {
        if (isBlank(major)) {
            return true;
        }
        String currentMajor = firstNonBlank(row, "majorProgram");
        return currentMajor != null && currentMajor.toLowerCase(Locale.ROOT).contains(major.trim().toLowerCase(Locale.ROOT));
    }

    private boolean matchesWorkloadStatus(Map<String, Object> row, String status) {
        if (isBlank(status)) {
            return true;
        }
        String normalizedFilter = normalizeWorkloadStatus(status);
        if (isBlank(normalizedFilter)) {
            return true;
        }
        return normalizedFilter.equalsIgnoreCase(firstNonBlank(row, "workloadStatus"));
    }

    private String normalizeWorkloadStatus(String status) {
        if (isBlank(status)) {
            return null;
        }
        String normalized = status.trim().toUpperCase(Locale.ROOT).replace(' ', '_');
        if ("NORMAL".equals(normalized) || "HIGH_ALERT".equals(normalized) || "NOT_APPLIED".equals(normalized)) {
            return normalized;
        }
        return null;
    }

    private void sortWorkloadRows(List<Map<String, Object>> rows, String sortBy) {
        String normalizedSortBy = isBlank(sortBy) ? "" : sortBy.trim().toLowerCase(Locale.ROOT).replace(" ", "");
        Comparator<Map<String, Object>> comparator;
        if ("fullnameasc".equals(normalizedSortBy) || "nameasc".equals(normalizedSortBy)) {
            comparator = Comparator.comparing((Map<String, Object> row) -> firstNonBlank(row, "fullName"), Comparator.nullsLast(String::compareTo));
        } else if ("fullnamedesc".equals(normalizedSortBy) || "namedesc".equals(normalizedSortBy)) {
            comparator = Comparator.comparing((Map<String, Object> row) -> firstNonBlank(row, "fullName"), Comparator.nullsLast(String::compareTo)).reversed();
        } else if ("majorasc".equals(normalizedSortBy)) {
            comparator = Comparator.comparing((Map<String, Object> row) -> firstNonBlank(row, "majorProgram"), Comparator.nullsLast(String::compareTo));
        } else if ("majordesc".equals(normalizedSortBy)) {
            comparator = Comparator.comparing((Map<String, Object> row) -> firstNonBlank(row, "majorProgram"), Comparator.nullsLast(String::compareTo)).reversed();
        } else if ("hoursasc".equals(normalizedSortBy) || "totalworkloadhoursasc".equals(normalizedSortBy)) {
            comparator = Comparator.comparingInt((Map<String, Object> row) -> safeInt(row.get("totalWorkloadHours")));
        } else {
            comparator = Comparator.comparingInt((Map<String, Object> row) -> safeInt(row.get("totalWorkloadHours"))).reversed()
                .thenComparing((Map<String, Object> row) -> firstNonBlank(row, "fullName"), Comparator.nullsLast(String::compareTo));
        }
        rows.sort(comparator);
    }

    private List<Map<String, Object>> paginateRows(List<Map<String, Object>> rows, int page, int size) {
        if (rows.isEmpty()) {
            return Collections.emptyList();
        }
        int safePage = Math.max(1, page);
        int safeSize = Math.max(1, size);
        int fromIndex = Math.min(rows.size(), (safePage - 1) * safeSize);
        int toIndex = Math.min(rows.size(), fromIndex + safeSize);
        return new ArrayList<Map<String, Object>>(rows.subList(fromIndex, toIndex));
    }

    private int parsePositiveInt(String value, int defaultValue) {
        if (isBlank(value)) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private int safeInt(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value).trim());
        } catch (Exception ex) {
            return 0;
        }
    }

    private String resolveWorkloadStatus(int activePositionCount, int totalWorkloadHours) {
        if (activePositionCount <= 0 || totalWorkloadHours <= 0) {
            return "NOT_APPLIED";
        }
        if (totalWorkloadHours >= 12) {
            return "HIGH_ALERT";
        }
        return "NORMAL";
    }

    private String resolveRoleType(String moduleType) {
        if (isBlank(moduleType)) {
            return "COURSE_TA";
        }
        String normalized = moduleType.trim().toUpperCase(Locale.ROOT);
        if (normalized.contains("LAB")) {
            return "LAB";
        }
        if (normalized.contains("INVIGILATION")) {
            return "INVIGILATION";
        }
        if (normalized.contains("CHECKOFF")) {
            return "CHECKOFF";
        }
        return "COURSE_TA";
    }

    private String resolvePeakDay(String timestamp, String fallback) {
        LocalDateTime parsed = parseDateTime(timestamp);
        if (parsed == null) {
            return fallback == null ? "N/A" : fallback;
        }
        return parsed.getDayOfWeek().name().substring(0, 1) + parsed.getDayOfWeek().name().substring(1).toLowerCase(Locale.ROOT);
    }

    private String resolveRiskLevel(int totalWorkloadHours) {
        if (totalWorkloadHours >= 12) {
            return "HIGH";
        }
        if (totalWorkloadHours >= 8) {
            return "MEDIUM";
        }
        return "LOW";
    }

    private String resolveStatusLabel(String riskLevel, int activePositionCount) {
        if (activePositionCount <= 0) {
            return "Not applied to active positions";
        }
        if ("HIGH".equalsIgnoreCase(riskLevel)) {
            return "High workload pressure";
        }
        if ("MEDIUM".equalsIgnoreCase(riskLevel)) {
            return "Moderate workload distribution";
        }
        return "Normal workload distribution";
    }

    private List<String> buildAdminSuggestions(String riskLevel, int activePositionCount) {
        List<String> suggestions = new ArrayList<String>();
        if (activePositionCount <= 0) {
            suggestions.add("No active TA assignment yet. Consider recommending suitable open positions.");
            suggestions.add("Follow up on profile readiness before urgent scheduling periods.");
            return suggestions;
        }
        if ("HIGH".equalsIgnoreCase(riskLevel)) {
            suggestions.add("Avoid assigning additional duties this week.");
            suggestions.add("Coordinate with MO to rebalance workload if possible.");
            return suggestions;
        }
        if ("MEDIUM".equalsIgnoreCase(riskLevel)) {
            suggestions.add("Monitor workload trend before assigning new long-hour tasks.");
            suggestions.add("Prefer short operational duties if extra support is required.");
            return suggestions;
        }
        suggestions.add("Safe to keep current assignments.");
        suggestions.add("Can absorb one short operational duty if needed.");
        return suggestions;
    }

    private Map<String, Object> bucketItem(String label, int count) {
        Map<String, Object> item = new LinkedHashMap<String, Object>();
        item.put("label", label);
        item.put("count", count);
        return item;
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

    private int countApplicationsForPosting(String postingId, List<Map<String, Object>> applications) {
        if (isBlank(postingId)) {
            return 0;
        }
        int n = 0;
        for (Map<String, Object> application : applications) {
            if (postingId.equals(firstNonBlank(application, "postingId"))) {
                n++;
            }
        }
        return n;
    }

    private Map<String, Object> buildMOReviewQueueRow(Map<String, Object> application) {
        String taId = firstNonBlank(application, "taId");
        Map<String, Object> ta = userRepository.findById(Role.TA, taId);
        String taEmail = ta == null ? "" : firstNonBlank(ta, "email");
        String taName = firstNonBlank(
            application.get("taName"),
            ta == null ? null : firstNonBlank(ta, "fullName", "displayName"),
            taId
        );

        String appliedAt = firstNonBlank(application, "appliedAt");
        LocalDateTime dt = parseDateTime(appliedAt);
        String submittedIso = "";
        if (dt != null) {
            submittedIso = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dt);
        } else if (!isBlank(appliedAt)) {
            submittedIso = appliedAt.trim().replace(' ', 'T');
        }

        int match = safeInt(application.get("skillMatchScore"));
        String priorityKey = match >= 80 ? "high" : (match >= 60 ? "medium" : "low");
        String priorityLabel = match >= 80 ? "High" : (match >= 60 ? "Medium" : "Low");

        String courseCode = firstNonBlank(application, "courseCode");
        String moName = firstNonBlank(application, "moName");
        String positionSubtitle = (isBlank(courseCode) ? "" : courseCode)
            + (isBlank(courseCode) || isBlank(moName) ? "" : " · ")
            + (isBlank(moName) ? "" : moName);

        Map<String, Object> row = new LinkedHashMap<String, Object>();
        row.put("applicationId", firstNonBlank(application, "applicationId"));
        row.put("taName", taName);
        row.put("taEmail", taEmail);
        row.put("postingTitle", firstNonBlank(application, "postingTitle", "courseName"));
        row.put("courseCode", courseCode);
        row.put("positionSubtitle", positionSubtitle);
        row.put("skillMatchScore", match);
        row.put("matchStrong", match >= 85);
        row.put("priorityKey", priorityKey);
        row.put("priorityLabel", priorityLabel);
        row.put("appliedAtRaw", appliedAt);
        row.put("submittedIso", submittedIso);
        row.put("submittedDateDisplay", dt == null ? "" : REVIEW_QUEUE_DAY_FORMATTER.format(dt));
        row.put("submittedTimeDisplay", dt == null ? "" : REVIEW_QUEUE_TIME_FORMATTER.format(dt));
        row.put("courseSortKey", courseCode == null ? "" : courseCode);
        row.put("taNameSortKey", taName == null ? "" : taName);
        return row;
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
