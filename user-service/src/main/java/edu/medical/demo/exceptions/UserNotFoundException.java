package edu.medical.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Пользователя с данным userId = '%s' не найден";

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(UUID userId) {
        super(DEFAULT_MESSAGE.formatted(userId));
    }
}
