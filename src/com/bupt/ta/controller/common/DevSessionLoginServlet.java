package com.bupt.ta.controller.common;

import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;
import com.bupt.ta.util.SessionKeys;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dev/login-as")
public class DevSessionLoginServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String roleParam = request.getParameter("role");
        if (roleParam == null || roleParam.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "role is required.");
            return;
        }

        Role role;
        try {
            role = Role.valueOf(roleParam.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unsupported role: " + roleParam);
            return;
        }

        User user = buildUser(role);
        request.getSession(true).setAttribute(SessionKeys.CURRENT_USER, user);
        request.getSession().setAttribute(SessionKeys.ROLE, user.getRole());

        String redirect = request.getParameter("redirect");
        if (redirect == null || redirect.isBlank()) {
            redirect = defaultRedirect(role);
        }
        response.sendRedirect(request.getContextPath() + redirect);
    }

    private User buildUser(Role role) {
        User user = new User();
        if (role == Role.TA) {
            user.setId("TA001");
            user.setUsername("ta_demo");
            user.setDisplayName("Alex Chen");
            user.setRole(Role.TA);
            return user;
        }
        if (role == Role.MO) {
            user.setId("MO001");
            user.setUsername("mo_demo");
            user.setDisplayName("Prof. Wang");
            user.setRole(Role.MO);
            return user;
        }
        user.setId("ADMIN001");
        user.setUsername("admin_demo");
        user.setDisplayName("Admin Demo");
        user.setRole(Role.ADMIN);
        return user;
    }

    private String defaultRedirect(Role role) {
        if (role == Role.TA) {
            return "/ta/profile";
        }
        if (role == Role.MO) {
            return "/mo/jobs/applicants?jobId=POST001";
        }
        return "/admin/dashboard";
    }
}
