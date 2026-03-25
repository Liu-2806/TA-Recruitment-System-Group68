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
        request.setAttribute("job", jobService.getJobById(jobId));
        request.setAttribute("matchAnalysis", recommendationService.buildJobMatchForTA(user.getId(), jobId));
        request.getRequestDispatcher("/WEB-INF/views/ta/position-details.jsp").forward(request, response);
    }
}
