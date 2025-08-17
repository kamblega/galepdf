package project.read.service.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_description")
    private String projectDescription;

    @Column(name = "project_domain")
    private String projectDomain;

    @Column(name = "reading_sources")
    private String readingSources;

    @Column(name = "project_level")
    private String projectLevel;

    @ElementCollection
    @CollectionTable(name = "project_second_supervisors",
            joinColumns = @JoinColumn(name = "project_id"))
    private List<String> secondSupervisors;

    @Column(name = "project_status")
    private boolean projectStatus;

    @ElementCollection
    @CollectionTable(name = "project_student_preferences",
            joinColumns = @JoinColumn(name = "project_id"))
    private List<String> studentPreferences;

    @Column(name = "staff_name")
    private String staffName;

    @Column(name = "staff_email")
    private String staffEmail;

    @Column(name = "school")
    private String school;

    @Column(name = "department")
    private String department;

    // Getters and Setters
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
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
}
