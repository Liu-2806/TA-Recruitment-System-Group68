package com.bupt.ta.controller.admin;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.service.AnalyticsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Admin 仪表盘 Servlet。
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends BaseServlet {
    private AnalyticsService analyticsService;

    /**
     * 展示系统概览页面。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("overview", analyticsService.getSystemOverview());
        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }
}
