import com.bupt.ta.dto.ApplicationQuery;
import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.match.RecommendationServiceImpl;
import com.bupt.ta.model.Role;
import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.repository.file.ApplicationDataRepository;
import com.bupt.ta.repository.file.JsonUserRepository;
import com.bupt.ta.repository.file.NotificationDataRepository;
import com.bupt.ta.repository.file.PostingDataRepository;
import com.bupt.ta.repository.file.SystemDataRepository;
import com.bupt.ta.repository.file.TADataRepository;
import com.bupt.ta.repository.file.TATimetableDataRepository;
import com.bupt.ta.resume.PdfResumeExtractor;
import com.bupt.ta.resume.ResumeStructurer;
import com.bupt.ta.service.AnalyticsService;
import com.bupt.ta.service.ApplicationService;
import com.bupt.ta.service.AuthService;
import com.bupt.ta.service.JobService;
import com.bupt.ta.service.NotificationService;
import com.bupt.ta.service.ProfileService;
import com.bupt.ta.service.RecommendationService;
import com.bupt.ta.service.ResumeService;
import com.bupt.ta.service.UserService;
import com.bupt.ta.service.impl.AnalyticsServiceImpl;
import com.bupt.ta.service.impl.ApplicationServiceImpl;
import com.bupt.ta.service.impl.AuthServiceImpl;
import com.bupt.ta.service.impl.JobServiceImpl;
import com.bupt.ta.service.impl.NotificationServiceImpl;
import com.bupt.ta.service.impl.ProfileServiceImpl;
import com.bupt.ta.service.impl.ResumeServiceImpl;
import com.bupt.ta.service.impl.UserServiceImpl;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class TestSupport {
    static final String TA_ID = "TA001";
    static final String MO_ID = "MO001";
    static final String ADMIN_ID = "ADMIN001";

    final UserRepository userRepository;
    final TADataRepository taDataRepository;
    final PostingDataRepository postingDataRepository;
    final ApplicationDataRepository applicationDataRepository;
    final TATimetableDataRepository timetableDataRepository;
    final SystemDataRepository systemDataRepository;
    final NotificationDataRepository notificationDataRepository;

    final AuthService authService;
    final UserService userService;
    final ProfileService profileService;
    final JobService jobService;
    final ApplicationService applicationService;
    final RecommendationService recommendationService;
    final ResumeService resumeService;
    final AnalyticsService analyticsService;

    TestSupport() {
        userRepository = new JsonUserRepository();
        taDataRepository = new TADataRepository();
        postingDataRepository = new PostingDataRepository();
        applicationDataRepository = new ApplicationDataRepository();
        timetableDataRepository = new TATimetableDataRepository();
        systemDataRepository = new SystemDataRepository();
        notificationDataRepository = new NotificationDataRepository();

        NotificationService notificationService = new NotificationServiceImpl(notificationDataRepository);
        recommendationService = new RecommendationServiceImpl(
            taDataRepository,
            postingDataRepository,
            applicationDataRepository
        );
        authService = new AuthServiceImpl(userRepository);
        userService = new UserServiceImpl(userRepository);
        profileService = new ProfileServiceImpl(taDataRepository, userRepository, systemDataRepository);
        jobService = new JobServiceImpl(postingDataRepository, userRepository, applicationDataRepository, notificationService);
        applicationService = new ApplicationServiceImpl(
            taDataRepository,
            postingDataRepository,
            applicationDataRepository,
            recommendationService,
            timetableDataRepository,
            notificationService
        );
        resumeService = new ResumeServiceImpl(
            taDataRepository,
            applicationDataRepository,
            postingDataRepository,
            new PdfResumeExtractor(),
            new ResumeStructurer()
        );
        analyticsService = new AnalyticsServiceImpl(userRepository);
    }

    interface AcceptanceTest {
        void run(TestSupport ctx) throws Exception;
    }

    static void ensureAcceptanceDataRoot() {
        String configured = System.getProperty("ta.data.dir");
        assertTrue(configured != null && !configured.isBlank(),
            "The test runner must pass -Dta.data.dir to isolate test data.");
        String normalized = configured.replace('\\', '/').toLowerCase(Locale.ROOT);
        assertTrue(normalized.contains(".acceptance-test-data"),
            "Refusing to run tests against non-isolated data root: " + configured);
    }

    static Path dataRoot() {
        return Paths.get(System.getProperty("ta.data.dir")).toAbsolutePath().normalize();
    }

    Map<String, Object> createOpenTestJob(String suffix) {
        return jobService.createJob(MO_ID, newJobParams(suffix));
    }

    Map<String, Object> newJobParams(String suffix) {
        String safeSuffix = suffix == null ? "GEN" : suffix.replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("courseCode", "TST" + safeSuffix);
        params.put("courseName", "Acceptance Testing Lab " + safeSuffix);
        params.put("vacancies", "2");
        params.put("deadline", LocalDate.now().plusDays(30).toString());
        params.put("description", "Support a controlled acceptance testing lab for Java, Git, testing, and communication workflows.");
        params.put("requiredSkills", Arrays.asList("Java", "Testing", "Communication"));
        params.put("postingType", "TA");
        params.put("estimatedWorkloadHours", "4");
        params.put("status", "OPEN");
        return params;
    }

    static void assertThrows(Runnable action, String message) {
        try {
            action.run();
        } catch (RuntimeException expected) {
            return;
        }
        fail(message);
    }

    static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            fail(message + " Expected=[" + expected + "] Actual=[" + actual + "]");
        }
    }

    static void assertTrue(boolean condition, String message) {
        if (!condition) {
            fail(message);
        }
    }

    static void assertNonBlank(Object value, String message) {
        assertTrue(value != null && !String.valueOf(value).trim().isEmpty(), message);
    }

    static void assertContains(List<?> list, Object expected, String message) {
        assertTrue(list != null && list.contains(expected), message + " Missing=[" + expected + "] Actual=" + list);
    }

    static Map<String, Object> findRecord(PageResult<Map<String, Object>> page, String key, Object value) {
        if (page == null || page.getRecords() == null) {
            return null;
        }
        for (Map<String, Object> record : page.getRecords()) {
            if (value == null ? record.get(key) == null : value.equals(record.get(key))) {
                return record;
            }
        }
        return null;
    }

    static int intValue(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(String.valueOf(value).trim());
    }

    static String stringValue(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    static void assertReadableStream(Map<String, Object> downloadInfo) throws Exception {
        assertNonBlank(downloadInfo.get("fileName"), "Resume download should expose a file name.");
        Object stream = downloadInfo.get("stream");
        assertTrue(stream instanceof InputStream, "Resume download should expose an InputStream.");
        try (InputStream input = (InputStream) stream) {
            assertTrue(input.read() >= 0, "Resume stream should contain bytes.");
        }
    }

    private static void fail(String message) {
        throw new AssertionError(message);
    }
}
