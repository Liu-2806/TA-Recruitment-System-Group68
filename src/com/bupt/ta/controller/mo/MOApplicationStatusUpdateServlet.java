package com.bupt.ta.controller.mo;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.ApplicationStatus;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ApplicationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * MO 更新申请状态 Servlet。
 */
@WebServlet("/mo/applications/status")
public class MOApplicationStatusUpdateServlet extends BaseServlet {
    private final ApplicationService applicationService = ServiceRegistry.applicationService();

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
            String ctx = request.getContextPath();
            String postingId = request.getParameter("postingId");
            if (postingId != null && !postingId.isBlank()) {
                String q = URLEncoder.encode(postingId.trim(), "UTF-8");
                response.sendRedirect(ctx + "/mo/jobs/applicants?jobId=" + q);
            } else {
                response.sendRedirect(ctx + "/mo/jobs/my");
            }
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        }
    }
}
