package com.bupt.ta.controller.ta;

import com.bupt.ta.config.ServiceRegistry;
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

@WebServlet("/ta/profile")
public class TAProfileServlet extends BaseServlet {
    private final ProfileService profileService = ServiceRegistry.profileService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        request.setAttribute("profile", profileService.getTAProfile(user.getId()));
        request.setAttribute("allSkillTags", profileService.listAllSkillTags());
        request.getRequestDispatcher("/WEB-INF/views/ta/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
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
            response.sendRedirect(request.getContextPath() + "/ta/profile?saved=1");
        } catch (Exception ex) {
            Map<String, Object> profile = new HashMap<>(profileService.getTAProfile(user.getId()));
            profile.put("fullName", params.get("name"));
            profile.put("email", params.get("email"));
            profile.put("phone", params.get("phone"));
            profile.put("majorProgram", params.get("major"));
            profile.put("academicYear", params.get("grade"));
            profile.put("intro", params.get("intro"));
            Object skillTags = params.get("skillTags");
            if (skillTags instanceof String[] tags) {
                profile.put("skills", java.util.List.of(tags));
            }
            request.setAttribute("errorMessage", ex.getMessage());
            request.setAttribute("profile", profile);
            request.setAttribute("allSkillTags", profileService.listAllSkillTags());
            request.getRequestDispatcher("/WEB-INF/views/ta/profile.jsp").forward(request, response);
        }
    }
}
