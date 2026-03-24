package com.bupt.ta.controller.ta;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.dto.ApplicationQuery;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ApplicationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TA 我的申请列表 Servlet。
 */
@WebServlet("/ta/applications/my")
public class TAMyApplicationsServlet extends BaseServlet {
    private ApplicationService applicationService;

    /**
     * 展示我的申请列表。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = currentUser(request);
        ApplicationQuery query = new ApplicationQuery();
        query.setStatus(request.getParameter("status"));
        query.setSortBy(request.getParameter("sortBy"));
        request.setAttribute("applicationsPage", applicationService.listApplicationsByTA(user.getId(), query));
        request.setAttribute("query", query);
        request.getRequestDispatcher("/WEB-INF/views/ta/my-applications.jsp").forward(request, response);
    }
}
