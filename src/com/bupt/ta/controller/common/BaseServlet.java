package com.bupt.ta.controller.common;

import com.bupt.ta.model.User;
import com.bupt.ta.util.SessionKeys;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet 基类，封装通用方法。
 */
public abstract class BaseServlet extends HttpServlet {

    /**
     * 获取当前登录用户。
     *
     * @param request HTTP 请求
     * @return 当前用户，未登录则返回 null
     */
    protected User currentUser(HttpServletRequest request) {
        Object obj = request.getSession(false) == null ? null : request.getSession(false).getAttribute(SessionKeys.CURRENT_USER);
        return obj instanceof User ? (User) obj : null;
    }
}
