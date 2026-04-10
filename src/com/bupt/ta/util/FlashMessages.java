package com.bupt.ta.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class FlashMessages {
    private FlashMessages() {
    }

    public static void success(HttpServletRequest request, String message) {
        putSessionMessage(request, SessionKeys.FLASH_SUCCESS, message);
    }

    public static void error(HttpServletRequest request, String message) {
        if (request == null) {
            return;
        }
        request.setAttribute("errorMessage", message);
    }

    private static void putSessionMessage(HttpServletRequest request, String key, String message) {
        if (request == null || message == null || message.trim().isEmpty()) {
            return;
        }
        HttpSession session = request.getSession(true);
        session.setAttribute(key, message.trim());
    }
}
