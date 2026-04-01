package com.bupt.ta.service.impl;

import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.repository.file.PostingDataRepository;
import com.bupt.ta.service.JobService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class JobServiceImpl implements JobService {
    private final PostingDataRepository postingDataRepository;

    public JobServiceImpl(PostingDataRepository postingDataRepository) {
        this.postingDataRepository = postingDataRepository;
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
                String major = safeQuery.getMajor().toLowerCase(Locale.ROOT).trim();
                String haystack = (String.valueOf(posting.get("courseName")) + " " + String.valueOf(posting.get("description")) + " " + requiredSkillsText)
                    .toLowerCase(Locale.ROOT);
                if (!haystack.contains(major)) {
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
        throw new UnsupportedOperationException("Job creation is outside the owned module scope.");
    }

    @Override
    public PageResult<Map<String, Object>> listJobsByMO(String moUserId, JobQuery query) {
        List<Map<String, Object>> records = new ArrayList<>();
        for (Map<String, Object> posting : postingDataRepository.findAll()) {
            if (moUserId.equals(String.valueOf(posting.get("moId")))) {
                records.add(new LinkedHashMap<>(posting));
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
    public PageResult<Map<String, Object>> searchAllJobsForAdmin(JobQuery query) {
        List<Map<String, Object>> records = new ArrayList<>(postingDataRepository.findAll());
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setRecords(records);
        result.setPage(query.getPage());
        result.setSize(query.getSize());
        result.setTotal(records.size());
        return result;
    }

    private void sortJobs(List<Map<String, Object>> records, String sortBy) {
        String normalized = sortBy == null ? "" : sortBy.trim().toLowerCase(Locale.ROOT).replace(" ", "");
        Comparator<Map<String, Object>> comparator;
        if ("upcomingdeadline".equals(normalized) || "deadline".equals(normalized)) {
            comparator = Comparator.comparing((Map<String, Object> job) -> String.valueOf(job.getOrDefault("deadline", "")));
        } else if ("mostvacancies".equals(normalized) || "vacancies".equals(normalized)) {
            comparator = Comparator.comparingInt((Map<String, Object> job) -> safeInt(job.get("vacancies"))).reversed();
        } else {
            comparator = Comparator.comparing((Map<String, Object> job) -> String.valueOf(job.getOrDefault("postingId", ""))).reversed();
        }
        records.sort(comparator);
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

    private int safeInt(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value));
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
}
