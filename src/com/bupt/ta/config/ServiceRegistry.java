package com.bupt.ta.config;

import com.bupt.ta.match.RecommendationServiceImpl;
import com.bupt.ta.repository.file.ApplicationDataRepository;
import com.bupt.ta.repository.file.PostingDataRepository;
import com.bupt.ta.repository.file.SystemDataRepository;
import com.bupt.ta.repository.file.TADataRepository;
import com.bupt.ta.resume.PdfResumeExtractor;
import com.bupt.ta.resume.ResumeStructurer;
import com.bupt.ta.service.ApplicationService;
import com.bupt.ta.service.JobService;
import com.bupt.ta.service.ProfileService;
import com.bupt.ta.service.RecommendationService;
import com.bupt.ta.service.ResumeService;
import com.bupt.ta.service.impl.ApplicationServiceImpl;
import com.bupt.ta.service.impl.JobServiceImpl;
import com.bupt.ta.service.impl.ProfileServiceImpl;
import com.bupt.ta.service.impl.ResumeServiceImpl;

public final class ServiceRegistry {
    private static final TADataRepository TA_DATA_REPOSITORY = new TADataRepository();
    private static final PostingDataRepository POSTING_DATA_REPOSITORY = new PostingDataRepository();
    private static final ApplicationDataRepository APPLICATION_DATA_REPOSITORY = new ApplicationDataRepository();
    private static final SystemDataRepository SYSTEM_DATA_REPOSITORY = new SystemDataRepository();

    private static final ResumeService RESUME_SERVICE = new ResumeServiceImpl(
        TA_DATA_REPOSITORY,
        APPLICATION_DATA_REPOSITORY,
        POSTING_DATA_REPOSITORY,
        new PdfResumeExtractor(),
        new ResumeStructurer()
    );

    private static final RecommendationService RECOMMENDATION_SERVICE = new RecommendationServiceImpl(
        TA_DATA_REPOSITORY,
        POSTING_DATA_REPOSITORY,
        APPLICATION_DATA_REPOSITORY
    );

    private static final ProfileService PROFILE_SERVICE = new ProfileServiceImpl(
        TA_DATA_REPOSITORY,
        SYSTEM_DATA_REPOSITORY
    );

    private static final JobService JOB_SERVICE = new JobServiceImpl(
        POSTING_DATA_REPOSITORY
    );

    private static final ApplicationService APPLICATION_SERVICE = new ApplicationServiceImpl(
        TA_DATA_REPOSITORY,
        POSTING_DATA_REPOSITORY,
        APPLICATION_DATA_REPOSITORY,
        RECOMMENDATION_SERVICE
    );

    private ServiceRegistry() {
    }

    public static ResumeService resumeService() {
        return RESUME_SERVICE;
    }

    public static RecommendationService recommendationService() {
        return RECOMMENDATION_SERVICE;
    }

    public static ProfileService profileService() {
        return PROFILE_SERVICE;
    }

    public static JobService jobService() {
        return JOB_SERVICE;
    }

    public static ApplicationService applicationService() {
        return APPLICATION_SERVICE;
    }
}
