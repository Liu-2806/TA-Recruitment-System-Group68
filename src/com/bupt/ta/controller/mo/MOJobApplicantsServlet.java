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
import java.util.Map;

@WebServlet("/mo/jobs/applicants")
public class MOJobApplicantsServlet extends BaseServlet {
    private final JobService jobService = ServiceRegistry.jobService();
    private final ApplicationService applicationService = ServiceRegistry.applicationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        String jobId = request.getParameter("jobId");
        if (jobId == null || jobId.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "jobId is required.");
            return;
        }

        Map<String, Object> job = jobService.getJobById(jobId);
        if (user == null || !user.getId().equals(String.valueOf(job.get("moId")))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to view applicants for this job.");
            return;
        }

        ApplicationQuery query = new ApplicationQuery();
        query.setKeyword(request.getParameter("keyword"));
        query.setStatus(request.getParameter("status"));
        query.setSortBy(request.getParameter("sortBy"));
        query.setPage(parsePositiveInt(request.getParameter("page"), 1));
        query.setSize(parsePositiveInt(request.getParameter("size"), 10));
        request.setAttribute("job", job);
        request.setAttribute("applicationsPage", applicationService.listApplicationsByJob(jobId, query));
        request.setAttribute("query", query);
        request.getRequestDispatcher("/WEB-INF/views/mo/applicants.jsp").forward(request, response);
    }

    private int parsePositiveInt(String rawValue, int defaultValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(rawValue.trim());
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
