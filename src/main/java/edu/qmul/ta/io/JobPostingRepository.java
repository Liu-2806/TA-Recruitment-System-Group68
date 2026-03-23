package edu.qmul.ta.io;

import edu.qmul.ta.model.JobPosting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JobPostingRepository {

    public List<JobPosting> loadFromCsv(Path csvPath) throws IOException {
        List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
        List<JobPosting> jobs = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\|", -1);
            if (parts.length < 6) {
                throw new IOException("Invalid job row at line " + (i + 1) + ": " + line);
            }
            JobPosting job = new JobPosting();
            job.setId(parts[0]);
            job.setTitle(parts[1]);
            job.setModule(parts[2]);
            job.setDescription(parts[3]);
            for (String skill : parts[4].split(",")) {
                String normalized = skill.trim();
                if (!normalized.isEmpty()) {
                    job.getRequiredSkills().add(normalized);
                }
            }
            job.setWorkload(parts[5]);
            jobs.add(job);
        }
        return jobs;
    }
}
