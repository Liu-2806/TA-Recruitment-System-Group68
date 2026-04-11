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

@WebServlet("/ta/applications/confirm")
public class TAApplyConfirmServlet extends BaseServlet {
    private final ApplicationService applicationService = ServiceRegistry.applicationService();
    private final JobService jobService = ServiceRegistry.jobService();
    private final ProfileService profileService = ServiceRegistry.profileService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        String jobId = request.getParameter("jobId");
        if (jobId == null || jobId.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/ta/jobs");
            return;
        }
        String encodedReturnQuery = request.getParameter("returnQuery");

        Map<String, Object> job = jobService.getJobById(jobId.trim());
        Map<String, Object> profile = profileService.getTAProfile(user.getId());
        Map<String, Object> eligibilityResult = applicationService.checkEligibility(user.getId(), jobId.trim());

        request.setAttribute("job", job);
        request.setAttribute("profile", profile);
        request.setAttribute("eligibilityResult", eligibilityResult);
        request.setAttribute("returnHref", buildReturnHref(request, jobId.trim(), encodedReturnQuery));
        request.setAttribute("encodedReturnQuery", encodedReturnQuery == null ? "" : encodedReturnQuery);
        request.getRequestDispatcher("/WEB-INF/views/ta/apply-confirm.jsp").forward(request, response);
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
