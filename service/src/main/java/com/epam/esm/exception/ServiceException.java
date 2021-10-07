package com.epam.esm.exception;

public class ServiceException extends RuntimeException {

    private final String errorCode;
    private final Object[] arguments;

    public ServiceException(String errorMessageIdentifier, Object... arguments) {
        this.errorCode = errorMessageIdentifier;
        this.arguments = arguments;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
