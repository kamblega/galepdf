package gale.shapley.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class Project {

    @JsonProperty("projectId")
    private Long id;
    private String projectName;
    private String projectDescription;
    private String projectDomain;
    private String readingSources;
    private String projectLevel;
    private List<String> secondSupervisors;
    private boolean projectStatus;
    private String staffName;
    private String staffEmail;
    private String school;
    private String department;
    private Map<String, String> requiredSkills;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectDomain() {
        return projectDomain;
    }

    public void setProjectDomain(String projectDomain) {
        this.projectDomain = projectDomain;
    }

    public String getReadingSources() {
        return readingSources;
    }

    public void setReadingSources(String readingSources) {
        this.readingSources = readingSources;
    }

    public String getProjectLevel() {
        return projectLevel;
    }

    public void setProjectLevel(String projectLevel) {
        this.projectLevel = projectLevel;
    }

    public List<String> getSecondSupervisors() {
        return secondSupervisors;
    }

    public void setSecondSupervisors(List<String> secondSupervisors) {
        this.secondSupervisors = secondSupervisors;
    }

    public boolean isProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(boolean projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Map<String, String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(Map<String, String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
}
