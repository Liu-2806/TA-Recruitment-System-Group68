package com.bupt.ta.controller.ta;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * TA 岗位列表 Servlet。
 */
@WebServlet("/ta/jobs")
public class TAJobListServlet extends BaseServlet {
    private final JobService jobService = ServiceRegistry.jobService();

    /**
     * 查询并展示岗位列表。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JobQuery query = new JobQuery();
        query.setKeyword(request.getParameter("keyword"));
        query.setMajor(request.getParameter("major"));
        query.setDepartment(request.getParameter("department"));
        query.setModuleType(request.getParameter("moduleType"));
        query.setResponsibilityKeyword(request.getParameter("responsibilityKeyword"));
        query.setStatus(request.getParameter("status"));
        query.setSortBy(request.getParameter("sortBy"));
        query.setPage(parseIntOrDefault(request.getParameter("page"), 1));
        query.setSize(parseIntOrDefault(request.getParameter("size"), 10));
        request.setAttribute("jobsPage", jobService.searchOpenJobs(query));
        request.setAttribute("query", query);
        request.setAttribute("encodedReturnQuery", encodeQueryString(request.getQueryString()));
        request.getRequestDispatcher("/WEB-INF/views/ta/positions.jsp").forward(request, response);
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private String encodeQueryString(String queryString) {
        if (queryString == null || queryString.isBlank()) {
            return "";
        }
        return URLEncoder.encode(queryString, StandardCharsets.UTF_8);
    }
}
