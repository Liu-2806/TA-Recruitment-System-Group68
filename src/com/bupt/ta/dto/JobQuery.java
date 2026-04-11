package com.bupt.ta.dto;

/**
 * 岗位查询条件。
 */
public class JobQuery {
    private String keyword;
    private String moFilter;
    private String moId;
    private String ownerId;
    private String major;
    private String department;
    private String moduleType;
    private String responsibilityKeyword;
    private String status;
    private int page = 1;
    private int size = 10;
    private String sortBy;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getMoFilter() {
        return moFilter;
    }

    public void setMoFilter(String moFilter) {
        this.moFilter = moFilter;
    }

    public String getMoId() {
        return moId;
    }

    public void setMoId(String moId) {
        this.moId = moId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getResponsibilityKeyword() {
        return responsibilityKeyword;
    }

    public void setResponsibilityKeyword(String responsibilityKeyword) {
        this.responsibilityKeyword = responsibilityKeyword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
