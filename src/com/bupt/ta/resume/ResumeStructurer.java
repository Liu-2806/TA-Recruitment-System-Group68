package com.bupt.ta.resume;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeStructurer {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+");
    private static final Pattern PHONE_PATTERN = Pattern.compile("(\\+?\\d[\\d\\- ]{7,}\\d)");
    private static final List<String> KNOWN_SKILLS = List.of(
        "java", "python", "sql", "excel", "communication", "teaching",
        "marking", "data analysis", "machine learning", "html", "css",
        "javascript", "servlet", "jsp", "git", "testing", "oop"
    );

    public Map<String, Object> structure(String rawText) {
        List<String> lines = normalizeLines(rawText);
        Map<String, Object> extracted = new LinkedHashMap<>();
        extracted.put("name", findLikelyName(lines));
        extracted.put("email", findFirstMatch(rawText, EMAIL_PATTERN));
        extracted.put("phone", findFirstMatch(rawText, PHONE_PATTERN));
        extracted.put("education", findEducation(lines));
        extracted.put("skills", findSkills(rawText));
        extracted.put("experienceHighlights", findExperience(lines));
        extracted.put("rawText", rawText);
        return extracted;
    }

    private List<String> normalizeLines(String rawText) {
        List<String> lines = new ArrayList<>();
        for (String item : rawText.replace("\r", "").split("\n")) {
            String line = item.trim();
            if (!line.isEmpty()) {
                lines.add(line);
            }
        }
        return lines;
    }

    private String findLikelyName(List<String> lines) {
        for (String line : lines) {
            if (line.length() > 2 && line.length() < 60 && !line.contains("@") && !line.matches(".*\\d.*")) {
                return line;
            }
        }
        return "";
    }

    private String findFirstMatch(String rawText, Pattern pattern) {
        Matcher matcher = pattern.matcher(rawText);
        if (!matcher.find()) {
            return "";
        }
        return matcher.groupCount() >= 1 && matcher.group(1) != null ? matcher.group(1) : matcher.group();
    }

    private String findEducation(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String lower = line.toLowerCase(Locale.ROOT);
            if (lower.equals("education") && i + 1 < lines.size()) {
                return lines.get(i + 1);
            }
            if (lower.contains("university") || lower.contains("college") || lower.contains("bsc") || lower.contains("msc")) {
                return line;
            }
        }
        return "";
    }

    private List<String> findSkills(String rawText) {
        String lower = rawText.toLowerCase(Locale.ROOT);
        Set<String> detected = new LinkedHashSet<>();
        for (String skill : KNOWN_SKILLS) {
            if (lower.contains(skill)) {
                detected.add(toDisplayName(skill));
            }
        }
        return new ArrayList<>(detected);
    }

    private List<String> findExperience(List<String> lines) {
        List<String> experience = new ArrayList<>();
        for (String line : lines) {
            String lower = line.toLowerCase(Locale.ROOT);
            if (lower.equals("experience") || lower.equals("skills") || lower.equals("education")) {
                continue;
            }
            long commaCount = line.chars().filter(ch -> ch == ',').count();
            if (commaCount >= 4) {
                continue;
            }
            if (lower.contains("assistant") || lower.contains("intern")
                || lower.contains("project") || lower.contains("teach")
                || lower.contains("research") || lower.contains("tutor")) {
                experience.add(line);
            }
            if (experience.size() == 4) {
                break;
            }
        }
        return experience;
    }

    private String toDisplayName(String skill) {
        if ("oop".equals(skill)) {
            return "Object-Oriented Programming";
        }
        String[] parts = skill.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            String part = parts[i];
            sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return sb.toString();
    }
}
