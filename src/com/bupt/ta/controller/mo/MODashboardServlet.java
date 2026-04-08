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

/**
 * MO 仪表盘 Servlet。
 */
@WebServlet("/mo/dashboard")
public class MODashboardServlet extends BaseServlet {
    private final AnalyticsService analyticsService = ServiceRegistry.analyticsService();

    /**
     * 展示 MO 仪表盘页面。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        request.setAttribute("overview", analyticsService.getMODashboardOverview(user.getId()));
        request.getRequestDispatcher("/WEB-INF/views/mo/dashboard.jsp").forward(request, response);
    }
}
