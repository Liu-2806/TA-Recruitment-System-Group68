package com.bupt.ta.service.impl;

import com.bupt.ta.repository.file.ApplicationDataRepository;
import com.bupt.ta.repository.file.PostingDataRepository;
import com.bupt.ta.repository.file.TADataRepository;
import com.bupt.ta.resume.PdfResumeExtractor;
import com.bupt.ta.resume.ResumeStructurer;
import com.bupt.ta.service.ResumeService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResumeServiceImpl implements ResumeService {
    private static final long MAX_FILE_SIZE = 5L * 1024L * 1024L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final TADataRepository taDataRepository;
    private final ApplicationDataRepository applicationDataRepository;
    private final PostingDataRepository postingDataRepository;
    private final PdfResumeExtractor pdfResumeExtractor;
    private final ResumeStructurer resumeStructurer;

    public ResumeServiceImpl(
        TADataRepository taDataRepository,
        ApplicationDataRepository applicationDataRepository,
        PostingDataRepository postingDataRepository,
        PdfResumeExtractor pdfResumeExtractor,
        ResumeStructurer resumeStructurer
    ) {
        this.taDataRepository = taDataRepository;
        this.applicationDataRepository = applicationDataRepository;
        this.postingDataRepository = postingDataRepository;
        this.pdfResumeExtractor = pdfResumeExtractor;
        this.resumeStructurer = resumeStructurer;
    }

    @Override
    public Map<String, Object> saveOrReplaceTAResume(String taUserId, String fileName, InputStream content) {
        if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalStateException("Only PDF resumes are allowed.");
        }
        byte[] bytes;
        try {
            bytes = content.readAllBytes();
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read the uploaded resume.", ex);
        }
        if (bytes.length == 0) {
            throw new IllegalStateException("Uploaded resume file is empty.");
        }
        if (bytes.length > MAX_FILE_SIZE) {
            throw new IllegalStateException("Resume file exceeds the 5MB limit.");
        }

        Map<String, Object> ta = taDataRepository.findByTaId(taUserId);
        if (ta == null) {
            throw new IllegalStateException("TA profile not found: " + taUserId);
        }

        Path savedFile = savePdfFile(taUserId, fileName, bytes);
        String rawText = pdfResumeExtractor.extractText(savedFile.toFile());
        Map<String, Object> extractedResume = resumeStructurer.structure(rawText);

        ta.put("resumeFileName", savedFile.getFileName().toString());
        ta.put("resumeUploadedAt", LocalDateTime.now().format(FORMATTER));
        ta.put("extractedResume", extractedResume);
        taDataRepository.save(ta);

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("resumeFileName", savedFile.getFileName().toString());
        metadata.put("resumeUploadedAt", ta.get("resumeUploadedAt"));
        metadata.put("extractedResume", extractedResume);
        return metadata;
    }

    @Override
    public Map<String, Object> openResumeStreamForMO(String applicationId, String moUserId) {
        Map<String, Object> application = applicationDataRepository.findByApplicationId(applicationId);
        if (application == null) {
            throw new IllegalStateException("Application not found: " + applicationId);
        }
        Map<String, Object> job = postingDataRepository.findByPostingId(String.valueOf(application.get("postingId")));
        if (job == null) {
            throw new IllegalStateException("Job posting not found for application: " + applicationId);
        }
        if (!moUserId.equals(String.valueOf(job.get("moId")))) {
            throw new IllegalStateException("You do not have permission to access this resume.");
        }
        Map<String, Object> ta = taDataRepository.findByTaId(String.valueOf(application.get("taId")));
        if (ta == null || ta.get("resumeFileName") == null) {
            throw new IllegalStateException("Resume metadata not found for the applicant.");
        }
        Path file = Paths.get("data", "resumes", String.valueOf(ta.get("resumeFileName")));
        try {
            byte[] bytes = Files.readAllBytes(file);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("fileName", ta.get("resumeFileName"));
            result.put("contentType", "application/pdf");
            result.put("stream", new ByteArrayInputStream(bytes));
            return result;
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to open stored resume file.", ex);
        }
    }

    private Path savePdfFile(String taUserId, String originalFileName, byte[] bytes) {
        String safeFileName = originalFileName.replaceAll("[^A-Za-z0-9._-]", "_");
        String finalName = taUserId + "_" + System.currentTimeMillis() + "_" + safeFileName;
        Path target = Paths.get("data", "resumes", finalName);
        try {
            Files.createDirectories(target.getParent());
            Files.write(target, bytes);
            return target;
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to store resume file.", ex);
        }
    }
}
