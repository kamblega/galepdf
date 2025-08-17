package gale.shapley.service;

import gale.shapley.model.Project;
import gale.shapley.model.Student;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;

@Service
public class CsvExportService {

    public String generateCsvData(Map<Student, Project> matches) {
        StringWriter stringWriter = new StringWriter();
        try (CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT
                .withHeader("Student Name", "Student Email", "Project Name", "Supervisor"))) {
            for (Map.Entry<Student, Project> entry : matches.entrySet()) {
                csvPrinter.printRecord(
                        entry.getKey().getName(),
                        entry.getKey().getEmail(),
                        entry.getValue().getProjectName(),
                        entry.getValue().getStaffName()
                );
            }
        } catch (IOException e) {
            // This is unlikely to happen with a StringWriter
            throw new RuntimeException("Failed to generate CSV data", e);
        }
        return stringWriter.toString();
    }
}
