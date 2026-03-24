package com.bupt.ta.controller.ta;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TA 岗位详情 Servlet。
 */
@WebServlet("/ta/jobs/detail")
public class TAJobDetailServlet extends BaseServlet {
    private JobService jobService;

    /**
     * 展示岗位详情。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jobId = request.getParameter("jobId");
        request.setAttribute("job", jobService.getJobById(jobId));
        request.getRequestDispatcher("/WEB-INF/views/ta/job-detail.jsp").forward(request, response);
    }
}
