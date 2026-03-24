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
 * Admin 编辑 MO 详情 Servlet。
 */
@WebServlet("/admin/mos/detail")
public class AdminMODetailServlet extends BaseServlet {
    private UserService userService;

    /**
     * 展示 MO 详情页面。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String moUserId = request.getParameter("moUserId");
        request.setAttribute("mo", userService.getMOById(moUserId));
        request.getRequestDispatcher("/WEB-INF/views/admin/mo-detail.jsp").forward(request, response);
    }

    /**
     * 更新 MO 详情。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("moUserId", request.getParameter("moUserId"));
        params.put("name", request.getParameter("name"));
        params.put("email", request.getParameter("email"));
        params.put("phone", request.getParameter("phone"));
        params.put("description", request.getParameter("description"));
        params.put("status", request.getParameter("status"));
        try {
            userService.updateMOByAdmin(params);
            response.sendRedirect(request.getContextPath() + "/admin/mos/detail?moUserId=" + request.getParameter("moUserId"));
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.setAttribute("mo", params);
            request.getRequestDispatcher("/WEB-INF/views/admin/mo-detail.jsp").forward(request, response);
        }
    }
}
