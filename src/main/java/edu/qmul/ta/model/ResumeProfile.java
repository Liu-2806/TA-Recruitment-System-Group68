package edu.qmul.ta.model;

import java.util.ArrayList;
import java.util.List;

public class ResumeProfile {
    private String name = "";
    private String email = "";
    private String phone = "";
    private String education = "";
    private final List<String> skills = new ArrayList<>();
    private final List<String> experienceHighlights = new ArrayList<>();
    private String rawText = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? "" : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? "" : phone.trim();
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education == null ? "" : education.trim();
    }

    public List<String> getSkills() {
        return skills;
    }

    public List<String> getExperienceHighlights() {
        return experienceHighlights;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText == null ? "" : rawText.trim();
    }
}
