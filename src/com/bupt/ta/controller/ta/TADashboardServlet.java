package com.bupt.ta.controller.ta;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.dto.ApplicationQuery;
import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.model.User;
import com.bupt.ta.repository.file.TATimetableDataRepository;
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
    private final TATimetableDataRepository taTimetableDataRepository = ServiceRegistry.taTimetableDataRepository();

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

        Map<String, Object> timetable = buildTimetable(user.getId(), allApplications);
        request.setAttribute("timetable", timetable);

        List<Map<String, Object>> notifications = buildNotifications(allApplications, taProfile, timetable);
        request.setAttribute("notifications", notifications);
        request.setAttribute("notificationCount", notifications.size());

        request.getRequestDispatcher("/WEB-INF/views/ta/dashboard.jsp").forward(request, response);
    }

    private Map<String, Object> buildTimetable(String taId, List<Map<String, Object>> applications) {
        LocalDate weekStart = LocalDate.now().with(java.time.temporal.TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        Map<String, Object> timetable = new LinkedHashMap<>(taTimetableDataRepository.findTimetableByTaIdAndWeek(taId, weekStart));
        timetable = alignTimetableWithApplications(timetable, applications);
        Map<String, Object> courseAssignment = asMap(timetable.get("courseAssignment"));
        if (!courseAssignment.isEmpty() && !hasValue(courseAssignment.get("relatedApplicationId"))) {
            Map<String, Object> relatedApplication = findApplicationByPostingId(applications, valueOf(courseAssignment.get("postingId")));
            if (relatedApplication != null) {
                courseAssignment.put("relatedApplicationId", relatedApplication.get("applicationId"));
            }
            timetable.put("courseAssignment", courseAssignment);
        }
        return timetable;
    }

    private Map<String, Object> alignTimetableWithApplications(Map<String, Object> timetable, List<Map<String, Object>> applications) {
        if (timetable == null || applications == null || applications.isEmpty()) {
            return timetable;
        }

        List<Map<String, Object>> orderedApplications = sortRecentApplications(applications);
        Map<String, Object> primaryApplication = orderedApplications.get(0);
        String primaryPostingId = valueOf(primaryApplication.get("postingId"));
        String primaryApplicationId = valueOf(primaryApplication.get("applicationId"));
        String primaryPostingTitle = valueOf(primaryApplication.get("postingTitle"));

        Map<String, Object> courseAssignment = asMap(timetable.get("courseAssignment"));
        String assignmentPostingId = valueOf(courseAssignment.get("postingId"));
        if (!isPostingLinkedToApplications(assignmentPostingId, orderedApplications)) {
            courseAssignment.put("postingId", primaryPostingId);
            courseAssignment.put("relatedApplicationId", primaryApplicationId);
            if (!hasValue(courseAssignment.get("courseName")) && hasValue(primaryPostingTitle)) {
                courseAssignment.put("courseName", primaryPostingTitle);
            }
            timetable.put("courseAssignment", courseAssignment);
        }

        Object rawEvents = timetable.get("activityEvents");
        if (!(rawEvents instanceof List<?> rawEventList) || rawEventList.isEmpty()) {
            return timetable;
        }

        List<Map<String, Object>> fixedEvents = new ArrayList<>();
        int appIndex = 0;
        for (Object eventObj : rawEventList) {
            Map<String, Object> event = asMap(eventObj);
            String eventPostingId = valueOf(event.get("postingId"));
            if (!isPostingLinkedToApplications(eventPostingId, orderedApplications)) {
                Map<String, Object> mappedApp = orderedApplications.get(appIndex % orderedApplications.size());
                event.put("postingId", valueOf(mappedApp.get("postingId")));
                event.put("applicationId", valueOf(mappedApp.get("applicationId")));
                appIndex++;
            }
            fixedEvents.add(event);
        }
        timetable.put("activityEvents", fixedEvents);
        return timetable;
    }

    private boolean isPostingLinkedToApplications(String postingId, List<Map<String, Object>> applications) {
        if (postingId == null || postingId.isBlank() || applications == null || applications.isEmpty()) {
            return false;
        }
        for (Map<String, Object> application : applications) {
            if (postingId.equals(valueOf(application.get("postingId")))) {
                return true;
            }
        }
        return false;
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
        if ("WITHDRAWN".equals(normalized)) {
            return "withdrawn";
        }
        if ("REVOCATION_REQUESTED".equals(normalized)) {
            return "pending";
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

    private List<Map<String, Object>> buildNotifications(
        List<Map<String, Object>> applications,
        Map<String, Object> taProfile,
        Map<String, Object> timetable
    ) {
        List<Map<String, Object>> notices = new ArrayList<>();
        if (!hasValue(taProfile.get("resumeFileName"))) {
            notices.add(notification("Profile", "Upload your resume to unlock AI matching and TA applications.", "/ta/profile#resume-upload"));
        }

        for (Map<String, Object> application : topN(sortRecentApplications(applications), 2)) {
            String status = valueOf(application.get("statusLabel"));
            String postingTitle = valueOf(application.get("postingTitle"));
            String applicationId = valueOf(application.get("applicationId"));
            notices.add(notification(
                "Application",
                (status == null ? "Application updated" : status) + " for " + (postingTitle == null ? "your latest role" : postingTitle) + ".",
                "/ta/applications/my#application-" + (applicationId == null ? "" : applicationId)
            ));
        }

        Map<String, Object> assignment = asMap(timetable.get("courseAssignment"));
        if (!assignment.isEmpty() && hasValue(assignment.get("relatedApplicationId"))) {
            notices.add(notification(
                "Timetable",
                "Your weekly timetable includes " + valueOf(assignment.getOrDefault("courseName", "a teaching assignment")) + ".",
                "/ta/applications/my#application-" + valueOf(assignment.get("relatedApplicationId"))
            ));
        }
        return topN(notices, 4);
    }

    private Map<String, Object> notification(String type, String message, String path) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("type", type);
        item.put("message", message);
        item.put("path", path);
        return item;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> asMap(Object value) {
        if (value instanceof Map<?, ?> map) {
            return new LinkedHashMap<>((Map<String, Object>) map);
        }
        return new LinkedHashMap<>();
    }

    private boolean hasValue(Object value) {
        return value != null && !String.valueOf(value).isBlank();
    }

    private Map<String, Object> findApplicationByPostingId(List<Map<String, Object>> applications, String postingId) {
        if (applications == null || postingId == null || postingId.isBlank()) {
            return null;
        }
        for (Map<String, Object> application : applications) {
            if (postingId.equals(valueOf(application.get("postingId")))) {
                return application;
            }
        }
        return null;
    }
}
