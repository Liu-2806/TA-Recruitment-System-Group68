package com.bupt.ta.controller.mo;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ApplicationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * MO 查看申请人详情 Servlet。
 */
@WebServlet("/mo/applicants/detail")
public class MOApplicantDetailServlet extends BaseServlet {
    private ApplicationService applicationService;

    /**
     * 展示申请详情页面。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        String applicationId = request.getParameter("applicationId");
        request.setAttribute("application", applicationService.getApplicationDetailForMO(applicationId, user.getId()));
        request.getRequestDispatcher("/WEB-INF/views/mo/applicant-detail.jsp").forward(request, response);
    }
}
