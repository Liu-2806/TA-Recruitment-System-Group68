package com.bupt.ta.controller.common;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一错误页面 Servlet。
 */
@WebServlet("/error")
public class ErrorPageServlet extends BaseServlet {

    /**
     * 展示错误页面。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(request, response);
    }
}
