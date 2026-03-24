package com.bupt.ta.controller.auth;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;
import com.bupt.ta.service.AuthService;
import com.bupt.ta.util.SessionKeys;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录 Servlet。
 */
@WebServlet("/auth/login")
public class LoginServlet extends BaseServlet {
    private AuthService authService;

    /**
     * 展示登录页面。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    /**
     * 处理登录提交。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String roleParam = request.getParameter("role");
        Role role = Role.valueOf(roleParam);

        try {
            User user = authService.authenticate(username, password, role);
            request.getSession().setAttribute(SessionKeys.CURRENT_USER, user);
            request.getSession().setAttribute(SessionKeys.ROLE, user.getRole());
            response.sendRedirect(request.getContextPath() + homePath(user.getRole()));
        } catch (Exception ex) {
            request.setAttribute("errorMessage", "用户名、密码或角色不正确");
            request.setAttribute("formData", request.getParameterMap());
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }

    private String homePath(Role role) {
        if (role == Role.TA) {
            return "/ta/dashboard";
        }
        if (role == Role.MO) {
            return "/mo/dashboard";
        }
        return "/admin/dashboard";
    }
}
