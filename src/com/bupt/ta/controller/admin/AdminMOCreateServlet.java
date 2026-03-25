package com.bupt.ta.controller.admin;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.service.UserService;
import com.bupt.ta.util.ServiceRegistry;

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
    private final UserService userService = ServiceRegistry.userService();

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
        params.put("name", firstNonBlank(request.getParameter("name"), request.getParameter("fullName")));
        params.put("fullName", request.getParameter("fullName"));
        params.put("staffId", request.getParameter("staffId"));
        params.put("email", request.getParameter("email"));
        params.put("department", request.getParameter("department"));
        params.put("phone", request.getParameter("phone"));
        params.put("description", request.getParameter("description"));
        params.put("initialPassword", firstNonBlank(request.getParameter("initialPassword"), request.getParameter("tempPassword")));
        try {
            userService.createMO(params);
            response.sendRedirect(request.getContextPath() + "/admin/mos");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.setAttribute("formData", params);
            request.getRequestDispatcher("/WEB-INF/views/admin/create-mo.jsp").forward(request, response);
        }
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) {
                return value.trim();
            }
        }
        return null;
    }
}
