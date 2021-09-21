package com.epam.esm.exception;

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
