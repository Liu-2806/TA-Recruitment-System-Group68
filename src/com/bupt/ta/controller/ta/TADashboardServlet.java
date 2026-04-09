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

        request.setAttribute("notificationCount", 0);

        request.getRequestDispatcher("/WEB-INF/views/ta/dashboard.jsp").forward(request, response);
    }

    private Map<String, Object> buildTimetable(String taId, List<Map<String, Object>> applications) {
        LocalDate weekStart = LocalDate.now().with(java.time.temporal.TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        List<Map<String, Object>> acceptedApplications = acceptedApplications(applications);
        if (acceptedApplications.isEmpty()) {
            return new LinkedHashMap<>(taTimetableDataRepository.findTimetableByTaIdAndWeek(taId, weekStart));
        }

        acceptedApplications.sort(Comparator
            .comparing((Map<String, Object> app) -> parseAppliedAt(valueOf(app.get("appliedAt"))), Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(app -> valueOf(app.get("postingId")), Comparator.nullsLast(String::compareTo)));

        Map<String, Object> primaryApplication = firstTaAssignment(acceptedApplications);

        Map<String, Object> timetable = new LinkedHashMap<>();
        timetable.put("taId", taId);
        timetable.put("weekStart", weekStart.toString());
        timetable.put("currentWeekLabel", "Week of " + weekStart.format(DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH))
            + " - " + weekStart.plusDays(6).format(DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH)));
        timetable.put("courseAssignment", primaryApplication == null ? defaultCourseAssignment() : buildCourseAssignment(primaryApplication, weekStart));
        timetable.put("activityEvents", buildActivityEvents(acceptedApplications));
        return timetable;
    }

    private List<Map<String, Object>> acceptedApplications(List<Map<String, Object>> applications) {
        List<Map<String, Object>> accepted = new ArrayList<>();
        if (applications == null) {
            return accepted;
        }
        for (Map<String, Object> application : applications) {
            if ("accepted".equals(statusGroup(application))) {
                accepted.add(application);
            }
        }
        return accepted;
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

    private Map<String, Object> buildCourseAssignment(Map<String, Object> application, LocalDate weekStart) {
        Map<String, Object> assignment = new LinkedHashMap<>();
        String postingId = valueOf(application.get("postingId"));
        List<Map<String, Object>> scheduleBlocks = taTimetableDataRepository.findPostingSchedule(postingId);
        Map<String, Object> primaryBlock = scheduleBlocks.isEmpty() ? new LinkedHashMap<>() : scheduleBlocks.get(0);
        assignment.put("postingId", postingId);
        assignment.put("courseCode", valueOf(application.get("courseCode")));
        assignment.put("courseName", valueOf(application.get("postingTitle")));
        assignment.put("label", hasValue(primaryBlock.get("label")) ? valueOf(primaryBlock.get("label")) : "Primary Assignment");
        assignment.put("dayOfWeek", hasValue(primaryBlock.get("dayOfWeek")) ? valueOf(primaryBlock.get("dayOfWeek")) : "TBD");
        assignment.put("startTime", hasValue(primaryBlock.get("startTime")) ? valueOf(primaryBlock.get("startTime")) : "TBD");
        assignment.put("endTime", hasValue(primaryBlock.get("endTime")) ? valueOf(primaryBlock.get("endTime")) : "TBD");
        assignment.put("location", hasValue(primaryBlock.get("location")) ? valueOf(primaryBlock.get("location")) : "Teaching venue");
        assignment.put("description", buildBlockDescription(application, primaryBlock, weekStart));
        assignment.put("relatedApplicationId", valueOf(application.get("applicationId")));
        return assignment;
    }

    private Map<String, Object> defaultCourseAssignment() {
        Map<String, Object> assignment = new LinkedHashMap<>();
        assignment.put("postingId", "");
        assignment.put("courseCode", "");
        assignment.put("courseName", "No ongoing TA assignment");
        assignment.put("label", "Course TA");
        assignment.put("dayOfWeek", "");
        assignment.put("startTime", "");
        assignment.put("endTime", "");
        assignment.put("location", "");
        assignment.put("description", "No accepted TA-position with a recurring teaching assignment is currently linked to this account.");
        assignment.put("relatedApplicationId", "");
        return assignment;
    }

    private Map<String, Object> firstTaAssignment(List<Map<String, Object>> acceptedApplications) {
        for (Map<String, Object> application : acceptedApplications) {
            if (!isActivityPosting(application)) {
                return application;
            }
        }
        return null;
    }

    private List<Map<String, Object>> buildActivityEvents(List<Map<String, Object>> acceptedApplications) {
        List<Map<String, Object>> events = new ArrayList<>();
        int eventCounter = 1;
        for (Map<String, Object> application : acceptedApplications) {
            if (!isActivityPosting(application)) {
                continue;
            }
            String applicationId = valueOf(application.get("applicationId"));
            String postingId = valueOf(application.get("postingId"));
            String postingTitle = valueOf(application.get("postingTitle"));
            String activityDate = valueOf(application.get("activityDate"));
            String startTime = valueOf(application.get("activityStartTime"));
            String endTime = valueOf(application.get("activityEndTime"));
            if (!hasValue(activityDate) || !hasValue(startTime)) {
                continue;
            }

            Map<String, Object> event = new LinkedHashMap<>();
            event.put("eventId", "AUTO-" + eventCounter++);
            event.put("postingId", postingId);
            event.put("applicationId", applicationId);
            event.put("title", postingTitle == null || postingTitle.isBlank() ? "Scheduled activity" : postingTitle);
            event.put("type", eventTypeFromApplication(application));
            event.put("date", activityDate);
            event.put("startTime", startTime);
            event.put("endTime", endTime == null ? "" : endTime);
            event.put("location", hasValue(application.get("activityLocation")) ? valueOf(application.get("activityLocation")) : "Assigned venue");
            event.put("description", buildActivityDescription(application));
            events.add(event);
        }
        events.sort(Comparator
            .comparing((Map<String, Object> event) -> valueOf(event.get("date")), Comparator.nullsLast(String::compareTo))
            .thenComparing(event -> valueOf(event.get("startTime")), Comparator.nullsLast(String::compareTo)));
        return events;
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

    private boolean isActivityPosting(Map<String, Object> application) {
        String postingType = valueOf(application.get("postingType"));
        return postingType != null && postingType.equalsIgnoreCase("ACTIVITY");
    }

    private String eventTypeFromApplication(Map<String, Object> application) {
        String configuredType = valueOf(application.get("activityType"));
        if (configuredType != null && !configuredType.isBlank()) {
            return configuredType.toLowerCase(Locale.ENGLISH);
        }
        String title = valueOf(application.get("postingTitle"));
        String normalized = title == null ? "" : title.toLowerCase(Locale.ENGLISH);
        if (normalized.contains("exam") || normalized.contains("invig")) {
            return "exam";
        }
        if (normalized.contains("check") || normalized.contains("review") || normalized.contains("acceptance")) {
            return "checkoff";
        }
        return "lab";
    }

    private String buildBlockDescription(Map<String, Object> application, Map<String, Object> block, LocalDate weekStart) {
        String description = valueOf(block.get("description"));
        if (description != null && !description.isBlank()) {
            return description;
        }
        String postingTitle = valueOf(application.get("postingTitle"));
        String dayOfWeek = valueOf(block.get("dayOfWeek"));
        String startTime = valueOf(block.get("startTime"));
        String endTime = valueOf(block.get("endTime"));
        return "Scheduled task for " + (postingTitle == null || postingTitle.isBlank() ? "your accepted TA role" : postingTitle)
            + " on " + (dayOfWeek == null || dayOfWeek.isBlank() ? weekStart.getDayOfWeek().name() : dayOfWeek)
            + ((startTime == null || startTime.isBlank()) ? "" : " from " + startTime)
            + ((endTime == null || endTime.isBlank()) ? "" : " to " + endTime)
            + ".";
    }

    private String buildActivityDescription(Map<String, Object> application) {
        String description = valueOf(application.get("description"));
        if (description != null && !description.isBlank()) {
            return description;
        }
        String postingTitle = valueOf(application.get("postingTitle"));
        String activityDate = valueOf(application.get("activityDate"));
        String startTime = valueOf(application.get("activityStartTime"));
        String endTime = valueOf(application.get("activityEndTime"));
        return "Scheduled activity for " + (postingTitle == null || postingTitle.isBlank() ? "your accepted role" : postingTitle)
            + " on " + (activityDate == null ? "the assigned date" : activityDate)
            + ((startTime == null || startTime.isBlank()) ? "" : " starting at " + startTime)
            + ((endTime == null || endTime.isBlank()) ? "" : " and ending at " + endTime)
            + ".";
    }
}
