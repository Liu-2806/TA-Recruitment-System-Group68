package com.bupt.ta.controller.ta;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ProfileService;
import com.bupt.ta.service.ResumeService;
import com.bupt.ta.util.FlashMessages;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet("/ta/profile/resume")
@MultipartConfig(
    maxFileSize = 5L * 1024L * 1024L,
    maxRequestSize = 6L * 1024L * 1024L
)
public class TAResumeUploadServlet extends BaseServlet {
    private final ResumeService resumeService = ServiceRegistry.resumeService();
    private final ProfileService profileService = ServiceRegistry.profileService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        Part resumeFile = request.getPart("resumeFile");
        try {
            if (resumeFile == null || resumeFile.getSubmittedFileName() == null || resumeFile.getSubmittedFileName().isBlank()) {
                throw new IllegalStateException("Resume file is missing.");
            }
            resumeService.saveOrReplaceTAResume(user.getId(), resumeFile.getSubmittedFileName(), resumeFile.getInputStream());
            FlashMessages.success(request, "Your PDF resume was uploaded, extracted, and linked to your profile.");
            response.sendRedirect(request.getContextPath() + "/ta/profile");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.setAttribute("resumeUploadFailed", Boolean.TRUE);
            request.setAttribute("profile", profileService.getTAProfile(user.getId()));
            request.getRequestDispatcher("/WEB-INF/views/ta/profile.jsp").forward(request, response);
        }
    }
}
