package com.bupt.ta.controller.mo;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ResumeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@WebServlet("/mo/applicants/resume")
public class MOApplicantResumeDownloadServlet extends BaseServlet {
    private final ResumeService resumeService = ServiceRegistry.resumeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        String applicationId = request.getParameter("applicationId");
        try {
            Map<String, Object> payload = resumeService.openResumeStreamForMO(applicationId, user.getId());
            response.setContentType(String.valueOf(payload.get("contentType")));
            response.setHeader("Content-Disposition", "attachment; filename=\"" + payload.get("fileName") + "\"");
            try (InputStream inputStream = (InputStream) payload.get("stream")) {
                inputStream.transferTo(response.getOutputStream());
            }
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        }
    }
}
