package edu.medical.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public boolean sendAuthenticationCode(String email, String code) {
        log.info("KAFKA_PRODUCER: Sending authentication code to kafka for email: {}", email);

        final String message = String.format("{\"email\": \"%s\",\"code\": \"%s\"}", email, code);

        final CompletableFuture<SendResult<String, String>> future =
                kafkaTemplate.send("user-authentication-code", message);

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
