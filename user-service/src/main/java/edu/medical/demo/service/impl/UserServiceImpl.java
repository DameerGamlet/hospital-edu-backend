package edu.medical.demo.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.medical.demo.exceptions.UserAlreadyActiveException;
import edu.medical.demo.exceptions.UserAlreadyArchivedException;
import edu.medical.demo.exceptions.UserNotFoundException;
import edu.medical.demo.model.ActivationUser;
import edu.medical.demo.model.User;
import edu.medical.demo.model.dto.request.CreateUserRequest;
import edu.medical.demo.models.request.ActivationMessage;
import edu.medical.demo.models.request.MailType;
import edu.medical.demo.repository.ActivationUserRepository;
import edu.medical.demo.repository.UserRepository;
import edu.medical.demo.service.KafkaProducerService;
import edu.medical.demo.service.UserService;
import edu.medical.demo.utils.CodeGeneratorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для выполнения операциями над пользователем.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ActivationUserRepository activationUserRepository;
    private final KafkaProducerService kafkaProducerService;

    /**
     * Получить список всех пользователей.
     *
     * @return - список пользователей.
     */
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Создать нового пользователя.
     */
    @Override
    public UUID createUser(CreateUserRequest request) throws JsonProcessingException {
        log.info("Creating newUser with email = {}", request.email());
        final boolean emailAlreadyExist = userRepository.existsByEmail(request.email());

        final String code = CodeGeneratorUtils.generateSixDigitCode();
        if (emailAlreadyExist) {
            final User user = userRepository.findByUserEmail(request.email())
                    .orElseThrow(() -> new UserNotFoundException("User with email %s not found."));

            if (!user.isActive() && !user.isArchived()) {
                log.info("User with email {} is not active, sending activation code again.", request.email());

                if (createUseKafkaSender(user, code, MailType.SEND_REACTIVATION_CODE) != null) {
                    return user.getUserId();
                }
                return null;
            } else if (user.isArchived()) {
                log.info("User with email {} is archived. Offer to restore the account.", request.email());
                throw new UserAlreadyArchivedException("User with this email was archived, would you like to restore the account?");
            } else {
                log.info("User with email {} is already active.", request.email());
                throw new UserAlreadyActiveException("", request.email());
            }
        }

        final User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setFullName(request.fullName());

        final User sendedUser = createUseKafkaSender(newUser, code, MailType.SEND_ACTIVATION_CODE);

        if (sendedUser != null) {
            activationUserRepository.save(new ActivationUser(newUser.getUserId(), code));
            return userRepository.save(sendedUser).getUserId();
        }
        return null;
    }

    private User createUseKafkaSender(User user, String code, MailType type) throws JsonProcessingException {
        final ActivationMessage mailRequest = new ActivationMessage(
                type,
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                code
        );

        // отправить новый код активации пользователя
        final boolean sendingStatus = kafkaProducerService.sendAuthenticationCode(mailRequest);

        if (!sendingStatus) {
            log.error("Kafka was an error in the kafka service and the message was not delivered.");
            return null;
        }
        return user;
    }

    /**
     * Получить пользователя по его userId (UUID).
     */
    @Override
    public User getUserByUserId(UUID userId) {
        log.info("Getting user by userId: {}", userId);
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with userId = %s is not found".formatted(userId)));
    }
}
