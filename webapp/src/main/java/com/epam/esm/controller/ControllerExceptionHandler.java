package com.epam.esm.controller;

import com.epam.esm.i18n.Translator;
import com.epam.esm.exception.HttpErrorMessage;
import com.epam.esm.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Translator messageTranslator;

    @Autowired
    public ControllerExceptionHandler(Translator messageTranslator) {
        this.messageTranslator = messageTranslator;
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public HttpErrorMessage resourceNotFound(ServiceException e) {
        String errorMessageIdentifier = e.getErrorMessageIdentifier();
        Object[] arguments = e.getArguments();
        return new HttpErrorMessage(40401, messageTranslator.toLocale(errorMessageIdentifier, arguments));
    }
}
