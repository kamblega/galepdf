package gale.shapley.service;

import gale.shapley.model.Project;
import gale.shapley.model.Student;
import gale.shapley.repository.ProjectRepository;
import gale.shapley.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GaleShapleyService {

    private final ProjectRepository projectRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public GaleShapleyService(ProjectRepository projectRepository, StudentRepository studentRepository) {
        this.projectRepository = projectRepository;
        this.studentRepository = studentRepository;
    }

    public Map<Student, Project> match() {
        Map<Long, Map<Long, Double>> aggregatedScores = calculateScores();
        List<Student> students = studentRepository.findAll();
        List<Project> projects = projectRepository.findAll();

        // Debug: Print scores for verification
        System.out.println("=== STUDENT-PROJECT SCORES ===");
        for (Student student : students) {
            System.out.println("Student: " + student.getName());
            for (Project project : projects) {
                double score = aggregatedScores.get(student.getId()).get(project.getId());
                System.out.println("  -> Project " + project.getProjectName() + ": " + score);
            }
            System.out.println();
        }

        Map<Student, Project> matching = new HashMap<>();
        Map<Project, Student> projectAssignments = new HashMap<>();

        // Track which students are free
        Queue<Student> freeStudents = new LinkedList<>(students);

        // Track the next project each student will propose to
        Map<Student, Integer> nextProjectIndex = new HashMap<>();
        for (Student student : students) {
            nextProjectIndex.put(student, 0);
        }

        int iterations = 0;
        int maxIterations = students.size() * projects.size() * 2; // Safety limit

        while (!freeStudents.isEmpty() && iterations < maxIterations) {
            iterations++;
            Student student = freeStudents.poll();

            System.out.println("Iteration " + iterations + ": Processing student " + student.getName());

            // Get student's preference list (sorted by score - highest first)
            List<Project> studentPreferences = projects.stream()
                    .sorted((p1, p2) -> Double.compare(
                            aggregatedScores.get(student.getId()).get(p2.getId()),
                            aggregatedScores.get(student.getId()).get(p1.getId())))
                    .collect(Collectors.toList());

            int currentIndex = nextProjectIndex.get(student);

            // If student has exhausted all preferences, they remain unmatched
            if (currentIndex >= studentPreferences.size()) {
                System.out.println("  Student " + student.getName() + " has exhausted all preferences");
                continue;
            }

            Project project = studentPreferences.get(currentIndex);
            double studentScore = aggregatedScores.get(student.getId()).get(project.getId());

            System.out.println("  Student " + student.getName() + " proposes to project " +
                    project.getProjectName() + " (score: " + studentScore + ")");

            nextProjectIndex.put(student, currentIndex + 1);

            if (!projectAssignments.containsKey(project)) {
                // Project is free, assign student to it
                System.out.println("    Project is free - assigning");
                assignStudentToProject(student, project, matching, projectAssignments);
            } else {
                // Project is already assigned, check if current student is better
                Student currentAssignedStudent = projectAssignments.get(project);
                double currentStudentScore = aggregatedScores.get(currentAssignedStudent.getId()).get(project.getId());

                System.out.println("    Project assigned to " + currentAssignedStudent.getName() +
                        " (score: " + currentStudentScore + ")");

                if (studentScore > currentStudentScore) {
                    // New student is better, reassign
                    System.out.println("    New student is better - reassigning");
                    unassignStudentFromProject(currentAssignedStudent, project, matching, projectAssignments);
                    assignStudentToProject(student, project, matching, projectAssignments);
                    freeStudents.offer(currentAssignedStudent); // Previously assigned student becomes free
                } else {
                    // Current student is not better, they remain free and will try next preference
                    System.out.println("    Current student is better - new student remains free");
                    freeStudents.offer(student);
                }
            }

            // Print current state
            System.out.println("  Current assignments:");
            for (Map.Entry<Student, Project> entry : matching.entrySet()) {
                System.out.println("    " + entry.getKey().getName() + " -> " + entry.getValue().getProjectName());
            }
            System.out.println();
        }

        if (iterations >= maxIterations) {
            System.out.println("WARNING: Maximum iterations reached. Possible infinite loop.");
        }

        System.out.println("=== FINAL MATCHING ===");
        for (Map.Entry<Student, Project> entry : matching.entrySet()) {
            System.out.println(entry.getKey().getName() + " -> " + entry.getValue().getProjectName());
        }

        // Check for unmatched students
        for (Student student : students) {
            if (!matching.containsKey(student)) {
                System.out.println("UNMATCHED: " + student.getName());
            }
        }

        return matching;
    }

    private void assignStudentToProject(Student student, Project project, Map<Student, Project> matching, Map<Project, Student> projectAssignments) {
        matching.put(student, project);
        projectAssignments.put(project, student);
    }

    private void unassignStudentFromProject(Student student, Project project, Map<Student, Project> matching, Map<Project, Student> projectAssignments) {
        matching.remove(student);
        projectAssignments.remove(project);
    }

    private int getSkillScore(String skillLevel) {
        if (skillLevel == null) {
            return 0;
        }
        return switch (skillLevel.toLowerCase()) {
            case "high" -> 3;
            case "medium" -> 2;
            case "low" -> 1;
            default -> 0;
        };
    }

    private Map<Long, Map<Long, Double>> calculateScores() {
        List<Student> students = studentRepository.findAll();
        List<Project> projects = projectRepository.findAll();
        Map<Long, Map<Long, Double>> aggregatedScores = new HashMap<>();

        for (Student student : students) {
            aggregatedScores.put(student.getId(), new HashMap<>());
            for (Project project : projects) {
                double score = 0;
                Map<String, String> studentSkills = student.getSkills();
                Map<String, String> projectSkills = project.getRequiredSkills();

                for (Map.Entry<String, String> projectSkill : projectSkills.entrySet()) {
                    String skillName = projectSkill.getKey();
                    String projectSkillLevel = projectSkill.getValue();
                    String studentSkillLevel = studentSkills.getOrDefault(skillName, "none");

                    int studentSkillScore = getSkillScore(studentSkillLevel);
                    int projectSkillScore = getSkillScore(projectSkillLevel);
                    score += (double) studentSkillScore * projectSkillScore;
                }
                aggregatedScores.get(student.getId()).put(project.getId(), score);
            }
        }
        return aggregatedScores;
    }
}



