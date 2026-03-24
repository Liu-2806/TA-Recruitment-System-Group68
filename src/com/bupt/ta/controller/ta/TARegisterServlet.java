package com.bupt.ta.controller.ta;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * TA 注册 Servlet。
 */
@WebServlet("/ta/register")
public class TARegisterServlet extends BaseServlet {
    private UserService userService;

    /**
     * 展示 TA 注册页面。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/ta/register.jsp").forward(request, response);
    }

    /**
     * 处理 TA 注册提交。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("username", request.getParameter("username"));
        params.put("password", request.getParameter("password"));
        params.put("confirmPassword", request.getParameter("confirmPassword"));
        params.put("email", request.getParameter("email"));
        params.put("name", request.getParameter("name"));
        params.put("studentId", request.getParameter("studentId"));

        try {
            userService.registerTA(params);
            response.sendRedirect(request.getContextPath() + "/auth/login");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.setAttribute("formData", params);
            request.getRequestDispatcher("/WEB-INF/views/ta/register.jsp").forward(request, response);
        }
    }
}
