package com.bupt.ta.controller.mo;

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
    private JobService jobService;

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
        request.setAttribute("jobsPage", jobService.listJobsByMO(user.getId(), query));
        request.setAttribute("query", query);
        request.getRequestDispatcher("/WEB-INF/views/mo/job-list.jsp").forward(request, response);
    }
}
