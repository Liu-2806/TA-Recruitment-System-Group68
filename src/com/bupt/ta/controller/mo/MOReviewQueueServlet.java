package com.bupt.ta.controller.mo;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.AnalyticsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * MO 待审核队列列表（跨岗位 SUBMITTED 申请）。
 */
@WebServlet("/mo/review-queue")
public class MOReviewQueueServlet extends BaseServlet {
    private final AnalyticsService analyticsService = ServiceRegistry.analyticsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        Map<String, Object> data = analyticsService.getMOReviewQueue(user.getId());
        Object rows = data.get("pendingRows");
        request.setAttribute("pendingRows", rows instanceof List ? rows : Collections.emptyList());
        request.setAttribute("pendingCount", data.get("pendingCount"));
        request.setAttribute("postingsInQueueCount", data.get("postingsInQueueCount"));
        request.getRequestDispatcher("/WEB-INF/views/mo/review-queue.jsp").forward(request, response);
    }
}
