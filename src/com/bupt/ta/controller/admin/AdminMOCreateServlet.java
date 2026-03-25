package com.bupt.ta.controller.admin;

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
 * Admin 创建 MO 账号 Servlet。
 */
@WebServlet("/admin/mos/create")
public class AdminMOCreateServlet extends BaseServlet {
    private UserService userService;

    /**
     * 展示创建 MO 页面。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/admin/create-mo.jsp").forward(request, response);
    }

    /**
     * 处理创建 MO 提交。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("username", request.getParameter("username"));
        params.put("name", request.getParameter("name"));
        params.put("email", request.getParameter("email"));
        params.put("tempPassword", request.getParameter("tempPassword"));
        try {
            userService.createMO(params);
            response.sendRedirect(request.getContextPath() + "/admin/mos");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.setAttribute("formData", params);
            request.getRequestDispatcher("/WEB-INF/views/admin/create-mo.jsp").forward(request, response);
        }
    }
}
