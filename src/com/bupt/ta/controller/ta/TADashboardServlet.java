package com.bupt.ta.controller.ta;

import com.bupt.ta.controller.common.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TA 仪表盘 Servlet。
 */
@WebServlet("/ta/dashboard")
public class TADashboardServlet extends BaseServlet {

    /**
     * 展示 TA 仪表盘页面。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/ta/dashboard.jsp").forward(request, response);
    }
}
