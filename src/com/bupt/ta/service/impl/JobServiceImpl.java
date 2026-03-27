package com.bupt.ta.service.impl;

import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.repository.file.PostingDataRepository;
import com.bupt.ta.service.JobService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JobServiceImpl implements JobService {
    private final PostingDataRepository postingDataRepository;

    public JobServiceImpl(PostingDataRepository postingDataRepository) {
        this.postingDataRepository = postingDataRepository;
    }

    @Override
    public PageResult<Map<String, Object>> searchOpenJobs(JobQuery query) {
        List<Map<String, Object>> filtered = new ArrayList<>();
        for (Map<String, Object> posting : postingDataRepository.findAll()) {
            if (!"OPEN".equalsIgnoreCase(String.valueOf(posting.get("status")))) {
                continue;
            }
            if (query.getKeyword() != null && !query.getKeyword().isBlank()) {
                String keyword = query.getKeyword().toLowerCase(Locale.ROOT);
                String haystack = (String.valueOf(posting.get("courseName")) + " " + String.valueOf(posting.get("description"))).toLowerCase(Locale.ROOT);
                if (!haystack.contains(keyword)) {
                    continue;
                }
            }
            if (query.getMajor() != null && !query.getMajor().isBlank()) {
                String major = query.getMajor().toLowerCase(Locale.ROOT).trim();
                Object skillsObj = posting.get("requiredSkills");
                String skillsText = "";
                if (skillsObj instanceof List<?> list) {
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
                    skillsText = builder.toString();
                } else if (skillsObj != null) {
                    skillsText = String.valueOf(skillsObj);
                }
                String haystack = (String.valueOf(posting.get("courseName")) + " " + String.valueOf(posting.get("description")) + " " + skillsText)
                    .toLowerCase(Locale.ROOT);
                if (!haystack.contains(major)) {
                    continue;
                }
            }
            filtered.add(new LinkedHashMap<>(posting));
        }
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setRecords(filtered);
        result.setPage(query.getPage());
        result.setSize(query.getSize());
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
}
