package edu.medical.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.medical.demo.models.request.ActivationMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${message-mail}")
    private String mailMessageTemplate;

    private String buildCodeActivationMessage(String name, String link, String code) {
        return String.format(mailMessageTemplate, name, link, link, link, code);
    }

    @Async
    private void send(String emailTo, String subject, String message) {
        log.info("Отправить сообщение от лица: {} на почту: {}", emailFrom, emailTo);
        try {
            final MimeMessage mime = mailSender.createMimeMessage();

            final MimeMessageHelper helper =
                    new MimeMessageHelper(mime, "utf-8");
            helper.setText(message, true);
            helper.setTo(emailTo);
            helper.setSubject(subject);
            helper.setFrom(emailFrom);

            mailSender.send(mime);
        } catch (MessagingException e) {
            final String FAILED_SEND_MESSAGE = "Failed to send email";
            log.error(FAILED_SEND_MESSAGE, e);
            throw new IllegalStateException(FAILED_SEND_MESSAGE);
        }
    }

    @KafkaListener(
            topics = "user-authentication-code",
            groupId = "notification-group"
    )
    public void sendActivationUserCreatingCode(String message) {
        try {
            log.info("NOTIFICATION_LISTENER: Receive message from kafka: {}", message);

            final ObjectMapper objectMapper = new ObjectMapper();
            final ActivationMessage response = objectMapper.readValue(message, ActivationMessage.class);

            final String link = "http://127.0.0.1:8080/private/api/v1/users/%s/activation?code=%s"
                    .formatted(response.userId(), response.code());
            final String build = buildCodeActivationMessage(response.fullName(), link, response.code());

            send(response.email(), "Активация пользователя", build);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
