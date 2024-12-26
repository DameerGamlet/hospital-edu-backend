package edu.medical.demo.model;

import java.util.UUID;

public record UserMailRequest(UUID externalId, String firstName, String lastName, String email, boolean isActive) {
}