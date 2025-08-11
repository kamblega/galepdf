package gale.shapley.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Value("${module.leader.email}")
    private String moduleLeaderEmail;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @CircuitBreaker(name = "emailService", fallbackMethod = "fallbackSendEmail")
    public void sendMatchResultsEmail(String csvData, byte[] pdfData) throws MessagingException {
        logger.info("Attempting to send match results email to {}", moduleLeaderEmail);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(moduleLeaderEmail);
        helper.setSubject("Gale-Shapley Matching Results");
        helper.setText("Please find the matching results attached as CSV and PDF files.");

        // Attach CSV
        helper.addAttachment("matches.csv", new ByteArrayResource(csvData.getBytes()));

        // Attach PDF
        helper.addAttachment("matches.pdf", new ByteArrayResource(pdfData));

        mailSender.send(mimeMessage);
        logger.info("Match results email sent successfully.");
    }

    public void fallbackSendEmail(String csvData, byte[] pdfData, Throwable t) {
        logger.error("Circuit breaker is open for the email service. Could not send email to {}. Error: {}", moduleLeaderEmail, t.getMessage());
        // In a real application, you might add the email to a dead-letter queue for later processing.
    }
}
