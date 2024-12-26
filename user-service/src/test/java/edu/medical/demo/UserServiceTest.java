package edu.medical.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.medical.demo.exceptions.UserAlreadyActiveException;
import edu.medical.demo.exceptions.UserAlreadyArchivedException;
import edu.medical.demo.model.User;
import edu.medical.demo.model.dto.request.CreateUserRequest;
import edu.medical.demo.models.request.ActivationMessage;
import edu.medical.demo.repository.UserRepository;
import edu.medical.demo.service.KafkaProducerService;
import edu.medical.demo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Юнит-тесты для класса UserServiceImpl.
 */
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    private CreateUserRequest validRequest;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        MockitoAnnotations.openMocks(this);
        validRequest = new CreateUserRequest("Фамилия Имя", "family_name@gmail.com");
        when(kafkaProducerService.sendAuthenticationCode(any(ActivationMessage.class))).thenReturn(true);
    }

    /**
     * Тест на случай, когда пользователь уже активен, и выбрасывается исключение UserAlreadyActiveException.
     */
    @Test
    void createUser_EmailAlreadyActive_ThrowsException() {
        final User existingUser = createUser(UUID.randomUUID(), validRequest.email(), validRequest.fullName(), true, false);

        when(userRepository.existsByEmail(validRequest.email())).thenReturn(true);
        when(userRepository.findByUserEmail(validRequest.email())).thenReturn(Optional.of(existingUser));

        final UserAlreadyActiveException exception = assertThrows(UserAlreadyActiveException.class,
                () -> userService.createUser(validRequest));

        assertEquals("User with this email is already active. If this is not you, please contact support.",
                exception.getMessage());
        verify(userRepository).existsByEmail(validRequest.email());
        verify(userRepository).findByUserEmail(validRequest.email());
    }

    /**
     * Тест на случай, когда пользователь архивирован, и выбрасывается исключение UserAlreadyArchivedException.
     */
    @Test
    void createUser_UserArchived_ThrowsException() {
        final User existingUser = createUser(UUID.randomUUID(), validRequest.email(), validRequest.fullName(), false, true);

        when(userRepository.existsByEmail(validRequest.email())).thenReturn(true);
        when(userRepository.findByUserEmail(validRequest.email())).thenReturn(Optional.of(existingUser));

        final UserAlreadyArchivedException exception = assertThrows(UserAlreadyArchivedException.class,
                () -> userService.createUser(validRequest));

        assertEquals("User with this email was archived, would you like to restore the account?", exception.getMessage());
    }

    /**
     * Тест на случай, когда email не существует, создается новый пользователь и отправляется код активации.
     */
    @Test
    void createUser_EmailNotExist_CreateNewUser() throws JsonProcessingException {
        final UUID generatedUserId = UUID.randomUUID();
        final User savedUser = createUser(generatedUserId, validRequest.email(), validRequest.fullName(), false, false);

        when(userRepository.existsByEmail(validRequest.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        final UUID userId = userService.createUser(validRequest);

        assertNotNull(userId);
        assertEquals(generatedUserId, userId);

        final ArgumentCaptor<ActivationMessage> captor = ArgumentCaptor.forClass(ActivationMessage.class);
        verify(kafkaProducerService).sendAuthenticationCode(captor.capture());

        assertEquals(validRequest.email(), captor.getValue().email());
        assertEquals(validRequest.fullName(), captor.getValue().fullName());
    }

    /**
     * Тест на случай, когда email уже существует, но пользователь неактивен, и отправляется код повторной активации.
     */
    @Test
    void createUser_EmailAlreadyExist_InactiveUser_SendsReactivationCode() throws JsonProcessingException {
        final User existingUser = createUser(UUID.randomUUID(), validRequest.email(), validRequest.fullName(), false, false);

        when(userRepository.existsByEmail(validRequest.email())).thenReturn(true);
        when(userRepository.findByUserEmail(validRequest.email())).thenReturn(Optional.of(existingUser));

        final UUID userId = userService.createUser(validRequest);

        assertNotNull(userId);

        final ArgumentCaptor<ActivationMessage> captor = ArgumentCaptor.forClass(ActivationMessage.class);
        verify(kafkaProducerService).sendAuthenticationCode(captor.capture());

        assertEquals(validRequest.email(), captor.getValue().email());
        assertEquals(validRequest.fullName(), captor.getValue().fullName());
    }

    /**
     * Тест на случай, когда отправка кода активации не удалась, и метод возвращает null.
     */
    @Test
    void createUser_SendingFails_ReturnsNull() throws JsonProcessingException {
        when(userRepository.existsByEmail(validRequest.email())).thenReturn(false);
        when(kafkaProducerService.sendAuthenticationCode(any(ActivationMessage.class))).thenReturn(false);

        final UUID userId = userService.createUser(validRequest);

        assertNull(userId);
    }

    /**
     * Утилитный метод для создания объекта пользователя с заданными параметрами.
     */
    private User createUser(UUID userId, String email, String fullName, boolean isActive, boolean isArchived) {
        final User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setIsActive(isActive);
        user.setArchived(isArchived);
        return user;
    }
}
