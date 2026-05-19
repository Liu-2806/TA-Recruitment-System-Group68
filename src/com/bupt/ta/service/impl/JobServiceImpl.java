package com.bupt.ta.service.impl;

import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.model.Role;
import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.repository.file.ApplicationDataRepository;
import com.bupt.ta.repository.file.PostingDataRepository;
import com.bupt.ta.service.JobService;
import com.bupt.ta.service.NotificationService;
import com.bupt.ta.util.ActivityTypeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JobServiceImpl implements JobService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final PostingDataRepository postingDataRepository;
    private final UserRepository userRepository;
    private final ApplicationDataRepository applicationDataRepository;
    private final NotificationService notificationService;

    public JobServiceImpl(PostingDataRepository postingDataRepository, UserRepository userRepository) {
        this(postingDataRepository, userRepository, null, null);
    }

    public JobServiceImpl(PostingDataRepository postingDataRepository,
                          UserRepository userRepository,
                          ApplicationDataRepository applicationDataRepository) {
        this(postingDataRepository, userRepository, applicationDataRepository, null);
    }

    public JobServiceImpl(PostingDataRepository postingDataRepository,
                          UserRepository userRepository,
                          ApplicationDataRepository applicationDataRepository,
                          NotificationService notificationService) {
        this.postingDataRepository = postingDataRepository;
        this.userRepository = userRepository;
        this.applicationDataRepository = applicationDataRepository;
        this.notificationService = notificationService;
    }

    @Override
    public PageResult<Map<String, Object>> searchOpenJobs(JobQuery query) {
        List<Map<String, Object>> filtered = new ArrayList<>();
        JobQuery safeQuery = query == null ? new JobQuery() : query;
        for (Map<String, Object> posting : postingDataRepository.findAll()) {
            if (!"OPEN".equalsIgnoreCase(String.valueOf(posting.get("status")))) {
                continue;
            }
            if (isExpired(posting.get("deadline"))) {
                continue;
            }
            String requiredSkillsText = joinValue(posting.get("requiredSkills"));
            String responsibilitiesText = joinValue(posting.get("roleResponsibilities"));
            if (safeQuery.getKeyword() != null && !safeQuery.getKeyword().isBlank()) {
                String keyword = safeQuery.getKeyword().toLowerCase(Locale.ROOT);
                String haystack = (
                    String.valueOf(posting.get("courseName")) + " "
                        + String.valueOf(posting.get("courseCode")) + " "
                        + String.valueOf(posting.get("moName")) + " "
                        + String.valueOf(posting.get("description")) + " "
                        + String.valueOf(posting.get("department")) + " "
                        + String.valueOf(posting.get("moduleType")) + " "
                        + requiredSkillsText + " "
                        + responsibilitiesText
                ).toLowerCase(Locale.ROOT);
                if (!haystack.contains(keyword)) {
                    continue;
                }
            }
            if (safeQuery.getMajor() != null && !safeQuery.getMajor().isBlank()) {
                if (!matchesMajorFilter(posting, safeQuery.getMajor().trim(), requiredSkillsText)) {
                    continue;
                }
            }
            if (!matchesFilter(posting.get("department"), safeQuery.getDepartment())) {
                continue;
            }
            if (!matchesFilter(posting.get("moduleType"), safeQuery.getModuleType())) {
                continue;
            }
            if (!matchesFilter(responsibilitiesText, safeQuery.getResponsibilityKeyword())) {
                continue;
            }
            filtered.add(new LinkedHashMap<>(posting));
        }
        sortJobs(filtered, safeQuery.getSortBy());
        List<Map<String, Object>> pagedRecords = paginate(filtered, safeQuery.getPage(), safeQuery.getSize());
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setRecords(pagedRecords);
        result.setPage(safeQuery.getPage());
        result.setSize(safeQuery.getSize());
        result.setTotal(filtered.size());
        return result;
    }

    @Override
    public Map<String, Object> getJobById(String jobId) {
        Map<String, Object> job = postingDataRepository.findByPostingId(jobId);
        if (job == null) {
            throw new IllegalStateException("Job posting not found: " + jobId);
        }
        return job;
    }

    @Override
    public Map<String, Object> createJob(String moUserId, Map<String, Object> params) {
        Map<String, Object> mo = requireMO(moUserId);
        Map<String, Object> posting = buildPostingFromParams(params);

        String now = LocalDateTime.now().format(FORMATTER);
        Map<String, Object> record = new LinkedHashMap<>();
        record.put("postingId", nextPostingId());
        record.put("courseCode", posting.get("courseCode"));
        record.put("courseName", posting.get("courseName"));
        record.put("moId", moUserId);
        record.put("moName", firstNonBlank(mo, "fullName", "displayName", "moId"));
        record.put("vacancies", posting.get("vacancies"));
        record.put("applicationCount", 0);
        record.put("deadline", posting.get("deadline"));
        record.put("description", posting.get("description"));
        record.put("requiredSkills", posting.get("requiredSkills"));
        record.put("postingType", posting.get("postingType"));
        record.put("estimatedWorkloadHours", posting.get("estimatedWorkloadHours"));
        record.put("status", posting.get("status"));
        copyActivityFields(posting, record);
        record.put("createdAt", now);
        record.put("updatedAt", now);
        postingDataRepository.save(record);
        return record;
    }

    @Override
    public Map<String, Object> updateJob(String moUserId, String postingId, Map<String, Object> params) {
        if (postingId == null || postingId.isBlank()) {
            throw new IllegalStateException("Posting ID is required.");
        }
        requireMO(moUserId);
        Map<String, Object> existing = postingDataRepository.findByPostingId(postingId);
        if (existing == null) {
            throw new IllegalStateException("Job posting not found: " + postingId);
        }
        if (!moUserId.equals(String.valueOf(existing.get("moId")))) {
            throw new IllegalStateException("You can only edit your own job postings.");
        }
        Map<String, Object> updated = buildPostingFromParams(params);

        Map<String, Object> record = new LinkedHashMap<>(existing);
        record.put("courseCode", updated.get("courseCode"));
        record.put("courseName", updated.get("courseName"));
        record.put("vacancies", updated.get("vacancies"));
        record.put("deadline", updated.get("deadline"));
        record.put("description", updated.get("description"));
        record.put("requiredSkills", updated.get("requiredSkills"));
        record.put("postingType", updated.get("postingType"));
        record.put("estimatedWorkloadHours", updated.get("estimatedWorkloadHours"));
        record.put("status", updated.get("status"));
        if ("ACTIVITY".equals(updated.get("postingType"))) {
            copyActivityFields(updated, record);
        } else {
            record.remove("activityType");
            record.remove("activityDate");
            record.remove("activityStartTime");
            record.remove("activityEndTime");
            record.remove("activityLocation");
        }
        record.put("updatedAt", LocalDateTime.now().format(FORMATTER));

        postingDataRepository.save(record);
        withdrawApplicationsForPostingUpdate(postingId);
        return record;
    }

    private Map<String, Object> buildPostingFromParams(Map<String, Object> params) {
        String postingType = normalizePostingType(firstNonBlank(params, "postingType"));
        String courseCode = requireText(firstNonBlank(params, "courseCode"), "Course code is required.");
        String courseName = requireText(firstNonBlank(params, "courseName", "title"), "Course name is required.");
        String deadline = normalizeDate(requireText(firstNonBlank(params, "deadline"), "Application deadline is required."));
        String description = requireText(firstNonBlank(params, "description"), "Position description is required.");
        int vacancies = requirePositiveInt(firstNonBlank(params, "vacancies", "headcount"), "Number of vacancies must be a positive integer.");
        int estimatedWorkloadHours = requirePositiveInt(
            firstNonBlank(params, "estimatedWorkloadHours", "workloadHours"),
            "Estimated workload hours must be a positive integer."
        );
        List<String> requiredSkills = parseSkills(params.get("requiredSkills"));
        if (requiredSkills.isEmpty()) {
            requiredSkills = parseSkills(params.get("requirements"));
        }
        if (requiredSkills.isEmpty()) {
            throw new IllegalStateException("At least one required skill must be provided.");
        }

        Map<String, Object> posting = new LinkedHashMap<>();
        posting.put("courseCode", courseCode);
        posting.put("courseName", courseName);
        posting.put("vacancies", vacancies);
        posting.put("deadline", deadline);
        posting.put("description", description);
        posting.put("requiredSkills", requiredSkills);
        posting.put("postingType", postingType);
        posting.put("estimatedWorkloadHours", estimatedWorkloadHours);
        posting.put("status", normalizePostingStatus(firstNonBlank(params, "status")));
        if ("ACTIVITY".equals(postingType)) {
            String activityDate = normalizeDate(requireText(firstNonBlank(params, "activityDate"), "Activity date is required."));
            String activityStartTime = normalizeTime(requireText(firstNonBlank(params, "activityStartTime"), "Activity start time is required."));
            String activityEndTime = normalizeTime(requireText(firstNonBlank(params, "activityEndTime"), "Activity end time is required."));
            posting.put("activityType", normalizeActivityType(firstNonBlank(params, "activityType")));
            posting.put("activityDate", activityDate);
            posting.put("activityStartTime", activityStartTime);
            posting.put("activityEndTime", activityEndTime);
            posting.put("activityLocation", firstNonBlankText(firstNonBlank(params, "activityLocation"), "Assigned venue"));
            posting.put("moduleType", "Activity");
        }
        return posting;
    }

    private void copyActivityFields(Map<String, Object> source, Map<String, Object> target) {
        if (!"ACTIVITY".equals(source.get("postingType"))) {
            return;
        }
        target.put("activityType", source.get("activityType"));
        target.put("activityDate", source.get("activityDate"));
        target.put("activityStartTime", source.get("activityStartTime"));
        target.put("activityEndTime", source.get("activityEndTime"));
        target.put("activityLocation", source.get("activityLocation"));
        target.put("moduleType", source.get("moduleType"));
    }

    private void withdrawApplicationsForPostingUpdate(String postingId) {
        if (applicationDataRepository == null) {
            return;
        }
        String now = LocalDateTime.now().format(FORMATTER);
        String reason = "Posting was edited by MO; application automatically withdrawn.";
        Map<String, Object> posting = postingDataRepository.findByPostingId(postingId);
        String courseName = posting == null ? "" : String.valueOf(posting.getOrDefault("courseName", ""));
        String courseCode = posting == null ? "" : String.valueOf(posting.getOrDefault("courseCode", ""));
        for (Map<String, Object> application : applicationDataRepository.findByPostingId(postingId)) {
            String status = String.valueOf(application.getOrDefault("status", "")).trim().toUpperCase(Locale.ROOT);
            if ("WITHDRAWN".equals(status) || "REJECTED".equals(status)) {
                continue;
            }
            Map<String, Object> record = new LinkedHashMap<>(application);
            record.put("status", "WITHDRAWN");
            record.put("statusLabel", "Withdrawn");
            record.put("withdrawalReason", reason);
            record.put("updatedAt", now);
            applicationDataRepository.save(record);
            emitPostingEditedNotification(record, courseName, courseCode);
        }
    }

    private void emitPostingEditedNotification(Map<String, Object> application, String courseName, String courseCode) {
        if (notificationService == null) {
            return;
        }
        String taId = String.valueOf(application.getOrDefault("taId", "")).trim();
        if (taId.isEmpty()) {
            return;
        }
        String courseLabel = courseName == null || courseName.isBlank() ? "the position" : courseName;
        if (courseCode != null && !courseCode.isBlank()) {
            courseLabel = courseLabel + " (" + courseCode + ")";
        }
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("recipientId", taId);
        payload.put("recipientRole", "TA");
        payload.put("type", "APPLICATION_AUTO_WITHDRAWN");
        payload.put("title", "Application withdrawn");
        payload.put("message", "Your application for " + courseLabel + " was automatically withdrawn because the module organizer updated the posting. Please review the new requirements and reapply if you remain eligible.");
        payload.put("relatedApplicationId", application.get("applicationId"));
        payload.put("relatedPostingId", application.get("postingId"));
        notificationService.create(payload);
    }

    @Override
    public PageResult<Map<String, Object>> listJobsByMO(String moUserId, JobQuery query) {
        JobQuery safeQuery = query == null ? new JobQuery() : query;
        List<Map<String, Object>> records = new ArrayList<>();
        for (Map<String, Object> posting : postingDataRepository.findAll()) {
            if (!moUserId.equals(String.valueOf(posting.get("moId")))) {
                continue;
            }
            Map<String, Object> enriched = enrichPostingForDisplay(posting);
            if (!matchesStatus(enriched, safeQuery.getStatus())) {
                continue;
            }
            if (!matchesKeyword(enriched, safeQuery.getKeyword())) {
                continue;
            }
            records.add(enriched);
        }
        sortJobs(records, safeQuery.getSortBy());

        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setRecords(paginate(records, safeQuery.getPage(), safeQuery.getSize()));
        result.setPage(safeQuery.getPage());
        result.setSize(safeQuery.getSize());
        result.setTotal(records.size());
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> searchAllJobsForAdmin(JobQuery query) {
        JobQuery safeQuery = query == null ? new JobQuery() : query;
        List<Map<String, Object>> records = new ArrayList<>();
        for (Map<String, Object> posting : postingDataRepository.findAll()) {
            if (!matchesMoFilter(posting, safeQuery)) {
                continue;
            }
            Map<String, Object> enriched = enrichPostingForDisplay(posting);
            if (!matchesStatus(enriched, safeQuery.getStatus())) {
                continue;
            }
            if (!matchesKeyword(enriched, safeQuery.getKeyword())) {
                continue;
            }
            records.add(enriched);
        }
        sortJobs(records, safeQuery.getSortBy());

        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setRecords(paginate(records, safeQuery.getPage(), safeQuery.getSize()));
        result.setPage(safeQuery.getPage());
        result.setSize(safeQuery.getSize());
        result.setTotal(records.size());
        return result;
    }

    private Map<String, Object> enrichPostingForDisplay(Map<String, Object> posting) {
        Map<String, Object> enriched = new LinkedHashMap<>(posting);
        int vacancies = safeInt(enriched.get("vacancies"));
        enriched.put("vacancies", vacancies);
        int liveApplicationCount = countLiveApplications(String.valueOf(enriched.get("postingId")));
        enriched.put("applicationCount", liveApplicationCount);
        String rawStatus = String.valueOf(enriched.getOrDefault("status", "OPEN"));
        enriched.put("status", deriveEffectiveStatus(rawStatus, enriched.get("deadline")));
        return enriched;
    }

    private int countLiveApplications(String postingId) {
        if (applicationDataRepository == null || postingId == null || postingId.isBlank()) {
            return 0;
        }
        int count = 0;
        for (Map<String, Object> application : applicationDataRepository.findByPostingId(postingId)) {
            String status = String.valueOf(application.getOrDefault("status", "")).trim().toUpperCase(Locale.ROOT);
            if ("WITHDRAWN".equals(status)) {
                continue;
            }
            count++;
        }
        return count;
    }

    private String deriveEffectiveStatus(String currentStatus, Object deadlineValue) {
        String normalized = currentStatus == null ? "OPEN" : currentStatus.trim().toUpperCase(Locale.ROOT);
        if ("DRAFT".equals(normalized) || "CLOSED".equals(normalized)) {
            return normalized;
        }
        if (isExpired(deadlineValue)) {
            return "CLOSED";
        }
        return "OPEN";
    }

    private boolean matchesMoFilter(Map<String, Object> posting, JobQuery query) {
        String rawFilter = firstNonBlankText(query == null ? null : query.getMoFilter(),
            query == null ? null : query.getMoId(),
            query == null ? null : query.getOwnerId());
        if (rawFilter == null) {
            return true;
        }

        String normalizedFilter = rawFilter.trim().toLowerCase(Locale.ROOT);
        String moId = String.valueOf(posting.getOrDefault("moId", "")).trim().toLowerCase(Locale.ROOT);
        String moName = String.valueOf(posting.getOrDefault("moName", "")).trim().toLowerCase(Locale.ROOT);
        String ownerId = String.valueOf(posting.getOrDefault("ownerId", "")).trim().toLowerCase(Locale.ROOT);

        return normalizedFilter.equals(moId)
            || normalizedFilter.equals(ownerId)
            || moId.contains(normalizedFilter)
            || moName.contains(normalizedFilter);
    }

    private void sortJobs(List<Map<String, Object>> records, String sortBy) {
        String normalized = sortBy == null ? "" : sortBy.trim().toLowerCase(Locale.ROOT).replace(" ", "");
        Comparator<Map<String, Object>> comparator;
        if ("deadlineasc".equals(normalized) || "upcomingdeadline".equals(normalized) || "deadline".equals(normalized)) {
            comparator = Comparator.comparing((Map<String, Object> job) -> String.valueOf(job.getOrDefault("deadline", "")));
        } else if ("deadlinedesc".equals(normalized)) {
            comparator = Comparator.comparing((Map<String, Object> job) -> String.valueOf(job.getOrDefault("deadline", ""))).reversed();
        } else if ("applicationsasc".equals(normalized)) {
            comparator = Comparator.comparingInt((Map<String, Object> job) -> safeInt(job.get("applicationCount")));
        } else if ("applicationsdesc".equals(normalized)) {
            comparator = Comparator.comparingInt((Map<String, Object> job) -> safeInt(job.get("applicationCount"))).reversed();
        } else if ("mostvacancies".equals(normalized) || "vacancies".equals(normalized)) {
            comparator = Comparator.comparingInt((Map<String, Object> job) -> safeInt(job.get("vacancies"))).reversed();
        } else {
            comparator = Comparator.comparing((Map<String, Object> job) -> String.valueOf(job.getOrDefault("postingId", ""))).reversed();
        }
        records.sort(comparator);
    }

    private List<Map<String, Object>> paginate(List<Map<String, Object>> records, int page, int size) {
        if (records.isEmpty()) {
            return Collections.emptyList();
        }
        int safePage = Math.max(1, page);
        int safeSize = Math.max(1, size);
        int fromIndex = Math.min(records.size(), (safePage - 1) * safeSize);
        int toIndex = Math.min(records.size(), fromIndex + safeSize);
        return new ArrayList<>(records.subList(fromIndex, toIndex));
    }

    private int safeInt(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value).trim());
        } catch (Exception ex) {
            return 0;
        }
    }

    private boolean matchesFilter(Object fieldValue, String queryValue) {
        if (queryValue == null || queryValue.isBlank()) {
            return true;
        }
        return String.valueOf(fieldValue).toLowerCase(Locale.ROOT)
            .contains(queryValue.trim().toLowerCase(Locale.ROOT));
    }

    private boolean matchesKeyword(Map<String, Object> posting, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }
        String lowered = keyword.trim().toLowerCase(Locale.ROOT);
        String haystack = (
            String.valueOf(posting.get("courseName")) + " "
                + String.valueOf(posting.get("courseCode")) + " "
                + String.valueOf(posting.get("description")) + " "
                + String.valueOf(posting.get("moName"))
        ).toLowerCase(Locale.ROOT);
        return haystack.contains(lowered);
    }

    private boolean matchesStatus(Map<String, Object> posting, String status) {
        if (status == null || status.isBlank()) {
            return true;
        }
        return status.trim().equalsIgnoreCase(String.valueOf(posting.get("status")));
    }

    private String joinValue(Object value) {
        if (value instanceof List<?> list) {
            StringBuilder builder = new StringBuilder();
            for (Object item : list) {
                if (item == null) {
                    continue;
                }
                if (builder.length() > 0) {
                    builder.append(' ');
                }
                builder.append(String.valueOf(item));
            }
            return builder.toString();
        }
        return value == null ? "" : String.valueOf(value);
    }

    private boolean isExpired(Object deadlineValue) {
        if (deadlineValue == null) {
            return false;
        }
        String text = String.valueOf(deadlineValue).trim();
        if (text.isEmpty()) {
            return false;
        }
        try {
            return LocalDate.parse(text).isBefore(LocalDate.now());
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    private List<String> parseSkills(Object rawSkills) {
        if (rawSkills instanceof List<?> skillList) {
            List<String> normalized = new ArrayList<>();
            for (Object skill : skillList) {
                String text = skill == null ? "" : String.valueOf(skill).trim();
                if (!text.isEmpty() && !normalized.contains(text)) {
                    normalized.add(text);
                }
            }
            return normalized;
        }
        if (rawSkills == null) {
            return Collections.emptyList();
        }
        List<String> normalized = new ArrayList<>();
        String[] parts = String.valueOf(rawSkills).split("[,;\\n]");
        for (String part : parts) {
            String skill = part == null ? "" : part.trim();
            if (!skill.isEmpty() && !normalized.contains(skill)) {
                normalized.add(skill);
            }
        }
        return normalized;
    }

    private String normalizePostingStatus(String status) {
        if (status == null || status.isBlank()) {
            return "OPEN";
        }
        String normalized = status.trim().toUpperCase(Locale.ROOT);
        if (!"OPEN".equals(normalized) && !"CLOSED".equals(normalized) && !"DRAFT".equals(normalized)) {
            throw new IllegalStateException("Unsupported posting status: " + status);
        }
        return normalized;
    }

    private String normalizePostingType(String postingType) {
        if (postingType == null || postingType.isBlank()) {
            return "TA";
        }
        String normalized = postingType.trim().toUpperCase(Locale.ROOT);
        if (!"TA".equals(normalized) && !"ACTIVITY".equals(normalized)) {
            throw new IllegalStateException("Unsupported posting type: " + postingType);
        }
        return normalized;
    }

    private String normalizeActivityType(String activityType) {
        return ActivityTypeUtils.normalize(activityType);
    }

    private String normalizeDate(String rawDate) {
        String candidate = rawDate.trim().replace('/', '-').replace('.', '-');
        return LocalDate.parse(candidate).toString();
    }

    private String normalizeTime(String rawTime) {
        String value = rawTime == null ? "" : rawTime.trim();
        if (!value.matches("^\\d{2}:\\d{2}$")) {
            throw new IllegalStateException("Time must use HH:mm format.");
        }
        return value;
    }

    private String nextPostingId() {
        int max = 0;
        for (Map<String, Object> posting : postingDataRepository.findAll()) {
            String postingId = String.valueOf(posting.get("postingId"));
            if (postingId != null && postingId.startsWith("POST")) {
                try {
                    int value = Integer.parseInt(postingId.substring(4));
                    if (value > max) {
                        max = value;
                    }
                } catch (NumberFormatException ignored) {
                    // Ignore malformed IDs and continue scanning.
                }
            }
        }
        return "POST" + String.format(Locale.ENGLISH, "%03d", max + 1);
    }

    private int requirePositiveInt(String rawValue, String message) {
        int parsed = safeInt(rawValue);
        if (parsed <= 0) {
            throw new IllegalStateException(message);
        }
        return parsed;
    }

    private String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(message);
        }
        return value.trim();
    }

    private Map<String, Object> requireMO(String moUserId) {
        Map<String, Object> mo = userRepository.findById(Role.MO, moUserId);
        if (mo == null) {
            throw new IllegalStateException("MO profile not found: " + moUserId);
        }
        return mo;
    }

    private String firstNonBlankText(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null) {
                String text = value.trim();
                if (!text.isEmpty()) {
                    return text;
                }
            }
        }
        return null;
    }

    private String firstNonBlank(Map<String, Object> values, String... keys) {
        if (values == null || keys == null) {
            return null;
        }
        for (String key : keys) {
            Object value = values.get(key);
            if (value != null) {
                String text = String.valueOf(value).trim();
                if (!text.isEmpty()) {
                    return text;
                }
            }
        }
        return null;
    }

    /**
     * If posting defines {@code targetMajor}, the filter must match one comma/semicolon/pipe-separated token (case-insensitive).
     * Otherwise fall back to substring match on course name, description, and required skills.
     */
    private boolean matchesMajorFilter(Map<String, Object> posting, String majorFilterRaw, String requiredSkillsText) {
        String majorNorm = majorFilterRaw.toLowerCase(Locale.ROOT).trim();
        Object targetMajorObj = posting.get("targetMajor");
        if (targetMajorObj != null) {
            String targetStr = String.valueOf(targetMajorObj).trim();
            if (!targetStr.isEmpty()) {
                return targetMajorWhitelistContains(targetStr, majorNorm);
            }
        }
        String haystack = (String.valueOf(posting.get("courseName")) + " " + String.valueOf(posting.get("description")) + " " + requiredSkillsText)
            .toLowerCase(Locale.ROOT);
        return haystack.contains(majorNorm);
    }

    private boolean targetMajorWhitelistContains(String targetMajorField, String majorFilterLower) {
        for (String token : targetMajorField.split("[,;|]")) {
            String t = token.trim().toLowerCase(Locale.ROOT);
            if (!t.isEmpty() && t.equals(majorFilterLower)) {
                return true;
            }
        }
        return false;
    }
}
