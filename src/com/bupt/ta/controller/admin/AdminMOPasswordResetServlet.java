package com.bupt.ta.controller.admin;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.service.UserService;
import com.bupt.ta.util.ServiceRegistry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Admin 重置 MO 密码 Servlet。
 */
@WebServlet("/admin/mos/reset-password")
public class AdminMOPasswordResetServlet extends BaseServlet {
    private final UserService userService = ServiceRegistry.userService();

    /**
     * 执行密码重置。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String moUserId = request.getParameter("moUserId");
        String newPassword = request.getParameter("newPassword");
        try {
            userService.resetPasswordByAdmin(moUserId, newPassword);
            response.sendRedirect(request.getContextPath() + "/admin/mos");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
        }
    }
}
