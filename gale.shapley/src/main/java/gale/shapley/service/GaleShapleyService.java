package gale.shapley.service;

import gale.shapley.model.Project;
import gale.shapley.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GaleShapleyService {

    private final RestTemplate restTemplate;

    // Weights for preference aggregation (can be configured)
    private static final double STUDENT_PREFERENCE_WEIGHT = 0.6;
    private static final double STAFF_PREFERENCE_WEIGHT = 0.4;

    @Autowired
    public GaleShapleyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private List<Student> fetchStudents() {
        ResponseEntity<List<Student>> response = restTemplate.exchange(
                "http://localhost:8081/query/students/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {}
        );
        return response.getBody();
    }

    private List<Project> fetchProjects() {
        ResponseEntity<List<Project>> response = restTemplate.exchange(
                "http://localhost:8080/read/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Project>>() {}
        );
        return response.getBody();
    }

    /**
     * Main matching method implementing the complete 4-step process:
     * 1. Create preference matrices for both students and research staff
     * 2. Normalize all preference scores between 0-1
     * 3. Aggregate the normalized scores to form final preference values
     * 4. Apply the Gale-Shapley algorithm to generate stable and optimal matches
     */
    public Map<Student, Project> match() {
        List<Student> students = fetchStudents();
        List<Project> projects = fetchProjects();

        if (students == null || projects == null || students.isEmpty() || projects.isEmpty()) {
            System.out.println("Not enough students or projects to run the matching algorithm.");
            return new HashMap<>();
        }

        // Step 1: Create preference matrices for both students and research staff
        System.out.println("=== STEP 1: CREATING PREFERENCE MATRICES ===");
        Map<Long, Map<Long, Double>> rawStudentPreferences = calculateStudentPreferences(students, projects);
        Map<Long, Map<Long, Double>> rawStaffPreferences = calculateStaffPreferences(students, projects);

        // Step 2: Normalize all preference scores between 0-1
        System.out.println("\n=== STEP 2: NORMALIZING PREFERENCE SCORES ===");
        Map<Long, Map<Long, Double>> normalizedStudentPreferences = normalizeScores(rawStudentPreferences, "Student");
        Map<Long, Map<Long, Double>> normalizedStaffPreferences = normalizeScores(rawStaffPreferences, "Staff");

        // Step 3: Aggregate the normalized scores to form final preference values
        System.out.println("\n=== STEP 3: AGGREGATING NORMALIZED SCORES ===");
        Map<Long, Map<Long, Double>> aggregatedPreferences = aggregatePreferences(
                normalizedStudentPreferences,
                normalizedStaffPreferences,
                students,
                projects
        );

        // Step 4: Apply the Gale-Shapley algorithm to generate stable and optimal matches
        System.out.println("\n=== STEP 4: APPLYING GALE-SHAPLEY ALGORITHM ===");
        return applyGaleShapleyAlgorithm(aggregatedPreferences, students, projects);
    }

    /**
     * STEP 1A: Calculate Student Preferences
     * Students prefer projects based on how well their skills match project requirements
     * Formula: Σ(student_skill_level × required_skill_level)
     */
    private Map<Long, Map<Long, Double>> calculateStudentPreferences(List<Student> students, List<Project> projects) {
        Map<Long, Map<Long, Double>> studentPreferences = new HashMap<>();

        System.out.println("Calculating Student Preferences (Student → Project scores):");

        for (Student student : students) {
            studentPreferences.put(student.getId(), new HashMap<>());
            System.out.println("\nStudent: " + student.getName());
            System.out.println("Skills: " + student.getSkills());

            for (Project project : projects) {
                double score = 0;
                Map<String, String> studentSkills = student.getSkills();
                Map<String, String> projectRequirements = project.getRequiredSkills();

                System.out.println("\n  Project: " + project.getProjectName());
                System.out.println("  Requirements: " + projectRequirements);

                // Calculate compatibility score
                for (Map.Entry<String, String> requirement : projectRequirements.entrySet()) {
                    String skillName = requirement.getKey();
                    String requiredLevel = requirement.getValue();
                    String studentLevel = studentSkills.getOrDefault(skillName, "none");

                    int studentSkillPoints = getSkillScore(studentLevel);
                    int requiredSkillPoints = getSkillScore(requiredLevel);
                    double skillScore = (double) studentSkillPoints * requiredSkillPoints;

                    score += skillScore;
                    System.out.println("    " + skillName + ": " + studentLevel + "(" + studentSkillPoints +
                            ") × " + requiredLevel + "(" + requiredSkillPoints + ") = " + skillScore);
                }

                studentPreferences.get(student.getId()).put(project.getId(), score);
                System.out.println("  Total Score: " + score);
            }
        }

        return studentPreferences;
    }

    /**
     * STEP 1B: Calculate Staff Preferences
     * Projects prefer students based on how well students fulfill project requirements
     * Formula: Σ(min(student_skill, required_skill) × skill_importance_weight)
     */
    private Map<Long, Map<Long, Double>> calculateStaffPreferences(List<Student> students, List<Project> projects) {
        Map<Long, Map<Long, Double>> staffPreferences = new HashMap<>();

        System.out.println("\nCalculating Staff Preferences (Project → Student scores):");

        for (Project project : projects) {
            staffPreferences.put(project.getId(), new HashMap<>());
            System.out.println("\nProject: " + project.getProjectName());
            System.out.println("Requirements: " + project.getRequiredSkills());

            for (Student student : students) {
                double score = 0;
                Map<String, String> studentSkills = student.getSkills();
                Map<String, String> projectRequirements = project.getRequiredSkills();

                System.out.println("\n  Student: " + student.getName());
                System.out.println("  Skills: " + studentSkills);

                // Calculate how well student satisfies project requirements
                for (Map.Entry<String, String> requirement : projectRequirements.entrySet()) {
                    String skillName = requirement.getKey();
                    String requiredLevel = requirement.getValue();
                    String studentLevel = studentSkills.getOrDefault(skillName, "none");

                    int studentSkillPoints = getSkillScore(studentLevel);
                    int requiredSkillPoints = getSkillScore(requiredLevel);

                    // Staff preference: how well student meets the requirement
                    // Use minimum to penalize over-qualification less than under-qualification
                    double satisfactionRatio = Math.min(studentSkillPoints, requiredSkillPoints);
                    double skillImportance = requiredSkillPoints; // Weight by requirement importance
                    double skillScore = satisfactionRatio * skillImportance;

                    score += skillScore;
                    System.out.println("    " + skillName + ": min(" + studentSkillPoints + ", " +
                            requiredSkillPoints + ") × " + skillImportance + " = " + skillScore);
                }

                staffPreferences.get(project.getId()).put(student.getId(), score);
                System.out.println("  Total Score: " + score);
            }
        }

        return staffPreferences;
    }

    /**
     * STEP 2: Normalize all preference scores between 0-1
     * Uses Min-Max normalization: (score - min) / (max - min)
     */
    private Map<Long, Map<Long, Double>> normalizeScores(Map<Long, Map<Long, Double>> rawScores, String type) {
        System.out.println("\nNormalizing " + type + " Preferences:");

        // Find global minimum and maximum scores
        double globalMin = Double.MAX_VALUE;
        double globalMax = Double.MIN_VALUE;

        for (Map<Long, Double> entityScores : rawScores.values()) {
            for (Double score : entityScores.values()) {
                globalMin = Math.min(globalMin, score);
                globalMax = Math.max(globalMax, score);
            }
        }

        System.out.println("Global Min Score: " + globalMin);
        System.out.println("Global Max Score: " + globalMax);

        double range = globalMax - globalMin;
        System.out.println("Score Range: " + range);

        // Apply Min-Max normalization
        Map<Long, Map<Long, Double>> normalizedScores = new HashMap<>();

        for (Map.Entry<Long, Map<Long, Double>> entityEntry : rawScores.entrySet()) {
            Long entityId = entityEntry.getKey();
            normalizedScores.put(entityId, new HashMap<>());

            for (Map.Entry<Long, Double> scoreEntry : entityEntry.getValue().entrySet()) {
                Long targetId = scoreEntry.getKey();
                Double rawScore = scoreEntry.getValue();

                double normalizedScore;
                if (range == 0) {
                    // All scores are identical
                    normalizedScore = 0.5;
                    System.out.println("Warning: All scores identical, using 0.5");
                } else {
                    normalizedScore = (rawScore - globalMin) / range;
                }

                normalizedScores.get(entityId).put(targetId, normalizedScore);
            }
        }

        // Print normalization summary
        System.out.println("Normalization applied: normalized_score = (raw_score - " + globalMin + ") / " + range);

        return normalizedScores;
    }

    /**
     * STEP 3: Aggregate the normalized scores to form final preference values
     * Formula: final_score = (student_weight × student_preference) + (staff_weight × staff_preference)
     */
    private Map<Long, Map<Long, Double>> aggregatePreferences(
            Map<Long, Map<Long, Double>> normalizedStudentPreferences,
            Map<Long, Map<Long, Double>> normalizedStaffPreferences,
            List<Student> students,
            List<Project> projects) {

        Map<Long, Map<Long, Double>> aggregatedPreferences = new HashMap<>();

        System.out.println("Aggregating preferences with weights:");
        System.out.println("Student preference weight: " + STUDENT_PREFERENCE_WEIGHT);
        System.out.println("Staff preference weight: " + STAFF_PREFERENCE_WEIGHT);

        for (Student student : students) {
            aggregatedPreferences.put(student.getId(), new HashMap<>());
            System.out.println("\nStudent: " + student.getName());

            for (Project project : projects) {
                // Get normalized preferences from both perspectives
                double studentPref = normalizedStudentPreferences.get(student.getId()).get(project.getId());
                double staffPref = normalizedStaffPreferences.get(project.getId()).get(student.getId());

                // Calculate aggregated score
                double aggregatedScore = (STUDENT_PREFERENCE_WEIGHT * studentPref) +
                        (STAFF_PREFERENCE_WEIGHT * staffPref);

                aggregatedPreferences.get(student.getId()).put(project.getId(), aggregatedScore);

                System.out.println("  " + project.getProjectName() + ":");
                System.out.println("    Student pref: " + String.format("%.3f", studentPref));
                System.out.println("    Staff pref: " + String.format("%.3f", staffPref));
                System.out.println("    Aggregated: " + String.format("%.3f", aggregatedScore));
            }
        }

        return aggregatedPreferences;
    }

    /**
     * STEP 4: Apply the Gale-Shapley algorithm to generate stable and optimal matches
     * Fixed version of the original algorithm with proper logic
     */
    private Map<Student, Project> applyGaleShapleyAlgorithm(Map<Long, Map<Long, Double>> aggregatedScores, List<Student> students, List<Project> projects) {

        Map<Student, Project> matching = new HashMap<>();
        Map<Project, Student> projectAssignments = new HashMap<>();

        // Track free students and their proposal progress
        Queue<Student> freeStudents = new LinkedList<>(students);
        Map<Student, Integer> nextProjectIndex = new HashMap<>();
        for (Student student : students) {
            nextProjectIndex.put(student, 0);
        }

        int iterations = 0;
        int maxIterations = students.size() * projects.size() * 2;

        while (!freeStudents.isEmpty() && iterations < maxIterations) {
            iterations++;
            Student student = freeStudents.poll();

            System.out.println("\nIteration " + iterations + ": Processing student " + student.getName());

            // Get student's preference list (sorted by aggregated score)
            List<Project> sortedProjects = projects.stream()
                    .sorted((p1, p2) -> Double.compare(
                            aggregatedScores.get(student.getId()).get(p2.getId()),
                            aggregatedScores.get(student.getId()).get(p1.getId())))
                    .collect(Collectors.toList());

            int currentIndex = nextProjectIndex.get(student);

            // Check if student has exhausted all preferences
            if (currentIndex >= sortedProjects.size()) {
                System.out.println("  Student " + student.getName() + " has exhausted all preferences");
                continue;
            }

            Project project = sortedProjects.get(currentIndex);
            nextProjectIndex.put(student, currentIndex + 1);

            double studentScore = aggregatedScores.get(student.getId()).get(project.getId());
            System.out.println("  Student proposes to " + project.getProjectName() +
                    " (score: " + String.format("%.3f", studentScore) + ")");

            // Correct condition check
            if (!projectAssignments.containsKey(project)) {
                // Project is free, assign student to it
                System.out.println("    Project is free - accepting");
                assignStudentToProject(student, project, matching, projectAssignments);
            } else {
                // Project is already assigned, check if current student is better
                Student currentAssignedStudent = projectAssignments.get(project);
                double currentStudentScore = aggregatedScores.get(currentAssignedStudent.getId()).get(project.getId());

                System.out.println("    Project assigned to " + currentAssignedStudent.getName() +
                        " (score: " + String.format("%.3f", currentStudentScore) + ")");

                if (studentScore > currentStudentScore) {
                    // New student is better, reassign
                    System.out.println("    New student is better - reassigning");
                    unassignStudentFromProject(currentAssignedStudent, project, matching, projectAssignments);
                    assignStudentToProject(student, project, matching, projectAssignments);
                    freeStudents.offer(currentAssignedStudent);
                } else {
                    // Current student is better, reject new proposal
                    System.out.println("    Current student is better - rejecting");
                    freeStudents.offer(student);
                }
            }
        }

        if (iterations >= maxIterations) {
            System.err.println("WARNING: Maximum iterations reached");
        }

        printFinalResults(matching);
        return matching;
    }

    // Helper methods
    private void assignStudentToProject(Student student, Project project,
                                      Map<Student, Project> matching,
                                      Map<Project, Student> projectAssignments) {
        matching.put(student, project);
        projectAssignments.put(project, student);
    }

    private void unassignStudentFromProject(Student student, Project project,
                                          Map<Student, Project> matching,
                                          Map<Project, Student> projectAssignments) {
        matching.remove(student);
        projectAssignments.remove(project);
    }

    private boolean isBetterThan(Student newStudent, Student oldStudent, Project project,
                               Map<Long, Map<Long, Double>> aggregatedScores) {
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

    // Legacy method for backward compatibility - now calls the new implementation
    private Map<Long, Map<Long, Double>> calculateScores(List<Student> students, List<Project> projects) {
        return calculateStudentPreferences(students, projects);
    }

    private void printFinalResults(Map<Student, Project> matching) {
        System.out.println("\n=== FINAL MATCHING RESULTS ===");
        List<String> results = new ArrayList<>();

        for (Map.Entry<Student, Project> entry : matching.entrySet()) {
            String result = "Student {" + entry.getKey().getName() + "} - Best match - Project {" +
                    entry.getValue().getId() + "} - Project {" + entry.getValue().getProjectName() + "}";
            results.add(result);
            System.out.println(result);
        }

        System.out.println("\nMatched " + matching.size() + " students to projects");
    }
}
