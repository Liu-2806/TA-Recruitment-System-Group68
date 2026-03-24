package com.bupt.ta.controller.admin;

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
    private JobService jobService;

    /**
     * 查询并展示岗位列表。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JobQuery query = new JobQuery();
        query.setKeyword(request.getParameter("keyword"));
        query.setStatus(request.getParameter("status"));
        query.setSortBy(request.getParameter("sortBy"));
        request.setAttribute("jobsPage", jobService.searchAllJobsForAdmin(query));
        request.setAttribute("query", query);
        request.getRequestDispatcher("/WEB-INF/views/admin/job-list.jsp").forward(request, response);
    }
}
