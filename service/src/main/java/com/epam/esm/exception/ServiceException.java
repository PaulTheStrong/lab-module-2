package com.epam.esm.exception;

public class ServiceException extends RuntimeException {

    private final String errorMessageIdentifier;
    private final Object[] arguments;

    public ServiceException(String errorMessageIdentifier, Object... arguments) {
        this.errorMessageIdentifier = errorMessageIdentifier;
        this.arguments = arguments;
    }

    public String getErrorMessageIdentifier() {
        return errorMessageIdentifier;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
