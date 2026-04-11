package com.bupt.ta.controller.ta;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ApplicationService;
import com.bupt.ta.service.JobService;
import com.bupt.ta.service.ProfileService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebServlet("/ta/applications")
public class TAApplySubmitServlet extends BaseServlet {
    private final ApplicationService applicationService = ServiceRegistry.applicationService();
    private final JobService jobService = ServiceRegistry.jobService();
    private final ProfileService profileService = ServiceRegistry.profileService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        String jobId = request.getParameter("jobId");
        String statement = request.getParameter("statement");
        String encodedReturnQuery = request.getParameter("returnQuery");
        try {
            Map<String, Object> application = applicationService.createApplication(user.getId(), jobId, statement);
            String applicationId = String.valueOf(application.getOrDefault("applicationId", ""));
            response.sendRedirect(request.getContextPath() + "/ta/applications/my?created=" + applicationId + "#application-" + applicationId);
        } catch (Exception ex) {
            Map<String, Object> job = jobService.getJobById(jobId);
            Map<String, Object> profile = profileService.getTAProfile(user.getId());
            Map<String, Object> eligibilityResult = applicationService.checkEligibility(user.getId(), jobId);

            request.setAttribute("job", job);
            request.setAttribute("profile", profile);
            request.setAttribute("eligibilityResult", eligibilityResult);
            request.setAttribute("errorMessage", ex.getMessage());
            request.setAttribute("statementDraft", statement == null ? "" : statement);
            request.setAttribute("encodedReturnQuery", encodedReturnQuery == null ? "" : encodedReturnQuery);
            request.setAttribute("returnHref", buildReturnHref(request, jobId, encodedReturnQuery));
            request.getRequestDispatcher("/WEB-INF/views/ta/apply-confirm.jsp").forward(request, response);
        }
    }

    private String buildReturnHref(HttpServletRequest request, String jobId, String encodedReturnQuery) {
        String detailBase = request.getContextPath() + "/ta/jobs/detail?jobId=" + jobId;
        if (encodedReturnQuery == null || encodedReturnQuery.isBlank()) {
            return detailBase;
        }
        String decoded = URLDecoder.decode(encodedReturnQuery, StandardCharsets.UTF_8);
        if (decoded.isBlank()) {
            return detailBase;
        }
        return detailBase + "&returnQuery=" + encodedReturnQuery;
    }
}
