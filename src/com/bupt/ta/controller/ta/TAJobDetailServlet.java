package com.bupt.ta.controller.ta;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.JobService;
import com.bupt.ta.service.RecommendationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/ta/jobs/detail")
public class TAJobDetailServlet extends BaseServlet {
    private final JobService jobService = ServiceRegistry.jobService();
    private final RecommendationService recommendationService = ServiceRegistry.recommendationService();

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

        Map<String, Object> job;
        try {
            job = jobService.getJobById(jobId.trim());
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Job posting not found.");
            return;
        }
        request.setAttribute("job", job);

        Map<String, Object> matchAnalysis;
        try {
            matchAnalysis = recommendationService.buildJobMatchForTA(user.getId(), jobId.trim());
        } catch (Exception ex) {
            matchAnalysis = buildFallbackMatchAnalysis(ex.getMessage());
        }
        request.setAttribute("matchAnalysis", matchAnalysis);
        request.getRequestDispatcher("/WEB-INF/views/ta/position-details.jsp").forward(request, response);
    }

    private Map<String, Object> buildFallbackMatchAnalysis(String reason) {
        Map<String, Object> fallback = new LinkedHashMap<>();
        fallback.put("score", 0);
        fallback.put("explanation", reason == null || reason.isBlank()
            ? "Match analysis is unavailable. Please upload your resume and complete your profile."
            : reason);
        fallback.put("method", "UNAVAILABLE");
        fallback.put("matchedSkills", List.of());
        fallback.put("missingSkills", List.of());
        return fallback;
    }
}
