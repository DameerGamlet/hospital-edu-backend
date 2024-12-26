package edu.medical.demo.service;

import java.util.UUID;

public interface UserActivationService {
    void registrationUserRegister(UUID userId, String code);
}
