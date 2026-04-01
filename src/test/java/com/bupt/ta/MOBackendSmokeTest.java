package com.bupt.ta;

import com.bupt.ta.dto.ApplicationQuery;
import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.match.RecommendationServiceImpl;
import com.bupt.ta.model.ApplicationStatus;
import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.repository.file.ApplicationDataRepository;
import com.bupt.ta.repository.file.JsonUserRepository;
import com.bupt.ta.repository.file.PostingDataRepository;
import com.bupt.ta.repository.file.SystemDataRepository;
import com.bupt.ta.repository.file.TADataRepository;
import com.bupt.ta.resume.PdfResumeExtractor;
import com.bupt.ta.resume.ResumeStructurer;
import com.bupt.ta.service.ApplicationService;
import com.bupt.ta.service.JobService;
import com.bupt.ta.service.ProfileService;
import com.bupt.ta.service.ResumeService;
import com.bupt.ta.service.impl.ApplicationServiceImpl;
import com.bupt.ta.service.impl.JobServiceImpl;
import com.bupt.ta.service.impl.ProfileServiceImpl;
import com.bupt.ta.service.impl.ResumeServiceImpl;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MOBackendSmokeTest {

    public static void main(String[] args) throws Exception {
        Path isolatedDataRoot = prepareIsolatedDataRoot();
        System.setProperty("ta.data.dir", isolatedDataRoot.toString());

        UserRepository userRepository = new JsonUserRepository();
        TADataRepository taDataRepository = new TADataRepository();
        PostingDataRepository postingDataRepository = new PostingDataRepository();
        ApplicationDataRepository applicationDataRepository = new ApplicationDataRepository();
        SystemDataRepository systemDataRepository = new SystemDataRepository();

        ProfileService profileService = new ProfileServiceImpl(taDataRepository, systemDataRepository, userRepository);
        JobService jobService = new JobServiceImpl(postingDataRepository, userRepository);
        RecommendationServiceImpl recommendationService = new RecommendationServiceImpl(
            taDataRepository,
            postingDataRepository,
            applicationDataRepository
        );
        ApplicationService applicationService = new ApplicationServiceImpl(
            taDataRepository,
            postingDataRepository,
            applicationDataRepository,
            recommendationService
        );
        ResumeService resumeService = new ResumeServiceImpl(
            taDataRepository,
            applicationDataRepository,
            postingDataRepository,
            new PdfResumeExtractor(),
            new ResumeStructurer()
        );

        Map<String, Object> moProfile = profileService.getMOProfile("MO001");
        assertEquals("MO001", moProfile.get("moId"), "Should load MO001 profile.");
        assertFalse(moProfile.containsKey("password"), "MO profile response must hide password.");

        Map<String, Object> profileUpdate = new LinkedHashMap<>();
        profileUpdate.put("name", "Prof. Go Updated");
        profileUpdate.put("email", "updated.mo@qmul.ac.uk");
        profileUpdate.put("department", "School of Software Engineering");
        profileUpdate.put("phone", "0200-1234");
        profileUpdate.put("description", "Updated MO profile for smoke test.");
        profileService.updateMOProfile("MO001", profileUpdate);

        Map<String, Object> updatedProfile = profileService.getMOProfile("MO001");
        assertEquals("Prof. Go Updated", updatedProfile.get("fullName"), "MO profile update should persist full name.");
        assertEquals("updated.mo@qmul.ac.uk", updatedProfile.get("email"), "MO profile update should persist email.");

        Map<String, Object> createJobParams = new LinkedHashMap<>();
        createJobParams.put("courseCode", "SE3999");
        createJobParams.put("courseName", "Advanced Software Studio TA");
        createJobParams.put("vacancies", "2");
        createJobParams.put("deadline", "2026/04/15");
        createJobParams.put("description", "Support demos, grading, and studio facilitation.");
        createJobParams.put("requiredSkills", "Java, Communication, Testing");
        createJobParams.put("estimatedWorkloadHours", "4");
        Map<String, Object> createdJob = jobService.createJob("MO001", createJobParams);
        assertEquals("POST004", createdJob.get("postingId"), "New MO job should receive the next posting ID.");
        assertEquals("OPEN", createdJob.get("status"), "New MO job should default to OPEN.");

        JobQuery jobsQuery = new JobQuery();
        jobsQuery.setKeyword("SE3999");
        PageResult<Map<String, Object>> jobsPage = jobService.listJobsByMO("MO001", jobsQuery);
        assertEquals(1L, jobsPage.getTotal(), "MO job search should find the newly created posting.");
        assertEquals("POST004", jobsPage.getRecords().get(0).get("postingId"), "MO job list should include the created posting.");

        ApplicationQuery applicantsQuery = new ApplicationQuery();
        applicantsQuery.setStatus("SUBMITTED");
        applicantsQuery.setSortBy("scoreDesc");
        PageResult<Map<String, Object>> applicantsPage = applicationService.listApplicationsByJob("POST001", applicantsQuery);
        assertEquals(1L, applicantsPage.getTotal(), "POST001 should still have one submitted application in seed data.");
        assertTrue(parseInt(applicantsPage.getRecords().get(0).get("skillMatchScore")) > 0, "Applicant listing should enrich match score.");

        Map<String, Object> applicationDetail = applicationService.getApplicationDetailForMO("APP001", "MO001");
        @SuppressWarnings("unchecked")
        Map<String, Object> job = (Map<String, Object>) applicationDetail.get("job");
        @SuppressWarnings("unchecked")
        Map<String, Object> taProfile = (Map<String, Object>) applicationDetail.get("taProfile");
        assertEquals("POST001", job.get("postingId"), "Application detail should embed the related posting.");
        assertEquals("TA001", taProfile.get("taId"), "Application detail should embed the applicant profile.");

        applicationService.updateStatusByMO("APP001", "MO001", ApplicationStatus.ACCEPTED, "Strong communication and testing background.");
        Map<String, Object> updatedApplication = applicationService.getApplicationDetailForMO("APP001", "MO001");
        assertEquals("ACCEPTED", updatedApplication.get("status"), "MO decision should persist ACCEPTED status.");
        assertEquals(
            "Strong communication and testing background.",
            updatedApplication.get("feedback"),
            "MO decision should persist feedback."
        );

        Map<String, Object> resumePayload = resumeService.openResumeStreamForMO("APP001", "MO001");
        assertEquals("sample_resume.pdf", resumePayload.get("fileName"), "Resume download should return the stored resume file.");
        try (InputStream stream = (InputStream) resumePayload.get("stream")) {
            assertTrue(stream.read() != -1, "Resume stream should contain PDF bytes.");
        }

        System.out.println("MO backend smoke test passed.");
        System.out.println("Isolated data root: " + isolatedDataRoot);
    }

    private static Path prepareIsolatedDataRoot() throws Exception {
        Path workingDirectory = Paths.get("build", "mo-smoke-data").toAbsolutePath().normalize();
        deleteRecursively(workingDirectory);
        Path dataRoot = workingDirectory.resolve("data");
        copyDirectory(Paths.get("data").toAbsolutePath().normalize(), dataRoot);
        return dataRoot;
    }

    private static void copyDirectory(Path source, Path target) throws Exception {
        try (var stream = Files.walk(source)) {
            for (Path path : (Iterable<Path>) stream::iterator) {
                Path relative = source.relativize(path);
                Path destination = target.resolve(relative);
                if (Files.isDirectory(path)) {
                    Files.createDirectories(destination);
                } else {
                    Files.createDirectories(destination.getParent());
                    Files.copy(path, destination, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private static void deleteRecursively(Path target) throws Exception {
        if (!Files.exists(target)) {
            return;
        }
        try (var stream = Files.walk(target)) {
            stream.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new IllegalStateException(message + " Expected: " + expected + ", actual: " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new IllegalStateException(message);
        }
    }

    private static int parseInt(Object value) {
        if (value == null) {
            return 0;
        }
        try {
            return Integer.parseInt(String.valueOf(value).trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
