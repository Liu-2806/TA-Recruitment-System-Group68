package com.bupt.ta.controller.common;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.model.User;
import com.bupt.ta.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/notifications/dismiss")
public class NotificationDismissServlet extends BaseServlet {
    private final NotificationService notificationService = ServiceRegistry.notificationService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        String notificationId = request.getParameter("notificationId");
        String redirect = request.getParameter("redirect");
        try {
            notificationService.dismiss(notificationId, user.getId());
        } catch (IllegalStateException ignored) {
            // 忽略非授权请求，保持静默
        }
        if (redirect == null || redirect.isBlank() || !redirect.startsWith(request.getContextPath())) {
            redirect = request.getContextPath() + "/";
        }
        response.sendRedirect(redirect);
    }
}
