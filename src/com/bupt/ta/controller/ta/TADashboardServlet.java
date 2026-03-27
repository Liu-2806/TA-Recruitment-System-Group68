package com.bupt.ta.controller.ta;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.dto.ApplicationQuery;
import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ApplicationService;
import com.bupt.ta.service.JobService;
import com.bupt.ta.service.ProfileService;
import com.bupt.ta.service.RecommendationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * TA 仪表盘 Servlet。
 */
@WebServlet("/ta/dashboard")
public class TADashboardServlet extends BaseServlet {
    private final ProfileService profileService = ServiceRegistry.profileService();
    private final ApplicationService applicationService = ServiceRegistry.applicationService();
    private final JobService jobService = ServiceRegistry.jobService();
    private final RecommendationService recommendationService = ServiceRegistry.recommendationService();

    private static final DateTimeFormatter APPLIED_AT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 展示 TA 仪表盘页面。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        Map<String, Object> taProfile = profileService.getTAProfile(user.getId());
        request.setAttribute("taProfile", taProfile);

        Map<String, Object> profileSummary = new LinkedHashMap<>();
        profileSummary.put("taId", taProfile.get("taId"));
        profileSummary.put("fullName", firstNonBlank(taProfile, "fullName", "name", "displayName"));
        profileSummary.put("studentId", taProfile.get("studentId"));
        profileSummary.put("majorProgram", taProfile.get("majorProgram"));
        request.setAttribute("profileSummary", profileSummary);

        PageResult<Map<String, Object>> applicationsPage = applicationService.listApplicationsByTA(user.getId(), new ApplicationQuery());
        List<Map<String, Object>> allApplications = applicationsPage == null || applicationsPage.getRecords() == null
            ? new ArrayList<>()
            : new ArrayList<>(applicationsPage.getRecords());

        Map<String, Object> applicationStats = new LinkedHashMap<>();
        applicationStats.put("pendingCount", countByGroup(allApplications, "pending"));
        applicationStats.put("acceptedCount", countByGroup(allApplications, "accepted"));
        applicationStats.put("rejectedCount", countByGroup(allApplications, "rejected"));
        request.setAttribute("applicationStats", applicationStats);

        Map<String, Object> resumeSummary = new LinkedHashMap<>();
        String resumeFileName = valueOf(taProfile.get("resumeFileName"));
        String resumeUploadedAt = valueOf(taProfile.get("resumeUploadedAt"));
        boolean hasResume = resumeFileName != null && !resumeFileName.isBlank();
        resumeSummary.put("hasResume", hasResume);
        resumeSummary.put("resumeFileName", resumeFileName);
        resumeSummary.put("resumeUploadedAt", resumeUploadedAt);
        resumeSummary.put("statusLabel", hasResume ? "Uploaded" : "Not uploaded");
        resumeSummary.put("downloadUrl", hasResume ? (request.getContextPath() + "/ta/resume/download") : "");
        request.setAttribute("resumeSummary", resumeSummary);

        PageResult<Map<String, Object>> jobsPage = jobService.searchOpenJobs(new JobQuery());
        List<Map<String, Object>> jobs = jobsPage == null || jobsPage.getRecords() == null
            ? new ArrayList<>()
            : new ArrayList<>(jobsPage.getRecords());
        request.setAttribute("recommendedJobs", topN(rankRecommendedJobs(user.getId(), jobs), 3));

        request.setAttribute("recentApplications", topN(sortRecentApplications(allApplications), 3));

        request.setAttribute("timetable", buildTimetable(user.getId(), jobs, allApplications));

