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
 * Admin MO 列表 Servlet。
 */
@WebServlet("/admin/mos")
public class AdminMOListServlet extends BaseServlet {
    private UserService userService;

    /**
     * 查询并展示所有 MO。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> query = new HashMap<>();
        query.put("keyword", request.getParameter("keyword"));
        query.put("status", request.getParameter("status"));
        query.put("sortBy", request.getParameter("sortBy"));
        request.setAttribute("mosPage", userService.searchMOs(query));
        request.setAttribute("query", query);
        request.getRequestDispatcher("/WEB-INF/views/admin/all-mos.jsp").forward(request, response);
    }
}
