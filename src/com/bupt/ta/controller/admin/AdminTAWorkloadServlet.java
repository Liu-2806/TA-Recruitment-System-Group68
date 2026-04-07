package com.bupt.ta.controller.admin;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.service.AnalyticsService;
import com.bupt.ta.util.ServiceRegistry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Admin TA 工作量分析 Servlet。
 */
@WebServlet("/admin/analytics/ta-workload")
public class AdminTAWorkloadServlet extends BaseServlet {
    private final AnalyticsService analyticsService = ServiceRegistry.analyticsService();

    /**
     * 展示 TA 工作量报表。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> query = new HashMap<>();
        query.put("keyword", request.getParameter("keyword"));
        query.put("term", request.getParameter("term"));
        query.put("minHours", request.getParameter("minHours"));
        query.put("maxHours", request.getParameter("maxHours"));
        request.setAttribute("reportPage", analyticsService.getTAWorkloadReport(query));
        request.setAttribute("query", query);
        request.getRequestDispatcher("/WEB-INF/views/admin/ta-workload.jsp").forward(request, response);
    }
}
