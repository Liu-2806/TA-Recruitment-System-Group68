package com.bupt.ta.controller.admin;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.service.AnalyticsService;
import com.bupt.ta.util.JsonUtils;
import com.bupt.ta.util.ServiceRegistry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Admin TA 工作量详情接口 Servlet。
 */
@WebServlet("/admin/analytics/ta-workload/detail")
public class AdminTAWorkloadDetailServlet extends BaseServlet {
    private final AnalyticsService analyticsService = ServiceRegistry.analyticsService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String taId = request.getParameter("taId");

        try {
            Map<String, Object> payload = new LinkedHashMap<String, Object>();
            payload.put("detail", analyticsService.getTAWorkloadDetail(taId));
            response.getWriter().write(JsonUtils.toJson(payload));
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, Object> error = new LinkedHashMap<String, Object>();
            error.put("errorMessage", ex.getMessage());
            response.getWriter().write(JsonUtils.toJson(error));
        }
    }
}

