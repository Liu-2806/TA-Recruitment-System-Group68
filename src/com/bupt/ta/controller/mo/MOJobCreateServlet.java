package com.bupt.ta.controller.mo;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.JobService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * MO 发布岗位 Servlet。
 */
@WebServlet("/mo/jobs/create")
public class MOJobCreateServlet extends BaseServlet {
    private JobService jobService;

    /**
     * 展示发布岗位页面。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/mo/job-create.jsp").forward(request, response);
    }

    /**
     * 处理岗位发布提交。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        Map<String, Object> params = new HashMap<>();
        params.put("title", request.getParameter("title"));
        params.put("courseCode", request.getParameter("courseCode"));
        params.put("courseName", request.getParameter("courseName"));
        params.put("description", request.getParameter("description"));
        params.put("requirements", request.getParameter("requirements"));
        params.put("workloadHours", request.getParameter("workloadHours"));
        params.put("headcount", request.getParameter("headcount"));
        params.put("deadline", request.getParameter("deadline"));
        try {
            jobService.createJob(user.getId(), params);
            response.sendRedirect(request.getContextPath() + "/mo/jobs/my");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.setAttribute("formData", params);
            request.getRequestDispatcher("/WEB-INF/views/mo/job-create.jsp").forward(request, response);
        }
    }
}
