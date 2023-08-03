package ru.practicum.mainservice.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoFoundObjectException extends RuntimeException {

    public NoFoundObjectException(String message) {
        super(message);
    }
}
