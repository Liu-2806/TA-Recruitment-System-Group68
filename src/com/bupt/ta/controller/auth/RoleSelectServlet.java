package com.bupt.ta.controller.auth;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;
import com.bupt.ta.util.RoleUtils;
import com.bupt.ta.util.SessionKeys;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 角色选择 Servlet（可选流程）。
 */
@WebServlet("/auth/role-select")
public class RoleSelectServlet extends BaseServlet {

    /**
     * 展示角色选择页面。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/auth/role-select.jsp").forward(request, response);
    }

    /**
     * 提交角色选择并更新登录会话角色。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        try {
            Role role = RoleUtils.parseRole(request.getParameter("role"));
            user.setRole(role);
            request.getSession().setAttribute(SessionKeys.CURRENT_USER, user);
            request.getSession().setAttribute(SessionKeys.ROLE, role);
            response.sendRedirect(request.getContextPath() + homePath(role));
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/auth/role-select.jsp").forward(request, response);
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
