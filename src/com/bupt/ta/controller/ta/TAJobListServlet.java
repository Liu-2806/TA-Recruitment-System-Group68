package com.bupt.ta.controller.ta;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.dto.PageResult;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ApplicationService;
import com.bupt.ta.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TA 岗位列表 Servlet。
 */
@WebServlet("/ta/jobs")
public class TAJobListServlet extends BaseServlet {
    private final JobService jobService = ServiceRegistry.jobService();
    private final ApplicationService applicationService = ServiceRegistry.applicationService();

    /**
     * 查询并展示岗位列表。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        JobQuery query = new JobQuery();
        query.setKeyword(request.getParameter("keyword"));
        query.setMajor(request.getParameter("major"));
        query.setDepartment(request.getParameter("department"));
        query.setModuleType(request.getParameter("moduleType"));
        query.setResponsibilityKeyword(request.getParameter("responsibilityKeyword"));
        query.setStatus(request.getParameter("status"));
        query.setSortBy(request.getParameter("sortBy"));
        query.setPage(parseIntOrDefault(request.getParameter("page"), 1));
        query.setSize(parseIntOrDefault(request.getParameter("size"), 6));
        PageResult<Map<String, Object>> jobsPage = jobService.searchOpenJobs(query);
        request.setAttribute("jobsPage", withApplicationState(jobsPage, user.getId()));
        request.setAttribute("query", query);
        request.setAttribute("encodedReturnQuery", encodeQueryString(request.getQueryString()));
        request.getRequestDispatcher("/WEB-INF/views/ta/positions.jsp").forward(request, response);
    }

    private PageResult<Map<String, Object>> withApplicationState(PageResult<Map<String, Object>> jobsPage, String taUserId) {
        if (jobsPage == null || jobsPage.getRecords() == null) {
            return jobsPage;
        }

        List<Map<String, Object>> records = new ArrayList<>();
        for (Map<String, Object> job : jobsPage.getRecords()) {
            Map<String, Object> copy = new LinkedHashMap<>(job);
            try {
                Map<String, Object> eligibility = applicationService.checkEligibility(taUserId, String.valueOf(job.get("postingId")));
                copy.put("canApply", Boolean.TRUE.equals(eligibility.get("eligible")));
                copy.put("alreadyApplied", Boolean.TRUE.equals(eligibility.get("alreadyApplied")));
            } catch (Exception ex) {
                copy.put("canApply", Boolean.FALSE);
                copy.put("alreadyApplied", Boolean.FALSE);
            }
            records.add(copy);
        }

        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setRecords(records);
        result.setPage(jobsPage.getPage());
        result.setSize(jobsPage.getSize());
        result.setTotal(jobsPage.getTotal());
        return result;
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
