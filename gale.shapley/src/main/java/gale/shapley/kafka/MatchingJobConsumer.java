package gale.shapley.kafka;

import gale.shapley.kafka.payload.MatchingJobPayload;
import gale.shapley.model.Project;
import gale.shapley.model.Student;
import gale.shapley.service.CsvProcessingService;
import gale.shapley.service.EmailService;
import gale.shapley.service.GaleShapleyService;
import gale.shapley.service.PdfGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MatchingJobConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MatchingJobConsumer.class);

    private final CsvProcessingService csvProcessingService;
    private final GaleShapleyService galeShapleyService;
    private final PdfGenerationService pdfGenerationService;
    private final EmailService emailService;

    @Autowired
    public MatchingJobConsumer(CsvProcessingService csvProcessingService,
                               GaleShapleyService galeShapleyService,
                               PdfGenerationService pdfGenerationService,
                               EmailService emailService) {
        this.csvProcessingService = csvProcessingService;
        this.galeShapleyService = galeShapleyService;
        this.pdfGenerationService = pdfGenerationService;
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${app.kafka.topic.matching-jobs}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMatchingJob(MatchingJobPayload payload) {
        logger.info("Received matching job from Kafka: {}", payload);
        try {
            // 1. Process CSVs to populate database
            logger.info("Populating database from CSV files at {} and {}", payload.getStudentsFilePath(), payload.getProjectsFilePath());
            csvProcessingService.processCsv(payload.getStudentsFilePath(), payload.getProjectsFilePath());
            logger.info("Database populated successfully.");

            // 2. Run the matching algorithm
            logger.info("Running Gale-Shapley algorithm...");
            Map<Student, Project> matches = galeShapleyService.match();
            logger.info("Matching complete.");

            // 3. Generate outputs
            String csvData = generateCsvData(matches);
            byte[] pdfData = pdfGenerationService.generatePdf(matches);
            logger.info("Output files generated.");

            // 4. Send email notification
            logger.info("Sending email notification...");
            emailService.sendMatchResultsEmail(csvData, pdfData);
            logger.info("Email sent.");

        } catch (Exception e) {
            logger.error("Error processing matching job: {}", payload, e);
            // In a real application, you would handle this error, e.g., by sending the message to a dead-letter topic.
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
