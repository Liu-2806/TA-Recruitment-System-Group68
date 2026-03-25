package com.bupt.ta.controller.ta;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ApplicationService;
import com.bupt.ta.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ta/applications/confirm")
public class TAApplyConfirmServlet extends BaseServlet {
    private final ApplicationService applicationService = ServiceRegistry.applicationService();
    private final JobService jobService = ServiceRegistry.jobService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        String jobId = request.getParameter("jobId");
        request.setAttribute("job", jobService.getJobById(jobId));
        request.setAttribute("eligibilityResult", applicationService.checkEligibility(user.getId(), jobId));
        request.getRequestDispatcher("/WEB-INF/views/ta/apply-confirm.jsp").forward(request, response);
    }
}
