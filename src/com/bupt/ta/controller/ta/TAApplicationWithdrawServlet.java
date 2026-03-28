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
            applicationService.withdrawApplicationByTA(applicationId, user.getId(), reason);
            response.sendRedirect(request.getContextPath() + "/ta/applications/my");
        } catch (IllegalStateException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        }
    }
}