/*
public class GaleShapleyService {

    private final ProjectRepository projectRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public GaleShapleyService(ProjectRepository projectRepository, StudentRepository studentRepository) {
        this.projectRepository = projectRepository;
        this.studentRepository = studentRepository;
    }

    public Map<Student, Project> match() {
        Map<Long, Map<Long, Double>> aggregatedScores = calculateScores();
        List<Student> students = studentRepository.findAll();
        List<Project> projects = projectRepository.findAll();

        Map<Student, Project> matching = new HashMap<>();
        Map<Project, List<Student>> projectAssignments = new HashMap<>();
        for (Project project : projects) {
            projectAssignments.put(project, new ArrayList<>());
        }

        List<Student> freeStudents = new ArrayList<>(students);

        while (!freeStudents.isEmpty()) {
            Student student = freeStudents.get(0);
            freeStudents.remove(0);

            List<Project> sortedProjects = projects.stream()
                    .sorted((p1, p2) -> Double.compare(
                            aggregatedScores.get(student.getId()).get(p2.getId()),
                            aggregatedScores.get(student.getId()).get(p1.getId())))
                    .collect(Collectors.toList());

            for (Project project : sortedProjects) {
                if (projectAssignments.get(project).size().isEmpty()) {
                    assignStudentToProject(student, project, matching, projectAssignments);
                    break;
                } else {
                    Student worstStudent = getWorstStudent(project, projectAssignments.get(project), aggregatedScores);
                    if (isBetterThan(student, worstStudent, project, aggregatedScores)) {
                        unassignStudentFromProject(worstStudent, project, matching, projectAssignments);
                        assignStudentToProject(student, project, matching, projectAssignments);
                        freeStudents.add(worstStudent);
                        break;
                    }
                }
            }
        }

        return matching;
    }

    private void assignStudentToProject(Student student, Project project, Map<Student, Project> matching, Map<Project, List<Student>> projectAssignments) {
        matching.put(student, project);
        projectAssignments.get(project).add(student);
    }

    private void unassignStudentFromProject(Student student, Project project, Map<Student, Project> matching, Map<Project, List<Student>> projectAssignments) {
        matching.remove(student);
        projectAssignments.get(project).remove(student);
    }

    private Student getWorstStudent(Project project, List<Student> assignedStudents, Map<Long, Map<Long, Double>> aggregatedScores) {
        Student worstStudent = null;
        double worstScore = Double.MAX_VALUE;

        for (Student student : assignedStudents) {
            double score = aggregatedScores.get(student.getId()).get(project.getId());
            if (score < worstScore) {
                worstScore = score;
                worstStudent = student;
            }
        }
        return worstStudent;
    }

    private boolean isBetterThan(Student newStudent, Student oldStudent, Project project, Map<Long, Map<Long, Double>> aggregatedScores) {
        double newStudentScore = aggregatedScores.get(newStudent.getId()).get(project.getId());
        double oldStudentScore = aggregatedScores.get(oldStudent.getId()).get(project.getId());

        return newStudentScore > oldStudentScore;
    }

    private int getSkillScore(String skillLevel) {
        if (skillLevel == null) {
            return 0;
        }
        return switch (skillLevel.toLowerCase()) {
            case "high" -> 3;
            case "medium" -> 2;
            case "low" -> 1;
            default -> 0;
        };
    }

    private Map<Long, Map<Long, Double>> calculateScores() {
        List<Student> students = studentRepository.findAll();
        List<Project> projects = projectRepository.findAll();
        Map<Long, Map<Long, Double>> aggregatedScores = new HashMap<>();

        for (Student student : students) {
            aggregatedScores.put(student.getId(), new HashMap<>());
            for (Project project : projects) {
                double score = 0;
                Map<String, String> studentSkills = student.getSkills();
                Map<String, String> projectSkills = project.getRequiredSkills();

                for (Map.Entry<String, String> projectSkill : projectSkills.entrySet()) {
                    String skillName = projectSkill.getKey();
                    String projectSkillLevel = projectSkill.getValue();
                    String studentSkillLevel = studentSkills.getOrDefault(skillName, "none");

                    score += (double) getSkillScore(studentSkillLevel) * getSkillScore(projectSkillLevel);
                }
                aggregatedScores.get(student.getId()).put(project.getId(), score);
            }
        }
        return aggregatedScores;
    }
}
*/