package com.bupt.ta.service;

import java.util.Map;

public interface RecommendationService {
    Map<String, Object> buildJobMatchForTA(String taUserId, String jobId);

    Map<String, Object> buildApplicationMatchForMO(String applicationId, String moUserId);
}
