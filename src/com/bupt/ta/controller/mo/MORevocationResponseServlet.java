package com.bupt.ta.controller.mo;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ApplicationService;
import com.bupt.ta.service.NotificationService;
import com.bupt.ta.util.FlashMessages;
import com.bupt.ta.util.SessionKeys;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/mo/notifications/revocation")
public class MORevocationResponseServlet extends BaseServlet {
    private final ApplicationService applicationService = ServiceRegistry.applicationService();
    private final NotificationService notificationService = ServiceRegistry.notificationService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        String applicationId = request.getParameter("applicationId");
        String notificationId = request.getParameter("notificationId");
        String decision = request.getParameter("decision");
        String comment = request.getParameter("comment");
        String redirect = request.getParameter("redirect");

        boolean approve = "approve".equalsIgnoreCase(decision);
        boolean reject = "reject".equalsIgnoreCase(decision);
        if (!approve && !reject) {
            flashError(request, "Please choose to approve or reject the revocation request.");
            redirectBack(response, request, redirect);
            return;
        }

        try {
            applicationService.respondToRevocationRequest(applicationId, user.getId(), approve, comment);
            if (notificationId != null && !notificationId.isBlank()) {
                notificationService.markActionResolved(notificationId, approve ? "APPROVED" : "REJECTED");
                notificationService.dismiss(notificationId, user.getId());
            }
            FlashMessages.success(request, approve
                ? "Revocation approved. The applicant has been notified."
                : "Revocation declined. The applicant has been notified.");
        } catch (IllegalStateException ex) {
            flashError(request, ex.getMessage());
        }
        redirectBack(response, request, redirect);
    }

    private void flashError(HttpServletRequest request, String message) {
        if (request == null || message == null || message.trim().isEmpty()) {
            return;
        }
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionKeys.FLASH_ERROR, message.trim());
    }

    private void redirectBack(HttpServletResponse response, HttpServletRequest request, String redirect) throws IOException {
        if (redirect == null || redirect.isBlank() || !redirect.startsWith(request.getContextPath())) {
            redirect = request.getContextPath() + "/mo/dashboard";
        }
        response.sendRedirect(redirect);
    }
}