        request.getRequestDispatcher("/WEB-INF/views/ta/dashboard.jsp").forward(request, response);
    }

    private Map<String, Object> buildTimetable(String taId, List<Map<String, Object>> jobs, List<Map<String, Object>> applications) {
        Map<String, Object> timetable = new LinkedHashMap<>();

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = weekStart.plusDays(6);
        DateTimeFormatter labelFormatter = DateTimeFormatter.ofPattern("dd MMM", Locale.UK);
        timetable.put("currentWeekLabel", "Week of " + labelFormatter.format(weekStart) + " - " + labelFormatter.format(weekEnd));

        Map<String, Object> baseJob = (jobs == null || jobs.isEmpty()) ? null : jobs.get(0);
        Map<String, Object> baseApp = (applications == null || applications.isEmpty()) ? null : applications.get(0);

        String postingId = valueOf(baseJob == null ? null : baseJob.get("postingId"));
        String courseCode = valueOf(baseJob == null ? null : baseJob.get("courseCode"));
        String courseName = valueOf(baseJob == null ? null : baseJob.get("courseName"));
        if (courseName == null || courseName.isBlank()) {
            courseName = "Software Engineering TA";
        }

        String applicationId = valueOf(baseApp == null ? null : baseApp.get("applicationId"));

        Map<String, Object> courseAssignment = new LinkedHashMap<>();
        courseAssignment.put("postingId", postingId == null ? "" : postingId);
        courseAssignment.put("courseCode", courseCode == null ? "" : courseCode);
        courseAssignment.put("courseName", courseName);
        courseAssignment.put("label", "Course TA");
        courseAssignment.put("dayOfWeek", "TUE");
        courseAssignment.put("startTime", "14:00");
        courseAssignment.put("endTime", "16:00");
        courseAssignment.put("location", "QB-302");
        courseAssignment.put("description", "Weekly support session");
        courseAssignment.put("relatedApplicationId", applicationId == null ? "" : applicationId);
        timetable.put("courseAssignment", courseAssignment);

        List<Map<String, Object>> activityEvents = new ArrayList<>();
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("eventId", "EVT001");
        event.put("postingId", postingId == null ? "" : postingId);
        event.put("applicationId", applicationId == null ? "" : applicationId);
        event.put("title", (courseCode == null || courseCode.isBlank() ? "SE3001" : courseCode) + " Lab Support");
        event.put("type", "lab");
        event.put("date", weekStart.plusDays(1).toString());
        event.put("startTime", "10:00");
        event.put("endTime", "12:00");
        event.put("location", "QB-302");
        event.put("description", "Guide students through the weekly lab and answer implementation questions.");
        activityEvents.add(event);
        timetable.put("activityEvents", activityEvents);

        timetable.put("taId", taId);
        return timetable;
    }

    private List<Map<String, Object>> rankRecommendedJobs(String taUserId, List<Map<String, Object>> jobs) {
        if (jobs == null || jobs.isEmpty()) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> enriched = new ArrayList<>();
        for (Map<String, Object> job : jobs) {
            if (job == null) {
                continue;
            }
            Map<String, Object> copy = new LinkedHashMap<>(job);
            String postingId = valueOf(job.get("postingId"));
            if (postingId != null && !postingId.isBlank()) {
                try {
                    Map<String, Object> match = recommendationService.buildJobMatchForTA(taUserId, postingId);
                    Object score = match == null ? null : match.get("score");
                    if (score != null) {
                        copy.put("matchScore", score);
                    }
                } catch (Exception ignored) {
                    // If TA has no structured resume yet, keep a simple open-jobs fallback ordering.
                }
            }
            enriched.add(copy);
        }

        enriched.sort(Comparator
            .comparingInt((Map<String, Object> job) -> safeInt(job.get("matchScore")))
            .reversed());
        return enriched;
    }

    private List<Map<String, Object>> sortRecentApplications(List<Map<String, Object>> applications) {
        if (applications == null || applications.isEmpty()) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> sorted = new ArrayList<>(applications);
        sorted.sort(Comparator
            .comparing((Map<String, Object> app) -> parseAppliedAt(valueOf(app.get("appliedAt"))), Comparator.nullsLast(Comparator.naturalOrder()))
            .reversed());
        return sorted;
    }

    private LocalDateTime parseAppliedAt(String appliedAt) {
        if (appliedAt == null || appliedAt.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(appliedAt.trim(), APPLIED_AT_FORMATTER);
        } catch (Exception ignored) {
            return null;
        }
    }

    private int safeInt(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value).trim());
        } catch (Exception ignored) {
            return 0;
        }
    }

    private List<Map<String, Object>> topN(List<Map<String, Object>> records, int n) {
        if (records == null || records.isEmpty() || n <= 0) {
            return new ArrayList<>();
        }
        int end = Math.min(records.size(), n);
        return new ArrayList<>(records.subList(0, end));
    }

    private int countByGroup(List<Map<String, Object>> applications, String group) {
        if (applications == null || applications.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Map<String, Object> application : applications) {
            if (group.equals(statusGroup(application))) {
                count++;
            }
        }
        return count;
    }

    private String statusGroup(Map<String, Object> application) {
        String status = valueOf(application == null ? null : application.get("status"));
        if (status == null) {
            return "pending";
        }
        String normalized = status.trim().toUpperCase();
        if ("ACCEPTED".equals(normalized)) {
            return "accepted";
        }
        if ("REJECTED".equals(normalized)) {
            return "rejected";
        }
        return "pending";
    }

    private String firstNonBlank(Map<String, Object> map, String... keys) {
        if (map == null || keys == null) {
            return null;
        }
        for (String key : keys) {
            String value = valueOf(map.get(key));
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private String valueOf(Object value) {
        return value == null ? null : String.valueOf(value).trim();
    }
}
