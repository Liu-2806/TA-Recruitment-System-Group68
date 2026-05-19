package com.bupt.ta.controller.mo;

import com.bupt.ta.config.ServiceRegistry;
import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.JobService;
import com.bupt.ta.util.FlashMessages;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * MO 修改岗位 Servlet。
 */
@WebServlet("/mo/jobs/edit")
public class MOJobEditServlet extends BaseServlet {
    private final JobService jobService = ServiceRegistry.jobService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        String jobId = request.getParameter("jobId");
        if (jobId == null || jobId.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/mo/jobs/my");
            return;
        }
        Map<String, Object> posting;
        try {
            posting = jobService.getJobById(jobId);
        } catch (Exception ex) {
            response.sendRedirect(request.getContextPath() + "/mo/jobs/my");
            return;
        }
        if (!user.getId().equals(String.valueOf(posting.get("moId")))) {
            response.sendRedirect(request.getContextPath() + "/mo/jobs/my");
            return;
        }

        request.setAttribute("editMode", Boolean.TRUE);
        request.setAttribute("editPostingId", jobId);
        request.setAttribute("formData", buildFormData(posting));
        request.getRequestDispatcher("/WEB-INF/views/mo/post-position.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        String jobId = request.getParameter("jobId");
        if (jobId == null || jobId.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/mo/jobs/my");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("title", firstNonBlank(request.getParameter("title"), request.getParameter("courseName")));
        params.put("postingType", request.getParameter("postingType"));
        params.put("courseCode", request.getParameter("courseCode"));
        params.put("courseName", request.getParameter("courseName"));
        params.put("description", request.getParameter("description"));
        params.put("requiredSkills", firstNonBlank(request.getParameter("requiredSkills"), request.getParameter("requirements")));
        params.put("requirements", request.getParameter("requirements"));
        params.put("estimatedWorkloadHours", firstNonBlank(request.getParameter("estimatedWorkloadHours"), request.getParameter("workloadHours")));
        params.put("workloadHours", request.getParameter("workloadHours"));
        params.put("vacancies", firstNonBlank(request.getParameter("vacancies"), request.getParameter("headcount")));
        params.put("headcount", request.getParameter("headcount"));
        params.put("deadline", request.getParameter("deadline"));
        params.put("activityType", request.getParameter("activityType"));
        params.put("activityDate", request.getParameter("activityDate"));
        params.put("activityStartTime", request.getParameter("activityStartTime"));
        params.put("activityEndTime", request.getParameter("activityEndTime"));
        params.put("activityLocation", request.getParameter("activityLocation"));
        params.put("status", request.getParameter("status"));
        try {
            jobService.updateJob(user.getId(), jobId, params);
            FlashMessages.success(request, "The position has been updated. Existing applications were automatically withdrawn.");
            response.sendRedirect(request.getContextPath() + "/mo/jobs/my");
        } catch (Exception ex) {
            request.setAttribute("errorMessage", ex.getMessage());
            request.setAttribute("editMode", Boolean.TRUE);
            request.setAttribute("editPostingId", jobId);
            request.setAttribute("formData", params);
            request.getRequestDispatcher("/WEB-INF/views/mo/post-position.jsp").forward(request, response);
        }
    }

    private Map<String, Object> buildFormData(Map<String, Object> posting) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("postingType", posting.getOrDefault("postingType", "TA"));
        data.put("courseName", posting.getOrDefault("courseName", ""));
        data.put("courseCode", posting.getOrDefault("courseCode", ""));
        data.put("vacancies", String.valueOf(safeInt(posting.get("vacancies"))));
        data.put("deadline", posting.getOrDefault("deadline", ""));
        data.put("description", posting.getOrDefault("description", ""));
        data.put("requiredSkills", joinSkills(posting.get("requiredSkills")));
        data.put("estimatedWorkloadHours", String.valueOf(safeInt(posting.get("estimatedWorkloadHours"))));
        data.put("activityType", posting.getOrDefault("activityType", ""));
        data.put("activityDate", posting.getOrDefault("activityDate", ""));
        data.put("activityStartTime", posting.getOrDefault("activityStartTime", ""));
        data.put("activityEndTime", posting.getOrDefault("activityEndTime", ""));
        data.put("activityLocation", posting.getOrDefault("activityLocation", ""));
        data.put("status", posting.getOrDefault("status", "OPEN"));
        return data;
    }

    private String joinSkills(Object value) {
        if (value instanceof List<?> list) {
            StringBuilder sb = new StringBuilder();
            for (Object item : list) {
                if (item == null) {
                    continue;
                }
                String text = String.valueOf(item).trim();
                if (text.isEmpty()) {
                    continue;
                }
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(text);
            }
            return sb.toString();
        }
        return value == null ? "" : String.valueOf(value);
    }

    private int safeInt(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        try {
            return Integer.parseInt(String.valueOf(value).trim());
        } catch (Exception ex) {
            return 0;
        }
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
}
