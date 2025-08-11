package gale.shapley.controller;

import gale.shapley.kafka.KafkaProducerService;
import gale.shapley.kafka.payload.MatchingJobPayload;
import gale.shapley.model.Project;
import gale.shapley.model.Student;
import gale.shapley.service.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/match")
public class MatchingController {

    private final GaleShapleyService galeShapleyService;
    private final CsvProcessingService csvProcessingService;
    private final PdfGenerationService pdfGenerationService;
    private final EmailService emailService;
    private final FileStorageService fileStorageService;
    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public MatchingController(GaleShapleyService galeShapleyService,
                              CsvProcessingService csvProcessingService,
                              PdfGenerationService pdfGenerationService,
                              EmailService emailService,
                              FileStorageService fileStorageService,
                              KafkaProducerService kafkaProducerService) {
        this.galeShapleyService = galeShapleyService;
        this.csvProcessingService = csvProcessingService;
        this.pdfGenerationService = pdfGenerationService;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("students") MultipartFile studentsFile,
                                            @RequestParam("projects") MultipartFile projectsFile) {
        try {
            // Store files temporarily
            String studentsFilePath = fileStorageService.storeFile(studentsFile);
            String projectsFilePath = fileStorageService.storeFile(projectsFile);

            // Create and send Kafka job
            MatchingJobPayload payload = new MatchingJobPayload(studentsFilePath, projectsFilePath);
            kafkaProducerService.sendMatchingJob(payload);

            return ResponseEntity.accepted().body("Your matching request has been accepted and is being processed asynchronously.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting matching request: " + e.getMessage());
        }
    }

    @PostMapping
    public List<String> match() {
        Map<Student, Project> matches = galeShapleyService.match();
        List<String> result = new ArrayList<>();
        for (Map.Entry<Student, Project> entry : matches.entrySet()) {
            Student student = entry.getKey();
            Project project = entry.getValue();
            result.add(String.format("Student {%s} - Best match - Project {%d} - Project {%s}",
                    student.getName(), project.getId(), project.getProjectName()));
        }
        return result;
    }

    @GetMapping("/csv")
    public ResponseEntity<String> getMatchesAsCsv() {
        Map<Student, Project> matches = galeShapleyService.match();

        String csvData = generateCsvData(matches);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=matches.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> getMatchesAsPdf() {
        Map<Student, Project> matches = galeShapleyService.match();
        byte[] pdfBytes = pdfGenerationService.generatePdf(matches);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=matches.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail() {
        try {
            Map<Student, Project> matches = galeShapleyService.match();
            String csvData = generateCsvData(matches);
            byte[] pdfData = pdfGenerationService.generatePdf(matches);

            emailService.sendMatchResultsEmail(csvData, pdfData);

            return ResponseEntity.ok("Email sent successfully to module leader.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }

    private String generateCsvData(Map<Student, Project> matches) {
        StringBuilder csvOutput = new StringBuilder();
        csvOutput.append("Student Name,Project Name\n"); // CSV Header

        for (Map.Entry<Student, Project> entry : matches.entrySet()) {
            csvOutput.append(entry.getKey().getName())
                    .append(",")
                    .append(entry.getValue().getProjectName())
                    .append("\n");
        }
        return csvOutput.toString();
    }
}
