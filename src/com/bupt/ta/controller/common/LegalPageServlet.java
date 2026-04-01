package com.bupt.ta.controller.common;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户协议与隐私政策占位页。
 */
@WebServlet("/legal")
public class LegalPageServlet extends BaseServlet {

    /**
     * 展示用户协议与隐私政策占位内容。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        String legalType = "privacy".equalsIgnoreCase(type) ? "privacy" : "agreement";
        request.setAttribute("legalType", legalType);
        request.getRequestDispatcher("/WEB-INF/views/common/legal.jsp").forward(request, response);
    }
}
