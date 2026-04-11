package com.bupt.ta.controller.auth;

import com.bupt.ta.controller.common.BaseServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出 Servlet。
 */
@WebServlet("/auth/logout")
public class LogoutServlet extends BaseServlet {

    /**
     * 处理登出请求并清理会话。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/auth/login");
    }

    /**
     * 兼容 GET 方式登出。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }
}
