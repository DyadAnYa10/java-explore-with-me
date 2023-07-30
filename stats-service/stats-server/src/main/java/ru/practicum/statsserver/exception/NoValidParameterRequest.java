package ru.practicum.statsserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NoValidParameterRequest extends RuntimeException {
    public NoValidParameterRequest(String message) {
        super(message);
    }
}
