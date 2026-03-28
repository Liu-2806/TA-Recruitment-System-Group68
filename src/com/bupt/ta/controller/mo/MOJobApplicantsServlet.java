package com.bupt.ta.controller.mo;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.dto.ApplicationQuery;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ApplicationService;
import com.bupt.ta.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/mo/jobs/applicants")
public class MOJobApplicantsServlet extends BaseServlet {
    private final JobService jobService = ServiceRegistry.jobService();
    private final ApplicationService applicationService = ServiceRegistry.applicationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        String jobId = request.getParameter("jobId");
        ApplicationQuery query = new ApplicationQuery();
        query.setKeyword(request.getParameter("keyword"));
        query.setStatus(request.getParameter("status"));
        query.setSortBy(request.getParameter("sortBy"));
        request.setAttribute("job", jobService.getJobById(jobId));
        request.setAttribute("applicationsPage", applicationService.listApplicationsByJob(jobId, query));
        request.setAttribute("query", query);
        request.getRequestDispatcher("/WEB-INF/views/mo/applicants.jsp").forward(request, response);
    }
}
