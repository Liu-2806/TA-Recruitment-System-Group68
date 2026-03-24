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

@WebServlet("/ta/applications")
public class TAApplySubmitServlet extends BaseServlet {
    private final ApplicationService applicationService = ServiceRegistry.applicationService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        String jobId = request.getParameter("jobId");
        String statement = request.getParameter("statement");
        try {
            applicationService.createApplication(user.getId(), jobId, statement);
            response.sendRedirect(request.getContextPath() + "/ta/applications/my");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/ta/apply-confirm.jsp").forward(request, response);
        }
    }
}
