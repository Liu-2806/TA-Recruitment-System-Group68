package com.bupt.ta.controller.ta;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ResumeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet("/ta/profile/resume")
@MultipartConfig
public class TAResumeUploadServlet extends BaseServlet {
    private final ResumeService resumeService = ServiceRegistry.resumeService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        Part resumeFile = request.getPart("resumeFile");
        try {
            resumeService.saveOrReplaceTAResume(user.getId(), resumeFile.getSubmittedFileName(), resumeFile.getInputStream());
            response.sendRedirect(request.getContextPath() + "/ta/profile");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/ta/profile.jsp").forward(request, response);
        }
    }
}
