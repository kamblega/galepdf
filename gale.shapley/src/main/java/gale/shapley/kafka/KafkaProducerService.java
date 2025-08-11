package gale.shapley.kafka;

import gale.shapley.kafka.payload.MatchingJobPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, MatchingJobPayload> kafkaTemplate;

    @Value("${app.kafka.topic.matching-jobs}")
    private String topicName;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, MatchingJobPayload> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMatchingJob(MatchingJobPayload payload) {
        logger.info("Sending job to Kafka topic '{}': {}", topicName, payload);
        kafkaTemplate.send(topicName, payload);
    }
}
