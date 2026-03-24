package com.bupt.ta.controller.mo;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.service.ResumeService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * MO 下载申请人简历 Servlet。
 */
@WebServlet("/mo/applicants/resume")
public class MOApplicantResumeDownloadServlet extends BaseServlet {
    private ResumeService resumeService;

    /**
     * 处理简历下载请求。
     * <p>
     * 当前仅保留接口骨架，二进制输出逻辑由后续实现补齐。
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = currentUser(request);
        String applicationId = request.getParameter("applicationId");
        resumeService.openResumeStreamForMO(applicationId, user.getId());
        response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "简历下载输出逻辑待实现");
    }
}
