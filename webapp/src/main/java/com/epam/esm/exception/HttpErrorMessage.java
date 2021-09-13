package com.epam.esm.exception;

import org.springframework.http.HttpStatus;

public class HttpErrorMessage {

    private final int code;
    private final String message;

    public HttpErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
