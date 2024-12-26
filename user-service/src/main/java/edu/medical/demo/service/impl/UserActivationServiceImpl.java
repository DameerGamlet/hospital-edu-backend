package edu.medical.demo.service.impl;

import edu.medical.demo.repository.UserRepository;
import edu.medical.demo.service.UserActivationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActivationServiceImpl implements UserActivationService {

    private UserRepository userRepository;

    public void registrationUserRegister(UUID userId, String code) {
        // TODO: надо получить токен

    }
}
