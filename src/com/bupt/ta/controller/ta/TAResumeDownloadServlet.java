package com.bupt.ta.controller.ta;

import com.bupt.ta.controller.common.BaseServlet;
import com.bupt.ta.model.User;
import com.bupt.ta.repository.file.TADataRepository;
import com.bupt.ta.util.DataPaths;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@WebServlet("/ta/resume/download")
public class TAResumeDownloadServlet extends BaseServlet {
    private final TADataRepository taDataRepository = new TADataRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = currentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        Map<String, Object> ta = taDataRepository.findByTaId(user.getId());
        if (ta == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "TA profile not found.");
            return;
        }

        Object resumeFileNameObj = ta.get("resumeFileName");
        if (resumeFileNameObj == null || String.valueOf(resumeFileNameObj).trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resume not uploaded.");
            return;
        }

        String resumeFileName = String.valueOf(resumeFileNameObj).trim();
        Path file = DataPaths.resolveDataRoot().resolve("resumes").resolve(resumeFileName);
        if (!Files.exists(file)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resume file not found.");
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + resumeFileName + "\"");
        try (InputStream inputStream = Files.newInputStream(file)) {
            inputStream.transferTo(response.getOutputStream());
        }
    }
}

