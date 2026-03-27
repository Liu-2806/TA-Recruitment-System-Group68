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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
        boolean resumeUploaded = hasValue(ta.get("resumeFileName"));
        boolean alreadyApplied = findExistingApplication(taUserId, jobId) != null;
        boolean jobOpen = "OPEN".equalsIgnoreCase(String.valueOf(job.get("status")));
        boolean beforeDeadline = isBeforeDeadline(job.get("deadline"));
        boolean profileCompleted = isProfileCompleted(ta);
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
        Map<String, Object> result = new LinkedHashMap<>();
        boolean eligible = profileCompleted && resumeUploaded && !alreadyApplied && beforeDeadline && jobOpen;

        // 文档要求字段（L565-L572）
        result.put("eligible", eligible);
        result.put("profileCompleted", profileCompleted);
        result.put("resumeUploaded", resumeUploaded);
        result.put("alreadyApplied", alreadyApplied);
        result.put("beforeDeadline", beforeDeadline);
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
        application.put("status", ApplicationStatus.SUBMITTED.name());
        application.put("statement", statement == null ? "" : statement.trim());
        application.put("feedback", "");
        recommendationService.buildJobMatchForTA(taUserId, jobId).forEach(application::put);
        application.put("skillMatchScore", application.get("score"));
        application.put("skillMatchExplanation", application.get("explanation"));
        application.put("matchMethod", application.get("method"));
        application.remove("score");
        application.remove("explanation");
        application.remove("method");
        applicationDataRepository.save(application);
        return application;
    }

    @Override
    public PageResult<Map<String, Object>> listApplicationsByTA(String taUserId, ApplicationQuery query) {
        List<Map<String, Object>> records = new ArrayList<>(applicationDataRepository.findByTaId(taUserId));
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setRecords(records);
        result.setPage(query.getPage());
        result.setSize(query.getSize());
        result.setTotal(records.size());
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> listApplicationsByJob(String jobId, ApplicationQuery query) {
        List<Map<String, Object>> records = new ArrayList<>();
        for (Map<String, Object> record : applicationDataRepository.findByPostingId(jobId)) {
            if (recommendationService instanceof RecommendationServiceImpl impl) {
                records.add(impl.enrichApplicationWithMatch(new LinkedHashMap<>(record)));
            } else {
                records.add(new LinkedHashMap<>(record));
            }
        }
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setRecords(records);
        result.setPage(query.getPage());
        result.setSize(query.getSize());
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
        return detail;
    }

    @Override
    public void updateStatusByMO(String applicationId, String moUserId, ApplicationStatus newStatus, String comment) {
        Map<String, Object> record = getApplicationDetailForMO(applicationId, moUserId);
        record.put("status", newStatus.name());
        record.put("feedback", comment == null ? "" : comment.trim());
        applicationDataRepository.save(record);
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
}
