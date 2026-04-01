package com.bupt.ta.controller.admin;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
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
 * Admin 编辑 MO 详情 Servlet。
 */
@WebServlet("/admin/mos/detail")
public class AdminMODetailServlet extends BaseServlet {
    private final UserService userService = ServiceRegistry.userService();

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
        params.put("fullName", firstNonBlank(request.getParameter("fullName"), request.getParameter("name")));
        params.put("name", firstNonBlank(request.getParameter("name"), request.getParameter("fullName")));
        params.put("email", request.getParameter("email"));
        params.put("phone", request.getParameter("phone"));
        params.put("description", request.getParameter("description"));
        params.put("status", request.getParameter("status"));
        try {
            userService.updateMOByAdmin(params);
            response.sendRedirect(request.getContextPath() + "/admin/mos/detail?moUserId=" + request.getParameter("moUserId"));
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.setAttribute("mo", buildMoForError(params));
            request.getRequestDispatcher("/WEB-INF/views/admin/mo-detail.jsp").forward(request, response);
        }
    }

    private User buildMoForError(Map<String, Object> params) {
        String moUserId = trimToNull(params.get("moUserId"));
        User mo = null;
        if (moUserId != null) {
            try {
                mo = userService.getMOById(moUserId);
            } catch (Exception ignored) {
                // Ignore and fall back to request payload only.
            }
        }
        if (mo == null) {
            mo = new User();
            mo.setId(moUserId);
            mo.setMoId(moUserId);
        }

        mo.setFullName(firstNonBlank(trimToNull(params.get("fullName")), trimToNull(params.get("name"))));
        mo.setDisplayName(mo.getFullName());
        mo.setEmail(trimToNull(params.get("email")));
        mo.setPhone(trimToNull(params.get("phone")));
        mo.setDescription(trimToNull(params.get("description")));
        if (trimToNull(params.get("status")) != null) {
            mo.setStatus(String.valueOf(params.get("status")).trim());
        }
        return mo;
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

    private String trimToNull(Object value) {
        if (value == null) {
            return null;
        }
        String text = String.valueOf(value).trim();
        return text.isEmpty() ? null : text;
    }
}
