package com.bupt.ta.controller.ta;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ApplicationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebServlet("/ta/applications/withdraw")
public class TAApplicationWithdrawServlet extends BaseServlet {
    private final ApplicationService applicationService = ServiceRegistry.applicationService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        String applicationId = request.getParameter("applicationId");
        String reason = request.getParameter("reason");
        try {
            Map<String, Object> updated = applicationService.withdrawApplicationByTA(applicationId, user.getId(), reason);
            String updatedApplicationId = String.valueOf(updated.getOrDefault("applicationId", applicationId));
            response.sendRedirect(request.getContextPath() + "/ta/applications/my?updated=" + updatedApplicationId + "#application-" + updatedApplicationId);
        } catch (IllegalStateException ex) {
            String error = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/ta/applications/my?error=" + error + "#application-" + applicationId);
        }
    }
}
