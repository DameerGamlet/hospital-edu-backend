package edu.medical.demo.service;

import edu.medical.demo.exceptions.UserAlreadyExistException;
import edu.medical.demo.model.User;
import edu.medical.demo.model.dto.request.CreateUserRequest;
import edu.medical.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;

    public User getUserByUserId(UUID userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with userId = %s is not found".formatted(userId)));
    }

    public UUID createUser(CreateUserRequest request) {
        final boolean emailAlreadyExist = userRepository.existsByEmail(request.email());

        if (emailAlreadyExist) {
            throw new UserAlreadyExistException("User with email %s already exist".formatted(request.email()));
        }

        final User user = new User();
        user.setEmail(request.email());
        user.setFullName(request.fullName());

        final String authenticationCode = UUID.randomUUID().toString().substring(0, 6);

        // отправить сообщение через брокер сообщений на сервис уведомлений
        final boolean sendingStatus = kafkaProducerService.sendAuthenticationCode(user.getEmail(), authenticationCode);

        if (!sendingStatus) {
            log.error("There was an error in the kafka service and the message was not delivered");
            return null;
        }

        return userRepository.save(user).getUserId();
    }
}
