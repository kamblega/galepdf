package gale.shapley.controller;

import gale.shapley.dto.MatchResultDto;
import gale.shapley.model.Project;
import gale.shapley.model.Student;
import gale.shapley.service.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/match")
public class MatchingController {

    private final GaleShapleyService galeShapleyService;
    private final PdfGenerationService pdfGenerationService;
    private final EmailService emailService;
    private final CsvExportService csvExportService;

    @Autowired
    public MatchingController(GaleShapleyService galeShapleyService,
                              PdfGenerationService pdfGenerationService,
                              EmailService emailService,
                              CsvExportService csvExportService) {
        this.galeShapleyService = galeShapleyService;
        this.pdfGenerationService = pdfGenerationService;
        this.emailService = emailService;
        this.csvExportService = csvExportService;
    }

    @PostMapping
    public ResponseEntity<List<MatchResultDto>> match() {
        Map<Student, Project> matches = galeShapleyService.match();
        List<MatchResultDto> resultDtos = matches.entrySet().stream()
                .map(entry -> new MatchResultDto(
                        entry.getKey().getName(),
                        entry.getKey().getEmail(),
                        entry.getValue().getProjectName(),
                        entry.getValue().getStaffName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resultDtos);
    }

    @GetMapping("/csv")
    public ResponseEntity<String> getMatchesAsCsv() {
        Map<Student, Project> matches = galeShapleyService.match();
        String csvData = csvExportService.generateCsvData(matches);

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
            String csvData = csvExportService.generateCsvData(matches);
            byte[] pdfData = pdfGenerationService.generatePdf(matches);

            emailService.sendMatchResultsEmail(csvData, pdfData);

            return ResponseEntity.ok("Email sent successfully to module leader.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
        }
    }
}
