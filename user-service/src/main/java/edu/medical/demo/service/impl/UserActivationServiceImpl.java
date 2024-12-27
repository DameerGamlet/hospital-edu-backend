package edu.medical.demo.service.impl;

import edu.medical.demo.exceptions.UserAlreadyActiveException;
import edu.medical.demo.exceptions.UserAlreadyArchivedException;
import edu.medical.demo.exceptions.UserNotFoundException;
import edu.medical.demo.model.ActivationUser;
import edu.medical.demo.model.User;
import edu.medical.demo.repository.ActivationUserRepository;
import edu.medical.demo.repository.UserRepository;
import edu.medical.demo.service.UserActivationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActivationServiceImpl implements UserActivationService {

    private final ActivationUserRepository activationUserRepository;
    private final UserRepository userRepository;

    @Transactional
    public void registrationUserRegister(UUID userId, String code) {
        log.info("Выполняется активация пользователя с userId = '{}", userId);
        final User founded = userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId));

        if (founded.isActive() && !founded.isArchived()) {
            throw new UserAlreadyActiveException("", founded.getEmail());
        } else if (founded.isActive()) {
            throw new UserAlreadyArchivedException("", founded.getEmail());
        }

        final ActivationUser activationUser = activationUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Для пользователя userId = '%s' код устарел, необходимо обновиться"));

        if (activationUser.getCode().equals(code)) {
            founded.setActive(true);
            activationUserRepository.delete(activationUser);
            log.info("Пользователь с userId = '{}' успешно активировано", userId);
        }
    }
}
