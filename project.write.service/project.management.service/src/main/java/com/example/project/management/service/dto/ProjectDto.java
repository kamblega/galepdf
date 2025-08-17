package com.example.project.management.service.dto;

import java.util.List;

public class ProjectDto {
    private String projectName;
    private String projectDescription;
    private String projectDomain;
    private String readingSources;
    private String projectLevel;
    private List<String> secondSupervisors;
    private boolean projectStatus;
    private List<String> studentPreferences;
    private String staffName;
    private String staffEmail;
    private String school;
    private String department;
    private List<String> preference;

    // Getters and Setters
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getProjectDescription() { return projectDescription; }
    public void setProjectDescription(String projectDescription) { this.projectDescription = projectDescription; }
    public String getProjectDomain() { return projectDomain; }
    public void setProjectDomain(String projectDomain) { this.projectDomain = projectDomain; }
    public String getReadingSources() { return readingSources; }
    public void setReadingSources(String readingSources) { this.readingSources = readingSources; }
    public String getProjectLevel() { return projectLevel; }
    public void setProjectLevel(String projectLevel) { this.projectLevel = projectLevel; }
    public List<String> getSecondSupervisors() { return secondSupervisors; }
    public void setSecondSupervisors(List<String> secondSupervisors) { this.secondSupervisors = secondSupervisors; }
    public boolean isProjectStatus() { return projectStatus; }
    public void setProjectStatus(boolean projectStatus) { this.projectStatus = projectStatus; }
    public List<String> getStudentPreferences() { return studentPreferences; }
    public void setStudentPreferences(List<String> studentPreferences) { this.studentPreferences = studentPreferences; }
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public String getStaffEmail() { return staffEmail; }
    public void setStaffEmail(String staffEmail) { this.staffEmail = staffEmail; }
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public List<String> getPreference() { return preference; }
    public void setPreference(List<String> preference) { this.preference = preference; }
}
