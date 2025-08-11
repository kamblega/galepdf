package gale.shapley.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import gale.shapley.model.Project;
import gale.shapley.model.Student;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfGenerationService {

    public byte[] generatePdf(Map<Student, Project> matches) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Gale-Shapley Matching Results").setBold().setFontSize(18));

        Table table = new Table(UnitValue.createPercentArray(new float[]{50, 50})).useAllAvailableWidth();
        table.addHeaderCell("Student Name");
        table.addHeaderCell("Project Name");

        for (Map.Entry<Student, Project> entry : matches.entrySet()) {
            table.addCell(entry.getKey().getName());
            table.addCell(entry.getValue().getProjectName());
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }
}
