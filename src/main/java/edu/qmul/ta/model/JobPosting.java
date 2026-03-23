package edu.qmul.ta.model;

import java.util.ArrayList;
import java.util.List;

public class JobPosting {
    private String id = "";
    private String title = "";
    private String module = "";
    private String description = "";
    private final List<String> requiredSkills = new ArrayList<>();
    private String workload = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title.trim();
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module == null ? "" : module.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description.trim();
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public String getWorkload() {
        return workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload == null ? "" : workload.trim();
    }
}
