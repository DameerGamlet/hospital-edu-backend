package edu.medical.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyActiveException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "User with this email = '%s' is already active.";

    public UserAlreadyActiveException(String message) {
        super(message);
    }

    public UserAlreadyActiveException(String message, String email) {
        super(DEFAULT_MESSAGE.formatted(email));
    }
}
