package edu.medical.demo.models.request;

import java.util.UUID;

public record ActivationMessage(MailType type, UUID userId, String email, String fullName, String code) {
}
