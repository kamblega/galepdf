package gale.shapley.kafka.payload;

public class MatchingJobPayload {
    private String studentsFilePath;
    private String projectsFilePath;

    public MatchingJobPayload() {
    }

    public MatchingJobPayload(String studentsFilePath, String projectsFilePath) {
        this.studentsFilePath = studentsFilePath;
        this.projectsFilePath = projectsFilePath;
    }

    public String getStudentsFilePath() {
        return studentsFilePath;
    }

    public void setStudentsFilePath(String studentsFilePath) {
        this.studentsFilePath = studentsFilePath;
    }

    public String getProjectsFilePath() {
        return projectsFilePath;
    }

    public void setProjectsFilePath(String projectsFilePath) {
        this.projectsFilePath = projectsFilePath;
    }

    @Override
    public String toString() {
        return "MatchingJobPayload{" +
                "studentsFilePath='" + studentsFilePath + '\'' +
                ", projectsFilePath='" + projectsFilePath + '\'' +
                '}';
    }
}
