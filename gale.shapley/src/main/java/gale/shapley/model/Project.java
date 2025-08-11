package gale.shapley.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.List;

import java.util.Map;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String projectName;
    private String projectDescription;
    private String projectDomain;
    private String readingSources;
    private String projectLevel;
    @ElementCollection
    private List<String> secondSupervisors;
    private boolean projectStatus;
    private String staffName;
    private String staffEmail;
    private String school;
    private String department;
    @ElementCollection
    private Map<String, String> requiredSkills;

    @ManyToOne
    private Supervisor supervisor;

    @ManyToMany
    private List<Student> assignedStudents;

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

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }
}
