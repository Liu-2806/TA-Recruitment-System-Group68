package com.bupt.ta.controller.mo;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ApplicationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/mo/applicants/detail")
public class MOApplicantDetailServlet extends BaseServlet {
    private final ApplicationService applicationService = ServiceRegistry.applicationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        String applicationId = request.getParameter("applicationId");
        request.setAttribute("application", applicationService.getApplicationDetailForMO(applicationId, user.getId()));
        request.getRequestDispatcher("/WEB-INF/views/mo/applicant-details.jsp").forward(request, response);
    }
}
