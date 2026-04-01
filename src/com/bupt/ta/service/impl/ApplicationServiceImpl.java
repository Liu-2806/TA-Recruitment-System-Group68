package com.bupt.ta.service.impl;

import com.bupt.ta.dto.ApplicationQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.match.RecommendationServiceImpl;
import com.bupt.ta.model.ApplicationStatus;
import com.bupt.ta.repository.file.ApplicationDataRepository;
import com.bupt.ta.repository.file.PostingDataRepository;
import com.bupt.ta.repository.file.TADataRepository;
import com.bupt.ta.service.ApplicationService;
import com.bupt.ta.service.RecommendationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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

    public ApplicationServiceImpl(
        TADataRepository taDataRepository,
        PostingDataRepository postingDataRepository,
        ApplicationDataRepository applicationDataRepository,
        RecommendationService recommendationService
    ) {
        this.taDataRepository = taDataRepository;
        this.postingDataRepository = postingDataRepository;
        this.applicationDataRepository = applicationDataRepository;
        this.recommendationService = recommendationService;
    }

    @Override
    public Map<String, Object> checkEligibility(String taUserId, String jobId) {
        Map<String, Object> ta = requireTa(taUserId);
        Map<String, Object> job = requireJob(jobId);
        boolean hasResume = ta.get("resumeFileName") != null && !String.valueOf(ta.get("resumeFileName")).isBlank();
        boolean duplicate = findExistingApplication(taUserId, jobId) != null;
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("eligible", hasResume && !duplicate && "OPEN".equalsIgnoreCase(String.valueOf(job.get("status"))));
        result.put("hasResume", hasResume);
        result.put("duplicateApplication", duplicate);
        result.put("jobOpen", "OPEN".equalsIgnoreCase(String.valueOf(job.get("status"))));
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
        application.put("status", ApplicationStatus.SUBMITTED.name());
        application.put("statement", statement == null ? "" : statement.trim());
        application.put("feedback", "");
        recommendationService.buildJobMatchForTA(taUserId, jobId).forEach(application::put);
        application.put("skillMatchScore", application.get("score"));
        application.put("skillMatchExplanation", application.get("explanation"));
        application.remove("score");
        application.remove("explanation");
        Map<String, Object> persistedRecord = toPersistedApplicationRecord(application);
        applicationDataRepository.save(persistedRecord);
        return persistedRecord;
    }

    @Override
    public PageResult<Map<String, Object>> listApplicationsByTA(String taUserId, ApplicationQuery query) {
        List<Map<String, Object>> records = new ArrayList<>();
        for (Map<String, Object> record : applicationDataRepository.findByTaId(taUserId)) {
            if (!matchesStatus(record, query == null ? null : query.getStatus())) {
                continue;
            }
            records.add(new LinkedHashMap<>(record));
        }
        sortApplications(records, query == null ? null : query.getSortBy());
        return toPageResult(query, records);
    }

    @Override
    public PageResult<Map<String, Object>> listApplicationsByJob(String jobId, ApplicationQuery query) {
        List<Map<String, Object>> records = new ArrayList<>();
        for (Map<String, Object> record : applicationDataRepository.findByPostingId(jobId)) {
            if (!matchesStatus(record, query == null ? null : query.getStatus())) {
                continue;
            }
            if (recommendationService instanceof RecommendationServiceImpl impl) {
                records.add(impl.enrichApplicationWithMatch(new LinkedHashMap<>(record)));
            } else {
                records.add(new LinkedHashMap<>(record));
            }
        }
        sortApplications(records, query == null ? null : query.getSortBy());
        return toPageResult(query, records);
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
        return detail;
    }

    @Override
    public void updateStatusByMO(String applicationId, String moUserId, ApplicationStatus newStatus, String comment) {
        if (newStatus == ApplicationStatus.SUBMITTED) {
            throw new IllegalStateException("MO review cannot set application status back to SUBMITTED.");
        }
        Map<String, Object> record = getApplicationDetailForMO(applicationId, moUserId);
        record.put("status", newStatus.name());
        record.put("feedback", comment == null ? "" : comment.trim());
        applicationDataRepository.save(toPersistedApplicationRecord(record));
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

    private boolean matchesStatus(Map<String, Object> record, String status) {
        if (status == null || status.isBlank()) {
            return true;
        }
        return status.trim().equalsIgnoreCase(String.valueOf(record.get("status")));
    }

    private void sortApplications(List<Map<String, Object>> records, String sortBy) {
        Comparator<Map<String, Object>> comparator;
        if (sortBy == null || sortBy.isBlank()) {
            comparator = Comparator.comparing((Map<String, Object> record) -> String.valueOf(record.get("appliedAt")), Comparator.nullsLast(String::compareTo)).reversed();
        } else {
            switch (sortBy.trim().toLowerCase(Locale.ROOT)) {
                case "scoreasc":
                    comparator = Comparator.comparingInt(record -> parseInt(record.get("skillMatchScore")));
                    break;
                case "scoredesc":
                    comparator = Comparator.comparingInt((Map<String, Object> record) -> parseInt(record.get("skillMatchScore"))).reversed();
                    break;
                case "timeasc":
                    comparator = Comparator.comparing((Map<String, Object> record) -> String.valueOf(record.get("appliedAt")), Comparator.nullsLast(String::compareTo));
                    break;
                case "timedesc":
                    comparator = Comparator.comparing((Map<String, Object> record) -> String.valueOf(record.get("appliedAt")), Comparator.nullsLast(String::compareTo)).reversed();
                    break;
                default:
                    comparator = Comparator.comparing((Map<String, Object> record) -> String.valueOf(record.get("appliedAt")), Comparator.nullsLast(String::compareTo)).reversed();
                    break;
            }
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

    private PageResult<Map<String, Object>> toPageResult(ApplicationQuery query, List<Map<String, Object>> allRecords) {
        ApplicationQuery safeQuery = query == null ? new ApplicationQuery() : query;
        int page = safeQuery.getPage() <= 0 ? 1 : safeQuery.getPage();
        int size = safeQuery.getSize() <= 0 ? allRecords.size() : safeQuery.getSize();
        int fromIndex = Math.min((page - 1) * size, allRecords.size());
        int toIndex = Math.min(fromIndex + size, allRecords.size());
        List<Map<String, Object>> paged = fromIndex >= toIndex
            ? Collections.emptyList()
            : new ArrayList<>(allRecords.subList(fromIndex, toIndex));

        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setRecords(paged);
        result.setPage(page);
        result.setSize(size);
        result.setTotal(allRecords.size());
        return result;
    }

    private Map<String, Object> toPersistedApplicationRecord(Map<String, Object> source) {
        Map<String, Object> persisted = new LinkedHashMap<>();
        copyIfPresent(source, persisted, "applicationId");
        copyIfPresent(source, persisted, "postingId");
        copyIfPresent(source, persisted, "postingTitle");
        copyIfPresent(source, persisted, "taId");
        copyIfPresent(source, persisted, "taName");
        copyIfPresent(source, persisted, "appliedAt");
        copyIfPresent(source, persisted, "status");
        copyIfPresent(source, persisted, "statement");
        copyIfPresent(source, persisted, "feedback");
        copyIfPresent(source, persisted, "skillMatchScore");
        copyIfPresent(source, persisted, "skillMatchExplanation");
        return persisted;
    }

    private void copyIfPresent(Map<String, Object> source, Map<String, Object> target, String key) {
        if (source.containsKey(key)) {
            target.put(key, source.get(key));
        }
    }
}
