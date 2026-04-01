package com.bupt.ta.controller.mo;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.ApplicationStatus;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ApplicationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * MO 更新申请状态 Servlet。
 */
@WebServlet("/mo/applications/status")
public class MOApplicationStatusUpdateServlet extends BaseServlet {
    private ApplicationService applicationService;

    /**
     * 处理录取/拒绝状态更新。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        String applicationId = request.getParameter("applicationId");
        String newStatus = request.getParameter("newStatus");
        String comment = request.getParameter("comment");
        try {
            applicationService.updateStatusByMO(applicationId, user.getId(), ApplicationStatus.valueOf(newStatus), comment);
            response.sendRedirect(request.getContextPath() + "/mo/jobs/my");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    }
}
