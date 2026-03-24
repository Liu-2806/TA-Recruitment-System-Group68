package com.bupt.ta.controller.ta;

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
 * TA 个人资料查看与编辑 Servlet。
 */
@WebServlet("/ta/profile")
public class TAProfileServlet extends BaseServlet {
    private ProfileService profileService;

    /**
     * 展示 TA 个人资料。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        request.setAttribute("profile", profileService.getTAProfile(user.getId()));
        request.setAttribute("allSkillTags", profileService.listAllSkillTags());
        request.getRequestDispatcher("/WEB-INF/views/ta/profile.jsp").forward(request, response);
    }

    /**
     * 更新 TA 个人资料。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        Map<String, Object> params = new HashMap<>();
        params.put("name", request.getParameter("name"));
        params.put("email", request.getParameter("email"));
        params.put("phone", request.getParameter("phone"));
        params.put("major", request.getParameter("major"));
        params.put("grade", request.getParameter("grade"));
        params.put("intro", request.getParameter("intro"));
        params.put("skillTags", request.getParameterValues("skillTags"));

        try {
            profileService.updateTAProfile(user.getId(), params);
            response.sendRedirect(request.getContextPath() + "/ta/profile");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.setAttribute("profile", params);
            request.setAttribute("allSkillTags", profileService.listAllSkillTags());
            request.getRequestDispatcher("/WEB-INF/views/ta/profile.jsp").forward(request, response);
        }
    }
}
