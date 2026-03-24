package com.bupt.ta.controller.ta;

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

/**
 * TA 简历上传/替换 Servlet。
 */
@WebServlet("/ta/profile/resume")
@MultipartConfig
public class TAResumeUploadServlet extends BaseServlet {
    private ResumeService resumeService;

    /**
     * 处理简历上传请求。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
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
