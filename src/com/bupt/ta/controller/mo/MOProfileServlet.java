package com.bupt.ta.controller.mo;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ProfileService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * MO 个人资料 Servlet。
 */
@WebServlet("/mo/profile")
public class MOProfileServlet extends BaseServlet {
    private ProfileService profileService;

    /**
     * 展示 MO 个人资料页面。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        request.setAttribute("profile", profileService.getMOProfile(user.getId()));
        request.getRequestDispatcher("/WEB-INF/views/mo/profile.jsp").forward(request, response);
    }

    /**
     * 更新 MO 个人资料。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        Map<String, Object> params = new HashMap<>();
        params.put("name", request.getParameter("name"));
        params.put("email", request.getParameter("email"));
        params.put("phone", request.getParameter("phone"));
        params.put("description", request.getParameter("description"));
        try {
            profileService.updateMOProfile(user.getId(), params);
            response.sendRedirect(request.getContextPath() + "/mo/profile");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.setAttribute("profile", params);
            request.getRequestDispatcher("/WEB-INF/views/mo/profile.jsp").forward(request, response);
        }
    }
}
