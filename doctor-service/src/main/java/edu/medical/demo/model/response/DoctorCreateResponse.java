package edu.medical.demo.model.response;

import java.util.UUID;

public record DoctorCreateResponse(UUID doctorId, String error) {
}
