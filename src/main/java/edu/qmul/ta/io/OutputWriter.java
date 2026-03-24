package edu.qmul.ta.io;

import edu.qmul.ta.model.MatchResult;
import edu.qmul.ta.model.ResumeProfile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class OutputWriter {

    public void writeProfile(Path outputPath, ResumeProfile profile) throws IOException {
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, toProfileJson(profile), StandardCharsets.UTF_8);
    }

    public void writeMatches(Path outputPath, List<MatchResult> matches) throws IOException {
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, toMatchesJson(matches), StandardCharsets.UTF_8);
    }

    private String toProfileJson(ResumeProfile profile) {
        return "{\n"
            + "  \"name\": \"" + escape(profile.getName()) + "\",\n"
            + "  \"email\": \"" + escape(profile.getEmail()) + "\",\n"
            + "  \"phone\": \"" + escape(profile.getPhone()) + "\",\n"
            + "  \"education\": \"" + escape(profile.getEducation()) + "\",\n"
            + "  \"skills\": " + toJsonArray(profile.getSkills()) + ",\n"
            + "  \"experienceHighlights\": " + toJsonArray(profile.getExperienceHighlights()) + "\n"
            + "}\n";
    }

    private String toMatchesJson(List<MatchResult> matches) {
        String payload = matches.stream()
            .map(match -> "    {\n"
                + "      \"jobId\": \"" + escape(match.getJobPosting().getId()) + "\",\n"
                + "      \"title\": \"" + escape(match.getJobPosting().getTitle()) + "\",\n"
                + "      \"module\": \"" + escape(match.getJobPosting().getModule()) + "\",\n"
                + "      \"score\": " + match.getScore() + ",\n"
                + "      \"matchingMethod\": \"" + escape(match.getMatchingMethod()) + "\",\n"
                + "      \"explanation\": \"" + escape(match.getExplanation()) + "\"\n"
                + "    }")
            .collect(Collectors.joining(",\n"));
        return "{\n  \"matches\": [\n" + payload + "\n  ]\n}\n";
    }

    private String toJsonArray(List<String> items) {
        return items.stream()
            .map(item -> "\"" + escape(item) + "\"")
            .collect(Collectors.joining(", ", "[", "]"));
    }

    private String escape(String input) {
        return input
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\r", " ")
            .replace("\n", " ");
    }
}
