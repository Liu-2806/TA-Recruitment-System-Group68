package com.bupt.ta.service.impl;

import com.bupt.ta.dto.ApplicationQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.match.RecommendationServiceImpl;
import com.bupt.ta.model.ApplicationStatus;
import com.bupt.ta.repository.file.ApplicationDataRepository;
import com.bupt.ta.repository.file.PostingDataRepository;
import com.bupt.ta.repository.file.TADataRepository;
import com.bupt.ta.repository.file.TATimetableDataRepository;
import com.bupt.ta.service.ApplicationService;
import com.bupt.ta.service.RecommendationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ApplicationServiceImpl implements ApplicationService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final TADataRepository taDataRepository;
    private final PostingDataRepository postingDataRepository;
    private final ApplicationDataRepository applicationDataRepository;
    private final RecommendationService recommendationService;
    private final TATimetableDataRepository taTimetableDataRepository;

    public ApplicationServiceImpl(
        TADataRepository taDataRepository,
        PostingDataRepository postingDataRepository,
        ApplicationDataRepository applicationDataRepository,
        RecommendationService recommendationService,
        TATimetableDataRepository taTimetableDataRepository
    ) {
        this.taDataRepository = taDataRepository;
        this.postingDataRepository = postingDataRepository;
        this.applicationDataRepository = applicationDataRepository;
        this.recommendationService = recommendationService;
        this.taTimetableDataRepository = taTimetableDataRepository;
    }

    @Override
    public Map<String, Object> checkEligibility(String taUserId, String jobId) {
        Map<String, Object> ta = requireTa(taUserId);
        Map<String, Object> job = requireJob(jobId);
        boolean resumeUploaded = hasValue(ta.get("resumeFileName"));
        boolean alreadyApplied = findExistingApplication(taUserId, jobId) != null;
        boolean jobOpen = "OPEN".equalsIgnoreCase(String.valueOf(job.get("status")));
        boolean beforeDeadline = isBeforeDeadline(job.get("deadline"));
        boolean profileCompleted = isProfileCompleted(ta);
        List<String> scheduleConflicts = findScheduleConflicts(taUserId, jobId);
        List<String> reasons = new ArrayList<>();
        if (!profileCompleted) {
            reasons.add("Profile is incomplete.");
        }
        if (!resumeUploaded) {
            reasons.add("Resume is not uploaded.");
        }
        if (alreadyApplied) {
            reasons.add("You have already applied for this job.");
        }
        if (!beforeDeadline) {
            reasons.add("Deadline has passed.");
        }
        if (!jobOpen) {
            reasons.add("Job is not open for applications.");
        }
        reasons.addAll(scheduleConflicts);
        Map<String, Object> result = new LinkedHashMap<>();
        boolean eligible = profileCompleted && resumeUploaded && !alreadyApplied && beforeDeadline && jobOpen && scheduleConflicts.isEmpty();

        // 文档要求字段（L565-L572）
        result.put("eligible", eligible);
        result.put("profileCompleted", profileCompleted);
        result.put("resumeUploaded", resumeUploaded);
        result.put("alreadyApplied", alreadyApplied);
        result.put("beforeDeadline", beforeDeadline);
        result.put("scheduleConflictFree", scheduleConflicts.isEmpty());
        result.put("reasons", reasons);

        // 兼容旧字段（不删，避免影响已接入页面/逻辑）
        result.put("hasResume", resumeUploaded);
        result.put("duplicateApplication", alreadyApplied);
        result.put("jobOpen", jobOpen);
        return result;
    }

    @Override
    public Map<String, Object> createApplication(String taUserId, String jobId, String statement) {
        Map<String, Object> eligibility = checkEligibility(taUserId, jobId);
        if (!Boolean.TRUE.equals(eligibility.get("eligible"))) {
            throw new IllegalStateException("The current TA profile is not eligible to submit this application.");
        }
        Map<String, Object> ta = requireTa(taUserId);
        Map<String, Object> job = requireJob(jobId);
        Map<String, Object> application = new LinkedHashMap<>();
        application.put("applicationId", applicationDataRepository.nextApplicationId());
        application.put("postingId", jobId);
        application.put("postingTitle", job.get("courseName"));
        application.put("taId", taUserId);
        application.put("taName", ta.get("fullName"));
        application.put("appliedAt", LocalDateTime.now().format(FORMATTER));
        application.put("updatedAt", application.get("appliedAt"));
        application.put("status", ApplicationStatus.SUBMITTED.name());
        application.put("statement", statement == null ? "" : statement.trim());
        application.put("feedback", "");
        application.put("courseCode", job.get("courseCode"));
        application.put("moName", job.get("moName"));
        application.put("historyLogs", buildHistoryLogs(
            Map.of(
                "time", application.get("appliedAt"),
                "action", ApplicationStatus.SUBMITTED.name(),
                "description", "Application submitted with supporting statement and resume."
            )
        ));
        recommendationService.buildJobMatchForTA(taUserId, jobId).forEach(application::put);
        application.put("skillMatchScore", application.get("score"));
        application.put("skillMatchExplanation", application.get("explanation"));
        application.put("matchMethod", application.get("method"));
        application.put("statusLabel", statusLabel(String.valueOf(application.get("status"))));
        application.remove("score");
        application.remove("explanation");
        application.remove("method");
        applicationDataRepository.save(application);
        updatePostingApplicationCount(jobId, 1);
        return application;
    }

    @Override
    public PageResult<Map<String, Object>> listApplicationsByTA(String taUserId, ApplicationQuery query) {
        ApplicationQuery safeQuery = query == null ? new ApplicationQuery() : query;
        List<Map<String, Object>> records = enrichApplicationsForTA(applicationDataRepository.findByTaId(taUserId));
        records = applyApplicationFilters(records, safeQuery);
        sortApplications(records, safeQuery.getSortBy());
        List<Map<String, Object>> pagedRecords = paginate(records, safeQuery.getPage(), safeQuery.getSize());
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setRecords(pagedRecords);
        result.setPage(safeQuery.getPage());
        result.setSize(safeQuery.getSize());
        result.setTotal(records.size());
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> listApplicationsByJob(String jobId, ApplicationQuery query) {
        ApplicationQuery safeQuery = query == null ? new ApplicationQuery() : query;
        List<Map<String, Object>> records = new ArrayList<>();
        for (Map<String, Object> record : applicationDataRepository.findByPostingId(jobId)) {
            if (recommendationService instanceof RecommendationServiceImpl impl) {
                records.add(enrichApplicationRecord(impl.enrichApplicationWithMatch(new LinkedHashMap<>(record))));
            } else {
                records.add(enrichApplicationRecord(new LinkedHashMap<>(record)));
            }
        }
        records = applyApplicationFilters(records, safeQuery);
        sortApplications(records, safeQuery.getSortBy());
        List<Map<String, Object>> pagedRecords = paginate(records, safeQuery.getPage(), safeQuery.getSize());
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setRecords(pagedRecords);
        result.setPage(safeQuery.getPage());
        result.setSize(safeQuery.getSize());
        result.setTotal(records.size());
        return result;
    }

    @Override
    public Map<String, Object> getApplicationDetailForMO(String applicationId, String moUserId) {
        Map<String, Object> record = applicationDataRepository.findByApplicationId(applicationId);
        if (record == null) {
            throw new IllegalStateException("Application not found: " + applicationId);
        }
        Map<String, Object> job = requireJob(String.valueOf(record.get("postingId")));
        if (!moUserId.equals(String.valueOf(job.get("moId")))) {
            throw new IllegalStateException("You do not have permission to review this application.");
        }
        Map<String, Object> ta = requireTa(String.valueOf(record.get("taId")));
        Map<String, Object> detail = new LinkedHashMap<>(record);
        detail.put("job", job);
        detail.put("taProfile", ta);
        if (recommendationService instanceof RecommendationServiceImpl impl) {
            impl.enrichApplicationWithMatch(detail);
        }
        return enrichApplicationRecord(detail);
    }

    @Override
    public Map<String, Object> withdrawApplicationByTA(String applicationId, String taUserId, String reason) {
        Map<String, Object> record = applicationDataRepository.findByApplicationId(applicationId);
        if (record == null) {
            throw new IllegalStateException("Application not found: " + applicationId);
        }
        if (!taUserId.equals(String.valueOf(record.get("taId")))) {
            throw new IllegalStateException("You do not have permission to update this application.");
        }
        if (!hasValue(reason)) {
            throw new IllegalStateException("A withdrawal reason is required.");
        }

        String status = normalize(String.valueOf(record.get("status")));
        if ("withdrawn".equals(status) || "rejected".equals(status) || "revocationrequested".equals(status)) {
            throw new IllegalStateException("This application can no longer be withdrawn.");
        }
        boolean acceptedAssignment = "accepted".equals(status);

        ApplicationStatus targetStatus;
        String actionLabel;
        if (acceptedAssignment) {
            targetStatus = ApplicationStatus.REVOCATION_REQUESTED;
            actionLabel = "Revocation request";
        } else {
            targetStatus = ApplicationStatus.WITHDRAWN;
            actionLabel = "Withdrawal request";
        }

        record.put("status", targetStatus.name());
        record.put("updatedAt", LocalDateTime.now().format(FORMATTER));
        record.put("statusLabel", statusLabel(targetStatus.name()));
        String message = actionLabel + " submitted by TA. Reason: " + reason.trim();
        record.put("feedback", message);
        record.put("historyLogs", appendHistoryLog(
            record.get("historyLogs"),
            Map.of(
                "time", record.get("updatedAt"),
                "action", targetStatus.name(),
                "description", message
            )
        ));
        applicationDataRepository.save(record);
        updatePostingApplicationCount(String.valueOf(record.get("postingId")), -1);
        if (acceptedAssignment) {
            taTimetableDataRepository.releaseAssignment(taUserId, applicationId);
        }
        return enrichApplicationRecord(new LinkedHashMap<>(record));
    }

    @Override
    public void updateStatusByMO(String applicationId, String moUserId, ApplicationStatus newStatus, String comment) {
        if (newStatus == null || newStatus == ApplicationStatus.SUBMITTED) {
            throw new IllegalStateException("MO review cannot set application status back to SUBMITTED.");
        }
        Map<String, Object> record = getApplicationDetailForMO(applicationId, moUserId);
        record.put("status", newStatus.name());
        record.put("feedback", comment == null ? "" : comment.trim());
        record.put("updatedAt", LocalDateTime.now().format(FORMATTER));
        record.put("statusLabel", statusLabel(newStatus.name()));
        record.put("historyLogs", appendHistoryLog(
            record.get("historyLogs"),
            Map.of(
                "time", record.get("updatedAt"),
                "action", newStatus.name(),
                "description", buildDecisionDescription(newStatus, comment)
            )
        ));
        applicationDataRepository.save(record);
    }

    private List<Map<String, Object>> enrichApplicationsForTA(List<Map<String, Object>> source) {
        List<Map<String, Object>> enriched = new ArrayList<>();
        for (Map<String, Object> record : source) {
            Map<String, Object> copy = new LinkedHashMap<>(record);
            enriched.add(enrichApplicationRecord(copy));
        }
        return enriched;
    }

    private Map<String, Object> enrichApplicationRecord(Map<String, Object> record) {
        if (record == null) {
            return new LinkedHashMap<>();
        }
        Map<String, Object> enriched = record;
        Map<String, Object> job = postingDataRepository.findByPostingId(String.valueOf(record.get("postingId")));
        Map<String, Object> ta = taDataRepository.findByTaId(String.valueOf(record.get("taId")));
        if (job != null) {
            enriched.putIfAbsent("postingTitle", job.get("courseName"));
            enriched.put("courseCode", job.get("courseCode"));
            enriched.put("moName", job.get("moName"));
        }
        if (ta != null) {
            enriched.put("taProfile", ta);
            enriched.putIfAbsent("taName", ta.get("fullName"));
        }
        String status = String.valueOf(enriched.getOrDefault("status", ApplicationStatus.SUBMITTED.name()));
        enriched.put("statusLabel", statusLabel(status));
        enriched.putIfAbsent("updatedAt", enriched.get("appliedAt"));
        enriched.putIfAbsent("historyLogs", buildHistoryLogs(
            Map.of(
                "time", String.valueOf(enriched.getOrDefault("appliedAt", "")),
                "action", status,
                "description", defaultHistoryDescription(status, enriched)
            )
        ));
        return enriched;
    }

    private List<Map<String, Object>> applyApplicationFilters(List<Map<String, Object>> records, ApplicationQuery query) {
        if (records.isEmpty() || query == null) {
            return records;
        }
        List<Map<String, Object>> filtered = new ArrayList<>();
        String statusFilter = normalize(query.getStatus());
        String keyword = normalize(query.getKeyword());
        for (Map<String, Object> record : records) {
            if (!statusFilter.isEmpty() && !normalize(String.valueOf(record.get("status"))).equals(statusFilter)) {
                continue;
            }
            if (!keyword.isEmpty()) {
                String haystack = String.join(" ",
                    normalize(String.valueOf(record.get("taName"))),
                    normalize(String.valueOf(record.get("postingTitle"))),
                    normalize(String.valueOf(record.get("courseCode"))),
                    normalize(String.valueOf(record.get("moName"))),
                    normalize(String.valueOf(record.get("feedback"))),
                    normalize(String.valueOf(record.get("statement")))
                );
                if (!haystack.contains(keyword)) {
                    continue;
                }
            }
            filtered.add(record);
        }
        return filtered;
    }

    private void sortApplications(List<Map<String, Object>> records, String sortBy) {
        String normalized = normalize(sortBy);
        Comparator<Map<String, Object>> comparator;
        if ("scoreasc".equals(normalized)) {
            comparator = Comparator.comparingInt((Map<String, Object> record) -> parseInt(record.get("skillMatchScore")));
        } else if ("scoredesc".equals(normalized)) {
            comparator = Comparator.comparingInt((Map<String, Object> record) -> parseInt(record.get("skillMatchScore"))).reversed();
        } else if ("timeasc".equals(normalized)) {
            comparator = Comparator.comparing((Map<String, Object> record) -> parseDateTime(record.get("appliedAt")), Comparator.nullsLast(Comparator.naturalOrder()));
        } else if ("timedesc".equals(normalized)) {
            comparator = Comparator.comparing((Map<String, Object> record) -> parseDateTime(record.get("appliedAt")), Comparator.nullsLast(Comparator.naturalOrder())).reversed();
        } else if ("status".equals(normalized) || "statuspriority".equals(normalized)) {
            comparator = Comparator.comparingInt((Map<String, Object> record) -> statusPriority(String.valueOf(record.get("status"))))
                .thenComparing(record -> parseDateTime(record.get("updatedAt")), Comparator.nullsLast(Comparator.reverseOrder()));
        } else if ("submitted".equals(normalized) || "recentlysubmitted".equals(normalized)) {
            comparator = Comparator.comparing((Map<String, Object> record) -> parseDateTime(record.get("appliedAt")), Comparator.nullsLast(Comparator.reverseOrder()));
        } else {
            comparator = Comparator.comparing((Map<String, Object> record) -> parseDateTime(record.get("updatedAt")), Comparator.nullsLast(Comparator.reverseOrder()));
        }
        records.sort(comparator);
    }

    private int parseInt(Object rawValue) {
        if (rawValue == null) {
            return 0;
        }
        try {
            return Integer.parseInt(String.valueOf(rawValue).trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private List<Map<String, Object>> paginate(List<Map<String, Object>> records, int page, int size) {
        if (records.isEmpty()) {
            return records;
        }
        int safePage = Math.max(1, page);
        int safeSize = Math.max(1, size);
        int fromIndex = Math.min(records.size(), (safePage - 1) * safeSize);
        int toIndex = Math.min(records.size(), fromIndex + safeSize);
        return new ArrayList<>(records.subList(fromIndex, toIndex));
    }

    private LocalDateTime parseDateTime(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(String.valueOf(value), FORMATTER);
        } catch (Exception ex) {
            return null;
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT).replace(" ", "").replace("_", "");
    }

    private int statusPriority(String status) {
        return switch (normalize(status)) {
            case "accepted" -> 0;
            case "submitted", "underreview", "pending", "revocationrequested" -> 1;
            case "rejected" -> 2;
            default -> 3;
        };
    }

    private String statusLabel(String status) {
        return switch (normalize(status)) {
            case "accepted" -> "Accepted";
            case "rejected" -> "Rejected";
            case "underreview" -> "Under Review";
            case "withdrawn" -> "Withdrawn";
            case "revocationrequested" -> "Revocation Requested";
            default -> "Pending Review";
        };
    }

    private String defaultHistoryDescription(String status, Map<String, Object> record) {
        return switch (normalize(status)) {
            case "accepted" -> "Application accepted by the module organiser.";
            case "rejected" -> "Application reviewed and declined.";
            case "underreview" -> "Application is currently being reviewed by the module organiser.";
            default -> "Application submitted with supporting statement and resume.";
        };
    }

    private String buildDecisionDescription(ApplicationStatus status, String comment) {
        String suffix = hasValue(comment) ? " Feedback: " + comment.trim() : "";
        return switch (status) {
            case ACCEPTED -> "Application accepted by the module organiser." + suffix;
            case REJECTED -> "Application rejected by the module organiser." + suffix;
            default -> "Application status updated to " + status.name() + "." + suffix;
        };
    }

    private List<Map<String, Object>> buildHistoryLogs(Map<String, Object> initialRecord) {
        List<Map<String, Object>> logs = new ArrayList<>();
        logs.add(new LinkedHashMap<>(initialRecord));
        return logs;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> appendHistoryLog(Object existingLogs, Map<String, Object> newLog) {
        List<Map<String, Object>> logs = new ArrayList<>();
        if (existingLogs instanceof List<?> existingList) {
            for (Object item : existingList) {
                if (item instanceof Map<?, ?> mapItem) {
                    logs.add(new LinkedHashMap<>((Map<String, Object>) mapItem));
                }
            }
        }
        logs.add(new LinkedHashMap<>(newLog));
        return logs;
    }

    private Map<String, Object> requireTa(String taUserId) {
        Map<String, Object> ta = taDataRepository.findByTaId(taUserId);
        if (ta == null) {
            throw new IllegalStateException("TA profile not found: " + taUserId);
        }
        return ta;
    }

    private Map<String, Object> requireJob(String jobId) {
        Map<String, Object> job = postingDataRepository.findByPostingId(jobId);
        if (job == null) {
            throw new IllegalStateException("Job posting not found: " + jobId);
        }
        return job;
    }

    private Map<String, Object> findExistingApplication(String taUserId, String jobId) {
        for (Map<String, Object> record : applicationDataRepository.findByTaId(taUserId)) {
            if (jobId.equals(String.valueOf(record.get("postingId")))) {
                return record;
            }
        }
        return null;
    }

    private boolean hasValue(Object value) {
        return value != null && !String.valueOf(value).trim().isEmpty();
    }

    private boolean isProfileCompleted(Map<String, Object> ta) {
        if (ta == null) {
            return false;
        }
        return hasValue(ta.get("fullName"))
            && hasValue(ta.get("studentId"))
            && hasValue(ta.get("majorProgram"))
            && hasValue(ta.get("email"));
    }

    private boolean isBeforeDeadline(Object deadlineValue) {
        if (deadlineValue == null) {
            return true;
        }
        String text = String.valueOf(deadlineValue).trim();
        if (text.isEmpty()) {
            return true;
        }
        try {
            LocalDate deadline = LocalDate.parse(text);
            return !LocalDate.now().isAfter(deadline);
        } catch (DateTimeParseException ex) {
            return true;
        }
    }

    private List<String> findScheduleConflicts(String taUserId, String jobId) {
        List<Map<String, Object>> targetBlocks = taTimetableDataRepository.findPostingSchedule(jobId);
        if (targetBlocks.isEmpty()) {
            return new ArrayList<>();
        }
        Map<String, Object> timetable = taTimetableDataRepository.findTimetableByTaIdAndWeek(taUserId, LocalDate.now());
        List<Map<String, Object>> existingBlocks = extractExistingBlocks(timetable);
        List<String> conflicts = new ArrayList<>();
        for (Map<String, Object> targetBlock : targetBlocks) {
            for (Map<String, Object> existingBlock : existingBlocks) {
                if (blocksOverlap(targetBlock, existingBlock)) {
                    conflicts.add(buildConflictMessage(targetBlock, existingBlock));
                }
            }
        }
        return conflicts;
    }

    private List<Map<String, Object>> extractExistingBlocks(Map<String, Object> timetable) {
        List<Map<String, Object>> blocks = new ArrayList<>();
        if (timetable == null || timetable.isEmpty()) {
            return blocks;
        }
        Object courseAssignmentObj = timetable.get("courseAssignment");
        if (courseAssignmentObj instanceof Map<?, ?> assignmentMap) {
            @SuppressWarnings("unchecked")
            Map<String, Object> assignment = new LinkedHashMap<>((Map<String, Object>) assignmentMap);
            if (hasValue(assignment.get("dayOfWeek")) && hasValue(assignment.get("startTime")) && hasValue(assignment.get("endTime"))) {
                assignment.putIfAbsent("label", assignment.getOrDefault("courseName", "Course TA assignment"));
                blocks.add(assignment);
            }
        }
        Object eventsObj = timetable.get("activityEvents");
        if (eventsObj instanceof List<?> events) {
            for (Object eventObj : events) {
                if (eventObj instanceof Map<?, ?> eventMap) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> event = new LinkedHashMap<>((Map<String, Object>) eventMap);
                    String dayOfWeek = deriveDayOfWeek(event.get("date"));
                    if (hasValue(dayOfWeek) && hasValue(event.get("startTime")) && hasValue(event.get("endTime"))) {
                        event.put("dayOfWeek", dayOfWeek);
                        event.putIfAbsent("label", event.getOrDefault("title", "Scheduled activity"));
                        blocks.add(event);
                    }
                }
            }
        }
        return blocks;
    }

    private boolean blocksOverlap(Map<String, Object> left, Map<String, Object> right) {
        String leftDay = String.valueOf(left.getOrDefault("dayOfWeek", "")).trim().toUpperCase(Locale.ROOT);
        String rightDay = String.valueOf(right.getOrDefault("dayOfWeek", "")).trim().toUpperCase(Locale.ROOT);
        if (leftDay.isEmpty() || rightDay.isEmpty() || !leftDay.equals(rightDay)) {
            return false;
        }
        int leftStart = parseMinutes(left.get("startTime"));
        int leftEnd = parseMinutes(left.get("endTime"));
        int rightStart = parseMinutes(right.get("startTime"));
        int rightEnd = parseMinutes(right.get("endTime"));
        return leftStart < rightEnd && rightStart < leftEnd;
    }

    private int parseMinutes(Object timeValue) {
        if (timeValue == null) {
            return -1;
        }
        String text = String.valueOf(timeValue).trim();
        String[] parts = text.split(":");
        if (parts.length != 2) {
            return -1;
        }
        try {
            return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private String deriveDayOfWeek(Object dateValue) {
        if (dateValue == null) {
            return "";
        }
        try {
            return LocalDate.parse(String.valueOf(dateValue)).getDayOfWeek().name().substring(0, 3);
        } catch (Exception ex) {
            return "";
        }
    }

    private String buildConflictMessage(Map<String, Object> targetBlock, Map<String, Object> existingBlock) {
        return "Schedule conflict: "
            + String.valueOf(targetBlock.getOrDefault("label", "Target duty"))
            + " overlaps with "
            + String.valueOf(existingBlock.getOrDefault("label", "existing timetable block"))
            + " on "
            + String.valueOf(targetBlock.getOrDefault("dayOfWeek", ""))
            + " "
            + String.valueOf(targetBlock.getOrDefault("startTime", ""))
            + "-"
            + String.valueOf(targetBlock.getOrDefault("endTime", ""));
    }

    private void updatePostingApplicationCount(String postingId, int delta) {
        if (!hasValue(postingId) || delta == 0) {
            return;
        }
        Map<String, Object> posting = postingDataRepository.findByPostingId(postingId);
        if (posting == null) {
            return;
        }
        int currentCount = 0;
        Object countObject = posting.get("applicationCount");
        if (countObject instanceof Number number) {
            currentCount = number.intValue();
        } else if (countObject != null) {
            try {
                currentCount = Integer.parseInt(String.valueOf(countObject));
            } catch (NumberFormatException ignored) {
                currentCount = 0;
            }
        }
        posting.put("applicationCount", Math.max(0, currentCount + delta));
        postingDataRepository.save(posting);
    }
}
