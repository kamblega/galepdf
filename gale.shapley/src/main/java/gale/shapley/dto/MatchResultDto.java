package gale.shapley.dto;

public class MatchResultDto {
    private String studentName;
    private String studentEmail;
    private String projectName;
    private String staffName;

    public MatchResultDto(String studentName, String studentEmail, String projectName, String staffName) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.projectName = projectName;
        this.staffName = staffName;
    }

    // Getters and Setters
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
