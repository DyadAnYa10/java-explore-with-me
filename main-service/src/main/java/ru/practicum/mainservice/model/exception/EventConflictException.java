package ru.practicum.mainservice.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EventConflictException extends RuntimeException {
    public EventConflictException(String message) {
        super(message);
    }
}
