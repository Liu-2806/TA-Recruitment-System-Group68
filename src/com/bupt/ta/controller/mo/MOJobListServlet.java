package com.bupt.ta.controller.mo;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.dto.JobQuery;
import com.bupt.ta.model.User;
import com.bupt.ta.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * MO 我的岗位列表 Servlet。
 */
@WebServlet("/mo/jobs/my")
public class MOJobListServlet extends BaseServlet {
    private final JobService jobService = ServiceRegistry.jobService();

    /**
     * 展示当前 MO 发布的岗位列表。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        JobQuery query = new JobQuery();
        query.setKeyword(request.getParameter("keyword"));
        query.setStatus(request.getParameter("status"));
        query.setSortBy(request.getParameter("sortBy"));
        query.setPage(parsePositiveInt(request.getParameter("page"), 1));
        query.setSize(parsePositiveInt(request.getParameter("size"), 6));
        request.setAttribute("jobsPage", jobService.listJobsByMO(user.getId(), query));
        request.setAttribute("query", query);
        request.getRequestDispatcher("/WEB-INF/views/mo/postings.jsp").forward(request, response);
    }

    private int parsePositiveInt(String rawValue, int defaultValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(rawValue.trim());
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
