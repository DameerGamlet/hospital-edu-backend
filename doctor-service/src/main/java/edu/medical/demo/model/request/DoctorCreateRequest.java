package edu.medical.demo.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record DoctorCreateRequest(
        @NotNull
        String fullName,

        @NotNull
        @Email
        String email) {
}
