package edu.medical.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyArchivedException extends RuntimeException {
    public UserAlreadyArchivedException(String message) {
        super(message);
    }
}
