package com.bupt.ta.controller.admin;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Admin 查看全系统岗位 Servlet。
 */
@WebServlet("/admin/jobs")
public class AdminJobListServlet extends BaseServlet {
    private final JobService jobService = ServiceRegistry.jobService();

    /**
     * 查询并展示岗位列表。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JobQuery query = new JobQuery();
        query.setKeyword(request.getParameter("keyword"));
        query.setStatus(request.getParameter("status"));
        query.setSortBy(request.getParameter("sortBy"));
        query.setPage(parsePositiveInt(request.getParameter("page"), 1));
        query.setSize(parsePositiveInt(request.getParameter("size"), 10));

        String moFilter = firstNonBlank(
            request.getParameter("moFilter"),
            request.getParameter("moId"),
            request.getParameter("ownerId")
        );
        query.setMoFilter(moFilter);
        query.setMoId(firstNonBlank(request.getParameter("moId"), moFilter));
        query.setOwnerId(firstNonBlank(request.getParameter("ownerId"), moFilter));

        request.setAttribute("jobsPage", jobService.searchAllJobsForAdmin(query));
        request.setAttribute("query", query);
        request.getRequestDispatcher("/WEB-INF/views/admin/all-jobs.jsp").forward(request, response);
    }

    private int parsePositiveInt(String rawValue, int defaultValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(rawValue.trim());
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) {
                return value.trim();
            }
        }
        return null;
    }
}
