package edu.medical.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyArchivedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "User with this email = '%s' was archived, would you like to restore the account?";
    public UserAlreadyArchivedException(String message) {
        super(message);
    }

    public UserAlreadyArchivedException(String message, String email) {
        super(DEFAULT_MESSAGE.formatted(email));
    }
}
