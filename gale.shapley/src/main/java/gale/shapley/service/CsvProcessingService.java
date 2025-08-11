package gale.shapley.service;

import gale.shapley.model.Project;
import gale.shapley.model.Student;
import gale.shapley.repository.ProjectRepository;
import gale.shapley.repository.StudentRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class CsvProcessingService {

    private final StudentRepository studentRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public CsvProcessingService(StudentRepository studentRepository, ProjectRepository projectRepository) {
        this.studentRepository = studentRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public void processCsv(String studentsFilePath, String projectsFilePath) throws IOException {
        // Clear existing data
        studentRepository.deleteAll();
        projectRepository.deleteAll();

        // Process students
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(studentsFilePath)), StandardCharsets.UTF_8))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            for (CSVRecord csvRecord : csvParser) {
                Student student = new Student();
                student.setId(Long.parseLong(csvRecord.get("id")));
                student.setName(csvRecord.get("name"));

                Map<String, String> skills = new HashMap<>();
                // Assuming skills are in columns like 'skill1', 'skill2' with format 'skillName:level'
                for (int i = 2; i < csvRecord.size(); i++) {
                    String skillData = csvRecord.get(i);
                    if (skillData != null && !skillData.isEmpty()) {
                        String[] parts = skillData.split(":");
                        if (parts.length == 2) {
                            skills.put(parts[0], parts[1]);
                        }
                    }
                }
                student.setSkills(skills);
                studentRepository.save(student);
            }
        }

        // Process projects
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(projectsFilePath)), StandardCharsets.UTF_8))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            for (CSVRecord csvRecord : csvParser) {
                Project project = new Project();
                project.setId(Long.parseLong(csvRecord.get("id")));
                project.setProjectName(csvRecord.get("projectName"));

                Map<String, String> requiredSkills = new HashMap<>();
                // Assuming skills are in columns like 'skill1', 'skill2' with format 'skillName:level'
                for (int i = 2; i < csvRecord.size(); i++) {
                     String skillData = csvRecord.get(i);
                    if (skillData != null && !skillData.isEmpty()) {
                        String[] parts = skillData.split(":");
                        if (parts.length == 2) {
                            requiredSkills.put(parts[0], parts[1]);
                        }
                    }
                }
                project.setRequiredSkills(requiredSkills);
                projectRepository.save(project);
            }
        }
    }
}
