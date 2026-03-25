package com.bupt.ta.util;

import com.bupt.ta.repository.UserRepository;
import com.bupt.ta.repository.file.JsonUserRepository;
import com.bupt.ta.service.AnalyticsService;
import com.bupt.ta.service.AuthService;
import com.bupt.ta.service.UserService;
import com.bupt.ta.service.impl.AnalyticsServiceImpl;
import com.bupt.ta.service.impl.AuthServiceImpl;
import com.bupt.ta.service.impl.UserServiceImpl;

/**
 * 简单服务注册表。
 */
public final class ServiceRegistry {
    private static final UserRepository USER_REPOSITORY = new JsonUserRepository();
    private static final UserService USER_SERVICE = new UserServiceImpl(USER_REPOSITORY);
    private static final AuthService AUTH_SERVICE = new AuthServiceImpl(USER_REPOSITORY);
    private static final AnalyticsService ANALYTICS_SERVICE = new AnalyticsServiceImpl(USER_REPOSITORY);

    private ServiceRegistry() {
    }

    public static UserService userService() {
        return USER_SERVICE;
    }

    public static AuthService authService() {
        return AUTH_SERVICE;
    }

    public static AnalyticsService analyticsService() {
        return ANALYTICS_SERVICE;
    }
}
