package com.bupt.ta.service.impl;

import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.model.Role;
import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.repository.file.PostingDataRepository;
import com.bupt.ta.service.JobService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.time.LocalDate;

public class JobServiceImpl implements JobService {
    private final PostingDataRepository postingDataRepository;
    private final UserRepository userRepository;

    public JobServiceImpl(PostingDataRepository postingDataRepository, UserRepository userRepository) {
        this.postingDataRepository = postingDataRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PageResult<Map<String, Object>> searchOpenJobs(JobQuery query) {
        List<Map<String, Object>> filtered = new ArrayList<>();
        for (Map<String, Object> posting : postingDataRepository.findAll()) {
            if (!"OPEN".equalsIgnoreCase(String.valueOf(posting.get("status")))) {
                continue;
            }
            if (!matchesKeyword(posting, query == null ? null : query.getKeyword())) {
                continue;
            }
            filtered.add(new LinkedHashMap<>(posting));
        }
        return toPageResult(query, filtered);
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
        posting.put("postingId", postingDataRepository.nextPostingId());
        posting.put("courseCode", courseCode);
        posting.put("courseName", courseName);
        posting.put("moId", moUserId);
        posting.put("moName", firstNonBlank(mo, "fullName", "displayName", "moId"));
        posting.put("vacancies", vacancies);
        posting.put("applicationCount", 0);
        posting.put("deadline", deadline);
        posting.put("description", description);
        posting.put("requiredSkills", requiredSkills);
        posting.put("estimatedWorkloadHours", estimatedWorkloadHours);
        posting.put("status", normalizePostingStatus(firstNonBlank(params, "status")));
        postingDataRepository.save(posting);
        return posting;
    }

    @Override
    public PageResult<Map<String, Object>> listJobsByMO(String moUserId, JobQuery query) {
        List<Map<String, Object>> records = new ArrayList<>();
        for (Map<String, Object> posting : postingDataRepository.findAll()) {
            if (!moUserId.equals(String.valueOf(posting.get("moId")))) {
                continue;
            }
            if (!matchesStatus(posting, query == null ? null : query.getStatus())) {
                continue;
            }
            if (!matchesKeyword(posting, query == null ? null : query.getKeyword())) {
                continue;
            }
            records.add(new LinkedHashMap<>(posting));
        }
        sortJobs(records, query == null ? null : query.getSortBy());
        return toPageResult(query, records);
    }

    @Override
    public PageResult<Map<String, Object>> searchAllJobsForAdmin(JobQuery query) {
        List<Map<String, Object>> records = new ArrayList<>();
        for (Map<String, Object> posting : postingDataRepository.findAll()) {
            if (!matchesStatus(posting, query == null ? null : query.getStatus())) {
                continue;
            }
            if (!matchesKeyword(posting, query == null ? null : query.getKeyword())) {
                continue;
            }
            records.add(new LinkedHashMap<>(posting));
        }
        sortJobs(records, query == null ? null : query.getSortBy());
        return toPageResult(query, records);
    }

    private Map<String, Object> requireMO(String moUserId) {
        Map<String, Object> mo = userRepository.findById(Role.MO, moUserId);
        if (mo == null) {
            throw new IllegalStateException("MO profile not found: " + moUserId);
        }
        return mo;
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

    private void sortJobs(List<Map<String, Object>> jobs, String sortBy) {
        if (jobs.isEmpty()) {
            return;
        }
        if (sortBy == null || sortBy.isBlank()) {
            jobs.sort(Comparator.comparing((Map<String, Object> job) -> parseDate(job.get("deadline"))).reversed());
            return;
        }

        Comparator<Map<String, Object>> comparator;
        switch (sortBy.trim()) {
            case "deadlineAsc":
                comparator = Comparator.comparing((Map<String, Object> job) -> parseDate(job.get("deadline")));
                break;
            case "deadlineDesc":
                comparator = Comparator.comparing((Map<String, Object> job) -> parseDate(job.get("deadline"))).reversed();
                break;
            case "applicationsAsc":
                comparator = Comparator.comparingInt(job -> parseNonNegativeInt(job.get("applicationCount")));
                break;
            case "applicationsDesc":
                comparator = Comparator.comparingInt((Map<String, Object> job) -> parseNonNegativeInt(job.get("applicationCount"))).reversed();
                break;
            default:
                comparator = Comparator.comparing((Map<String, Object> job) -> String.valueOf(job.get("postingId")), Comparator.nullsLast(String::compareTo)).reversed();
                break;
        }
        jobs.sort(comparator);
    }

    private PageResult<Map<String, Object>> toPageResult(JobQuery query, List<Map<String, Object>> allRecords) {
        JobQuery safeQuery = query == null ? new JobQuery() : query;
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

    private String normalizeDate(String rawDate) {
        String candidate = rawDate.trim().replace('/', '-').replace('.', '-');
        return LocalDate.parse(candidate).toString();
    }

    private LocalDate parseDate(Object rawDate) {
        if (rawDate == null) {
            return LocalDate.MIN;
        }
        String candidate = String.valueOf(rawDate).trim();
        if (candidate.isEmpty()) {
            return LocalDate.MIN;
        }
        try {
            return LocalDate.parse(candidate.replace('/', '-').replace('.', '-'));
        } catch (Exception ignored) {
            return LocalDate.MIN;
        }
    }

    private int requirePositiveInt(String rawValue, String message) {
        int parsed = parseNonNegativeInt(rawValue);
        if (parsed <= 0) {
            throw new IllegalStateException(message);
        }
        return parsed;
    }

    private int parseNonNegativeInt(Object rawValue) {
        if (rawValue == null) {
            return 0;
        }
        try {
            return Integer.parseInt(String.valueOf(rawValue).trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(message);
        }
        return value.trim();
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
}
