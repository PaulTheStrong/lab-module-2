package com.epam.esm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
public class ResourceNotFoundException extends RuntimeException {

    private final int id;

    public ResourceNotFoundException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
