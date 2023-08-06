package ru.practicum.mainservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessException extends RuntimeException {
    public AccessException(String message) {
        super(message);
    }
}
