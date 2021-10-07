package com.epam.esm.exception;

public class HttpErrorResponse {

    private final int errorCode;
    private final String errorMessage;

    public HttpErrorResponse(int code, String message) {
        this.errorCode = code;
        this.errorMessage = message;
    }

    public HttpErrorResponse(String code, String message) {
        this.errorCode = Integer.parseInt(code);
        this.errorMessage = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
