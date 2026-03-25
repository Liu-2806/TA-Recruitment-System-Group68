package com.bupt.ta.filter;

import com.bupt.ta.model.Role;
import com.bupt.ta.model.User;
import com.bupt.ta.util.SessionKeys;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一权限过滤器。
 * <p>
 * 规则：
 * 1. 放行登录、注册和静态资源；
 * 2. 未登录访问受保护资源跳转登录页；
 * 3. 登录后根据角色限制访问对应模块路径。
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length());

        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = (User) request.getSession().getAttribute(SessionKeys.CURRENT_USER);
        if (user == null) {
            response.sendRedirect(contextPath + "/auth/login");
            return;
        }

        if (!isRoleAllowed(path, user.getRole())) {
            request.getRequestDispatcher("/WEB-INF/views/common/403.jsp").forward(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/auth/login")
                || path.startsWith("/auth/logout")
                || path.startsWith("/dev/login-as")
                || path.startsWith("/ta/register")
                || path.startsWith("/assets/")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/error")
                || path.startsWith("/403");
    }

    private boolean isRoleAllowed(String path, Role role) {
        if (path.startsWith("/ta/")) {
            return role == Role.TA;
        }
        if (path.startsWith("/mo/")) {
            return role == Role.MO;
        }
        if (path.startsWith("/admin/")) {
            return role == Role.ADMIN;
        }
        return true;
    }
}
