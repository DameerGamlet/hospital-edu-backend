package edu.medical.demo.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.medical.demo.models.request.ActivationMessage;
import edu.medical.demo.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Сервис по работе над Kafka из user-service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Отправить код активации через брокер сообщений Kafka на notification-service.
     */
    @Override
    public boolean sendAuthenticationCode(ActivationMessage request) throws JsonProcessingException {
        log.info("KAFKA_PRODUCER: Sending authentication code to kafka for email: {}", request.email());

        final ObjectMapper objectMapper = new ObjectMapper();
        final String message = objectMapper.writeValueAsString(request);
        final CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("user-authentication-code", message);

        future.thenApply(result -> {
            if (result != null && result.getRecordMetadata() != null) {
                log.info("Message send successfully: {}", result.getRecordMetadata());
                return true;
            } else {
                log.error("Failed to send message.");
                return false;
            }
        }).exceptionally(ex -> {
            log.error("Error occurred while sending message", ex);
            return false;
        });

        return true;
    }
}
